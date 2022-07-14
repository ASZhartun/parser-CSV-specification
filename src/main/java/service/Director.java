package service;

import config.EntityConfiguration;
import config.GUIConfiguration;
import config.ServiceConfiguration;
import gui.MainMenuFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;

import javax.swing.*;

public class Director {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class, ServiceConfiguration.class, GUIConfiguration.class);
        context.refresh();
        final Director director = (Director) context.getBean("director");
        director.init();
        director.run();
    }

    private Operator operator;
    private MainMenuFrame menu;

    public Director() {

    }

    public void init() {
        menu.init();
    }

    public void run() {
        System.out.println("123");
    }

    public Operator getOperator() {
        return operator;
    }

    @Autowired
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public MainMenuFrame getMenu() {
        return menu;
    }

    @Autowired
    @Qualifier("menu")
    public void setMenu(MainMenuFrame menu) {
        this.menu = menu;
    }
}
