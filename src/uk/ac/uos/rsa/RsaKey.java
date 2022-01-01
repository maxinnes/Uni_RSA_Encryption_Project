package uk.ac.uos.rsa;

import java.math.BigInteger;

public interface RsaKey{
    public BigInteger getModulus();
    public BigInteger getPublicExponent();
    public int getKeyBitLength();
}
