package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;

public class RsaHome {

    private JPanel mainPanel;
    private JButton encryptSignButton;
    private JButton decryptVerifyButton;
    private JButton exportMyKeyButton;
    private JButton viewMyContactsButton;

    public RsaHome(){
        JFrame test = new JFrame("Rsa Tool");
        test.add(mainPanel);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        test.setSize(new Dimension(550,700));
//        test.setMaximumSize(new Dimension(550,700));
        test.setMinimumSize(new Dimension(550,700));
        test.setResizable(false);
        // Get look and feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        test.pack();
        test.setVisible(true);
    }

    public static void main(String[] args) {
        RsaHome test1 = new RsaHome();
    }

}
