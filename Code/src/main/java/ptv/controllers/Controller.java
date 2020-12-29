package ptv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ptv.views.View;

import java.io.File;
import java.io.FileNotFoundException;
//TODO sprint 2
// MARTYNA
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class Controller {
    private View view;
    private TextField xCoord;
    private TextField yCoord;
    @FXML
    private Label cursorLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Canvas canvas;

    private void loadPatientFromFile(String FilePath){}

    private void loadPatientFromCoordinates(){}

    private void loadPatientFromMap(MouseEvent mouseEvent){}

    @FXML
    private void handleCursor(MouseEvent mouseEvent){
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        String cursorFormat = "Cursor(%.2f; %.2f)";
        this.cursorLabel.setText(String.format(cursorFormat, mouseX, mouseY));
    }


    private void startSimulation(){}

    private void stopSimulation(){}

    private void getSimulationSpeed(){}


    public void loadMap(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/resources/dataSets";
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File mapFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if (mapFile != null) {
            this.view = new View(mapFile.getAbsolutePath(), canvas);
        }

    }
}
