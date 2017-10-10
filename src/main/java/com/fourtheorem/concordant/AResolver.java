package com.fourtheorem.concordant;

import java.net.InetAddress;
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

class AResolver implements IResolver {
	
  private final static Logger log = Logger.getLogger(AResolver.class.getSimpleName());
  private final Resolver dnsResolver;
  
  /**
   * @throws UnknownHostException
   */
	AResolver(Resolver dnsResolver) throws UnknownHostException {		
		this.dnsResolver = dnsResolver;
	}
	
	/**
	 * @see IResolver#resolve(String)
	 */
	public Result[] resolve(final String name) {
	  final List<Result> results = new ArrayList<Result>();
	  
	  try {
    		Lookup lookup = new Lookup(name, Type.A);
    		lookup.setResolver(dnsResolver);
    		Record[] srvRecords = lookup.run();
    		if (lookup.getResult() != Lookup.SUCCESSFUL) {
    		  log.warning("Lookup of A " + name + " gave an unsuccessful result, " + lookup.getResult());
    		  return new Result[0];
    		}
    		for(Record srvRecord : srvRecords) {
    		  InetAddress address = ((ARecord)srvRecord).getAddress();
        results.add(new Result(address.getHostAddress()));      		
    		}
    		return results.toArray(new Result[0]);
	  } catch (TextParseException e) {
	    throw new IllegalArgumentException(e);
	  }
	}
}
