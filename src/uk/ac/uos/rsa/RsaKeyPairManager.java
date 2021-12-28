package uk.ac.uos.rsa;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import uk.ac.uos.pem.PemReader;

public class RsaKeyPairManager {

    private ArrayList<RsaKeyPair> listOfKeyPairs = new ArrayList<RsaKeyPair>();
    private Path pathOfDb;

    public static void main(String[] args) throws IOException {
        RsaKeyPairManager testing = new RsaKeyPairManager("db.json");
    }

    public RsaKeyPairManager(String jsonFileName) throws IOException { // TODO if file does not exist, create it
        String currentDirectory = System.getProperty("user.dir");
        Path dbFilePath = Path.of(currentDirectory+"/"+jsonFileName);
        String dbJsonContents = Files.readString(dbFilePath);
        JSONObject dbJsonObj = new JSONObject(dbJsonContents);
        JSONArray listOfJsonKeyPairs = (JSONArray) dbJsonObj.get("keyPairs");

        listOfJsonKeyPairs.forEach((jsonParam)->{
            JSONObject convertParam = (JSONObject) jsonParam;
            String privateKeyFileName = (String) convertParam.get("privateKeyFileName");
            String keyPairName = (String) convertParam.get("name");
            int keyPairBitLength = (int) convertParam.get("bitLength");
            File test = new File(currentDirectory+"/keys/"+privateKeyFileName);
            try {
                RsaPrivateKey rsaPrivateKey = PemReader.getPrivateKeyFromPem(test);
                RsaPublicKey rsaPublicKey = rsaPrivateKey.getPublicKey();
                RsaKeyPair addKeyPair = new RsaKeyPair(keyPairBitLength,keyPairName,rsaPrivateKey,rsaPublicKey);
                listOfKeyPairs.add(addKeyPair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateJsonDb(){
        
    }

    public void addAdditionalKeyPair(RsaKeyPair newKeyPairToAdd){
        listOfKeyPairs.add(newKeyPairToAdd);
    }
}
