package uk.ac.uos.pem;

import org.bouncycastle.asn1.*;
import uk.ac.uos.rsa.RsaPrivateKey;
import uk.ac.uos.rsa.RsaPublicKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

public abstract class PemReader {
    private static String getBase64FromPem(String pemText,String pemLabel){
        String pemBeginLabel = "-----BEGIN "+pemLabel+"-----";
        String pemEndLabel = "-----END "+pemLabel+"-----";

        return pemText
                .replace(pemBeginLabel,"")
                .replaceAll(System.lineSeparator(),"")
                .replace(pemEndLabel,"");
    }

    private static ASN1InputStream getDerAsn1Base64(String base64String){
        byte[] derEncodedAsn1 = Base64.getDecoder().decode(base64String);

        return new ASN1InputStream(derEncodedAsn1);
    }

    public static RsaPublicKey getPublicKeyFromPem(File pemFile) throws IOException {
        // Get DER encoded ASN1 from pem
        String base64Content = getBase64FromPem(Files.readString(pemFile.toPath(), Charset.defaultCharset()),"PUBLIC KEY");

        // Get asn1 contents from DER encoded asn1
        ASN1InputStream rawAsn1Contents = getDerAsn1Base64(base64Content);
        DLSequence asn1Content = (DLSequence) rawAsn1Contents.readObject();

        // Check for correct object
        DLSequence firstSequence = (DLSequence) asn1Content.getObjectAt(0);
        ASN1ObjectIdentifier objectIdentifier = (ASN1ObjectIdentifier) firstSequence.getObjectAt(0);
        if(Objects.equals(objectIdentifier.toString(), "1.2.840.113549.1.1.1")){
            DERBitString derBitString = (DERBitString) asn1Content.getObjectAt(1);
            DLSequence secondSequence = (DLSequence) DLSequence.getInstance(derBitString.getBytes());
            ASN1Integer asn1Modulus = (ASN1Integer) secondSequence.getObjectAt(0);
            ASN1Integer asn1Exponent = (ASN1Integer) secondSequence.getObjectAt(1);
            BigInteger modulus = asn1Modulus.getValue();
            BigInteger exponent = asn1Exponent.getValue();

            return new RsaPublicKey(modulus,exponent);
        }
        return null;
    }

    public static RsaPrivateKey getPrivateKeyFromPem(File pemFile) throws IOException{
        // Get DER encoded ASN1 from pem
        String base64Content = getBase64FromPem(Files.readString(pemFile.toPath(), Charset.defaultCharset()),"RSA PRIVATE KEY");

        // Get asn1 contents from DER encoded asn1
        ASN1InputStream rawAsn1Contents = getDerAsn1Base64(base64Content);
        DLSequence asn1Content = (DLSequence) rawAsn1Contents.readObject();
        ASN1Integer asn1Modulus = (ASN1Integer) asn1Content.getObjectAt(1);
        ASN1Integer asn1PublicExponent = (ASN1Integer) asn1Content.getObjectAt(2);
        ASN1Integer asn1PrivateExponent = (ASN1Integer) asn1Content.getObjectAt(3);
        ASN1Integer asn1Prime1 = (ASN1Integer) asn1Content.getObjectAt(4);
        ASN1Integer asn1Prime2 = (ASN1Integer) asn1Content.getObjectAt(5);
//        ASN1Integer asn1Exponent1 = (ASN1Integer) asn1Content.getObjectAt(6);
//        ASN1Integer asn1Exponent2 = (ASN1Integer) asn1Content.getObjectAt(7);
//        ASN1Integer asn1Coefficient = (ASN1Integer) asn1Content.getObjectAt(8);

        BigInteger n = asn1Modulus.getValue();
        BigInteger e = asn1PublicExponent.getValue();
        BigInteger d = asn1PrivateExponent.getValue();
        BigInteger p = asn1Prime1.getValue();
        BigInteger q = asn1Prime2.getValue();

        return new RsaPrivateKey(n,e,d,p,q);
    }

