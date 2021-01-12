package ptv.models.data;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Country {
    private List<Facility> facilitiesList;
    private List<Hospital> hospitalsList;
    private List<Distance> distancesList;
    private List<Junction> junctionsList;
    private List<Patient> patientList;
    private Hospital currentVisitedHospital;
    private List<Point2D> polygon;

    public Country(){
        facilitiesList = new ArrayList<>();
        hospitalsList = new ArrayList<>();
        distancesList = new ArrayList<>();
        patientList = new LinkedList<>();
    }

    public List<Facility> getFacilitiesList() {
        return facilitiesList;
    }

    public void setFacilitiesList(List<Facility> facilitiesList) {
        this.facilitiesList = facilitiesList;
    }

    public List<Hospital> getHospitalsList() {
        return hospitalsList;
    }

    public void setHospitalsList(List<Hospital> hospitalsList) {
        this.hospitalsList = hospitalsList;
    }

    public List<Distance> getDistancesList(){
        return distancesList;
    }

    public void setDistancesList(List<Distance> distancesList){
        this.distancesList = distancesList;
    }

    public List<Junction> getJunctionsList(){
        return junctionsList;
    }

    public List<Patient> getPatientList(){
        return patientList;
    }

    public void setJunctionsList(List<Junction> junctionsList){
        this.junctionsList = junctionsList;
    }

    public Hospital getCurrentVisitedHospital() {
        return currentVisitedHospital;
    }

    public void setCurrentVisitedHospital(Hospital currentVisitedHospital) {
        this.currentVisitedHospital = currentVisitedHospital;
    }

    public List<Point2D> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Point2D> polygon) {
        this.polygon = polygon;
    }

    public void addHospital(Hospital hospital){
        hospitalsList.add(hospital);
    }

    public void addFacility(Facility facility){
        facilitiesList.add(facility);
    }

    public void addDistance(Distance distance){
        distancesList.add(distance);
    }

    public void addPatient(Patient patient){
        patientList.add(patient);
    }

    public void addPatients(List<Patient> patients){
        patientList.addAll(patients);
    }
}
