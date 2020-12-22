package ptv.models.data;

import java.awt.geom.Point2D;

public class Patient {

    private final int id;
    private final Point2D coordinates;

    public Patient(int id, Point2D coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }
}
