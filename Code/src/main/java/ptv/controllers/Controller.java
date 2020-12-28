package ptv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ptv.views.View;

import java.io.File;
import java.io.FileNotFoundException;

public class Controller {
    private View view;
    private TextField xCoord;
    private TextField yCoord;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Canvas canvas;

    private void loadMap(String FilePath){
        System.out.println("hello");

    }

    private void loadPatientFromFile(String FilePath){}

    private void loadPatientFromCoordinates(){}

    private void loadPatientFromMap(MouseEvent mouseEvent){}

    private void handleCursor(MouseEvent mouseEvent){}

    private void startSimulation(){}

    private void stopSimulation(){}

    private void getSimulationSpeed(){}

    private Point2D getSimulationCoordinates(MouseEvent mouseEvent){
        return null;
    }

    public void loadMap(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File mapFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (mapFile != null) {
            View view = new View(mapFile.getAbsolutePath(), canvas);
        }

    }
}
