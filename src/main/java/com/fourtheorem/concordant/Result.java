package com.fourtheorem.concordant;

public class Result implements Comparable<Result> {
    private final String address;
    private final int port;

    Result(final String address, final int port) {
        this.address = address;
        this.port = port;
    }

    Result(final String address) {
        this(address, -1);
    }

    public String getAddress() {
        return this.address;
    }

    /**
     * @return The port number if relevant, -1 otherwise.
     */
    public int getPort() {
        return this.port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Result other = (Result) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (port != other.port)
            return false;
        return true;
    }

    public int compareTo(Result o) {
        int diff = address.compareTo(o.address);
        if (diff == 0) {
            diff = port - o.port;
        }
        return diff;
    }
}
