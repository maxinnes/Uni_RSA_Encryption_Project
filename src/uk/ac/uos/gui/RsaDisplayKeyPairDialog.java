package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPair;
import uk.ac.uos.rsa.RsaKeyPairManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RsaDisplayKeyPairDialog extends JDialog {
    private JPanel contentPane;
    private JTextField keyPairNameField;
    private JLabel bitLengthField;
    private JButton closeButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JLabel errorText;

    public RsaDisplayKeyPairDialog(int listIndex,RsaKeyPairManager rsaKeyPairManager) {
        // Get info
        RsaKeyPair rsaKeyPair = rsaKeyPairManager.getKeyPairByIndex(listIndex);
        // Update components
        keyPairNameField.setText(rsaKeyPair.getKeyPairName());
        String keyBitLength = String.valueOf(rsaKeyPair.getKeyPairBitLength());
        bitLengthField.setText(keyBitLength);

        // Create events
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    rsaKeyPairManager.deleteKeyPairByIndex(listIndex);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newName = keyPairNameField.getText();

                ArrayList<String> keyPairNames = new ArrayList<>();
                Collections.addAll(keyPairNames,rsaKeyPairManager.getListOfNames());

                if(Objects.equals(newName, "")){
                    errorText.setText("Key pair name cannot be empty");
                } else if(keyPairNames.contains(newName)){
                    errorText.setText("A keypair with this name already exists");
                }else{
                    try {
                        rsaKeyPairManager.renameKeyPair(listIndex,newName);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    dispose();
                }
            }
        });

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(400,200));
        pack();
        setVisible(true);
    }
}
