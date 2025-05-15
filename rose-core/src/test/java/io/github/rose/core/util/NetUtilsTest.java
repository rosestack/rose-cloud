package io.github.rose.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class NetUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(NetUtilsTest.class);

    @Test
    public void testGetLocalAddress() throws UnknownHostException {
        InetAddress address = NetUtils.getLocalAddress();
        log.info("address: {}", NetUtils.getLocalAddress());
        log.info("ip: {}", NetUtils.getLocalIp());
        log.info("localhostName: {}", NetUtils.getLocalhostName());

        Assertions.assertNotNull(address);
        Assertions.assertTrue(NetUtils.isValidAddress(address));

        if (NetUtils.isValidAddress(InetAddress.getLocalHost())) {
            Assertions.assertEquals(InetAddress.getLocalHost(), address);
        }
    }

    @Test
    public void testGetLocalAddressByDatagram() {
        String ip = NetUtils.getLocalAddressByDatagram();
        System.out.println("ip = " + ip);
    }

    @Test
    public void testResolveHost2ip() {
        String ip = NetUtils.resolveHost2Address("www.google.ca");
        System.out.println("ip = " + ip);
    }

    @Test
    public void testInetSocketAddress() throws UnknownHostException {
        InetSocketAddress inetSocketAddress1 = new InetSocketAddress("google.ca", 443);
        System.out.println("inetSocketAddress1 = " + inetSocketAddress1);
        InetAddress inetAddress = InetAddress.getByName("142.251.41.67");
        InetSocketAddress inetSocketAddress2 = new InetSocketAddress(inetAddress, 443);
        System.out.println("inetSocketAddress2 = " + inetSocketAddress2);
    }

    @Test
    public void testReplaceUriHostname2Address() throws URISyntaxException {
        URI uri = new URI("https://localhost:8443");
        uri = NetUtils.resolveUriHost2Address(uri);
        System.out.println("uri = " + uri);
    }
}
