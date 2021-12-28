import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;

import uk.ac.uos.gui.RsaDisplayPrivateKeyDialog;
import uk.ac.uos.gui.RsaDisplayPublicKeyDialog;
import uk.ac.uos.pem.PemReader;
import uk.ac.uos.rsa.RsaPrivateKey;
import uk.ac.uos.rsa.RsaPublicKey;

public class PemViewer {
    private JPanel testPanel1;
    private JButton loadPublicKeyButton;
    private JButton loadPrivateKeyButton;

    public static void main(String[] args) {
        new PemViewer();
    }

    public PemViewer(){
        // Configure frame
        JFrame mainFrame = new JFrame("Public Key Reader");
        mainFrame.add(testPanel1);
        mainFrame.setSize(250,150);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add action listeners
        loadPublicKeyButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\Max\\Downloads");
            int operation = fileChooser.showOpenDialog(mainFrame);
            File f = fileChooser.getSelectedFile();
            RsaPublicKey publicKey = null;
            try {
                publicKey = PemReader.getPublicKeyFromPem(f);
                String modulus = publicKey.getModulus().toString();
                String exponent = publicKey.getPublicExponent().toString();
                RsaDisplayPublicKeyDialog displayInfoTest = new RsaDisplayPublicKeyDialog(modulus,exponent);
                displayInfoTest.setTitle(f.getAbsolutePath());
                displayInfoTest.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        loadPrivateKeyButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\Max\\Downloads");
            int operation = fileChooser.showOpenDialog(mainFrame);
            File f = fileChooser.getSelectedFile();
            RsaPrivateKey privateKey = null;
            try{
                privateKey = PemReader.getPrivateKeyFromPem(f);
                String modulus = privateKey.getModulus().toString();
                String publicExponent = privateKey.getPublicExponent().toString();
                String privateExponent = privateKey.getPrivateExponent().toString();
                String prime1 = privateKey.getP().toString();
                String prime2 = privateKey.getQ().toString();
                RsaDisplayPrivateKeyDialog displayInfoTest = new RsaDisplayPrivateKeyDialog(modulus,publicExponent,privateExponent,prime1,prime2);
                displayInfoTest.setTitle(f.getAbsolutePath());
                displayInfoTest.setVisible(true);
            } catch(IOException ex){
                ex.printStackTrace();
            }
        });

        // Display frame
        mainFrame.setVisible(true);
    }
}
