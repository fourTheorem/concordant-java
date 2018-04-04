package com.fourtheorem.concordant;

import java.net.UnknownHostException;

import org.xbill.DNS.SimpleResolver;

public class Concordant {

    public enum Mode {
        System, Direct
    }

    private SimpleResolver dnsResolver;

    private SrvResolver srvResolver;
    private AResolver aResolver;

    public Concordant() throws UnknownHostException {
        this(Mode.Direct);
    }

    public Concordant(Mode mode) throws UnknownHostException {
        switch (mode) {
            case System:
                init(null, -1);
                break;
            case Direct:
                final String dnsHost = System.getenv().get("DNS_HOST");
                final String dnsPort = System.getenv().get("DNS_PORT");
                init(dnsHost, dnsHost == null ? -1 : (dnsPort == null ? 53053 : Integer.parseInt(dnsPort)));
                break;
        }
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

    public SrvResolver getSrvResolver() {
        return this.srvResolver;
    }

    public IResolver getAResolver() {
        return this.aResolver;
    }

}