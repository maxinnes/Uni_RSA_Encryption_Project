package uk.ac.uos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class RsaDisplayPublicKeyDialog extends JDialog {
    private JPanel contentPane;
    private JLabel modulusField;
    private JLabel publicExponentField;
    private JButton copyModulusButton;
    private JButton copyExponentButton;

    public RsaDisplayPublicKeyDialog(String modulus, String publicExponent){
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        copyModulusButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(modulus);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        copyExponentButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(publicExponent);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        // Set text
        modulusField.setText(modulus);
        publicExponentField.setText(publicExponent);

        // Configure dialog
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(500,175));
        setResizable(true);
        pack();
    }

    public static void main(String[] args) {
        RsaDisplayPublicKeyDialog test1 = new RsaDisplayPublicKeyDialog("1","2");
        test1.setVisible(true);
    }
}
