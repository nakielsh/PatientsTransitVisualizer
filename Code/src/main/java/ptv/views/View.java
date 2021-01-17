package ptv.views;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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
    private boolean drawDistancesValue;


    public View(ResponsiveCanvas canvas) {
        drawDistancesValue = true;
        this.canvas = canvas;
        canvas.setView(this);
        this.affine = new Affine();
        this.isLoadedMap = false;
        this.scaleAffine = 1;
        this.extremeCoord = new HashMap<>();
        this.p0 = new Point2D(0, 0);
    }

    public boolean getIsLoadedMap() {
        return this.isLoadedMap;
    }

    public void paintMap(){
        //canvas.setView(this);
        canvas.redraw();
    }

    public void paintObjectsOnMap() {
        canvas.setView(this);
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        this.countTransformPoint();
        this.affine.appendTranslation(-p0.getX(), -p0.getY());
        g.setTransform(this.affine);
        g.clearRect(this.p0.getX(), this.p0.getY(), canvas.getWidth(), canvas.getHeight());
        g.setStroke(Color.LIGHTGRAY);
        g.setLineWidth(0.05);
        for (int i = (int)this.p0.getX(); i < this.canvas.getHeight()+(int)this.p0.getY(); i+=5) {
            g.strokeLine(i, (int)this.p0.getY(), i, this.canvas.getWidth()+(int)this.p0.getX());
        }
        for (int i = (int)this.p0.getY(); i < this.canvas.getWidth()+(int)this.p0.getX(); i+=5) {
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
        String value;
        while (iterator.hasNext()) {
            currentHospital = iterator.next();
            xCoord = currentHospital.getCoordinates().getX();
            yCoord = currentHospital.getCoordinates().getY();
            value = "H" + (currentHospital.getId());
            paintLabel(g, value, Color.RED, xCoord, yCoord);
        }

    }

    private void paintLabel(GraphicsContext g, String value, Paint color, double xCoord, double yCoord) {
        double topBottomMargin = 25;
        double lengthOfLabel = (12.0* value.length() + 5.0);
        double pointSize = 7;
        g.setFont(new Font("Arial", 18.0/scaleAffine));
        g.setFill(Color.WHITE);
        g.fillRect(xCoord - lengthOfLabel/scaleAffine/2, yCoord - topBottomMargin*1.2/scaleAffine, lengthOfLabel/scaleAffine, topBottomMargin/scaleAffine );
        g.setFill(color);
        g.fillText(value, xCoord, yCoord-topBottomMargin*0.75/scaleAffine);
        g.setLineWidth(1/scaleAffine);
        g.fillOval(xCoord-pointSize/scaleAffine/2, yCoord-pointSize/scaleAffine/2, pointSize/scaleAffine, pointSize/scaleAffine);
        g.strokeRect(xCoord - lengthOfLabel/scaleAffine/2, yCoord - topBottomMargin*1.2/scaleAffine, lengthOfLabel/scaleAffine, topBottomMargin/scaleAffine);

    }

    public void paintFacilities(GraphicsContext g) {
        Iterator<Facility> iterator = country.getFacilitiesList().iterator();
        Facility currentFacility;
        double xCoord, yCoord;
        g.setFill(Color.GREEN);
        g.setStroke(Color.GREEN);
        g.setLineWidth(0.1);
        g.setFont(new Font("Arial", 0.5));
        String value;
        while (iterator.hasNext()) {
            currentFacility = iterator.next();
            xCoord = currentFacility.getCoordinates().getX();
            yCoord = currentFacility.getCoordinates().getY();
            value = "F" + (currentFacility.getId());
            this.paintLabel(g, value, Color.GREEN, xCoord, yCoord);
        }
    }

    public void paintDistances(GraphicsContext g) {
        Iterator<Distance> iterator = country.getDistancesList().iterator();
        Distance currentDistance;
        double firstXCoord, firstYCoord, secondXCoord, secondYCoord;
        Point2D labelPoint;
        String value;
//        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
//        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentDistance = iterator.next();
            firstXCoord = currentDistance.getFirstNode().getCoordinates().getX();
            firstYCoord = currentDistance.getFirstNode().getCoordinates().getY();
            secondXCoord = currentDistance.getSecondNode().getCoordinates().getX();
            secondYCoord = currentDistance.getSecondNode().getCoordinates().getY();
            g.strokeLine(firstXCoord, firstYCoord, secondXCoord, secondYCoord);

            if(drawDistancesValue){
                labelPoint = (findCentreOfSegment(currentDistance.getFirstNode().getCoordinates(), currentDistance.getSecondNode().getCoordinates()));
                double xCoord = labelPoint.getX();
                double yCoord = labelPoint.getY();
                value = String.valueOf((int)currentDistance.getDist());
                double topBottomMargin = 20;
                double lengthOfLabel = (8.0* value.length() + 7.0);
                double pointSize = 7;
                g.setFont(new Font("Arial", 15.0/scaleAffine));
                g.setFill(Color.BLACK);
                g.fillOval(xCoord - lengthOfLabel/scaleAffine/2, yCoord - topBottomMargin*0.5/scaleAffine, lengthOfLabel/scaleAffine, topBottomMargin/scaleAffine );
                g.setFill(Color.WHITE);
                g.fillText(value, xCoord, yCoord);
                g.setLineWidth(1/scaleAffine);
                g.strokeOval(xCoord - lengthOfLabel/scaleAffine/2, yCoord - topBottomMargin*0.5/scaleAffine, lengthOfLabel/scaleAffine, topBottomMargin/scaleAffine);
            }
        }
    }

    private static Point2D findCentreOfSegment(Point2D p1, Point2D p2) {
        return new Point2D((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY())/2);
    }

    public void paintJunctions(GraphicsContext g) {
        Iterator<Junction> iterator = country.getJunctionsList().iterator();
        Junction currentJunction;
        double xCoord, yCoord;
        g.setFill(Color.BLUE);
        g.setLineWidth(0.1);
        while (iterator.hasNext()) {
            currentJunction = iterator.next();
            xCoord = currentJunction.getCoordinates().getX();
            yCoord = currentJunction.getCoordinates().getY();
            int pointSize = 7;
//            g.strokeOval(junctionXCoord - 0.1, junctionYCoord - 0.1, 0.2, 0.2);
//            g.fillOval(junctionXCoord - 0.1, junctionYCoord - 0.1, 0.2, 0.2);
            g.fillOval(xCoord-pointSize/scaleAffine/2, yCoord-pointSize/scaleAffine/2, pointSize/scaleAffine, pointSize/scaleAffine);


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

        g.setFill(Color.LIGHTBLUE);
        g.setGlobalAlpha(0.3);
        g.setStroke(Color.GREEN);
        g.strokePolygon(xCoords, yCoords, nPoints);
        g.fillPolygon(xCoords, yCoords, nPoints);
        g.setGlobalAlpha(1);

    }

    public void paintPatient() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        Iterator<Patient> iterator = country.getPatientList().iterator();
        Patient currentPatient;
        double xCoord, yCoord, lengthOfLabel;
        String value;
        double topBottomMargin = 20;
        double pointSize = 5;
        g.setFont(new Font("Arial", 14.0/scaleAffine));
        while (iterator.hasNext()) {
            currentPatient = iterator.next();
            xCoord = currentPatient.getCoordinates().getX();
            yCoord = currentPatient.getCoordinates().getY();
            value = "P" + currentPatient.getId();
            lengthOfLabel = (12.0* value.length() + 5.0);
            g.setFill(Color.BLUE);
            g.fillOval(xCoord-pointSize/scaleAffine/2, yCoord-pointSize/scaleAffine/2, pointSize/scaleAffine, pointSize/scaleAffine);
            g.fillRect(xCoord - lengthOfLabel/scaleAffine/2, yCoord - topBottomMargin*1.2/scaleAffine, lengthOfLabel/scaleAffine, topBottomMargin/scaleAffine );
            g.setFill(Color.WHITE);
            g.fillText(value, xCoord, yCoord-topBottomMargin*0.75/scaleAffine);
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
        g.setStroke(Color.DARKBLUE);
        g.setLineWidth(5/scaleAffine);
        double pointSize = 10;
        double xCoord = hospitalCoordinates.getX();
        double yCoord = hospitalCoordinates.getY();
        g.strokeOval(xCoord-pointSize/scaleAffine/2, yCoord-pointSize/scaleAffine/2, pointSize/scaleAffine, pointSize/scaleAffine);
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
        double xDistance = this.extremeCoord.get("maxX") - this.extremeCoord.get("minX") + 60/scaleAffine;
        double yDistance = this.extremeCoord.get("maxY") - this.extremeCoord.get("minY") + 60/scaleAffine;
        double height = this.canvas.getHeight();
        double width = this.canvas.getWidth();
        this.setScaleAffine(Math.min(width/xDistance, height/yDistance));
    }

    public void countTransformPoint() {
        this.setP0(new Point2D(this.extremeCoord.get("minX") - 30/scaleAffine, this.extremeCoord.get("minY") - 50/scaleAffine));

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
                minY = currentPoint.getY();
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

    public void setDrawDistancesValue(boolean drawDistances){
        drawDistancesValue = drawDistances;
    }
}

