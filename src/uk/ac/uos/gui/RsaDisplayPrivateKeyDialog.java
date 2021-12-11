package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RsaDisplayPrivateKeyDialog extends JDialog {
    private JPanel contentPane;
    private JLabel modulusField;
    private JLabel publicExponentField;
    private JLabel privateExponentField;
    private JLabel firstPrimeField;
    private JLabel secondPrimeField;

    public RsaDisplayPrivateKeyDialog(String modulus, String publicExponent, String privateExponent, String firstPrimeNumber, String secondPrimeNumber) {
        // Make exit button work
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                dispose();
            }
        });

        // Close window once escape key is called
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Create button functionality here

        // Set text
        modulusField.setText(modulus);
        publicExponentField.setText(publicExponent);
        privateExponentField.setText(privateExponent);
        firstPrimeField.setText(firstPrimeNumber);
        secondPrimeField.setText(secondPrimeNumber);

        // Configure dialog
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(500,175));
        setResizable(true);
        pack();
    }
}
