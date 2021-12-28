package uk.ac.uos.rsa;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

public class RsaKeyPair {

    private RsaPrivateKey privateKey;
    private RsaPublicKey publicKey;

    public static void main(String[] args) {
        RsaKeyPair test1 = new RsaKeyPair(2048);
    }

    public RsaKeyPair(int bitLength){
        while (true){
            // Generate random prime numbers
            Random rng = new Random();
            BigInteger p = BigInteger.probablePrime(bitLength, rng);
            BigInteger q = BigInteger.probablePrime(bitLength, rng);

            // Work out phi
            BigInteger e = new BigInteger("65537");
            BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

            // Check if they are both equal and both numbers are prime and gcd of e and phi is 1
            if(!p.equals(q) && Objects.equals(gcd(e, phi), BigInteger.ONE)){ // check if prime using big int function
                BigInteger n = p.multiply(q);
                BigInteger d = e.modInverse(phi);

                privateKey = new RsaPrivateKey(n,e,d,p,q);
                publicKey = privateKey.getPublicKey();

                break;
            }
        }
    }
    public RsaKeyPair(RsaPrivateKey parsedPrivateKey, RsaPublicKey parsedPublicKey){
        privateKey = parsedPrivateKey;
        publicKey = parsedPublicKey;
    }
    private static BigInteger gcd(BigInteger a, BigInteger b){
        if(Objects.equals(b, BigInteger.ZERO)){
            return a;
        }else{
            return gcd(b,a.mod(b));
        }
    }
}
