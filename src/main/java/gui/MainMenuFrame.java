package gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private JLabel title;


    public MainMenuFrame() throws HeadlessException {

    }

    public void init() {
        this.add(title);
        this.pack();
    }

    @Autowired
    @Qualifier("frameTitle")
    public void setTitle(JLabel title) {
        this.title = title;
    }

}
