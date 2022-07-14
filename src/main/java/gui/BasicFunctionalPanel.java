package gui;

import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

public class BasicFunctionalPanel extends JPanel {
    private CalcSpecButton button;


    public void init() {
        this.add(button);
    }
    @Autowired
    public void setButton(CalcSpecButton button) {
        this.button = button;
    }
}
