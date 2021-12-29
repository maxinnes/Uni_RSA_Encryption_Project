package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncryptMessageDialog extends JDialog {
    private JPanel contentPane;
    private JComboBox publicKeySelector;
    private JButton exitButton;
    private JButton encryptButton;
    private JTextArea messageToEncrypt;
    private JTextArea displayEncryptedMessage;
    private JButton copyButton;

    public EncryptMessageDialog(RsaKeyPairManager rsaKeyPairManager) {
        // Update components
        for(String keyPairName : rsaKeyPairManager.getListOfNames()){
            publicKeySelector.addItem(keyPairName);
        }
        displayEncryptedMessage.setLineWrap(true);
        displayEncryptedMessage.setWrapStyleWord(true);
        messageToEncrypt.setLineWrap(true);

        // Set up events
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String textToEncrypt = messageToEncrypt.getText();
                int selectedIndex = publicKeySelector.getSelectedIndex();
                RsaKeyPair selectedKeyPair = rsaKeyPairManager.getKeyPairByIndex(selectedIndex);
                String encryptedMessage = selectedKeyPair.encryptMessage(textToEncrypt);
                displayEncryptedMessage.setText(encryptedMessage);
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
        setPreferredSize(new Dimension(400,300));
        pack();
        setVisible(true);
    }
}
