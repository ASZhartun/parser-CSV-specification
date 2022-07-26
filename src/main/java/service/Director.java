package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;
import service.extra.Librarian;


import java.net.URL;

public class Director extends Application {
    private Operator operator;
    private Librarian librarian;

    public static void main(String[] args) {
        launch(args);
    }

    public Director() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        // �������� ���������
        final Label title = new Label("������ ������������ 1.0");
        title.setAlignment(Pos.TOP_CENTER);
        final HBox header = new HBox();
        header.getChildren().addAll(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20,20,20,20));

        // ��������� ����� ���������� ���������
        final GridPane main = new GridPane();

        //���� � ����� �� ������ ����� + ������ "�������"
        final TextField path = new TextField("������� ����");
        final Button choose = new Button("Choose file");
        final HBox choseFileBlock = new HBox(path, choose);
        choseFileBlock.setAlignment(Pos.CENTER);
        choseFileBlock.setSpacing(5);
        // ���� � ������� ��� �������
        final Button run = new Button("Run");
        final HBox runBlock = new HBox(run);
        runBlock.setAlignment(Pos.CENTER);
        // ���� ������������
        final GridPane librarianBlock = new GridPane();
        // ��������� ������������
        final Label libreTitle = new Label("�������������� ��.�");
        // ������� � ��������������� ��.�
        final ObservableList<String> rebarMeshes = FXCollections.observableArrayList("������ ��1", "������ ��2");
        final TableView<String> viewRebarMeshes = new TableView<>(rebarMeshes);
        viewRebarMeshes.prefHeight(300);
        viewRebarMeshes.prefWidth(300);
        // ���� � ������� ���������� ����� ��.�

        // ���� � ������� ��������� ����� ���������� � ������� ��.�, � ������� �������� ���������� � ������� ��.�

        librarianBlock.add(libreTitle,0,0);
        librarianBlock.add(viewRebarMeshes,0,1);


        main.add(choseFileBlock,0,0);
        main.add(runBlock,0,1);
        main.add(librarianBlock,0,2);
        main.setPadding(new Insets(5,5,5,5));
        main.setVgap(5);

        final GridPane body = new GridPane();
//        body.setGridLinesVisible(true);

        body.add(header, 0,0);
        body.add(main, 0,1);
//        body.setPadding(new Insets(200,-50,50,50));
        body.setAlignment(Pos.CENTER);
        body.prefHeight(600);
        body.prefWidth(800);
        final Scene scene = new Scene(body);
        scene.setRoot(body);
        stage.setScene(scene);
        stage.show();
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
