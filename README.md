# Concordant for Java

A Java port of [Concordant](https://github.com/apparatus/concordant).

Read the documentation for the Node.js version for an overview.

## Usage
Create a Concordant instance. Optionally specific the DNS Server host and port. If these are not specified, the system resolvers are used. See http://www.dnsjava.org/dnsjava-current/README for the resolution methods used.

### Example SRV lookup

```java
Concordant c = new Concordant()

Result[] results = c.getSrvResolver().resolve('full.service.domain.name')

// Connect to results[0].host reports[0].port and do stuff
```

### Example A lookup

```javascript
Concordant c = new Concordant()

Result[] results = c.getAResolver().resolve('full.service.domain.name')

// connect to results[0].host and do stuff. In this case, no port value is returned.
```

## Environment Variables
Concordant uses the following environment variables:

* DNS_HOST - the DNS host to perform lookup against. If not set use the system supplied DNS configuration
* DNS_PORT - the DNS port to use. Defaults to 50353 if DNS_HOST is set, otherwise uses the system supplied DNS configuration
* DNS_MODE - the lookup mode to use, if set to 'A' then perform host only lookup up. If set to 'SRV' perform SRV and A queries to resolve both host and port number. Defaults to 'SRV'

## License
Licensed under [MIT](LICENSE).
