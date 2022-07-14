package gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private TitlePanel titlePanel;
    private BasicFunctionalPanel basics;

    public MainFrame() throws HeadlessException {

    }

    public void init() {
        titlePanel.init();
        this.add(titlePanel);
        this.add(basics);
        this.pack();
    }

    @Autowired
    public void setTitlePanel(TitlePanel titlePanel) {
        this.titlePanel = titlePanel;
    }

    @Autowired
    public void setBasics(BasicFunctionalPanel basics) {
        this.basics = basics;
    }

}
