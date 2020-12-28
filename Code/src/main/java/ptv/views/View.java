package ptv.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import ptv.models.borders.InBorders;
import ptv.models.data.Country;
import ptv.models.data.Hospital;
import ptv.models.reader.CountryFileReader;
import ptv.models.simulation.Simulator;

import java.io.FileNotFoundException;
import java.util.Iterator;

//TODO sprint 2
// MARTYNA
// Drawing Junctions and borders

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

        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 1, 1);

    }

    public void paintHospitals(){
        Iterator<Hospital> iterator = country.getHospitalsList().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());

        }

    }

    public void paintFacilities(){}

    public void paintPatient(){}

    private int countAffine(){
        return 10;
    }

    private Point2D findCenter(){return null;}
}
