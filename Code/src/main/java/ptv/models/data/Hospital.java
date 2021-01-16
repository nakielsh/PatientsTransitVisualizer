package ptv.models.data;


import javafx.geometry.Point2D;

import java.util.LinkedList;

public class Hospital extends Node {

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

    public void useBed() {
        availableBeds--;
    }

    /*public void addDestination(Hospital destination, Distance distance){
        adjacentHospitals.put(destination, distance);
    }*/

    public void addToShortestPath(Hospital hospital) {
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

    @Override
    public int hashCode() {
        return super.hashCode() + 89 * name.hashCode() + 97 * allBeds;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Hospital) {
            Hospital hospital = (Hospital) object;
            return name.equals(hospital.name) && id == hospital.id && allBeds == hospital.allBeds;
        }
        return false;
    }
}