    public static void writePrivateKeyToPemFile(RsaPrivateKey rsaPrivateKey,String fileName) throws IOException {
        String currentDirectory = System.getProperty("user.dir")+"/keys/";
        ASN1Integer asn1Blank = new ASN1Integer(BigInteger.ZERO);
        ASN1Integer asn1Modulus = new ASN1Integer(rsaPrivateKey.getModulus());
        ASN1Integer asn1PublicExponent = new ASN1Integer(rsaPrivateKey.getPublicExponent());
        ASN1Integer asn1PrivateExponent = new ASN1Integer(rsaPrivateKey.getPrivateExponent());
        ASN1Integer asn1Prime1 = new ASN1Integer(rsaPrivateKey.getP());
        ASN1Integer asn1Prime2 = new ASN1Integer(rsaPrivateKey.getQ());
        ASN1Integer[] asn1Array = {asn1Blank,asn1Modulus,asn1PublicExponent,asn1PrivateExponent,asn1Prime1,asn1Prime2};
        DLSequence dlSequence = new DLSequence(asn1Array);
        byte[] derEncodedArray = dlSequence.getEncoded("DER");
        String base64Text = Base64.getEncoder().encodeToString(derEncodedArray);
        String pemFileContents = "-----BEGIN RSA PRIVATE KEY-----\n"+base64Text+"\n-----END RSA PRIVATE KEY-----";
        String privateKeyFullPath = currentDirectory+fileName+"_private.pem";
        File newPemFile = new File(privateKeyFullPath);
        if(newPemFile.createNewFile()){
            FileWriter writeNewPemFile = new FileWriter(privateKeyFullPath);
            writeNewPemFile.write(pemFileContents);
            writeNewPemFile.close();
        }
    }

    public static void writePublicKeyToPemFile(RsaPublicKey rsaPublicKey,String fileName) throws IOException {
        String currentDirectory = System.getProperty("user.dir")+"/keys/";
        ASN1ObjectIdentifier objectIdentifier = new ASN1ObjectIdentifier("1.2.840.113549.1.1.1");
        //ASN1Null asn1Null = new ASN1Null();
        ASN1Integer asn1Modulus = new ASN1Integer(rsaPublicKey.getModulus());
        ASN1Integer asn1PublicExponent = new ASN1Integer(rsaPublicKey.getPublicExponent());
        ASN1Integer[] asn1Array = {asn1Modulus,asn1PublicExponent};
        DLSequence firstSequence = new DLSequence(objectIdentifier);
        DLSequence secondSequence = new DLSequence(asn1Array);
        //ASN1BitString bitString = ASN1BitString.getInstance(secondSequence);
        DERBitString bitString = new DERBitString(secondSequence);
        ASN1Encodable[] rootArray = {firstSequence,bitString};
        DLSequence rootSequence = new DLSequence(rootArray);
        byte[] derEncodedArray = rootSequence.getEncoded("DER");
        String base64Text = Base64.getEncoder().encodeToString(derEncodedArray);
        String pemFileContents = "-----BEGIN PUBLIC KEY-----\n"+base64Text+"\n-----END PUBLIC KEY-----";
        String publicKeyFullPath = currentDirectory+fileName+"_public.pem";
        File newPemFile = new File(publicKeyFullPath);
        if(newPemFile.createNewFile()){
            FileWriter writeNewPemFile = new FileWriter(publicKeyFullPath);
            writeNewPemFile.write(pemFileContents);
            writeNewPemFile.close();
        }
    }

    public static void deletePemFile(String fileName){
        String currentDirectory = System.getProperty("user.dir")+"/keys/";
        String fullFilePath = currentDirectory+fileName;
        File fileToDelete = new File(fullFilePath);
        fileToDelete.delete();
    }

    public static void renamePemFile(String oldFileName,String newFileName){
        String currentDirectory = System.getProperty("user.dir")+"/keys/";
        String fullOldFileName = currentDirectory+oldFileName;
        String fullNewFileName = currentDirectory+newFileName;
        File oldFile = new File(fullOldFileName);
        File newFile = new File(fullNewFileName);
        oldFile.renameTo(newFile);
    }
}