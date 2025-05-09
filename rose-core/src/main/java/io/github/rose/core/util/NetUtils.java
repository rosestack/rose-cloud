/*
 * Copyright © 2025 rose-group.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.core.util;

import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络相关工具
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class NetUtils {

    public static final String LOCAL_HOST = "localhost";

    public static final String LOCAL_IP = "127.0.0.1";

    public static final String DEFAULT_MASK = "255.255.255.0";

    public static final int INT_VALUE_127_0_0_1 = 0x7f000001;

    private static final Pattern ip4RegExp = Pattern.compile("^((?:1?[1-9]?\\d|2(?:[0-4]\\d|5[0-5]))\\.){4}$");

    private static final InetAddress LOCAL_ADDRESS;

    private static String LOCAL_HOSTNAME;

    static {
        /**
         * 获取本机网卡IP地址，规则如下：
         *
         * <pre>
         * 1. 查找所有网卡地址，必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv4地址
         * 2. 如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址
         * </pre>
         * <p>
         * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
         * <p>
         * 见：https://github.com/looly/hutool/issues/428
         *
         */
        InetAddress localAddress = null;
        final LinkedHashSet<InetAddress> localAddressList = localAddressList(address -> address.isSiteLocalAddress()
            && !address.isLoopbackAddress()
            && !address.getHostAddress().contains(":"));

        if (localAddressList != null && localAddressList.size() > 0) {
            localAddress = localAddressList.iterator().next();
        }
        if (localAddress == null) {
            try {
                localAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // ignore
            }
        }
        LOCAL_ADDRESS = localAddress;

        if (null != LOCAL_ADDRESS) {
            String name = LOCAL_ADDRESS.getHostName();
            if (StringUtils.isEmpty(name)) {
                name = LOCAL_ADDRESS.getHostAddress();
            }
            LOCAL_HOSTNAME = name;
        }
    }

    public static boolean isUnknown(String ipAddress) {
        return StringUtils.isBlank(ipAddress) || StringPool.UNKNOWN.equalsIgnoreCase(ipAddress);
    }

    public static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }

    public static boolean isInternalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return isInternalIp(addr) || LOCAL_IP.equals(ip);
    }

    private static boolean isInternalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return Boolean.TRUE;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return Boolean.TRUE;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return Boolean.TRUE;
                }
            default:
                return Boolean.FALSE;
        }
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) return null;
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) return null;
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) return null;
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) return null;
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    /**
     * Resolves IP address from a hostname.
     */
    public static String resolveIpAddress(final String hostname) {
        try {
            final InetAddress netAddress;

            if (hostname == null || hostname.equalsIgnoreCase(LOCAL_HOST)) {
                netAddress = InetAddress.getLocalHost();
            } else {
                netAddress = Inet4Address.getByName(hostname);
            }
            return netAddress.getHostAddress();
        } catch (final UnknownHostException ignore) {
            return null;
        }
    }

    /**
     * Returns IP address as integer.
     */
    public static int getIpAsInt(final String ipAddress) {
        int ipIntValue = 0;
        final String[] tokens = StringUtils.split(ipAddress, StringPool.DOT);
        for (final String token : tokens) {
            if (ipIntValue > 0) {
                ipIntValue <<= 8;
            }
            ipIntValue += Integer.parseInt(token);
        }
        return ipIntValue;
    }

    public static int getMaskAsInt(String mask) {
        if (!validateIPv4(mask)) {
            mask = DEFAULT_MASK;
        }
        return getIpAsInt(mask);
    }

    public static boolean isSocketAccessAllowed(final int localIp, final int socketIp, final int mask) {
        boolean _retVal = false;

        if (socketIp == INT_VALUE_127_0_0_1 || (localIp & mask) == (socketIp & mask)) {
            _retVal = true;
        }
        return _retVal;
    }

    /**
     * Checks given string against IP address v4 format.
     *
     * @param input an ip address - may be null
     * @return <tt>true</tt> if param has a valid ip v4 format <tt>false</tt> otherwise
     * @see <a href="https://en.wikipedia.org/wiki/IP_address#IPv4_addresses">ip address
     * v4</a>
     */
    public static boolean validateIPv4(final String input) {
        final Matcher m = ip4RegExp.matcher(input + '.');
        return m.matches();
    }

    /**
     * 获取所有满足过滤条件的本地IP地址对象
     *
     * @param addressFilter 过滤器，null表示不过滤，获取所有地址
     * @return 过滤后的地址对象列表
     * @since 4.5.17
     */
    public static LinkedHashSet<InetAddress> localAddressList(Predicate<InetAddress> addressFilter) {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        if (networkInterfaces == null) {
            throw new RuntimeException("Get network interface error!");
        }

        final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.test(inetAddress))) {
                    ipSet.add(inetAddress);
                }
            }
        }
        return ipSet;
    }

    /**
     * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
     * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
     * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
     * <p>
     * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     *
     * @return 本机网卡IP地址，获取失败返回<code>null</code>
     * @since 3.0.7
     */
    public static String getLocalhostStr() {
        InetAddress localhost = getLocalhost();
        if (null != localhost) {
            return localhost.getHostAddress();
        }
        return null;
    }

    public static InetAddress getLocalhost() {
        return LOCAL_ADDRESS;
    }

    /**
     * 获得本机MAC地址
     *
     * @return 本机MAC地址
     */
    public static String getLocalMacAddress() {
        return getMacAddress(getLocalhost());
    }

    /**
     * 获得指定地址信息中的MAC地址，使用分隔符“-”
     *
     * @param inetAddress {@link InetAddress}
     * @return MAC地址，用-分隔
     */
    public static String getMacAddress(InetAddress inetAddress) {
        return getMacAddress(inetAddress, "-");
    }

    /**
     * 获得指定地址信息中的MAC地址
     *
     * @param inetAddress {@link InetAddress}
     * @param separator   分隔符，推荐使用“-”或者“:”
     * @return MAC地址，用-分隔
     */
    public static String getMacAddress(InetAddress inetAddress, String separator) {
        if (null == inetAddress) {
            return null;
        }

        byte[] mac = null;
        try {
            final NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            if (null != networkInterface) {
                mac = networkInterface.getHardwareAddress();
            }
        } catch (SocketException e) {
            throw new RuntimeException("获取MAC地址失败", e);
        }
        if (null != mac) {
            final StringBuilder sb = new StringBuilder();
            String s;
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(separator);
                }
                // 字节转换为整数
                s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString();
        }

        return null;
    }

    public static String getLocalHostName() {
        return LOCAL_HOSTNAME;
    }
}
