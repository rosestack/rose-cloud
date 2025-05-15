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

        String url = "https://localhost:8443";
        URI uri = NetUtils.resolveAddress(new URI(url));
        Assertions.assertEquals("https://127.0.0.1:8443", uri.toString());
    }
}
