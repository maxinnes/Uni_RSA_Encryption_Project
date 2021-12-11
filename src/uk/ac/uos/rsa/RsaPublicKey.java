package uk.ac.uos.rsa;

import java.math.BigInteger;

public class RsaPublicKey implements RsaKey {
    private final BigInteger n;
    private final BigInteger e;

    public RsaPublicKey(BigInteger n, BigInteger e){
        this.n = n;
        this.e = e;
    }
    public BigInteger encrypt(BigInteger m){
        return m.modPow(e,n);
    }
    public BigInteger getModulus(){
        return this.n;
    }
    public BigInteger getPublicExponent(){
        return this.e;
    }
}
