package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import ptv.models.borders.InBorders;
import ptv.models.data.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//need to append scale

public class View {
    private ResponsiveCanvas canvas;
    private Affine affine;
    private InBorders polygon;
    private Country country;

    public View(ResponsiveCanvas canvas) {
        this.canvas = canvas;
        this.affine = new Affine();
    }

    public void paintMap() {
        canvas.setView(this);
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setTransform(this.affine);
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(0.1);
        for (int i = 0; i < this.canvas.getHeight(); i++) {
            g.strokeLine(i, 0, i, this.canvas.getWidth());
        }
        for (int i = 0; i < this.canvas.getWidth(); i++) {
            g.strokeLine(0, i, this.canvas.getHeight(), i);
        }
        this.paintPolygon(g);
        this.paintDistances(g);
        this.paintJunctions(g);
        this.paintHospitals(g);
        this.paintFacilities(g);
        this.paintPatient();
    }

    public void paintHospitals(GraphicsContext g) {
        Iterator<Hospital> iterator = country.getHospitalsList().iterator();
        Hospital currentHospital;
        double xCoord, yCoord;
        g.setFill(Color.RED);
        g.setStroke(Color.RED);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentHospital = iterator.next();
            xCoord = currentHospital.getCoordinates().getX();
            yCoord = currentHospital.getCoordinates().getY();
            g.strokeOval(xCoord - 0.4, yCoord - 0.4, 0.8, 0.8);
            g.setFill(Color.WHITE);
            g.fillOval(xCoord - 0.4, yCoord - 0.4, 0.8, 0.8);
        }

    }

    public void paintFacilities(GraphicsContext g) {
        Iterator<Facility> iterator = country.getFacilitiesList().iterator();
        Facility currentFacility;
        double xCoord, yCoord;
        g.setFill(Color.GREEN);
        g.setStroke(Color.GREEN);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentFacility = iterator.next();
            xCoord = currentFacility.getCoordinates().getX();
            yCoord = currentFacility.getCoordinates().getY();
            g.strokeOval(xCoord - 0.2, yCoord - 0.2, 0.4, 0.4);
            g.setFill(Color.WHITE);
            g.fillOval(xCoord - 0.2, yCoord - 0.2, 0.4, 0.4);
        }
    }

    public void paintDistances(GraphicsContext g) {
        Iterator<Distance> iterator = country.getDistancesList().iterator();
        Distance currentDistance;
        double firstXCoord, firstYCoord, secondXCoord, secondYCoord;
        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentDistance = iterator.next();
            firstXCoord = currentDistance.getFirstNode().getCoordinates().getX();
            firstYCoord = currentDistance.getFirstNode().getCoordinates().getY();
            secondXCoord = currentDistance.getSecondNode().getCoordinates().getX();
            secondYCoord = currentDistance.getSecondNode().getCoordinates().getY();
            g.strokeLine(firstXCoord, firstYCoord, secondXCoord, secondYCoord);
        }
    }

    public void paintJunctions(GraphicsContext g) {
        Iterator<Junction> iterator = country.getJunctionsList().iterator();
        Junction currentJunction;
        double junctionXCoord, junctionYCoord;
        g.setFill(Color.LIGHTBLUE);
        g.setStroke(Color.BLUE);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentJunction = iterator.next();
            junctionXCoord = currentJunction.getCoordinates().getX();
            junctionYCoord = currentJunction.getCoordinates().getY();
            g.strokeOval(junctionXCoord - 0.2, junctionYCoord - 0.2, 0.4, 0.4);
            g.fillOval(junctionXCoord - 0.2, junctionYCoord - 0.2, 0.4, 0.4);

        }
    }

    public void paintPolygon(GraphicsContext g) {
        List<Point2D> allPoints = new ArrayList<>();

        for (Hospital hospital : country.getHospitalsList()) {
            allPoints.add(hospital.getCoordinates());
        }

        for (Facility facility : country.getFacilitiesList()) {
            allPoints.add(facility.getCoordinates());
        }
        GrahamScan grahamScan = new GrahamScan();
        List<Point2D> hull = grahamScan.returnGrahamHull(allPoints);


        int nPoints = hull.size();
        double[] xCoords = new double[nPoints];
        double[] yCoords = new double[nPoints];

        for (int i = 0; i <= nPoints - 1; i++) {
            xCoords[i] = hull.get(i).getX();
            yCoords[i] = hull.get(i).getY();
        }
        xCoords[0] = hull.get(0).getX();
        yCoords[0] = hull.get(0).getY();

        g.setFill(Color.LIGHTGRAY);
        g.setGlobalAlpha(0.3);
        g.setStroke(Color.LIGHTGRAY);
        g.strokePolygon(xCoords, yCoords, nPoints);
        g.fillPolygon(xCoords, yCoords, nPoints);
        g.setGlobalAlpha(1);

    }

    public void paintPatient() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        Iterator<Patient> iterator = country.getPatientList().iterator();
        Patient currentPatient;
        double xCoord, yCoord;
        g.setFill(Color.RED);
        g.setStroke(Color.RED);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentPatient = iterator.next();
            xCoord = currentPatient.getCoordinates().getX();
            yCoord = currentPatient.getCoordinates().getY();
            g.strokeOval(xCoord - 0.2, yCoord - 0.2, 0.4, 0.4);
            g.setFill(Color.BLACK);
            g.fillOval(xCoord - 0.2, yCoord - 0.2, 0.4, 0.4);
        }
    }

    public void appendScale(){
        affine.appendScale(countAffine(), countAffine());
    }

    public int countAffine() {
        //need to add function
        return 20;
    }

    private Point2D findCenter() {
        return null;
    }

    public Affine getAffine() {
        return this.affine;
    }

    public void setCountry(Country country){
        this.country = country;
        paintMap();
    }
}

