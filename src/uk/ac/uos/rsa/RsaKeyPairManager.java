package uk.ac.uos.rsa;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.uos.pem.PemReader;

public class RsaKeyPairManager {

    private ArrayList<RsaKeyPair> listOfKeyPairs = new ArrayList<RsaKeyPair>();
    private Path pathOfDb;

    public RsaKeyPairManager(String jsonFileName) throws IOException { // TODO if file does not exist, create it
        String currentDirectory = System.getProperty("user.dir");
        Path dbFilePath = Path.of(currentDirectory+"/"+jsonFileName);

        JSONArray listOfJsonKeyPairs;
        File dbFile = new File(dbFilePath.toString());
        if(dbFile.exists()){
            String dbJsonContents = Files.readString(dbFilePath);
            JSONObject dbJsonObj = new JSONObject(dbJsonContents);
            listOfJsonKeyPairs = (JSONArray) dbJsonObj.get("keyPairs");
        }else{
            HashMap<String,ArrayList<Object>> rootJson = new HashMap<>();
            rootJson.put("keyPairs",new ArrayList<>());
            JSONObject jsonContents = new JSONObject(rootJson);
            if(dbFile.createNewFile()){
                FileWriter writeNewDbContents = new FileWriter(dbFilePath.toString());
                writeNewDbContents.write(jsonContents.toString());
                writeNewDbContents.close();
            }
            String dbJsonContents = Files.readString(dbFilePath);
            JSONObject dbJsonObj = new JSONObject(dbJsonContents);
            listOfJsonKeyPairs = (JSONArray) dbJsonObj.get("keyPairs");
        }

        String dbJsonContents = Files.readString(dbFilePath);
        JSONObject dbJsonObj = new JSONObject(dbJsonContents);
        //JSONArray listOfJsonKeyPairs = (JSONArray) dbJsonObj.get("keyPairs");

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
        pathOfDb = dbFilePath;
    }

    public String[] getListOfNames(){
        ArrayList<String> tempList = new ArrayList<>();
        listOfKeyPairs.forEach(rsaKeyPair ->{
            tempList.add(rsaKeyPair.getKeyPairName());
        });
        String[] stringArray = new String[tempList.size()];
        stringArray = tempList.toArray(stringArray);
        return stringArray;
    }

    public RsaKeyPair getKeyPairByIndex(int indexSelection){
        return listOfKeyPairs.get(indexSelection);
    }

    public void deleteKeyPairByIndex(int indexSelection) throws IOException {
        RsaKeyPair rsaKeyPair = this.getKeyPairByIndex(indexSelection);
        PemReader.deletePemFile(rsaKeyPair.getKeyPairName()+"_private.pem");
        PemReader.deletePemFile(rsaKeyPair.getKeyPairName()+"_public.pem");

        listOfKeyPairs.remove(indexSelection);
        updateJsonDb();
    }

    private void updateJsonDb() throws IOException {
        HashMap<String,ArrayList<HashMap<String,Object>>> rootJson = new HashMap<String,ArrayList<HashMap<String,Object>>>();

        ArrayList<HashMap<String,Object>> tempList = new ArrayList<>();
        listOfKeyPairs.forEach(rsaKeyPair -> {
            HashMap<String,Object> tempHashMap = new HashMap<>();
            tempHashMap.put("name",rsaKeyPair.getKeyPairName());
            tempHashMap.put("bitLength",rsaKeyPair.getKeyPairBitLength());
            tempHashMap.put("privateKeyFileName",rsaKeyPair.getKeyPairName()+"_private.pem");
            tempHashMap.put("publicKeyFileName",rsaKeyPair.getKeyPairName()+"_public.pem");
            tempList.add(tempHashMap);
        });
        rootJson.put("keyPairs",tempList);
        JSONObject jsonContents = new JSONObject(rootJson);
        FileWriter newDbJson = new FileWriter(pathOfDb.toString());
        newDbJson.write(jsonContents.toString());
        newDbJson.close();
    }

    public void renameKeyPair(int indexSelection,String newName) throws IOException {
        RsaKeyPair oldKeyPair = getKeyPairByIndex(indexSelection);

        String oldPrivateKeyFileName = oldKeyPair.getKeyPairName()+"_private.pem";
        String oldPublicFileName = oldKeyPair.getKeyPairName()+"_public.pem";

        RsaKeyPair newKeyPair = new RsaKeyPair(oldKeyPair.getKeyPairBitLength(), newName, oldKeyPair.getPrivateKey(), oldKeyPair.getPublicKey());

        String newPrivateKeyFileName = newKeyPair.getKeyPairName()+"_private.pem";
        String newPublicFileName = newKeyPair.getKeyPairName()+"_public.pem";

        PemReader.renamePemFile(oldPrivateKeyFileName,newPrivateKeyFileName);
        PemReader.renamePemFile(oldPublicFileName,newPublicFileName);

        listOfKeyPairs.set(indexSelection,newKeyPair);

        updateJsonDb();
    }

    public void addAdditionalKeyPair(RsaKeyPair newKeyPairToAdd) throws IOException {
        listOfKeyPairs.add(newKeyPairToAdd);
        PemReader.writePrivateKeyToPemFile(newKeyPairToAdd.getPrivateKey(), newKeyPairToAdd.getKeyPairName());
        PemReader.writePublicKeyToPemFile(newKeyPairToAdd.getPublicKey(), newKeyPairToAdd.getKeyPairName());
        updateJsonDb();
    }
}
