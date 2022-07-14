package gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.*;

public class TitlePanel extends JPanel {
    private JLabel title;

    public void init() {
        this.add(title);
    }

    @Autowired
    @Qualifier("mainTitle")
    public void setTitle(JLabel title) {
        this.title = title;
    }
}
