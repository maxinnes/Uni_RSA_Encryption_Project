import uk.ac.uos.gui.RsaHome;
import uk.ac.uos.rsa.RsaKeyPairManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RsaEncryptionTool {
    public static void main(String[] args) throws IOException {
        String keyDirectory = System.getProperty("user.dir")+"/keys";
        Path keyPath = Path.of(keyDirectory);
        if(Files.notExists(keyPath)){
            Files.createDirectories(keyPath);
        }

        RsaKeyPairManager rsaKeyPairManager = new RsaKeyPairManager("db.json");
        RsaHome MainWindow = new RsaHome(rsaKeyPairManager);
    }
}
