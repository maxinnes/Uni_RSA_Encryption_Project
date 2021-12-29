package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecryptMessageDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea messageToDecrypt;
    private JButton exitButton;
    private JButton decryptButton;
    private JTextArea displayDecryptedMessage;
    private JComboBox selectPrivateKey;

    public DecryptMessageDialog(RsaKeyPairManager rsaKeyPairManager) {
        // Update components
        messageToDecrypt.setLineWrap(true);
        for(String keyPairName : rsaKeyPairManager.getListOfNames()){
            selectPrivateKey.addItem(keyPairName);
        }

        // set up events
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String encryptedMessage = messageToDecrypt.getText();
                RsaKeyPair selectedKeyPair = rsaKeyPairManager.getKeyPairByIndex(selectPrivateKey.getSelectedIndex());
                String decryptedMessage = selectedKeyPair.decryptMessage(encryptedMessage);
                displayDecryptedMessage.setText(decryptedMessage);
            }
        });

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(400,300));
        pack();
        setVisible(true);
    }
}
