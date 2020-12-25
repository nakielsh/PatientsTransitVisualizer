package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Affine;
import ptv.models.borders.InBorders;
import ptv.models.simulation.Simulator;

public class View {
    private Canvas canvas;
    private Affine affine;
    private Simulator simulator;
    private InBorders polygon;

    public void paintMap(){}

    public void paintHospitals(){}

    public void paintFacilities(){}

    public void paintPatient(){}

    private void countAffine(){}

    private Point2D findCenter(){return null;}
}
