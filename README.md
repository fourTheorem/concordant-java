# Concordant for Java

A Java port of [Concordant](https://github.com/apparatus/concordant).

Read the documentation for the Node.js version for an overview.

## Usage
Create a Concordant instance. Optionally specific the DNS Server host and port. If these are not specified, the system resolvers are used. See http://www.dnsjava.org/dnsjava-current/README for the resolution methods used.

## Project Configuration

Add the library to your project's dependencies. For Maven, the `pom.xml` dependency looks like this.
```xml
<dependency>
  <groupId>com.fourtheorem</groupId>
  <artifactId>concordant</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

To find this dependency, you should also add the Bintray repository to your repository list.

```xml
  <repositories>
    <repository>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
      <id>bintray-fourtheorem-concordant-java</id>
      <name>bintray</name>
      <url>https://dl.bintray.com/fourtheorem/concordant-java</url>
    </repository>
  </repositories>
```

### Example SRV lookup

```java
Concordant c = new Concordant()

Result[] results = c.getSrvResolver().resolveSrv('full.service.domain.name')

// Connect to results[0].host results[0].port and do stuff
```

Note: To do full resolution of the SRV target and perform the IP address lookup of that `A` record, use `resolve` instead of `resolveSrv`.

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
