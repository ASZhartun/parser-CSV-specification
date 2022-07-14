package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestSimpleGUI extends JFrame {
    private JButton load = new JButton("Load csv-file");

    public static void main(String[] args) {
        //default settings
        final TestSimpleGUI gui = new TestSimpleGUI();
        gui.setSize(128,128);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setResizable(false);

        gui.setLayout(new GridLayout(1,1));
        gui.add(gui.load);

        gui.load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.load.setText("Hui");
                final JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jFileChooser.showOpenDialog(jFileChooser);
                final File file = jFileChooser.getSelectedFile();
                final FileReader fileReader;
                try {
                    fileReader = new FileReader(file);
                    final BufferedReader bufferedReader = new BufferedReader(fileReader);
                    bufferedReader.lines().forEach(System.out::println);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }
}
