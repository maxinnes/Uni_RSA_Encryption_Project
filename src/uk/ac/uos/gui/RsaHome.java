package uk.ac.uos.gui;

import uk.ac.uos.rsa.RsaKeyPairManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RsaHome {

    private JPanel mainPanel;
    private JButton generateNewKeyPairButton;
    private JList listOfKeyPairs;
    private JButton encryptButton;
    private JButton decryptButton;

    public RsaHome(RsaKeyPairManager rsaKeyPairManager){
        // Construct frame
        JFrame test = new JFrame("Rsa Tool");
        test.add(mainPanel);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setMinimumSize(new Dimension(550,300));
        test.setResizable(false);

        // Construct components
        listOfKeyPairs.setListData(rsaKeyPairManager.getListOfNames());

        // Add events
        generateNewKeyPairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GenerateNewPairKeyMenu(rsaKeyPairManager);
                listOfKeyPairs.setListData(rsaKeyPairManager.getListOfNames());
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EncryptMessageDialog(rsaKeyPairManager);
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DecryptMessageDialog(rsaKeyPairManager);
            }
        });
        listOfKeyPairs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //System.out.println("Event - " + listOfKeyPairs.getSelectedIndex());
                if(listOfKeyPairs.getSelectedIndex()!=-1) {
                    new RsaDisplayKeyPairDialog(listOfKeyPairs.getSelectedIndex(), rsaKeyPairManager);
                    listOfKeyPairs.setListData(rsaKeyPairManager.getListOfNames());
                }
            }
        });

        // Create frame
        test.pack();
        test.setVisible(true);
    }
}
