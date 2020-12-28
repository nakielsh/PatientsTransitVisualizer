package ptv.models.data;


import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Hospital {

    private final int id;
    private final String name;
    private final Point2D coordinates;
    private final int allBeds;
    private int availableBeds;
    private LinkedList<Hospital> shortestPath;
    private final Map<Hospital, Distance> adjacentHospitals;

    public Hospital(int id, String name, Point2D coordinates, int allBeds, int availableBeds) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.allBeds = allBeds;
        this.availableBeds = availableBeds;
        adjacentHospitals = new HashMap<>();
    }

    public void useBed(){
        availableBeds--;
    }

    public void addDestination(Hospital destination, Distance distance){
        adjacentHospitals.put(destination, distance);
    }

    public void addToShortestPath(Hospital hospital){
        shortestPath.add(hospital);
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

    public int getAllBeds() {
        return allBeds;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public LinkedList<Hospital> getShortestPath() {
        return shortestPath;
    }

    public Map<Hospital, Distance> getAdjacentHospitals() {
        return adjacentHospitals;
    }

    public void addDistance(Distance distance){
        adjacentHospitals.put(distance.getSecondOM(), distance);
    }
}
