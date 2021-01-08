package ptv.models.borders;

import javafx.geometry.Point2D;
import ptv.models.data.GrahamScan;

import java.util.ArrayList;
import java.util.List;

public class InBorders {
    private final List<Point2D> borderPoints;

    public InBorders(List<Point2D> borderPoints) {
        this.borderPoints = borderPoints;
    }

    public boolean isInside(Point2D patient) {
        List<Point2D> isInBorder = new ArrayList<>(this.borderPoints);
        isInBorder.add(patient);
        GrahamScan grahamScan = new GrahamScan();
        return !grahamScan.returnGrahamHull(isInBorder).contains(patient);
    }

}
