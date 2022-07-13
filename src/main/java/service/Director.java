package service;

import config.EntityConfiguration;
import config.GUIConfiguration;
import config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;

import javax.swing.*;

public class Director {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class, ServiceConfiguration.class, GUIConfiguration.class);
        context.refresh();
        final Director director = (Director) context.getBean("director");
        director.run();
    }

    private Operator operator;
    private JFrame menu;

    public Director() {

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

    public JFrame getMenu() {
        return menu;
    }

    @Autowired
    public void setMenu(JFrame menu) {
        this.menu = menu;
    }
}
