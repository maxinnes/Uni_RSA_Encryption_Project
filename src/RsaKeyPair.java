import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;
import java.util.HashMap;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public class RsaKeyPair {
    /*private final BigInteger p;
    private final BigInteger q;
    private final BigInteger n;
    private final BigInteger phi;
    private final BigInteger d;
    private final BigInteger e;*/
    private final HashMap<String,BigInteger> keyPair = new HashMap<>(); // Make sure this is private

    public static void main(String[] args) { // function for testing
        RsaKeyPair test1 = new RsaKeyPair(2048);
    }
    // Generate a new public keypair
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
                keyPair.put("e",e);
                keyPair.put("n",n);
                keyPair.put("d",d);
                break;
            }
        }
    }
    // Load existin
    // Encrypt message
    public BigInteger encryptMessage(BigInteger message){
        return message.modPow(keyPair.get("e"),keyPair.get("n"));
    }
    // Decrypt message
    public BigInteger decryptMessage(BigInteger message){
        return message.modPow(keyPair.get("d"),keyPair.get("n"));
    }
    // GCD by Euclid’s Algorithm
    public static BigInteger gcd(BigInteger a, BigInteger b){
        if(Objects.equals(b, BigInteger.ZERO)){
            return a;
        }else{
            return gcd(b,a.mod(b));
        }
    }
}
