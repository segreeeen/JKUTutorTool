package at.hsol.ttool.gui;

import javax.swing.*;

public class GUI extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    GUI() {
        setTitle("JKU Tutor-Tool");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(new SelectionPanel());

        pack();
        setVisible(true);
    }
}
