package com.fourtheorem.concordant;

public interface ISrvResolver extends IResolver {

    /**
     * Perform a SRV resolution but return A record names in the results instead of resolving them to IP addresses
     */
    Result[] resolveSrv(final String name);

}
