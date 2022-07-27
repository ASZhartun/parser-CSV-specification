package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;
import service.extra.Librarian;

public class Director extends Application {
    public static final String APP_TITLE = "Расчет спецификации 1.0";
    public static final int SMALL_GAP = 5;
    private Operator operator;
    private Librarian librarian;

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
        // Заголовк программы
        final Label title = new Label(APP_TITLE);
        title.setAlignment(Pos.TOP_CENTER);
        final HBox header = new HBox();
        header.getChildren().addAll(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 20, 20, 20));

        // Основаная часть интерфейса программы
        final GridPane main = new GridPane();

        //Поле с путем до сиэсви файла + кнопка "Выбрать"
        final TextField path = new TextField("Введите путь");
        final Button choose = new Button("Choose file");
        final HBox choseFileBlock = new HBox(path, choose);
        choseFileBlock.setAlignment(Pos.CENTER);
        choseFileBlock.setSpacing(SMALL_GAP);
        // блок с кнопкой для запуска
        final Button run = new Button("Run");
        final HBox runBlock = new HBox(run);
        runBlock.setAlignment(Pos.CENTER);
        // блок библиотекаря
        final GridPane librarianBlock = new GridPane();
        // заголовок библиотекаря
        final Label libreTitle = new Label("Дополнительные КЖ.И");
        libreTitle.setAlignment(Pos.CENTER);
        librarianBlock.setVgap(SMALL_GAP);
        final HBox libreTitleBlock = new HBox(libreTitle);
        libreTitleBlock.setAlignment(Pos.CENTER);
        librarianBlock.setPadding(new Insets(10, 10, 10, 10));
        // таблица с дополнительными КЖ.И
        final ObservableList<String> rebarMeshes = FXCollections.observableArrayList("Каркас КР1", "Каркас КР2");
        final TableView<String> viewRebarMeshes = new TableView<>(rebarMeshes);
        viewRebarMeshes.prefHeight(300);
        viewRebarMeshes.prefWidth(300);
        // блок с инструментами библиотекаря
        final GridPane librarianTools = new GridPane();
        // поле с кнопкой добавление новой КЖ.И
        final TextField newItem = new TextField();
        final Button addRCP = new Button("Add RCP");
        final HBox addingRCPBlock = new HBox(newItem, addRCP);
        addingRCPBlock.setSpacing(SMALL_GAP);
        addingRCPBlock.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        addingRCPBlock.setAlignment(Pos.CENTER);
        // поле с кнопкой изменения имени выделенной в таблице КЖ.И, с кнопкой удаление выделенной в таблице КЖ.И
        final TextField name = new TextField();
        final Button edit = new Button("Edit");
        final Button delete = new Button("Delete");
        final HBox updatingRCPBlock = new HBox(name, edit, delete);
        updatingRCPBlock.setAlignment(Pos.CENTER);
        updatingRCPBlock.setSpacing(SMALL_GAP);
        updatingRCPBlock.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));

        librarianBlock.add(libreTitleBlock, 0, 0);
        librarianBlock.add(viewRebarMeshes, 0, 1);
        librarianBlock.add(addingRCPBlock, 0, 2);
        librarianBlock.add(updatingRCPBlock, 0, 3);

        // блок с подписью
        final Label sign = new Label("as.zhartun@gmail.com, 2022");
        final HBox footer = new HBox(sign);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 20, 20, 20));
        sign.setAlignment(Pos.CENTER);

        main.add(choseFileBlock, 0, 0);
        main.add(runBlock, 0, 1);
        main.add(librarianBlock, 0, 2);
        main.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        main.setVgap(SMALL_GAP);

        final GridPane body = new GridPane();
//        body.setGridLinesVisible(true);

        body.add(header, 0, 0);
        body.add(main, 0, 1);
        body.add(footer, 0, 2);
//        body.setPadding(new Insets(200,-SMALL_GAP0,SMALL_GAP0,SMALL_GAP0));
        body.setAlignment(Pos.CENTER);
        body.prefHeight(600);
        body.prefWidth(800);
        final Scene scene = new Scene(body);
        scene.setRoot(body);
        stage.setScene(scene);
        stage.setTitle("Расчет спецификации 1.0");
        stage.setResizable(false);
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
