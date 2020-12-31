package ptv.models.simulation;

import ptv.models.data.Country;
import ptv.models.data.Patient;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private boolean firstStep;
    private Country country;
    private List<Patient> patients;

    public Simulator() {
        patients = new ArrayList<>();
    }

    public void makeStep(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean needNextStep(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void firstStep(){

    }

    private void nextStep(){

    }

    public Country getCountry(){return this.country;}

    public List<Patient> getPatients(){return this.patients;}

    public void setCountry(Country country) {this.country = country;}

    public void setPatients(List<Patient> patients){this.patients = patients;}

    public void addPatient(Patient patient){this.patients.add(patient);}




}
