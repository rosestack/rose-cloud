/*
 * Copyright © 2025 rosestack.github.io
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

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NetUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(NetUtilsTest.class);

    @Test
    void isIp4Address() {
        Map<String, Boolean> ips = new HashMap<>();
        ips.put("192.168.1.1", true);
        ips.put("255.255.255.255", true);
        ips.put("127.0.0.1", true);
        ips.put("0.0.0.0", false);
        ips.put("256.255.255.255", false);
        ips.put("192.168.1", false);
        ips.put("192.168.1.1.1", false);
        ips.put("123.45.67.890", false);
        ips.put("123.045.67.89", false);
        ips.put("192.168.1.1.1.1", false);

        ips.forEach((k, v) -> {
            Assertions.assertEquals(NetUtils.isIp4Address(k), v);
        });
    }

    @Test
    void testGetLocalAddress() {
        InetAddress inetAddress = NetUtils.getLocalInetAddress();
        Assertions.assertNotNull(inetAddress);
        Assertions.assertTrue(NetUtils.isIp4Address(inetAddress));

        String address = NetUtils.getLocalAddress();
        Assertions.assertNotNull(address);
        log.info("Local address: {}", address);

        String localhostName = NetUtils.getLocalhostName();
        log.info("Local hostname: {}", localhostName);
    }

    @Test
    void testGetLocalAddressByDatagram() {
        String ip = NetUtils.getLocalAddressByDatagram();
        Assertions.assertNotNull(ip);
    }

    @Test
    void testInetSocketAddress() throws UnknownHostException {
        InetSocketAddress inetSocketAddress1 = new InetSocketAddress("www.baidu.com", 443);
        InetAddress inetAddress =
            InetAddress.getByName(inetSocketAddress1.getAddress().getHostAddress());
        InetSocketAddress inetSocketAddress2 = new InetSocketAddress(inetAddress, 443);

        Assertions.assertEquals(
            inetSocketAddress1.getAddress().getHostAddress(),
            inetSocketAddress2.getAddress().getHostAddress());
    }

    @Test
    void testResolveAddress() throws URISyntaxException {
        String ip = NetUtils.resolveAddress("www.baidu.com");
        Assertions.assertNotNull(ip);

        URI uri = NetUtils.resolveAddress(new URI("https://www.baidu.com"));
        Assertions.assertEquals("https://" + ip, uri.toString());
    }

    @Test
    void isReachable() throws IOException {
        Assertions.assertTrue(NetUtils.isReachable("www.baidu.com"));
        Assertions.assertTrue(NetUtils.isReachable("www.baidu.com", 443, 5000));
    }
}
