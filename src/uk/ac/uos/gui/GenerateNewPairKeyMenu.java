package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import uk.ac.uos.rsa.RsaKeyPair;

public class GenerateNewPairKeyMenu extends JDialog{
    private JPanel mainPanel;
    private JComboBox keyPairBitLength;
    private JButton generateButton;
    private JTextField keyPairName;

    public GenerateNewPairKeyMenu(){
        // Set combo box options
        keyPairBitLength.addItem(2048);
        keyPairBitLength.addItem(4096);

        // set up events
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int bitLength = (Integer) keyPairBitLength.getSelectedItem();
                String newKeypairName = keyPairName.getText();

                RsaKeyPair newKeyPair = new RsaKeyPair(bitLength,newKeypairName);
            }
        });

        setContentPane(mainPanel);
        setModal(true);
        setPreferredSize(new Dimension(500,175));
        setResizable(true);
        pack();
        setVisible(true);
    }
}
