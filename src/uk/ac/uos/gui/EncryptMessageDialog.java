package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPairManager;

import javax.swing.*;
import java.awt.*;

public class EncryptMessageDialog extends JDialog {
    private JPanel contentPane;
    private JComboBox publicKeySelector;
    private JButton exitButton;
    private JButton encryptButton;
    private JTextArea textArea1;

    public EncryptMessageDialog(RsaKeyPairManager rsaKeyPairManager) {
        // Update components
        for(String keyPairName : rsaKeyPairManager.getListOfNames()){
            publicKeySelector.addItem(keyPairName);
        }

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(350,300));
        pack();
        setVisible(true);
    }
}
