package ui;

import javax.swing.*;

public class TestRun {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final ParserSpec parserSpec = new ParserSpec();
            parserSpec.pack();
            parserSpec.setVisible(true);
        });
    }
}
