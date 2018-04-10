package com.fourtheorem.concordant;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

/*
 * Uses the following public DNS records (output from
 * > aws route53 list-resource-record-sets --hosted-zone-id=/hostedzone/Z25COYVU2WS88B | jq '.ResourceRecordSets[] | "\(.Type) \(.Name) \(.ResourceRecords)"'
 *
 * SRV _main._tcp.testservice-multi.micro.codespyre.com. [{"Value":"1 10 443 testservice-multi.codespyre.com"},{"Value":"2 12 80 testservice-multi2.codespyre.com"}]
 * SRV _main._tcp.testservice.micro.codespyre.com. [{"Value":"1 10 443 testservice.codespyre.com."}]
 * A testservice-multi.codespyre.com. [{"Value":"192.168.0.124"},{"Value":"192.168.0.125"}]
 * A testservice-multi2.codespyre.com. [{"Value":"192.168.0.126"},{"Value":"192.168.0.127"}]
 * A testservice.codespyre.com. [{"Value":"192.168.0.123"}]
 */
public class ConcordantTest {

    @Test
    public void testSrvLookup() throws Exception {
        Concordant c = new Concordant();
        testSrvResolver(c.getSrvResolver());
    }

    @Test
    public void testSrvLookupMulti() throws Exception {
        Concordant c = new Concordant();
        testSrvResolverMulti(c.getSrvResolver());
    }

    @Test
    public void testSrvLookupSpecificDNS() throws Exception {
        Concordant c = new Concordant("37.235.1.174", 53); // freedns.zone
        testSrvResolver(c.getSrvResolver());
    }

    @Test
    public void testSrvLookupSrvOnly() throws Exception {
        Concordant c = new Concordant();
        testSrvResolverSrvOnly(c.getSrvResolver());
    }

    @Test
    public void testSrvLookupMultiSrvOnly() throws Exception {
        Concordant c = new Concordant();
        testSrvResolverMultiSrvOnly(c.getSrvResolver());
    }

    @Test
    public void testSrvLookupSpecificDNSSrvOnly() throws Exception {
        Concordant c = new Concordant("37.235.1.174", 53); // freedns.zone
        testSrvResolverSrvOnly(c.getSrvResolver());
    }

    @Test
    public void testALookup() throws Exception {
        Concordant c = new Concordant();
        testAResolver(c.getAResolver());
    }

    @Test
    public void testALookupMulti() throws Exception {
        Concordant c = new Concordant();
        testAResolverMulti(c.getAResolver());
    }

    @Test
    public void testALookupSpecificDNS() throws Exception {
        Concordant c = new Concordant("37.235.1.174", 53); // freedns.zone
        testAResolver(c.getAResolver());
    }

    private void testSrvResolver(IResolver srvResolver) {
        Result[] results = srvResolver.resolve("_main._tcp.testservice.micro.codespyre.com");
        assertEquals(1, results.length);
        assertEquals(443, results[0].getPort());
        assertEquals("192.168.0.123", results[0].getAddress());
    }

    private void testSrvResolverSrvOnly(ISrvResolver srvResolver) {
        Result[] results = srvResolver.resolveSrv("_main._tcp.testservice.micro.codespyre.com");
        assertEquals(1, results.length);
        assertEquals(443, results[0].getPort());
        assertEquals("testservice.codespyre.com", results[0].getAddress());
    }

    private void testSrvResolverMulti(ISrvResolver srvResolver) {
        Result[] results = srvResolver.resolve("_main._tcp.testservice-multi.micro.codespyre.com");
        assertEquals(4, results.length);
        Arrays.sort(results);
        assertEquals(443, results[0].getPort());
        assertEquals(443, results[1].getPort());
        assertEquals(80, results[2].getPort());
        assertEquals(80, results[3].getPort());
        assertEquals("192.168.0.124", results[0].getAddress());
        assertEquals("192.168.0.125", results[1].getAddress());
        assertEquals("192.168.0.126", results[2].getAddress());
        assertEquals("192.168.0.127", results[3].getAddress());
    }

    private void testSrvResolverMultiSrvOnly(ISrvResolver srvResolver) {
        Result[] results = srvResolver.resolveSrv("_main._tcp.testservice-multi.micro.codespyre.com");
        assertEquals(2, results.length);
        Arrays.sort(results);
        assertEquals(443, results[0].getPort());
        assertEquals(80, results[1].getPort());
        assertEquals("testservice-multi.codespyre.com", results[0].getAddress());
        assertEquals("testservice-multi2.codespyre.com", results[1].getAddress());
    }

    private void testAResolver(IResolver aResolver) {
        Result[] results = aResolver.resolve("testservice.codespyre.com");
        assertEquals(1, results.length);
        assertEquals(-1, results[0].getPort());
        assertEquals("192.168.0.123", results[0].getAddress());
    }

    private void testAResolverMulti(IResolver aResolver) {
        Result[] results = aResolver.resolve("testservice-multi.codespyre.com");
        Arrays.sort(results);
        assertEquals(2, results.length);
        assertEquals(-1, results[0].getPort());
        assertEquals(-1, results[1].getPort());
        assertEquals("192.168.0.124", results[0].getAddress());
        assertEquals("192.168.0.125", results[1].getAddress());
    }

}
