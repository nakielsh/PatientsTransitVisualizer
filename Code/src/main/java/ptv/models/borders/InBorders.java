package ptv.models.borders;

import javafx.geometry.Point2D;

import java.util.List;

public class InBorders {
    private List<Point2D> borderPoints;
    private int nSides;
    private static int INF;

    public boolean isInside(Point2D patient){return false;}
    static boolean onSegment(Point2D p, Point2D q, Point2D r){return false;}
    static int orientation(Point2D p, Point2D q, Point2D r){return 0;}
    static boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2){return false;}


}
