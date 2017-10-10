package com.fourtheorem.concordant;

import java.net.UnknownHostException;

import org.xbill.DNS.SimpleResolver;

public class Concordant {

  private SimpleResolver dnsResolver;

  private SrvResolver srvResolver;
  private AResolver aResolver;

  public Concordant() throws UnknownHostException {
    final String dnsHost = System.getenv().get("DNS_HOST");
    final String dnsPort = System.getenv().get("DNS_PORT");
    init(dnsHost, dnsPort == null ? -1 : Integer.parseInt(dnsPort));
  }

  public Concordant(final String dnsHost, final int dnsPort) throws UnknownHostException {
    init(dnsHost, dnsPort);
  }

  private void init(final String dnsHost, final int dnsPort) throws UnknownHostException {
    dnsResolver = dnsHost == null ? new SimpleResolver() : new SimpleResolver(dnsHost);
    if (dnsPort > 0) {
      dnsResolver.setPort(dnsPort);
    }

    srvResolver = new SrvResolver(dnsResolver);
    aResolver = new AResolver(dnsResolver);
  }

  public IResolver getSrvResolver() {
    return this.srvResolver;
  }

  public IResolver getAResolver() {
    return this.aResolver;
  }

}