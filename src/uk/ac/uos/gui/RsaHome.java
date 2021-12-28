package uk.ac.uos.gui;

import uk.ac.uos.gui.*;
import uk.ac.uos.rsa.RsaKeyPairManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RsaHome {

    private JPanel mainPanel;
    private JButton generateNewKeyPairButton;
    private JList listOfKeyPairs;

    public RsaHome(RsaKeyPairManager rsaKeyPairManager){
        // Construct components
        listOfKeyPairs.setListData(rsaKeyPairManager.getListOfNames());

        // Add events
        generateNewKeyPairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateNewPairKeyMenu generateNewPairKeyMenu = new GenerateNewPairKeyMenu(rsaKeyPairManager);
            }
        });

        // Create frame
        JFrame test = new JFrame("Rsa Tool");
        test.add(mainPanel);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setMinimumSize(new Dimension(550,700));
        test.setResizable(false);
        test.pack();
        test.setVisible(true);
    }

    public static void main(String[] args) {
        //RsaHome test1 = new RsaHome();
    }

}
