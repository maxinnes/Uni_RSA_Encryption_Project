package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;

public class GenerateNewPairKeyMenu extends JDialog{
    private JPanel mainPanel;
    private JComboBox keyPairBitLength;
    private JButton generateButton;
    private JTextField keyPairName;
    private JLabel errorText;

    public GenerateNewPairKeyMenu(RsaKeyPairManager rsaKeyPairManager){
        // Set combo box options
        keyPairBitLength.addItem(1024);
        keyPairBitLength.addItem(2048);
        keyPairBitLength.addItem(4096);

        // set up events
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int bitLength = (Integer) keyPairBitLength.getSelectedItem();
                String newKeypairName = keyPairName.getText();

                ArrayList<String> keyPairNames = new ArrayList<>();
                Collections.addAll(keyPairNames,rsaKeyPairManager.getListOfNames());

                if(Objects.equals(newKeypairName, "")){
                    errorText.setText("Key pair name cannot be empty");
                } else if(keyPairNames.contains(newKeypairName)){
                    errorText.setText("A keypair with this name already exists");
                }else{
                    RsaKeyPair newKeyPair = new RsaKeyPair(bitLength,newKeypairName);
                    try {
                        rsaKeyPairManager.addAdditionalKeyPair(newKeyPair);
                        dispose();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
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
