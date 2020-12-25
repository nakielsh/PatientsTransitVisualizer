package ptv.controllers;

import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ptv.views.View;

public class Controller {
    private View view;
    private TextField xCoord;
    private TextField yCoord;

    private void loadMap(String FilePath){}

    private void loadPatientFromFile(String FilePath){}

    private void loadPatientFromCoordinates(){}

    private void loadPatientFromMap(MouseEvent mouseEvent){}

    private void handleCursor(MouseEvent mouseEvent){}

    private void startSimulation(){}

    private void stopSimulation(){}

    private void getSimulationSpeed(){}

    private Point2D getSimulationCoordinates(MouseEvent mouseEvent){return null;}

}
