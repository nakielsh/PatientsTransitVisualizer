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
    static boolean onSegment(Point2D p, Point2D q, Point2D r){return false;}
    static int orientation(Point2D p, Point2D q, Point2D r){return 0;}
    static boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2){return false;}


}
