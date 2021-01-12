package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import ptv.models.borders.InBorders;
import ptv.models.data.*;
import ptv.models.reader.CountryFileReader;
import ptv.models.reader.PatientsFileReader;
import ptv.models.simulation.Simulator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//need to append scale

public class View {
    private ResponsiveCanvas canvas;
    private Affine affine;
    private InBorders polygon;
    private Country country;
    private boolean isLoadedMap;
    private double scaleAffine;



    public View(ResponsiveCanvas canvas) {
        this.canvas = canvas;
        this.affine = new Affine();
        this.isLoadedMap = false;
        this.scaleAffine = 1;
    }

    public boolean getIsLoadedMap() {return this.isLoadedMap;}

    public void loadMap(String filePath) throws FileNotFoundException {
        CountryFileReader countryFileReader = new CountryFileReader();
        try {
            Country country = countryFileReader.readFile(filePath);
            country.setJunctionsList(new JunctionFinder().findJunctions(country.getDistancesList()));
            GrahamScan grahamScan = new GrahamScan();
            grahamScan.setAllPoints(GrahamScan.createPointsList(country.getHospitalsList(), country.getFacilitiesList()));
            grahamScan.countGrahamHull();
            this.setCountry(country);
            this.country.setPolygon(grahamScan.getPolygon());
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid data");
        }
        this.setIsLoadedMap(true);
        paintMap();
    }

    public void addPatientsList(String filePath) throws Exception {
        if (this.country == null) {

            throw new Exception("Country file not loaded");
        }

        PatientsFileReader patientsFileReader = new PatientsFileReader();
        List<Patient> patients = patientsFileReader.readFile(filePath);
        for (Patient patient : patients) {
            this.country.addPatient(patient);
        }

    }

    public void paintMap() {
        canvas.setView(this);
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
        allPoints = GrahamScan.createPointsList(this.country.getHospitalsList(),
                this.country.getFacilitiesList());

        GrahamScan grahamScan = new GrahamScan();
        grahamScan.setAllPoints(allPoints);
        grahamScan.countGrahamHull();
        List<Point2D> hull = grahamScan.getPolygon();


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

    public double countAffine() {
        double far = findDistance() + 4;
        double height = this.canvas.getHeight();
        double width = this.canvas.getWidth();
        return (Math.min(height, width)/far);
    }

    private Point2D findCenter() {
        double sumX = 0;
        double sumY = 0;
        List<Point2D> convexHull = this.country.getPolygon();
        if(convexHull.isEmpty()) {
            throw new IllegalArgumentException("convexHull can't be empty");
        }
        for (Point2D point : convexHull) {
            sumX = point.getX();
            sumY = point.getY();
        }
        double centerX = sumX / convexHull.size();
        double centerY = sumY / convexHull.size();
        return new Point2D(centerX, centerY);
    }

    private double findDistance() {
        double minX, maxX, minY, maxY;
        List<Point2D> convexHull = this.country.getPolygon();
        if(convexHull.isEmpty()) {
            throw new IllegalArgumentException("convexHull can't be empty");
        }
        minX = maxX = convexHull.get(0).getX();
        minY = maxY = convexHull.get(0).getY();
        for (int i=1; i<convexHull.size(); i++) {
            Point2D currentPoint = convexHull.get(i);
            if(currentPoint.getX() < minX) {
                minX = currentPoint.getX();
            }
            if(currentPoint.getX() > maxX) {
                maxX = currentPoint.getX();
            }
            if(currentPoint.getY() < minY) {
                minY = currentPoint.getX();
            }
            if(currentPoint.getY() > maxY) {
                maxY = currentPoint.getY();
            }
        }
        return Math.max(maxX - minX, maxY - minY);
    }


    public void setCountry(Country country){
        this.country = country;
        paintMap();
    }

    public Country getCountry(){return this.country;}

    public void setIsLoadedMap(boolean loadedMap) {isLoadedMap = loadedMap;}

    public Affine getAffine(){return this.affine;}

    public void setAffine(Affine affine){this.affine = affine;}

}

