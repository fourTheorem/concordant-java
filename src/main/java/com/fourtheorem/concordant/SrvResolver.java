package com.fourtheorem.concordant;

import java.net.UnknownHostException;
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

class SrvResolver implements IResolver {
	
  private final static Logger log = Logger.getLogger(SrvResolver.class.getSimpleName());
  private final Resolver dnsResolver;
  
  /**
   * @throws UnknownHostException
   */
	SrvResolver(Resolver dnsResolver) throws UnknownHostException {		
		this.dnsResolver = dnsResolver;
	}
	
	/**
	 * @see IResolver#resolve(String)
	 */
	public Result[] resolve(final String name) {
	  final List<Result> results = new ArrayList<Result>();
	  
	  try {
    		Lookup lookup = new Lookup(name, Type.SRV);
    		lookup.setResolver(dnsResolver);
    		Record[] srvRecords = lookup.run();
    		if (lookup.getResult() != Lookup.SUCCESSFUL) {
    		  log.warning("Lookup of SRV " + name + " gave an unsuccessful result, " + lookup.getResult());
    		  return new Result[0];
    		}
    		for(Record srvRecord : srvRecords) {
    		  Name srvTarget = ((SRVRecord)srvRecord).getTarget();
    		  int port = ((SRVRecord)srvRecord).getPort();
          
    		  Lookup hostLookup = new Lookup(srvTarget, Type.A);
    		  hostLookup.setResolver(dnsResolver);
    		  Record[] aRecords = hostLookup.run();
    		  if (lookup.getResult() != Lookup.SUCCESSFUL) {
    		    log.warning("Lookup of A " + srvTarget + " via SRV " + name + " gave an unsuccessful result, " + lookup.getResult());
    		  }
    		  else {
    		    for (Record aRecord: aRecords) {
      		    final String address = ((ARecord)aRecord).getAddress().getHostAddress();
      		    results.add(new Result(address, port));
      		  }
    		  }
    		}
    		return results.toArray(new Result[0]);
	  } catch (TextParseException e) {
	    throw new IllegalArgumentException(e);
	  }
	}
}
