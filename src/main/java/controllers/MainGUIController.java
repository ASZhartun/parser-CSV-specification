package controllers;


import config.EntityConfiguration;
import config.ServiceConfiguration;
import entities.RebarCage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.Director;

import java.io.File;

import java.util.Random;

public class MainGUIController {

    private Director director;

    @FXML
    private Button addRPCButton;

    @FXML
    private Button deleteRPCButton;

    @FXML
    private Button generator;

    @FXML
    private Button fileButton;

    @FXML
    private Button calculation;

    @FXML
    private Button resumes;

    @FXML
    private Label filepath;

    @FXML
    private TableView<RebarCage> tableRPC;
    @FXML
    private TableColumn<RebarCage, String> name;
    @FXML
    private TableColumn<RebarCage, Double> weight;

    public MainGUIController() {
        System.out.println("Constructor of controller");
    }

    @FXML
    public void initialize() {
        this.director = initializeBusinessLogicPart();
        this.name.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("unitWeight"));
        this.tableRPC.setItems(FXCollections.observableList(director.getLibrarian().getExtraUnitStorage().getExtraUnits()));
        this.tableRPC.getSelectionModel();

        System.out.println("initialize of controller");
    }

    public Director initializeBusinessLogicPart() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class);
        context.register(ServiceConfiguration.class);
        context.refresh();
        return (Director) context.getBean("director");
    }

    @FXML
    public void chooseSpec() {
        final Window window = fileButton.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите csv файл со спецификацией");
        if (window != null) {
            final File file = fileChooser.showOpenDialog(window);
            filepath.setText(file.getPath());
            fileButton.setText("Ладное");
            this.tableRPC.refresh();
        } else {
            fileButton.setText("Неладное");
        }
    }

    @FXML
    public void calculateSpec() {
        final String filepath = this.filepath.getText();
        director.getOperator().doWork(filepath);
    }

    @FXML
    public void showResultFiles() {
        final Window window = resumes.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(director.getOperator().getReaderCSV().getPathFolder()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setTitle("Результаты расчета");
        if (window != null) {
            final File file = fileChooser.showOpenDialog(window);
        }
    }

    @FXML
    public void addRPC() {
        final Window window = addRPCButton.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите csv файл со спецификацией");
        if (window != null) {
            final File file = fileChooser.showOpenDialog(window);
            director.getLibrarian().addNewRebarCageFrom(file.getPath());
            addRPCButton.setText("Ладное");
            this.tableRPC.refresh();
        } else {
            addRPCButton.setText("Неладное");
        }
    }

    @FXML
    public void deleteRPC() {
        deleteRPCButton.setOnMouseReleased(event -> {
            final RebarCage selectedItem = this.tableRPC.getSelectionModel().getSelectedItem();
            this.director.getLibrarian().getExtraUnitStorage().getExtraUnits().remove(selectedItem);
            this.tableRPC.refresh();
        });
    }

    public RebarCage getRebarCage() {
        final Random random = new Random();
        final RebarCage rebarCage = new RebarCage();
        rebarCage.setTitle("Zalupa" + random.nextInt(0, 10) * 10);
        return rebarCage;
    }

    @FXML
    public void addZalupa() {
        generator.setOnMouseReleased(event -> {
            this.tableRPC.getItems().add(getRebarCage());
        });
    }

    public Director getDirector() {
        return director;
    }

    @Autowired
    public void setDirector(Director director) {
        this.director = director;
    }
}
