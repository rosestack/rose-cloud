package io.github.rose.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class NetUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(NetUtilsTest.class);

    @Test
    public void isIp4Address() {
        String[] testIPs = {
            "192.168.1.1",    // 有效
            "255.255.255.255", // 有效
            "127.0.0.1",         // 无效
            "0.0.0.0",         // 无效
            "256.255.255.255", // 无效
            "192.168.1",       // 无效
            "192.168.1.1.1",   // 无效
            "123.45.67.890",   // 无效（最后一位超过255）
            "123.045.67.89"    // 无效（前导零）
        };

        for (String ip : testIPs) {
            log.info("{}: {}", ip, NetUtils.isIp4Address(ip));
        }
    }

    @Test
    public void testGetLocalAddress() {
        InetAddress address = NetUtils.getLocalInetAddress();
        log.info("address: {}", NetUtils.getLocalAddress());
        log.info("ip: {}", NetUtils.getLocalAddress());
        log.info("localhostName: {}", NetUtils.getLocalhostName());

        Assertions.assertNotNull(address);
        Assertions.assertTrue(NetUtils.isIp4Address(address));
    }

    @Test
    public void testGetLocalAddressByDatagram() {
        String ip = NetUtils.getLocalAddressByDatagram();
        log.info("ip = {}", ip);
    }

    @Test
    public void testInetSocketAddress() throws UnknownHostException {
        InetSocketAddress inetSocketAddress1 = new InetSocketAddress("google.ca", 443);
        InetAddress inetAddress = InetAddress.getByName(inetSocketAddress1.getAddress().getHostAddress());
        InetSocketAddress inetSocketAddress2 = new InetSocketAddress(inetAddress, 443);

        Assertions.assertEquals(inetSocketAddress1.getAddress().getHostAddress(), inetSocketAddress2.getAddress().getHostAddress());
    }

    @Test
    public void testResolveAddress() throws URISyntaxException {
        String ip = NetUtils.resolveAddress("www.google.ca");
        log.info("ip = {}", ip);

        URI uri = NetUtils.resolveAddress(new URI("https://localhost:8443/index"));
        Assertions.assertEquals("https://127.0.0.1:8443/index", uri.toString());
    }
}
