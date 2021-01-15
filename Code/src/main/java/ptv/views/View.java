package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import ptv.models.borders.InBorders;
import ptv.models.data.*;

import java.util.*;

//need to append scale

public class View {
    private ResponsiveCanvas canvas;
    private Affine affine;
    private InBorders polygon;
    private Country country;
    private boolean isLoadedMap;
    private double scaleAffine;
    private Map<String, Double> extremeCoord;
    private Point2D p0;


    public View(ResponsiveCanvas canvas) {
        this.canvas = canvas;
        this.affine = new Affine();
        this.isLoadedMap = false;
        this.scaleAffine = 1;
        this.extremeCoord = new HashMap<>();
        this.p0 = new Point2D(0, 0);
    }

    public boolean getIsLoadedMap() {
        return this.isLoadedMap;
    }

    public void paintMap() {
        canvas.setView(this);
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        g.clearRect(this.p0.getX(), this.p0.getY(), canvas.getWidth(), canvas.getHeight());
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(0.1);
        for (int i = (int)this.p0.getX(); i < this.canvas.getHeight()+(int)this.p0.getY(); i++) {
            g.strokeLine(i, (int)this.p0.getY(), i, this.canvas.getWidth()+(int)this.p0.getX());
        }
        for (int i = (int)this.p0.getY(); i < this.canvas.getWidth()+(int)this.p0.getX(); i++) {
            g.strokeLine((int)this.p0.getX(), i, this.canvas.getHeight()+(int)this.p0.getY(), i);
        }
        this.paintPolygon(g);
        this.paintDistances(g);
        this.paintJunctions(g);
        this.paintHospitals(g);
        this.paintFacilities(g);
        this.paintPatient();
        paintSimulation(g);
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

    private void paintSimulation(GraphicsContext g){
        Hospital currentVisitedHospital = country.getCurrentVisitedHospital();
        Patient currentHandledPatient = country.getCurrentHandledPatient();

        if(currentVisitedHospital == null && currentHandledPatient != null) {
            paintCurrentHandledPatient(g, currentHandledPatient.getCoordinates());
        }
        else if(currentVisitedHospital != null){
            paintCurrentVisitedHospital(g, currentVisitedHospital.getCoordinates());
        }
    }

    private void paintCurrentVisitedHospital(GraphicsContext g, Point2D hospitalCoordinates){
        g.setStroke(Color.GREEN);
        g.setLineWidth(0.1);
        g.strokeOval(hospitalCoordinates.getX() - 0.5, hospitalCoordinates.getY() - 0.5, 1, 1);
    }

    private void paintCurrentHandledPatient(GraphicsContext g, Point2D patientCoordinates){
        double patientX = patientCoordinates.getX();
        double patientY = patientCoordinates.getY();

        g.setStroke(Color.GREEN);
        g.setFill(Color.BLACK);
        g.setLineWidth(0.1);

        g.fillOval(patientX - 0.2, patientY - 0.2, 0.4, 0.4);
        g.strokeOval(patientX - 0.2, patientY - 0.2, 0.4, 0.4);
    }

    public void countAffine() {
        double far = findDistance() + 2;
        double height = this.canvas.getHeight();
        double width = this.canvas.getWidth();
        this.setScaleAffine(Math.min(height, width)/far);
    }

    public void countTransformPoint() {
        double height = this.canvas.getHeight()/this.scaleAffine;
        double width = this.canvas.getWidth()/this.scaleAffine;
        Point2D canvasCenter = new Point2D(width/2, height/2);
        double mapXCenter = (this.extremeCoord.get("maxX") - this.extremeCoord.get("minX"))/2 + this.extremeCoord.get("minX");
        double mapYCenter = (this.extremeCoord.get("maxY") - this.extremeCoord.get("minY"))/2 + this.extremeCoord.get("minY");
        double distX = mapXCenter - canvasCenter.getX();
        double distY = mapYCenter - canvasCenter.getY();
        double transformX = this.extremeCoord.get("minX") - distX -2;
        double transformY = this.extremeCoord.get("minY") - distY - 2;
        System.out.println(p0);
        this.setP0(new Point2D(transformX, transformY));
    }



    private void countExtremePoints() {
        List<Point2D> convexHull = this.country.getPolygon();
        if(convexHull.isEmpty()) {
            throw new IllegalArgumentException("convexHull can't be empty");
        }
        double minX, maxX, minY, maxY;
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
        this.extremeCoord.put("minX", minX);
        this.extremeCoord.put("maxX", maxX);
        this.extremeCoord.put("minY", minY);
        this.extremeCoord.put("maxY", maxY);
    }

    private double findDistance() {
        return Math.max(this.extremeCoord.get("maxX") - this.extremeCoord.get("minX"),
                this.extremeCoord.get("maxY") - this.extremeCoord.get("minY"));
    }

    public void setCountry(Country country){
        this.country = country;
        GrahamScan grahamScan = new GrahamScan();
        grahamScan.setAllPoints(GrahamScan.createPointsList(country.getHospitalsList(), country.getFacilitiesList()));
        grahamScan.countGrahamHull();
        country.setPolygon(grahamScan.getPolygon());
        this.countExtremePoints();
    }

    public void setP0(Point2D p0) {this.p0 = p0;}

    public Point2D getP0(){return this.p0;}

    public Country getCountry(){return this.country;}

    public void setIsLoadedMap(boolean loadedMap) {isLoadedMap = loadedMap;}

    public Affine getAffine(){return this.affine;}

    public void setAffine(Affine affine){this.affine = affine;}

    public void setScaleAffine(double scaleAffine) {this.scaleAffine = scaleAffine;}

    public double getScaleAffine() {return this.scaleAffine;}
}

