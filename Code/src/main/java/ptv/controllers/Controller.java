package ptv.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.FileChooser;

import ptv.models.data.Country;
import ptv.models.data.Hospital;
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
    @FXML
    private ToggleButton toggleAddButton;
    @FXML
    private ToggleButton startSimulationButton;
    @FXML
    private ToggleButton stopSimulationButton;
    @FXML
    private Slider simulationSpeedSlider;
    @FXML
    private TextArea text;
    @FXML
    private RadioButton drawDistances;

    private Simulator simulator;
    private Country country;
    private boolean isSimulationRunning;
    private int id = -1;

    public Controller() {
        simulator = new Simulator();
        isSimulationRunning = false;
    }

    @FXML
    public void initialize() {
        this.view = new View(canvas);
    }

    @FXML
    public void loadMap() throws FileNotFoundException {
        if (isSimulationRunning) {
            printAlert(new Exception("Simulation is running"));
            return;
        }

        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
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
            Country newCountry = countryFileReader.readFile(filePath);
            newCountry.setJunctionsList(new JunctionFinder().findJunctions(newCountry.getDistancesList()));
            this.country = newCountry;
            this.view.setCountry(country);
            simulator.setCountry(country);
            view.setIsLoadedMap(true);
            view.paintMap();
            text.clear();
        } catch (IllegalArgumentException exception) {
            printAlert(exception);
        }
    }

    @FXML
    private void loadPatientFromFile() {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File patientsFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (patientsFile != null) {
            try {
                addPatientsList(patientsFile.getAbsolutePath());
                this.view.paintMap();
            } catch (Exception exception) {
                printAlert(exception);
            }
        }
    }

    private void printAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    private void printAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addPatientsList(String filePath) throws Exception {
        if (country == null) {
            throw new Exception("Country file not loaded");
        }
        PatientsFileReader patientsFileReader = new PatientsFileReader();
        List<Patient> patients = patientsFileReader.readFile(filePath);

        String alert = country.addPatients(patients);
        if (!alert.equals("No patient has been removed")) {
            printAlert(alert);
        }
    }

    @FXML
    private void loadPatientFromCoordinates() {
        try {
            double mouseX = Double.parseDouble(xCoord.getText());
            double mouseY = Double.parseDouble(yCoord.getText());
            if (country == null) {
                throw new Exception("Country file not loaded");
            }
            if (!simulator.getInBorders().isInside(new Point2D(mouseX, mouseY))) {
                throw new Exception("Patient out of borders");
            }
            Patient addedPatient = new Patient(id--, new Point2D(mouseX, mouseY));
            country.addPatient(addedPatient);
            view.paintMap();
        } catch (Exception exception) {
            printAlert(exception);
        }

    }


    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (toggleAddButton.isSelected()) {
            Point2D mapCoord = this.getMapCoordinates(mouseEvent);
            try {
                if (country == null) {
                    throw new Exception("Country file not loaded");
                }
                if (!simulator.getInBorders().isInside(mapCoord)) {
                    throw new Exception("Patient out of borders");
                }
                Patient addedPatient = new Patient(id--, new Point2D(mapCoord.getX(), mapCoord.getY()));
                country.addPatient(addedPatient);
                view.paintMap();
            } catch (Exception exception) {
                printAlert(exception);
            }
        }
    }

    private Point2D getMapCoordinates(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        try {
            return this.view.getAffine().inverseTransform(mouseX, mouseY);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException("Non invertible transform");
        }
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

    @FXML
    private void startSimulation() {
        if (country == null) {
            printAlert(new Exception("Map is not loaded"));
            startSimulationButton.setSelected(false);
            stopSimulationButton.setSelected(true);
            return;
        }
        if (!startSimulationButton.isSelected()) {
            startSimulationButton.setSelected(true);
            return;
        }

        isSimulationRunning = true;
        Thread simulationThread = new Thread(this::simulate);
        simulationThread.start();
    }

    private void simulate() {
        while (isSimulationRunning) {
            List<Patient> patients = country.getPatientList();

            boolean deletePatient = makeSimulationStep(patients);

            if (deletePatient) {
                Platform.runLater(() -> {
                    if (patients.size() > 0) {
                        patients.remove(0);
                    }
                });
            }
            Platform.runLater(() -> canvas.redraw());

            try {
                Thread.sleep(getSimulationSpeed());
            } catch (InterruptedException e) {
                stopSimulation();
                Platform.runLater(() -> canvas.redraw());
                return;
            }
        }
    }

    private boolean makeSimulationStep(List<Patient> patients) {
        boolean deletePatient = false;
        if (simulator.hasNextStep()) {
            Patient currentHandledPatient = country.getCurrentHandledPatient();
            Hospital currentVisitedHospital = country.getCurrentVisitedHospital();

            try {
                simulator.nextStep();

                if (country.getCurrentHandledPatient() == null && country.getCurrentVisitedHospital() == null) {
                    Platform.runLater(() -> text.appendText(" - Patient (" + currentHandledPatient.getId() +
                            ") is accepted in hospital - " + currentVisitedHospital.getName() + "\n" + "-----------------------------\n"));
                }
            } catch (IllegalStateException e) {
                if (e.getMessage().equals("There is not available beds in any hospital")) {
                    Platform.runLater(() -> text.appendText(" - Patient (" + currentHandledPatient.getId() +
                            ") is waiting in queue in hospital - " + currentVisitedHospital.getName() + "\n" + "-----------------------------\n"));
                }
            }
        } else if (patients.size() != 0) {
            simulator.setHandledPatient(patients.get(0));
            deletePatient = true;
        }
        return deletePatient;
    }

    @FXML
    private void stopSimulation() {
        if (!stopSimulationButton.isSelected()) {
            stopSimulationButton.setSelected(true);
            return;
        }

        isSimulationRunning = false;
    }

    private int getSimulationSpeed() {
        double speed = simulationSpeedSlider.valueProperty().getValue() * -1 + simulationSpeedSlider.getMax();
        return (int) speed;
    }

    @FXML
    private void setDrawDistances() {
        view.setDrawDistancesValue(drawDistances.isSelected());
    }
}