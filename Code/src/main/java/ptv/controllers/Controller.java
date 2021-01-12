package ptv.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ptv.models.data.Country;
import ptv.models.data.JunctionFinder;
import ptv.models.data.Patient;
import ptv.models.reader.CountryFileReader;
import ptv.models.reader.PatientsFileReader;
import ptv.models.simulation.Simulator;
import ptv.views.ResponsiveCanvas;
import ptv.views.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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

    private boolean isClickable;
    private Simulator simulator;
    private Country country;

    public Controller() {
        simulator = new Simulator();
        isClickable = false;
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
            loadMapFromFile(mapFile.getAbsolutePath());
        }
    }

    public void loadMapFromFile(String filePath) throws FileNotFoundException {
        CountryFileReader countryFileReader = new CountryFileReader();
        try {
            Country country = countryFileReader.readFile(filePath);
            country.setJunctionsList(new JunctionFinder().findJunctions(country.getDistancesList()));
            this.country = country;
            view.setCountry(country);
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid data");
        }
        view.appendScale();
        view.paintMap();
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
                addPatientsList(patientsFile.getAbsolutePath());
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

    public void addPatientsList(String filePath) throws Exception {
        if (country == null) {
            throw new Exception("Country file not loaded");
        }

        PatientsFileReader patientsFileReader = new PatientsFileReader();
        List<Patient> patients = patientsFileReader.readFile(filePath);
        country.addPatients(patients);
    }


    @FXML
    private void loadPatientFromCoordinates() {
        double mouseX = Double.parseDouble(xCoord.getText());
        double mouseY = Double.parseDouble(yCoord.getText());
        Patient addedPatient = new Patient(findPosibleID(), new Point2D(mouseX, mouseY));
        country.addPatient(addedPatient);
        view.paintMap();
    }

    @FXML
    private void loadPatientFromMap() {
        isClickable = true;

    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (isClickable) {
            double mouseX = mouseEvent.getX() / this.view.countAffine();
            double mouseY = mouseEvent.getY() / this.view.countAffine();
            Patient addedPatient = new Patient(findPosibleID(), new Point2D(mouseX, mouseY));
            country.addPatient(addedPatient);
            view.paintMap();
        }
    }

    private int findPosibleID() {
        List<Integer> ids = new ArrayList<>();
        int possibleId = -1;
        for (Patient patient : country.getPatientList()) {
            ids.add(patient.getId());
        }
        while (ids.contains(possibleId)) {
            possibleId--;
        }
        return possibleId;
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
