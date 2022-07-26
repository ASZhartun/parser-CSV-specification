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
        // Заголовк программы
        final Label title = new Label("Расчет спецификации 1.0");
        title.setAlignment(Pos.TOP_CENTER);
        final HBox header = new HBox();
        header.getChildren().addAll(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20,20,20,20));

        // Основаная часть интерфейса программы
        final GridPane main = new GridPane();

        //Поле с путем до сиэсви файла + кнопка "Выбрать"
        final TextField path = new TextField("Введите путь");
        final Button choose = new Button("Choose file");
        final HBox choseFileBlock = new HBox(path, choose);
        choseFileBlock.setAlignment(Pos.CENTER);
        choseFileBlock.setSpacing(5);
        // блок с кнопкой для запуска
        final Button run = new Button("Run");
        final HBox runBlock = new HBox(run);
        runBlock.setAlignment(Pos.CENTER);
        // блок библиотекаря
        final GridPane librarianBlock = new GridPane();
        // заголовок библиотекаря
        final Label libreTitle = new Label("Дополнительные КЖ.И");
        // таблица с дополнительными КЖ.И
        final ObservableList<String> rebarMeshes = FXCollections.observableArrayList("Каркас КР1", "Каркас КР2");
        final TableView<String> viewRebarMeshes = new TableView<>(rebarMeshes);
        viewRebarMeshes.prefHeight(300);
        viewRebarMeshes.prefWidth(300);
        // поле с кнопкой добавление новой КЖ.И

        // поле с кнопкой изменения имени выделенной в таблице КЖ.И, с кнопкой удаление выделенной в таблице КЖ.И

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
