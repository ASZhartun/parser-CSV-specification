package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;
import service.extra.Librarian;

public class Director extends Application {
    private Operator operator;
    private Librarian librarian;

    public static void main(String[] args) {

    }

    public Director() {

    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Autowired
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Autowired
    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}
