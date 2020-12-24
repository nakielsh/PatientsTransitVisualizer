package ptv.models.data;


import javafx.geometry.Point2D;

public class Facility {

    private final int id;
    private final String name;
    private final Point2D coordinates;

    public Facility(int id, String name, Point2D coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }
}

