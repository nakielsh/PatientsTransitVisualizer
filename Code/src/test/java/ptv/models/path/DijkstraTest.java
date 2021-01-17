package ptv.models.path;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import ptv.models.data.*;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSourceHospitalIsNull() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Dijkstra().findNearestHospitalFromHospital(null));
        assertEquals("Source hospital cannot be null", e.getMessage());
    }

    @Test
    public void shouldReturnNearestHospitalsWhenMethodIsCalledFewTimes() {
        Dijkstra dijkstra = new Dijkstra();
        Hospital hospital1 = new Hospital(1, "H1", new Point2D(10, 10), 5, 5);
        Junction junction1 = new Junction(-1, new Point2D(10, 10));
        Hospital hospital2 = new Hospital(2, "H2", new Point2D(10, 10), 5, 5);
        Hospital hospital3 = new Hospital(3, "H3", new Point2D(10, 10), 5, 5);
        Hospital hospital4 = new Hospital(4, "H4", new Point2D(10, 10), 5, 5);
        Distance d1 = new Distance(1, hospital1, junction1, 1);
        Distance d2 = new Distance(2, hospital1, hospital2, 3);
        Distance d3 = new Distance(3, hospital2, hospital3, 4);
        Distance d4 = new Distance(4, junction1, hospital3, 1);
        Distance d5 = new Distance(5, junction1, hospital4, 2);

        hospital1.addNode(junction1, d1);
        hospital1.addNode(hospital2, d2);
        junction1.addNode(hospital1, d1);
        junction1.addNode(hospital3, d4);
        junction1.addNode(hospital4, d5);
        hospital2.addNode(hospital1, d2);
        hospital2.addNode(hospital3, d3);
        hospital3.addNode(junction1, d4);
        hospital3.addNode(hospital2, d3);
        hospital4.addNode(junction1, d5);

        Hospital expectedNearestHospital1 = hospital3;
        Hospital expectedNearestHospital2 = hospital4;
        Hospital expectedNearestHospital3 = hospital2;
        Hospital actualNearestHospital1 = dijkstra.findNearestHospitalFromHospital(hospital1);
        Hospital actualNearestHospital2 = dijkstra.findNearestHospitalFromHospital(actualNearestHospital1);
        Hospital actualNearestHospital3 = dijkstra.findNearestHospitalFromHospital(actualNearestHospital2);


        assertEquals(expectedNearestHospital1.getId(), actualNearestHospital1.getId());
        assertEquals(expectedNearestHospital1.getName(), actualNearestHospital1.getName());
        assertEquals(expectedNearestHospital2.getId(), actualNearestHospital2.getId());
        assertEquals(expectedNearestHospital2.getName(), actualNearestHospital2.getName());
        assertEquals(expectedNearestHospital3.getId(), actualNearestHospital3.getId());
        assertEquals(expectedNearestHospital3.getName(), actualNearestHospital3.getName());
    }

    @Test
    public void shouldReturnNullWhenGraphHasOnlyJunctions() {
        Hospital hospital = new Hospital(1, "Hospital", new Point2D(10, 10), 5, 0);
        Junction junction1 = new Junction(-1, new Point2D(10, 10));
        Junction junction2 = new Junction(-2, new Point2D(10, 10));
        Junction junction3 = new Junction(-3, new Point2D(10, 10));
        Distance d1 = new Distance(1, hospital, junction1, 1);
        Distance d2 = new Distance(2, junction1, junction2, 3);
        Distance d3 = new Distance(3, junction2, junction3, 6);
        Distance d4 = new Distance(3, hospital, junction3, 3);
        Distance d5 = new Distance(3, hospital, junction2, 4);

        hospital.addNode(junction1, d1);
        hospital.addNode(junction2, d5);
        hospital.addNode(junction3, d4);
        junction1.addNode(hospital, d1);
        junction1.addNode(junction2, d2);
        junction2.addNode(junction1, d2);
        junction2.addNode(hospital, d5);
        junction2.addNode(junction3, d3);
        junction3.addNode(hospital, d4);
        junction3.addNode(junction2, d3);

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromHospital(hospital);

        assertNull(actualNearestHospital);
    }

    @Test
    public void shouldReturnNullWhenGraphHasOnlySourceHospital() {
        Hospital h1 = new Hospital(1, "H1", new Point2D(10, 10), 5, 0);

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromHospital(h1);

        assertNull(actualNearestHospital);
    }


    //Method findNearestHospitalFromPatient() tests

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPatientIsNull() {
        Country country = new Country();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Dijkstra().findNearestHospitalFromPatient(null, country));
        assertEquals("Patient and country arguments cannot be null", e.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenCountryIsNull() {
        Patient patient = new Patient(0, new Point2D(50, 50));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Dijkstra().findNearestHospitalFromPatient(patient, null));
        assertEquals("Patient and country arguments cannot be null", e.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPatientAndCountryAreNulls() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Dijkstra().findNearestHospitalFromPatient(null, null));
        assertEquals("Patient and country arguments cannot be null", e.getMessage());
    }

    @Test
    public void shouldCorrectlyCalculateNearestHospitalWhenCountryHasFewHospitals() {
        Country country = new Country();
        Patient patient = new Patient(0, new Point2D(100, 100));
        Hospital expectedNearestHospital = new Hospital(0, "Expected", new Point2D(115, 104.97), 100, 100);

        country.addHospital(new Hospital(1, "H1", new Point2D(119.45, 103.99), 100, 100));
        country.addHospital(new Hospital(2, "H2", new Point2D(200, 200), 100, 100));
        country.addHospital(expectedNearestHospital);
        country.addHospital(new Hospital(3, "H3", new Point2D(100, 119.54), 100, 100));

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromPatient(patient, country);

        assertEquals(expectedNearestHospital.getId(), actualNearestHospital.getId());
        assertEquals(expectedNearestHospital.getName(), actualNearestHospital.getName());
    }

    @Test
    public void shouldCorrectlyCalculateNearestHospitalWhenHospitalAndPatientHaveSameCoordinates() {
        Country country = new Country();
        Patient patient = new Patient(0, new Point2D(100, 100));
        Hospital expectedNearestHospital = new Hospital(0, "Expected", new Point2D(100, 100), 100, 100);

        country.addHospital(new Hospital(1, "H1", new Point2D(119.45, 103.99), 100, 100));
        country.addHospital(new Hospital(2, "H2", new Point2D(200, 200), 100, 100));
        country.addHospital(expectedNearestHospital);
        country.addHospital(new Hospital(3, "H3", new Point2D(100, 119.54), 100, 100));

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromPatient(patient, country);

        assertEquals(expectedNearestHospital.getId(), actualNearestHospital.getId());
        assertEquals(expectedNearestHospital.getName(), actualNearestHospital.getName());
    }

    @Test
    public void shouldCorrectlyCalculateNearestHospitalWhenHospitalsAreInSameDistance() {
        Country country = new Country();
        Patient patient = new Patient(0, new Point2D(100, 100));
        Hospital expectedNearestHospital = new Hospital(0, "Expected", new Point2D(115, 104.97), 100, 100);

        country.addHospital(new Hospital(1, "H1", new Point2D(119.45, 103.99), 100, 100));
        country.addHospital(new Hospital(0, "Expected", new Point2D(104.97, 115), 100, 100));
        country.addHospital(expectedNearestHospital);
        country.addHospital(new Hospital(0, "Expected", new Point2D(85, 95.03), 100, 100));

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromPatient(patient, country);

        assertEquals(expectedNearestHospital.getId(), actualNearestHospital.getId());
        assertEquals(expectedNearestHospital.getName(), actualNearestHospital.getName());
    }

    @Test
    public void shouldReturnNullWhenCountryDoesNotHaveAnyHospital() {
        Country country = new Country();
        Patient patient = new Patient(0, new Point2D(100, 100));

        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalFromPatient(patient, country);

        assertNull(actualNearestHospital);
    }
}