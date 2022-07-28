package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;

import entities.RebarCage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;
import service.extra.Librarian;

import java.io.File;
import java.net.URL;

public class Director extends Application {

    //TODO: separate business logic and JavaFx Controller

    private Operator operator;
    private Librarian librarian;

    @FXML
    private Button addRPCButton;

    public static void main(String[] args) {
        launch(args);
    }

    public Director() {

    }

    @Override
    public void init() throws Exception {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class);
        context.register(ServiceConfiguration.class);
        context.refresh();
        this.operator = (Operator) context.getBean("operator");
        this.librarian = (Librarian) context.getBean("librarian");
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/gui/main.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void addRPC() {
        final Window window = addRPCButton.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите csv файл со спецификацией");
        if (window != null) {
            final File file = fileChooser.showOpenDialog(window);
            librarian.addNewRebarCageFrom(file.getPath());
            addRPCButton.setText("Ладное");
        } else {
            addRPCButton.setText("Неладное");
        }
    }

    public void deleteRPC() {

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
