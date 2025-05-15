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
    public static final String LOCAL_IP4 = "127.0.0.1";
    public static final String LOCAL_IP6 = "0:0:0:0:0:0:0:1";
    public static final String DEFAULT_MASK = "255.255.255.0";
    public static final int INT_VALUE_127_0_0_1 = 0x7f000001;
    private static final Pattern ip4RegExp = Pattern.compile("^((?:1?[1-9]?\\d|2(?:[0-4]\\d|5[0-5]))\\.){4}$");
    private static final InetAddress LOCAL_ADDRESS;
    private static final String LOCAL_HOSTNAME;

    static {
        LOCAL_ADDRESS = getLocalAddress();
        LOCAL_HOSTNAME = getLocalHostName(LOCAL_ADDRESS);
    }

    public static boolean isUnknown(String ipAddress) {
        return StringUtils.isBlank(ipAddress) || StringPool.UNKNOWN.equalsIgnoreCase(ipAddress);
    }

    public static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(StringPool.COMMA) > 0) {
            String[] ips = ip.trim().split(StringPool.COMMA);
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return LOCAL_IP6.equals(ip) ? LOCAL_IP4 : ip;
    }

    public static boolean isInternalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return isInternalIp(addr) || LOCAL_IP4.equals(ip);
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
        return socketIp == INT_VALUE_127_0_0_1 || (localIp & mask) == (socketIp & mask);
    }

    public static boolean validateIPv4(final String input) {
        final Matcher m = ip4RegExp.matcher(input + '.');
        return m.matches();
    }

    public static LinkedHashSet<InetAddress> localAddressList(Predicate<InetAddress> addressFilter) {
        final LinkedHashSet<InetAddress> result = new LinkedHashSet<>();

        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return result;
        }

        if (networkInterfaces == null) {
            return result;
        }

        while (networkInterfaces.hasMoreElements()) {
            final Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.test(inetAddress))) {
                    result.add(inetAddress);
                }
            }
        }
        return result;
    }

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

    public static String getLocalMacAddress() {
        return getMacAddress(getLocalhost());
    }

    public static String getMacAddress(InetAddress inetAddress) {
        return getMacAddress(inetAddress, "-");
    }

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

    private static String getLocalHostName(InetAddress inetAddress) {
        if (null == inetAddress) {
            return null;
        }
        return StringUtils.defaultIfBlank(inetAddress.getHostName(), inetAddress.getHostAddress());
    }

    private static InetAddress getLocalAddress() {
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

        return localAddress;
    }
}
