// TODO Remember to include getPublicKeyMethod

package uk.ac.uos.rsa;

import java.math.BigInteger;
import uk.ac.uos.rsa.RsaPublicKey;

public class RsaPrivateKey implements RsaKey {
    private final BigInteger n;
    private final BigInteger e;
    private final BigInteger d;
    private final BigInteger p;
    private final BigInteger q;

//    public RsaPrivateKey(){
//        this.n = new BigInteger("1");
//        this.e = new BigInteger("1");
//        this.d = new BigInteger("1");
//        this.p = new BigInteger("1");
//        this.q = new BigInteger("1");
//    }

    public RsaPrivateKey(BigInteger n, BigInteger e, BigInteger d, BigInteger p, BigInteger q){
        this.n = n;
        this.e = e;
        this.d = d;
        this.p = p;
        this.q = q;
    }

    public RsaPublicKey getPublicKey(){
        return new RsaPublicKey(this.n,this.e);
    }

    public BigInteger encryptMessage(BigInteger m){
        return m.modPow(e,n);
    }

    public BigInteger decryptMessage(BigInteger m){
        return m.modPow(d,n);
    }

    public BigInteger getModulus(){
        return this.n;
    }

    public BigInteger getPublicExponent(){
        return this.e;
    }

    public BigInteger getPrivateExponent(){
        return this.d;
    }

    public BigInteger getP(){
        return this.p;
    }

    public BigInteger getQ(){
        return this.q;
    }
}
