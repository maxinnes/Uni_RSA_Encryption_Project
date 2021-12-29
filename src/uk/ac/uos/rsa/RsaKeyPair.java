package uk.ac.uos.rsa;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

public class RsaKeyPair {

    private final RsaPrivateKey privateKey;
    private final RsaPublicKey publicKey;
    private int keyPairBitLength;
    private String keyPairName;

    public RsaKeyPair(int bitLength,String parseKeyPairName){
        keyPairBitLength = bitLength;
        keyPairName = parseKeyPairName;
        while (true){
            // Generate random prime numbers
            Random rng = new Random();
            BigInteger p = BigInteger.probablePrime(bitLength/2, rng);
            BigInteger q = BigInteger.probablePrime(bitLength/2, rng);

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
    public RsaKeyPair(int bitLength,String parseKeyPairName,RsaPrivateKey parsedPrivateKey, RsaPublicKey parsedPublicKey){
        keyPairBitLength = bitLength;
        keyPairName = parseKeyPairName;
        privateKey = parsedPrivateKey;
        publicKey = parsedPublicKey;
    }
    public RsaPublicKey getPublicKey(){
        return publicKey;
    }
    public RsaPrivateKey getPrivateKey(){
        return privateKey;
    }
    public String encryptMessage(String message){
        byte[] messageBytes = message.getBytes();
        BigInteger m = new BigInteger(messageBytes);
        BigInteger encryptedMessage = publicKey.encrypt(m);
        return encryptedMessage.toString();
    }
    public String decryptMessage(String message){
        BigInteger encryptedMessage = new BigInteger(message);
        BigInteger m = privateKey.decryptMessage(encryptedMessage);
        return new String(m.toByteArray());
    }
    public int getKeyPairBitLength(){
        return keyPairBitLength;
    }
    public String getKeyPairName(){
        return keyPairName;
    }
    private static BigInteger gcd(BigInteger a, BigInteger b){
        if(Objects.equals(b, BigInteger.ZERO)){
            return a;
        }else{
            return gcd(b,a.mod(b));
        }
    }
}
