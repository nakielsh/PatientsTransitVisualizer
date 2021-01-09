package ptv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ptv.views.ResponsiveCanvas;
import ptv.views.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class Controller {
    private View view;
    private TextField xCoord;
    private TextField yCoord;
    @FXML
    private Label cursorLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ResponsiveCanvas canvas;

    public Controller() {
    }

    @FXML
    public void initialize() {

        this.view = new View(canvas);
    }

    @FXML
    public void loadMap() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();//+ "Code/src/main/resources/dataSets";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File mapFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (mapFile != null) {
            this.view.loadMap(mapFile.getAbsolutePath());
        }
    }

    @FXML
    private void loadPatientFromFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString(); //+ "/src/main/resources/dataSets";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File patientsFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (patientsFile != null) {
            this.view.addPatientsList(patientsFile.getAbsolutePath());
            this.view.paintMap();
        }


    }

    private void loadPatientFromCoordinates() {

    }

    private void loadPatientFromMap(MouseEvent mouseEvent) {
    }

    @FXML
    private void handleCursor(MouseEvent mouseEvent) {
        if (this.view == null) {
            return;
        }
        double mouseX = mouseEvent.getX() / this.view.countAffine();
        double mouseY = mouseEvent.getY() / this.view.countAffine();
        String cursorFormat = "Cursor(%.2f; %.2f)";
        this.cursorLabel.setText(String.format(cursorFormat, mouseX, mouseY));
    }


    private void startSimulation() {
    }

    private void stopSimulation() {
    }

    private void getSimulationSpeed() {
    }


}
