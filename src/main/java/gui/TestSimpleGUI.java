package gui;

import org.springframework.beans.factory.annotation.Autowired;
import service.base.Operator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TestSimpleGUI extends JFrame {
    private JButton load = new JButton("Load csv-file");
    private Operator operator;

    public void run() {
        //default settings
        final TestSimpleGUI gui = new TestSimpleGUI();
        gui.setLocation((getScreenSize().width - 256)/2, (getScreenSize().height-96)/2);
        gui.setSize(256, 96);
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setResizable(false);

//        gui.setLayout(new GridLayout(1, 1));
        gui.add(gui.load);

        gui.load.addActionListener(e -> {

            final JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jFileChooser.showOpenDialog(jFileChooser);
            final File file = jFileChooser.getSelectedFile();
            operator.doWork(file.getPath());
            gui.load.setVisible(false);
            final JLabel close = new JLabel("Закройте приложение");
            close.setHorizontalAlignment(SwingConstants.CENTER);
            gui.add(close);

        });
    }

    public Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public Operator getOperator() {
        return operator;
    }

    @Autowired
    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
