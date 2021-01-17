package ptv.models.data;

import javafx.geometry.Point2D;
import ptv.models.borders.InBorders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Country {
    private final List<Facility> facilitiesList;
    private final List<Hospital> hospitalsList;
    private final List<Distance> distancesList;
    private List<Junction> junctionsList;
    private final List<Patient> patientList;
    private Hospital currentVisitedHospital;
    private Patient currentHandledPatient;
    private List<Point2D> polygon;

    public Country() {
        facilitiesList = new ArrayList<>();
        hospitalsList = new ArrayList<>();
        distancesList = new ArrayList<>();
        patientList = new LinkedList<>();
    }

    public List<Facility> getFacilitiesList() {
        return facilitiesList;
    }

    public List<Hospital> getHospitalsList() {
        return hospitalsList;
    }

    public List<Distance> getDistancesList() {
        return distancesList;
    }

    public List<Junction> getJunctionsList() {
        return junctionsList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setJunctionsList(List<Junction> junctionsList) {
        this.junctionsList = junctionsList;
    }

    public Hospital getCurrentVisitedHospital() {
        return currentVisitedHospital;
    }

    public void setCurrentVisitedHospital(Hospital currentVisitedHospital) {
        this.currentVisitedHospital = currentVisitedHospital;
    }

    public Patient getCurrentHandledPatient() {
        return currentHandledPatient;
    }

    public void setCurrentHandledPatient(Patient patient) {
        currentHandledPatient = patient;
    }

    public List<Point2D> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Point2D> polygon) {
        this.polygon = polygon;
    }

    public void addHospital(Hospital hospital) {
        hospitalsList.add(hospital);
    }

    public void addFacility(Facility facility) {
        facilitiesList.add(facility);
    }

    public void addDistance(Distance distance) {
        distancesList.add(distance);
    }

    public void addPatient(Patient patient) throws Exception {
        InBorders inBorders = new InBorders(polygon);
        if (!inBorders.isInside(patient.getCoordinates())) {
            throw new Exception("Patient out of bound");
        } else {
            patientList.add(patient);
        }
    }

    public String addPatients(List<Patient> patients) {
        InBorders inBorders = new InBorders(polygon);
        String removedPatients = "Patients with these ids were removed due to out of bound coordinates:\n";
        String notRemovedPatients = "No patient has been removed";
        boolean wasRemoved = false;
        List<Patient> toRemove = new ArrayList<>();

        for (Patient patient : patients) {
            if (!inBorders.isInside(patient.getCoordinates())) {
                toRemove.add(patient);
                removedPatients = removedPatients.concat((patient.getId()) + " | ");
                wasRemoved = true;
            }
        }
        patients.removeAll(toRemove);
        patientList.addAll(patients);
        return !wasRemoved ? notRemovedPatients : removedPatients;
    }
}
