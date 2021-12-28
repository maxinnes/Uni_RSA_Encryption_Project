package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;

public class GenerateNewPairKeyMenu extends JDialog{
    private JPanel mainPanel;
    private JComboBox keyPairBitLength;
    private JButton generateButton;
    private JTextField keyPairName;

    public GenerateNewPairKeyMenu(){
        // Set combo box options
        keyPairBitLength.addItem("2048");
        keyPairBitLength.addItem("4096");

        setContentPane(mainPanel);
        setModal(true);
        setPreferredSize(new Dimension(500,175));
        setResizable(true);
        pack();
        setVisible(true);
    }
}
