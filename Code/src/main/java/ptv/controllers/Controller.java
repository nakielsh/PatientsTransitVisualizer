package ptv.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.FileChooser;
import ptv.models.data.Patient;
import ptv.views.ResponsiveCanvas;
import ptv.views.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private View view;
    @FXML
    private TextField xCoord;
    @FXML
    private TextField yCoord;
    @FXML
    private Label cursorLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ResponsiveCanvas canvas;
    private boolean isClickable = false;

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
    private void loadPatientFromFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString(); //+ "/src/main/resources/dataSets";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File patientsFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (patientsFile != null) {
            try {
                this.view.addPatientsList(patientsFile.getAbsolutePath());
                this.view.paintMap();
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Country file not loaded");
                alert.setContentText("First, add the country file");
                alert.showAndWait();
            }
        }
    }


    @FXML
    private void loadPatientFromCoordinates() {
        double mouseX = Double.parseDouble(xCoord.getText());
        double mouseY = Double.parseDouble(yCoord.getText());
        Patient addedPatient = new Patient(findPosibleID(), new Point2D(mouseX, mouseY));
        view.getSimulator().addPatient(addedPatient);
        view.paintMap();
    }

    @FXML
    private void loadPatientFromMap() {
        isClickable = true;

    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (isClickable) {
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            Patient addedPatient = new Patient(findPosibleID(), new Point2D(mouseX, mouseY));
            view.getSimulator().addPatient(addedPatient);
            view.paintMap();
        }
    }

    private Point2D getMapCoordinates(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        try {
            return this.view.getAffine().inverseTransform(mouseX, mouseY);
        } catch (NonInvertibleTransformException e){
            throw new RuntimeException("Non invertible transform");
        }
    }

    private int findPosibleID() {
        List<Integer> ids = new ArrayList<>();
        int possibleId = -1;
        for (Patient patient : view.getSimulator().getPatients()) {
            ids.add(patient.getId());
        }
        while (ids.contains(possibleId)) {
            possibleId--;
        }
        return possibleId;
    }


    @FXML
    private void handleCursor(MouseEvent mouseEvent) {
        if (this.view == null || !this.view.getIsLoadedMap()) {
            return;
        }
        String cursorFormat = "Cursor(%.2f; %.2f)";
        Point2D mapCoord = this.getMapCoordinates(mouseEvent);
        this.cursorLabel.setText(String.format(cursorFormat, mapCoord.getX(), mapCoord.getY()));
    }


    private void startSimulation() {
    }

    private void stopSimulation() {
    }

    private void getSimulationSpeed() {
    }


}
