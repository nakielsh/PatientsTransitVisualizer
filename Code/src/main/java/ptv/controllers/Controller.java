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
    @FXML
    private ToggleButton toggle_add;
    @FXML
    private ToggleButton start_simulation;
    @FXML
    private ToggleButton stop_simulation;
    @FXML
    private Slider simulation_speed;
    @FXML
    private TextArea text;
    @FXML
    private RadioButton drawDistances;

    //TODO
    // change names of variables with '_'

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
        if(isSimulationRunning){
            printAlert(new Exception("Simulation is running"));
            return;
        }

        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();//+ "Code/src/main/resources/dataSets";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File mapFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (mapFile != null) {
            loadMapFromFile(mapFile.getAbsolutePath());
        }
        text.clear();
    }


    public void loadMapFromFile(String filePath) throws FileNotFoundException {
        CountryFileReader countryFileReader = new CountryFileReader();
        try {
            Country country = countryFileReader.readFile(filePath);
            country.setJunctionsList(new JunctionFinder().findJunctions(country.getDistancesList()));
            this.country = country;
            this.view.setCountry(country);
            simulator.setCountry(country);
            view.setIsLoadedMap(true);
            view.paintMap();
        } catch (IllegalArgumentException exception) {
            printAlert(exception);
            initialize();
        }
    }


    @FXML
    private void loadPatientFromFile() {
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
                printAlert(exception);
            }
        }
    }

    public void printAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    public void printAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void addPatientsList(String filePath) throws Exception {
        if (country == null) {
            throw new Exception("Country file not loaded");
        }
        PatientsFileReader patientsFileReader = new PatientsFileReader();
        List<Patient> patients = patientsFileReader.readFile(filePath);

        String alert = country.addPatients(patients);
        if (!alert.equals("No patient has been removed")){
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
    private void onMousePressed(MouseEvent mouseEvent) throws Exception {
        if (toggle_add.isSelected()) {
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

//    private int findPosibleID() {
//        List<Integer> ids = new ArrayList<>();
//        int possibleId = -1;
//        for (Patient patient : country.getPatientList()) {
//            ids.add(patient.getId());
//        }
//        while (ids.contains(possibleId)) {
//            possibleId--;
//        }
//        return possibleId;
//    }


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
        if(country == null){
            printAlert(new Exception("Map is not loaded"));
            start_simulation.setSelected(false);
            stop_simulation.setSelected(true);
            return;
        }
        if(!start_simulation.isSelected()){
            start_simulation.setSelected(true);
            return;
        }

        isSimulationRunning = true;
        Thread simulationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                simulate();
            }
        });
        simulationThread.start();
    }

    private void simulate() {
        while (isSimulationRunning) {
            List<Patient> patients = country.getPatientList();
            boolean deletePatient = false;
            if (simulator.hasNextStep()) {
                Patient currentHandledPatient = country.getCurrentHandledPatient();
                Hospital currentVisitedHospital = country.getCurrentVisitedHospital();

                try {
                    simulator.nextStep();

                    if (country.getCurrentHandledPatient() == null && country.getCurrentVisitedHospital() == null) {
                        Platform.runLater(() -> text.appendText(" - Patient (" + currentHandledPatient.getId() + ") is accepted in hospital - " + currentVisitedHospital.getName() + "\n" +
                                "-----------------------------\n"));
                    }
                }catch(IllegalStateException e){
                    if(e.getMessage().equals("There is not available beds in any hospital")){
                        Platform.runLater(() -> text.appendText(" - Patient (" + currentHandledPatient.getId() + ") is waiting in queue in hospital - " + currentVisitedHospital.getName() + "\n" +
                                "-----------------------------\n"));
                    }
                }
            } else if (patients.size() != 0) {
                simulator.setHandledPatient(patients.get(0));
                deletePatient = true;
            }

            if(deletePatient) {
                Platform.runLater(() -> {
                    if(patients.size() > 0){
                        patients.remove(0);
                    }
                });
            }
            Platform.runLater(() -> canvas.redraw());

            try {
                Thread.sleep(getSimulationSpeed());//+ 1);
            } catch (InterruptedException e) {
                stopSimulation();
                Platform.runLater(() -> canvas.redraw());
                return;
            }
        }
    }

    @FXML
    private void stopSimulation() {
        if(!stop_simulation.isSelected()){
            stop_simulation.setSelected(true);
            return;
        }

        isSimulationRunning = false;
    }

    private int getSimulationSpeed() {
        double speed = simulation_speed.valueProperty().getValue();
        return (int) speed;
    }

    @FXML
    private void setDrawDistances(){
        view.setDrawDistancesValue(drawDistances.isSelected());
    }
}