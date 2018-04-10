package com.fourtheorem.concordant;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

class SrvResolver implements ISrvResolver {

    private final static Logger log = Logger.getLogger(SrvResolver.class.getSimpleName());
    private final Resolver dnsResolver;

    SrvResolver(Resolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    /**
     * @see IResolver#resolve(String)
     */
    public Result[] resolve(final String name) {
        return performResolve(name, false);
    }

    /**
     * @see ISrvResolver#resolveSrv(String)
     */
    public Result[] resolveSrv(final String name) {
        return performResolve(name, true);
    }

    private Result[] performResolve(final String name, final boolean srvOnly) {
        try {
            Record[] srvRecords = performSrvLookup(name);
            final List<Result> results = new ArrayList<Result>(srvRecords.length);
            for (Record srvRecord : srvRecords) {
                Name srvTarget = ((SRVRecord) srvRecord).getTarget();
                int port = ((SRVRecord) srvRecord).getPort();

                if (srvOnly) {
                    results.add(new Result(srvTarget.toString(true), port));
                } else { // Resolve A record
                    Lookup hostLookup = new Lookup(srvTarget, Type.A);
                    hostLookup.setResolver(dnsResolver);
                    Record[] aRecords = hostLookup.run();
                    if (hostLookup.getResult() != Lookup.SUCCESSFUL) {
                        log.warning("Lookup of A " + srvTarget + " via SRV " + name + " gave an unsuccessful result, " + hostLookup.getResult());
                    } else {
                        for (Record aRecord : aRecords) {
                            final String address = ((ARecord) aRecord).getAddress().getHostAddress();
                            results.add(new Result(address, port));
                        }
                    }
                }
            }
            return results.toArray(new Result[0]);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Record[] performSrvLookup(String name) throws TextParseException {
        Lookup lookup = new Lookup(name, Type.SRV);
        lookup.setResolver(dnsResolver);
        Record[] srvRecords = lookup.run();
        if (lookup.getResult() != Lookup.SUCCESSFUL) {
            log.warning("Lookup of SRV " + name + " gave an unsuccessful result, " + lookup.getResult());
            return new Record[0];
        }
        return srvRecords;
    }
}
