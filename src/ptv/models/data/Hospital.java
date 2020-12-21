package ptv.models.data;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Map;

public class Hospital {

    private final int id;
    private final String name;
    private final Point2D coordinates;
    private final int allBeds;
    private final int avaliableBeds;
    private LinkedList<Hospital> shortestPath;
    private Map<Hospital, Distance> adjacentHospitals;

    public Hospital(int id, String name, Point2D coordinates, int allBeds, int avaliableBeds) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.allBeds = allBeds;
        this.avaliableBeds = avaliableBeds;
    }

    public void useBed(){}

    public void addDestination(Hospital destination, Distance distance){}

    public void addToShortestPath(Hospital hospital){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public int getAllBeds() {
        return allBeds;
    }

    public int getAvaliableBeds() {
        return avaliableBeds;
    }

    public LinkedList<Hospital> getShortestPath() {
        return shortestPath;
    }

    public Map<Hospital, Distance> getAdjacentHospitals() {
        return adjacentHospitals;
    }
}
