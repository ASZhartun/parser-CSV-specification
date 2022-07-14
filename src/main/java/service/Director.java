package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;
import gui.TestSimpleGUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Director {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class, ServiceConfiguration.class);
        context.refresh();
        final Director director = (Director) context.getBean("director");
        director.run();
    }

    private TestSimpleGUI gui;

    public Director() {

    }

    public void run() {
        gui.run();
    }

    public TestSimpleGUI getGui() {
        return gui;
    }

    @Autowired
    public void setGui(TestSimpleGUI gui) {
        this.gui = gui;
    }
}
