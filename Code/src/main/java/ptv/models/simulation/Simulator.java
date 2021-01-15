package ptv.models.simulation;

import ptv.models.borders.InBorders;
import ptv.models.data.Country;
import ptv.models.data.Hospital;
import ptv.models.data.Patient;
import ptv.models.path.Dijkstra;

public class Simulator {

    private Country country;
    private Dijkstra dijkstra;
    private InBorders inBorders;

    private void firstStep(Patient handledPatient) throws IllegalStateException{
        checkClassState();

        if(!inBorders.isInside(handledPatient.getCoordinates())){
            country.setCurrentHandledPatient(null);
            return;
        }

        Hospital hospital = dijkstra.findNearestHospitalFromPatient(handledPatient, country);
        country.setCurrentVisitedHospital(hospital);

        if(hospital == null){
            country.setCurrentHandledPatient(null);
            throw new IllegalStateException("Country does not contain any hospital");
        }
    }

    public void nextStep() throws IllegalStateException{
        checkClassState();
        Patient handledPatient = country.getCurrentHandledPatient();
        Hospital hospital = country.getCurrentVisitedHospital();

        if(hospital == null){
            firstStep(handledPatient);
            return;
        }
        else if(hospital.getAvailableBeds() > 0){
            hospital.useBed();
            country.setCurrentVisitedHospital(null);
            country.setCurrentHandledPatient(null);
            return;
        }

        hospital = dijkstra.findNearestHospitalFromHospital(hospital);
        country.setCurrentVisitedHospital(hospital);

        if(hospital == null){
            country.setCurrentHandledPatient(null);
        }
        /*else if(hospital.getAvailableBeds() > 0){
            hospital.useBed();
            handledPatient = null;
        }*/
    }

    public boolean hasNextStep(){
        if(country == null){
            return false;
        }
        return country.getCurrentHandledPatient() != null;
    }

    private void checkClassState(){
        if(country == null){
            throw new IllegalStateException("Country is not set");
        }
        else if(country.getCurrentHandledPatient() == null){
            throw new IllegalStateException("Patient is not set");
        }
    }

    public void setCountry(Country country) {
        this.country = country;
        inBorders = new InBorders(country.getPolygon());
    }

    public void setHandledPatient(Patient patient){
        if(country == null){
            throw new IllegalStateException("Country is not set");
        }

        country.setCurrentHandledPatient(patient);
        dijkstra = new Dijkstra();
        country.setCurrentVisitedHospital(null);
    }
}
