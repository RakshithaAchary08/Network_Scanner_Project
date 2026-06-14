package com.networkscanner;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScannerGUI gui = new ScannerGUI();
            gui.setVisible(true);
        });
    }
}
