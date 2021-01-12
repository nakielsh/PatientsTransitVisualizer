package ptv.models.simulation;

import ptv.models.data.Country;
import ptv.models.data.Hospital;
import ptv.models.data.Patient;
import ptv.models.path.Dijkstra;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private Patient handledPatient;
    private Country country;
    private Dijkstra dijkstra;

    public void firstStep() throws IllegalStateException{
        checkClassState();

        Hospital hospital = dijkstra.findNearestHospitalFromPatient(handledPatient, country);
        country.setCurrentVisitedHospital(hospital);

        if(hospital == null){
            handledPatient = null;
            throw new IllegalStateException("Country does not contain any hospital");
        }
    }

    public void nextStep() throws IllegalStateException{
        checkClassState();
        Hospital hospital = country.getCurrentVisitedHospital();

        if(hospital == null){
            firstStep();
            return;
        }
        else if(hospital.getAvailableBeds() > 0){
            hospital.useBed();
            country.setCurrentVisitedHospital(null);
            handledPatient = null;
            return;
        }

        hospital = dijkstra.findNearestHospitalFromHospital(hospital);
        country.setCurrentVisitedHospital(hospital);

        if(hospital == null){
            handledPatient = null;
        }
        /*else if(hospital.getAvailableBeds() > 0){
            hospital.useBed();
            handledPatient = null;
        }*/
    }

    public boolean hasNextStep(){
        return handledPatient != null;
    }

    private void checkClassState(){
        if(handledPatient == null){
            throw new IllegalStateException("Patient is not set");
        }
        else if(country == null){
            throw new IllegalStateException("Country is not set");
        }
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setHandledPatient(Patient patient){
        if(country == null){
            throw new IllegalStateException("Country is not set");
        }

        handledPatient = patient;
        dijkstra = new Dijkstra();
        country.setCurrentVisitedHospital(null);
    }

    // ---------------------------------------------------------


    private List<Patient> patients;

    public Simulator() {
        patients = new ArrayList<>(); // delete this!
    }

    public Country getCountry(){
        return this.country;
    }

    public List<Patient> getPatients(){
        return this.patients;
    }

    public void setPatients(List<Patient> patients){
        this.patients = patients;
    }

    public void addPatient(Patient patient){
        this.patients.add(patient);
    }
}
