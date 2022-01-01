package uk.ac.uos.gui;

import uk.ac.uos.pem.PemReader;
import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;
import uk.ac.uos.rsa.RsaPublicKey;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;

public class EncryptMessageDialog extends JDialog {
    private JPanel contentPane;
    private JComboBox publicKeySelector;
    private JButton exitButton;
    private JButton encryptButton;
    private JTextArea messageToEncrypt;
    private JTextArea displayEncryptedMessage;
    private JButton copyButton;
    private JButton importPublicKeyButton;
    private JLabel displayFilePath;

    private boolean selectedCustomPublicKey = false;
    private RsaPublicKey selectedPublicKey;

    public EncryptMessageDialog(RsaKeyPairManager rsaKeyPairManager) {
        // Update components
        for(String keyPairName : rsaKeyPairManager.getListOfNames()){
            publicKeySelector.addItem(keyPairName);
        }
        displayEncryptedMessage.setLineWrap(true);
        displayEncryptedMessage.setWrapStyleWord(true);
        messageToEncrypt.setLineWrap(true);
        messageToEncrypt.setWrapStyleWord(true);

        // Set up events
        importPublicKeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentDirectory = System.getProperty("user.dir");
                JFileChooser fileChooser = new JFileChooser(currentDirectory);
                fileChooser.showOpenDialog(contentPane);
                File f = fileChooser.getSelectedFile();
                try{
                    RsaPublicKey customPublicKey = PemReader.getPublicKeyFromPem(f);
                    selectedCustomPublicKey = true;
                    selectedPublicKey = customPublicKey;
                    displayFilePath.setText(f.getAbsolutePath());
                    publicKeySelector.setEditable(false);
                } catch(Exception exception){
                    displayFilePath.setText("Could not load file.");
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String textToEncrypt = messageToEncrypt.getText();
                if(!selectedCustomPublicKey) {
//                    String textToEncrypt = messageToEncrypt.getText();
                    int selectedIndex = publicKeySelector.getSelectedIndex();
                    RsaKeyPair selectedKeyPair = rsaKeyPairManager.getKeyPairByIndex(selectedIndex);
                    String encryptedMessage = selectedKeyPair.encryptMessage(textToEncrypt);
                    displayEncryptedMessage.setText(encryptedMessage);
                }else{
                    BigInteger m = new BigInteger(textToEncrypt.getBytes());
                    BigInteger encryptedMessage = selectedPublicKey.encrypt(m);
                    displayEncryptedMessage.setText(encryptedMessage.toString());
                }
            }
        });
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(displayEncryptedMessage.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(500,300));
        pack();
        setVisible(true);
    }
}
