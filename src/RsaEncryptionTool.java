import uk.ac.uos.gui.RsaHome;
import uk.ac.uos.rsa.RsaKeyPairManager;

import java.io.IOException;

public class RsaEncryptionTool {
    public static void main(String[] args) throws IOException {
        RsaKeyPairManager rsaKeyPairManager = new RsaKeyPairManager("db.json");
        RsaHome MainWindow = new RsaHome(rsaKeyPairManager);
    }
}
