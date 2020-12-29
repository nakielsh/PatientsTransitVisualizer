package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import ptv.models.borders.InBorders;
import ptv.models.data.Country;
import ptv.models.data.Facility;
import ptv.models.data.Hospital;
import ptv.models.reader.CountryFileReader;
import ptv.models.simulation.Simulator;

import java.io.FileNotFoundException;
import java.util.Iterator;

//TODO sprint 2
// MARTYNA
// Drawing Junctions and borders

//need to append scale

public class View {
    private Canvas canvas;
    private Affine affine;
    private Country country;
    private InBorders polygon;

    public View(String filePath, Canvas canvas) throws FileNotFoundException {
        CountryFileReader countryFileReader = new CountryFileReader();
        // need to add gui window with printed exceptions
        try {
            this.country = countryFileReader.readFile(filePath);
        }catch (IllegalArgumentException exception) {
            System.out.println("Invalid data");
        }
        this.canvas = canvas;
        this.affine = new Affine();
        this.affine.appendScale(countAffine(), countAffine());
        paintMap();
    }

    public void paintMap(){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);
        this.paintHospitals(g);
        this.paintFacilities(g);

    }

    public void paintHospitals(GraphicsContext g){
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
            g.strokeOval(xCoord-0.5, yCoord-0.5, 0.4, 0.4);
        }

    }

    public void paintFacilities(GraphicsContext g){
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
            g.strokeOval(xCoord, yCoord, 0.4, 0.4);
        }
    }

    public void paintPatient(){}

    public int countAffine(){
        //need to add function
        return 20;
    }

    private Point2D findCenter(){return null;}

    public Affine getAffine() {
        return this.affine;
    }


}

