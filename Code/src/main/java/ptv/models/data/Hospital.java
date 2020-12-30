package ptv.models.data;


import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Hospital extends Node{

    private final String name;
    private final int allBeds;
    private int availableBeds;
    private LinkedList<Hospital> shortestPath;
    //private final Map<Hospital, Distance> adjacentHospitals;

    public Hospital(int id, String name, Point2D coordinates, int allBeds, int availableBeds) {
        super(id, coordinates);
        this.name = name;
        this.allBeds = allBeds;
        this.availableBeds = availableBeds;
        //adjacentHospitals = new HashMap<>();
    }

    public void useBed(){
        availableBeds--;
    }

    /*public void addDestination(Hospital destination, Distance distance){
        adjacentHospitals.put(destination, distance);
    }*/

    public void addToShortestPath(Hospital hospital){
        shortestPath.add(hospital);
    }

    public String getName() {
        return name;
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

    /*public Map<Hospital, Distance> getAdjacentHospitals() {
        return adjacentHospitals;
    }

    public void addDistance(Distance distance){
        adjacentHospitals.put(distance.getSecondOM(), distance);
    }*/
}
