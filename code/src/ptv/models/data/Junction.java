package ptv.models.data;

import java.awt.geom.Point2D;

public class Junction {

    private final int id;
    private final Point2D intersectionPoint;
    private final double firstDistance;
    private final double secondDistance;

    public Junction(int id, Point2D intersectionPoint, double firstDistance, double secondDistance) {
        this.id = id;
        this.intersectionPoint = intersectionPoint;
        this.firstDistance = firstDistance;
        this.secondDistance = secondDistance;
    }

    public int getId() {
        return id;
    }

    public Point2D getIntersectionPoint() {
        return intersectionPoint;
    }

    public double getFirstDistance() {
        return firstDistance;
    }

    public double getSecondDistance() {
        return secondDistance;
    }
}
