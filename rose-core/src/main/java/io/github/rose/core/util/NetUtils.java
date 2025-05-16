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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 网络相关工具
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class NetUtils {
    public static final String ANY_IP4 = "0.0.0.0";
    public static final String LOCALHOST = "localhost";
    public static final String LOCAL_IP4 = "127.0.0.1";
    private static final Logger log = LoggerFactory.getLogger(NetUtils.class);
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
    private static final Pattern IP4_PATTERN = Pattern.compile(
        "^(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])" + // 第一个数字部分
            "(\\.(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])){3}$" // 接下来的三个数字部分
    );
    private static final String LEGAL_LOCAL_IP_PROPERTY = "java.net.preferIPv6Addresses";

    private static InetAddress localInetAddress;
    private static String localhostName;

    public static String getLocalAddress() {
        return localInetAddress.getHostAddress();
    }

    public static String getLocalhostName() {
        if (localhostName != null) {
            return localhostName;
        }

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            localhostName = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            localhostName = LOCALHOST;
        }
        return localhostName;
    }

    public static InetAddress getLocalInetAddress(Map<String, Integer> destHostPorts) {
        if (localInetAddress != null) {
            return localInetAddress;
        }

        InetAddress inetAddress = getFirstInet4AddressByNetwork();

        if (inetAddress == null) {
            inetAddress = getLocalAddressBySocket(destHostPorts);
        }

        if (inetAddress == null) {
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // ignore
            }
        }

        localInetAddress = inetAddress;
        return inetAddress;
    }


    public static InetAddress getLocalInetAddress() {
        return getLocalInetAddress(null);
    }

    public static boolean isIp4Address(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        return !ANY_IP4.equals(ip) && IP4_PATTERN.matcher(ip).matches();
    }

    public static boolean isIp4Address(InetAddress address) {
        if (address == null) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null && !ANY_IP4.equals(name) && !LOCAL_IP4.equals(name) && IP4_PATTERN.matcher(name).matches());
    }

    //return ip to avoid lookup dns
    public static String getHostName(SocketAddress socketAddress) {
        if (socketAddress == null) {
            return null;
        }

        if (socketAddress instanceof InetSocketAddress) {
            InetAddress inetAddress = ((InetSocketAddress) socketAddress).getAddress();
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            }
        }

        return null;
    }

    public static String getLocalAddressByDatagram() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            log.error("Failed to retrieving ip address.", e);
        }
        return null;
    }

    public static int getAvailablePort() {
        for (int i = 0; i < 50; i++) {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int port = serverSocket.getLocalPort();
                if (port != 0) {
                    return port;
                }
            } catch (IOException ignored) {
            }
        }

        throw new RuntimeException("Could not find a free permitted port on the machine.");
    }

    public static String resolveAddress(String fqdn) {
        String ip = null;
        try {
            InetAddress address = InetAddress.getByName(fqdn);
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("UnknownHostException " + fqdn, e);
        }
        return ip;
    }

    public static URI resolveAddress(URI uri) {
        // convert the uri to URL.
        try {
            URL url = uri.toURL();
            String host = url.getHost();
            String ip = resolveAddress(host);
            if (ip != null) {
                url = new URL(url.getProtocol(), ip, url.getPort(), url.getFile());
                uri = url.toURI();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }

    private static InetAddress getLocalAddressBySocket(Map<String, Integer> destHostPorts) {
        if (destHostPorts == null || destHostPorts.size() == 0) {
            return null;
        }

        for (Map.Entry<String, Integer> entry : destHostPorts.entrySet()) {
            String host = entry.getKey();
            int port = entry.getValue();
            try {
                Socket socket = new Socket();
                try {
                    SocketAddress socketAddress = new InetSocketAddress(host, port);
                    socket.connect(socketAddress, 1000);
                    return socket.getLocalAddress();
                } finally {
                    try {
                        socket.close();
                    } catch (Throwable e) {
                    }
                }
            } catch (Exception e) {
                log.error("Failed to retrieve local address by connecting to dest host:port({}:{}) false, e={}", host, port, e);
            }
        }
        return null;
    }

    /**
     * 参考 nacos-client NetUtils
     *
     * @return
     */
    private static InetAddress getFirstInet4AddressByNetwork() {
        InetAddress result = null;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            int lowest = Integer.MAX_VALUE;

            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                if (network.isUp() && (network.getIndex() < lowest || result == null)) {
                    lowest = network.getIndex();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        boolean isLegalIpVersion = Boolean.parseBoolean(System.getProperty(LEGAL_LOCAL_IP_PROPERTY))
                            ? address instanceof Inet6Address : address instanceof Inet4Address;
                        if (isLegalIpVersion && !address.isLoopbackAddress()) {
                            result = address;
                        }
                    }
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return result;
    }
}
