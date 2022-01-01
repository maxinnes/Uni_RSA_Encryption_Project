package uk.ac.uos.gui;

import uk.ac.uos.pem.PemReader;
import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;
import uk.ac.uos.rsa.RsaPrivateKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;

public class DecryptMessageDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea messageToDecrypt;
    private JButton exitButton;
    private JButton decryptButton;
    private JTextArea displayDecryptedMessage;
    private JComboBox selectPrivateKey;
    private JButton importCustomPrivateKeyButton;
    private JLabel displayFilePath;

    private boolean selectedPrivateKey = false;
    private RsaPrivateKey selectedCustomPrivateKey;

    public DecryptMessageDialog(RsaKeyPairManager rsaKeyPairManager) {
        // Update components
        messageToDecrypt.setLineWrap(true);
        messageToDecrypt.setWrapStyleWord(true);
        displayDecryptedMessage.setLineWrap(true);
        displayDecryptedMessage.setWrapStyleWord(true);
        for(String keyPairName : rsaKeyPairManager.getListOfNames()){
            selectPrivateKey.addItem(keyPairName);
        }

        // set up events
        importCustomPrivateKeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentDirectory = System.getProperty("user.dir");
                JFileChooser fileChooser = new JFileChooser(currentDirectory);
                fileChooser.showOpenDialog(contentPane);
                File f = fileChooser.getSelectedFile();
                try{
                    RsaPrivateKey customPrivateKey = PemReader.getPrivateKeyFromPem(f);
                    selectedPrivateKey = true;
                    selectedCustomPrivateKey = customPrivateKey;
                    displayFilePath.setText(f.getAbsolutePath());
                    selectPrivateKey.setEditable(false);
                } catch(Exception exception){
                    displayFilePath.setText("Could not load selected file.");
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String encryptedMessage = messageToDecrypt.getText();
                if(!selectedPrivateKey) {
                    RsaKeyPair selectedKeyPair = rsaKeyPairManager.getKeyPairByIndex(selectPrivateKey.getSelectedIndex());
                    String decryptedMessage = selectedKeyPair.decryptMessage(encryptedMessage);
                    displayDecryptedMessage.setText(decryptedMessage);
                }else{
                    BigInteger m = new BigInteger(encryptedMessage);
                    BigInteger decryptedM = selectedCustomPrivateKey.decryptMessage(m);
                    String message = new String(decryptedM.toByteArray());
                    displayDecryptedMessage.setText(message);
                }
            }
        });

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(525,300));
        pack();
        setVisible(true);
    }
}
