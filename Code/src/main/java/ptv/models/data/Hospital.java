package ptv.models.data;

import javafx.geometry.Point2D;

public class Hospital extends Node {

    private final String name;
    private final int allBeds;
    private int availableBeds;

    public Hospital(int id, String name, Point2D coordinates, int allBeds, int availableBeds) {
        super(id, coordinates);
        this.name = name;
        this.allBeds = allBeds;
        this.availableBeds = availableBeds;
    }

    public void useBed() {
        availableBeds--;
    }

    public String getName() {
        return name;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

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
