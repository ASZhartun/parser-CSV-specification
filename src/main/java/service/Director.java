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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;
import service.extra.Librarian;

import java.io.File;
import java.util.HashSet;

public class Director extends Application {
    public static final String APP_TITLE = "������ ������������ 1.0";
    public static final int SMALL_GAP = 5;

    private Stage stageForAll;
    private final ObservableList<RebarCage> rebarMeshes = FXCollections.observableArrayList();

    private Operator operator;
    private Librarian librarian;
    private TableView<RebarCage> libreTable;

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
        // ���������� ���� ���������
        stageForAll = stage;
        final Scene scene = new Scene(getBody());
        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * ���������� ������, ���������� ���� ������� ���� ����������
     *
     * @return GridPane
     */
    private GridPane getBody() {
        final GridPane body = new GridPane();
//        body.setGridLinesVisible(true);

        // �������� ���������
        HBox header = getHeader();

        // ��������� ����� ���������� ���������
        GridPane main = getMainSection();

        // ���� � ��������
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
     * ���������� ������ �����, ����������� ��������������� ����������
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
     * ���������� ������ �����, �����������:
     * <ul>
     *     <li>���� � ������� csv ������������</li>
     *     <li>���� � �������� ������������</li>
     *     <li>���� � ������������ ������������</li>
     * </ul>
     *
     * @return GridPane
     */
    private GridPane getMainSection() {
        final GridPane main = new GridPane();

        //���� � ����� �� ������ ����� + ������ "�������"
        HBox choseFileBlock = getChoseFileBlock();

        // ���� � ������� ��� �������
        HBox runBlock = getRunBLock();

        // ���� ������������
        GridPane librarianBlock = createLibrarianBlock();

        main.add(choseFileBlock, 0, 0);
        main.add(runBlock, 0, 1);
        main.add(librarianBlock, 0, 2);
        main.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        main.setVgap(SMALL_GAP);
        return main;
    }

    /**
     * ���������� ������ �����, ����������� ����� ����������� ������������.
     *
     * @return GridPane
     */
    private GridPane createLibrarianBlock() {
        // ���� ������������
        final GridPane librarianBlock = new GridPane();
        librarianBlock.setVgap(SMALL_GAP);
        librarianBlock.setPadding(new Insets(10, 10, 10, 10));
        // ��������� ������������
        HBox libreTitleBlock = getLibreTitleBlock();
        // ������� � ��������������� ��.�
        TableView<RebarCage> viewRebarMeshes = getViewRebarMeshes();
        // ���� � ������� ���������� ����� ��.�
        HBox addingRCPBlock = getAddingRCPBlock();
        // ���� � ������� ��������� ����� ���������� � ������� ��.�, � ������� �������� ���������� � ������� ��.�
        HBox updatingRCPBlock = getUpdatingRCPBlock();
        librarianBlock.add(libreTitleBlock, 0, 0);
        librarianBlock.add(viewRebarMeshes, 0, 1);
        librarianBlock.add(addingRCPBlock, 0, 2);
        librarianBlock.add(updatingRCPBlock, 0, 3);
        return librarianBlock;
    }

    /**
     * ���������� ������ �����, ����������� �������-����������� ���� ��������, �������� �������������
     *
     * @return TableView
     */
    private TableView<RebarCage> getViewRebarMeshes() {
        fillingEmpty();
        rebarMeshes.addAll(librarian.getExtraUnitStorage().getExtraUnits());
        final TableView<RebarCage> viewRebarMeshes = new TableView<>(rebarMeshes);
        viewRebarMeshes.setEditable(true);
        viewRebarMeshes.prefHeight(300);
        viewRebarMeshes.prefWidth(300);

        final TableColumn<RebarCage, String> nameColumn = new TableColumn<>("Name");
        final TableColumn<RebarCage, Double> weightColumn = new TableColumn<>("Weight");
//        final TableColumn<RebarCage, String> docColumn = new TableColumn<>("doc");
        try {
            rebarMeshes.forEach((cage) -> {
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                weightColumn.setCellValueFactory(new PropertyValueFactory<>(String.valueOf("unitWeight")));
//                docColumn.setCellValueFactory(new PropertyValueFactory<>(cage.getDoc()));
            });
        } catch (NullPointerException e) {
            System.out.println("�� ������� �������� � ������� ��������� ������������ ��-�� null");
        } catch (Exception e) {
            System.out.println("������ � �������� ������������ ��-�� �� ����");
        }
//        viewRebarMeshes.getColumns().addAll(nameColumn, weightColumn, docColumn);
        viewRebarMeshes.getColumns().addAll(nameColumn, weightColumn);
        viewRebarMeshes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.libreTable = viewRebarMeshes;

        return viewRebarMeshes;
    }

    private void fillingEmpty() {
        final HashSet<RebarCage> extraUnits = librarian.getExtraUnitStorage().getExtraUnits();
        final RebarCage rebarCage = new RebarCage();
        rebarCage.setTitle("empty");
        rebarCage.setUnitWeight(0d);
        extraUnits.add(rebarCage);
    }

    /**
     * ���������� ������ ����� ����������, �����������:
     * <ul>
     *     <li>��������� ���� � ������ ���. ����������� ������� (��������� �������������)</li>
     *     <li>������ ��� ������������� ��������� ����� �������</li>
     *     <li>������ �������� ������� �� ���� ������������</li>
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
     * ���������� ������ ����� ����������, ����������� ���� � ��������� ����� ��� ���� ������������ csv ����� � �������
     * "��������"
     *
     * @return HBox
     */
    private HBox getAddingRCPBlock() {
        final TextField newItem = new TextField();
        Button addRCP = createAddingRCPButton();
        Button refreshRCPTable = createRefreshingRCPButton();
        final HBox addingRCPBlock = new HBox(newItem, addRCP, refreshRCPTable);
        addingRCPBlock.setSpacing(SMALL_GAP);
        addingRCPBlock.setPadding(new Insets(SMALL_GAP, SMALL_GAP, SMALL_GAP, SMALL_GAP));
        addingRCPBlock.setAlignment(Pos.CENTER);
        return addingRCPBlock;
    }

    private Button createRefreshingRCPButton() {
        final Button refreshRCPTable = new Button("Refresh");
        refreshRCPTable.setOnMouseReleased(event -> {

        });
        return refreshRCPTable;
    }

    private Button createAddingRCPButton() {
        final Button addRCP = new Button("Add RCP");
        addRCP.setOnMouseReleased(event -> {
            final FileChooser fileChooser = new FileChooser();
            final File file = fileChooser.showOpenDialog(stageForAll);
            if (file != null) {
                librarian.addNewRebarCageFrom(file.getPath());
//                rebarMeshes.addAll(librarian.getExtraUnitStorage().getExtraUnits()); !!!��������!!!
                librarian.getExtraUnitStorage().getExtraUnits().stream().filter(item -> !rebarMeshes.contains(item)).forEach(rebarMeshes::add);

            }
        });
        return addRCP;
    }

    /**
     * ���������� ������ ����� ����������, ����������� ��������� ����� ������������.
     *
     * @return HBox
     */
    private HBox getLibreTitleBlock() {
        final Label libreTitle = new Label("�������������� ��.�");
        libreTitle.setAlignment(Pos.CENTER);
        final HBox libreTitleBlock = new HBox(libreTitle);
        libreTitleBlock.setAlignment(Pos.CENTER);
        return libreTitleBlock;
    }

    /**
     * ���������� ������ ����� ����������, ����������� ������ ������� ������� �������� ������������.
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
     * ���������� ������ ����� ����������, ����������� ���� � ����� � csv ����� � ������ ������ ����� � ��.
     *
     * @return HBox
     */
    private HBox getChoseFileBlock() {
        final TextField path = new TextField("������� ����");
        final Button choose = new Button("Choose file");
        final HBox choseFileBlock = new HBox(path, choose);
        choseFileBlock.setAlignment(Pos.CENTER);
        choseFileBlock.setSpacing(SMALL_GAP);
        return choseFileBlock;
    }

    /**
     * ���������� ������ ����� ����������, ����������� ��������� ���������.
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
