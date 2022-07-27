package service;

import config.EntityConfiguration;
import config.ServiceConfiguration;

import entities.RebarCage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        // содержимое окна программы
        final Scene scene = new Scene(getBody());
        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Возвращает объект, содержащий весь контент окна приложения
     *
     * @return GridPane
     */
    private GridPane getBody() {
        final GridPane body = new GridPane();
//        body.setGridLinesVisible(true);

        // Заголовк программы
        HBox header = getHeader();

        // Основаная часть интерфейса программы
        GridPane main = getMainSection();

        // блок с подписью
        HBox footer = getFooter();

        body.add(header, 0, 0);
        body.add(main, 0, 1);
        body.add(footer, 0, 2);
//        body.setPadding(new Insets(200,-SMALL_GAP0,SMALL_GAP0,SMALL_GAP0));
        body.setAlignment(Pos.CENTER);
        body.prefHeight(600);
        body.prefWidth(800);
        return body;
    }

    /**
     * Возвращает объект блока, содержащего вспомогательную информацию
     *
     * @return HBox
     */
    private HBox getFooter() {
        final Label sign = new Label("as.zhartun@gmail.com, 2022");
        final HBox footer = new HBox(sign);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 20, 20, 20));
        sign.setAlignment(Pos.CENTER);
        return footer;
    }

    /**
     * Возвращает объект блока, содержащего:
     * <ul>
     *     <li>блок с выбором csv спецификации</li>
     *     <li>блок с расчетом спецификации</li>
     *     <li>блок с функционалом библиотекаря</li>
     * </ul>
     *
     * @return GridPane
     */
    private GridPane getMainSection() {
        final GridPane main = new GridPane();

        //Поле с путем до сиэсви файла + кнопка "Выбрать"
        HBox choseFileBlock = getChoseFileBlock();

        // блок с кнопкой для запуска
        HBox runBlock = getRunBLock();

        // блок библиотекаря
        GridPane librarianBlock = createLibrarianBlock();

        main.add(choseFileBlock, 0, 0);
        main.add(runBlock, 0, 1);
        main.add(librarianBlock, 0, 2);
        main.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        main.setVgap(SMALL_GAP);
        return main;
    }

    /**
     * Возвращает объект блока, содержащего блоки функционала библиотекаря.
     *
     * @return GridPane
     */
    private GridPane createLibrarianBlock() {
        // блок библиотекаря
        final GridPane librarianBlock = new GridPane();
        librarianBlock.setVgap(SMALL_GAP);
        librarianBlock.setPadding(new Insets(10, 10, 10, 10));
        // заголовок библиотекаря
        HBox libreTitleBlock = getLibreTitleBlock();
        // таблица с дополнительными КЖ.И
        TableView<RebarCage> viewRebarMeshes = getViewRebarMeshes();
        // блок с инструментами библиотекаря
        final GridPane librarianTools = new GridPane();
        // поле с кнопкой добавление новой КЖ.И
        HBox addingRCPBlock = getAddingRCPBlock();
        // поле с кнопкой изменения имени выделенной в таблице КЖ.И, с кнопкой удаление выделенной в таблице КЖ.И
        HBox updatingRCPBlock = getUpdatingRCPBlock();
        librarianBlock.add(libreTitleBlock, 0, 0);
//        librarianBlock.add(viewRebarMeshes, 0, 1);
        librarianBlock.add(addingRCPBlock, 0, 2);
        librarianBlock.add(updatingRCPBlock, 0, 3);
        return librarianBlock;
    }

    /**
     * Возвращает объект блока, содержащего таблицу-отображение базы каркасов, хранимых библиотекарем
     *
     * @return TableView
     */
    private TableView<RebarCage> getViewRebarMeshes() {
        final ObservableList<RebarCage> rebarMeshes =
                FXCollections.observableArrayList(librarian.getExtraUnitStorage().getExtraUnits());
        final TableView<RebarCage> viewRebarMeshes = new TableView<>(rebarMeshes);
        viewRebarMeshes.prefHeight(300);
        viewRebarMeshes.prefWidth(300);

        final TableColumn<RebarCage, String> nameColumn = new TableColumn<>("Name");
        final TableColumn<RebarCage, Double> weightColumn = new TableColumn<>("Weight");
        final TableColumn<RebarCage, String> docColumn = new TableColumn<>("doc");
        try {
            librarian.getExtraUnitStorage().getExtraUnits().forEach((cage) -> {
                nameColumn.setCellValueFactory(new PropertyValueFactory<>(cage.getTitle()));
                weightColumn.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(cage.getUnitWeight())));
                docColumn.setCellValueFactory(new PropertyValueFactory<>(cage.getDoc()));
            });
        } catch (NullPointerException e) {
            System.out.println("Не удалось закинуть в таблицу хранилище библиотекаря из-за null");
        } catch (Exception e) {
            System.out.println("Ошибка с таблицей библиотекаря из-за хз чего");
        }
        viewRebarMeshes.getColumns().addAll(nameColumn, weightColumn, docColumn);
        return viewRebarMeshes;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего:
     * <ul>
     *     <li>текстовое поле с именем доп. арматурного каркаса (хранимого библиотекарем)</li>
     *     <li>кнопка для подтверждения изменения имени каркаса</li>
     *     <li>кнопка удаления каркаса из базы библиотекаря</li>
     * </ul>
     *
     * @return HBox
     */
    private HBox getUpdatingRCPBlock() {
        final TextField name = new TextField();
        final Button edit = new Button("Rename");
        final Button delete = new Button("Delete");
        final HBox updatingRCPBlock = new HBox(name, edit, delete);
        updatingRCPBlock.setAlignment(Pos.CENTER);
        updatingRCPBlock.setSpacing(SMALL_GAP);
        updatingRCPBlock.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        return updatingRCPBlock;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего блок с текстовым полем для пути добавляемого csv файла и кнопкой
     * "Добавить"
     *
     * @return HBox
     */
    private HBox getAddingRCPBlock() {
        final TextField newItem = new TextField();
        final Button addRCP = new Button("Add RCP");
        final HBox addingRCPBlock = new HBox(newItem, addRCP);
        addingRCPBlock.setSpacing(SMALL_GAP);
        addingRCPBlock.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        addingRCPBlock.setAlignment(Pos.CENTER);
        return addingRCPBlock;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего заголовок блока библиотекаря.
     *
     * @return HBox
     */
    private HBox getLibreTitleBlock() {
        final Label libreTitle = new Label("Дополнительные КЖ.И");
        libreTitle.setAlignment(Pos.CENTER);
        final HBox libreTitleBlock = new HBox(libreTitle);
        libreTitleBlock.setAlignment(Pos.CENTER);
        return libreTitleBlock;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего кнопку запуска расчета неполной спецификации.
     *
     * @return HBox
     */
    private HBox getRunBLock() {
        final Button run = new Button("Run");
        final HBox runBlock = new HBox(run);
        runBlock.setAlignment(Pos.CENTER);
        return runBlock;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего поле с путем к csv файлу и кнопку выбора файла в ОС.
     *
     * @return HBox
     */
    private HBox getChoseFileBlock() {
        final TextField path = new TextField("Введите путь");
        final Button choose = new Button("Choose file");
        final HBox choseFileBlock = new HBox(path, choose);
        choseFileBlock.setAlignment(Pos.CENTER);
        choseFileBlock.setSpacing(SMALL_GAP);
        return choseFileBlock;
    }

    /**
     * Возвращает объект блока интерфейса, содержащего заголовок программы.
     *
     * @return HBox
     */
    private HBox getHeader() {
        final Label title = new Label(APP_TITLE);
        title.setAlignment(Pos.TOP_CENTER);
        final HBox header = new HBox();
        header.getChildren().addAll(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 20, 20, 20));
        return header;
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
