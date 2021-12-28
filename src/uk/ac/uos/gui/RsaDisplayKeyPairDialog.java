package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPair;

import javax.swing.*;
import java.awt.*;

public class RsaDisplayKeyPairDialog extends JDialog {
    private JPanel contentPane;
    private JTextField keyPairNameField;
    private JLabel bitLengthField;

    public RsaDisplayKeyPairDialog(RsaKeyPair rsaKeyPair) {
        keyPairNameField.setText(rsaKeyPair.getKeyPairName());
        String keyBitLength = String.valueOf(rsaKeyPair.getKeyPairBitLength());
        bitLengthField.setText(keyBitLength);

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(400,200));
        pack();
        setVisible(true);
    }
}
