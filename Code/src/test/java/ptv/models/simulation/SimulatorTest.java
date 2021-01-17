package ptv.models.simulation;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ptv.models.data.Country;
import ptv.models.data.Distance;
import ptv.models.data.Hospital;
import ptv.models.data.Patient;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {

    private Simulator simulator;

    @BeforeEach
    public void setUp() {
        simulator = new Simulator();
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenCountryIsNotSet() {
        Patient patient = new Patient(0, new Point2D(0, 0));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> simulator.setHandledPatient(patient));
        assertEquals("Country is not set", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenPatientIsNotSet() {
        Country country = new Country();
        simulator.setCountry(country);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> simulator.nextStep());
        assertEquals("Patient is not set", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenCountryIsEmpty() {
        Country country = new Country();
        Patient patient = new Patient(0, new Point2D(0, 0));

        simulator.setCountry(country);
        simulator.setHandledPatient(patient);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> simulator.nextStep());
        assertEquals("Country does not contain any hospital", exception.getMessage());
    }

    @Test
    public void shouldMakeFirstStepAutomaticallyWhenNextStepIsCalled() {
        Country country = new Country();
        Patient patient = new Patient(1, new Point2D(10, 10));
        Hospital h1 = new Hospital(1, "H1", new Point2D(0, 0), 10, 0);
        Hospital h2 = new Hospital(2, "H2", new Point2D(100, 0), 10, 10);
        Distance d = new Distance(1, h1, h2, 10);

        h1.addNode(h2, d);
        h2.addNode(h1, d);
        country.addHospital(h1);
        country.addHospital(h2);
        country.addDistance(d);

        simulator.setCountry(country);
        simulator.setHandledPatient(patient);

        simulator.nextStep();

        assertEquals(h1, country.getCurrentVisitedHospital());
        assertTrue(simulator.hasNextStep());
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenEveryHospitalInCountryDoesNotHaveAvailableBeds() {
        Country country = new Country();
        Patient patient = new Patient(1, new Point2D(10, 10));
        Hospital h1 = new Hospital(1, "H1", new Point2D(0, 0), 10, 0);
        Hospital h2 = new Hospital(2, "H2", new Point2D(100, 0), 10, 0);
        Distance d = new Distance(1, h1, h2, 10);

        h1.addNode(h2, d);
        h2.addNode(h1, d);
        country.addHospital(h1);
        country.addHospital(h2);
        country.addDistance(d);

        simulator.setCountry(country);
        simulator.setHandledPatient(patient);

        boolean actualHasNextStep1 = simulator.hasNextStep();
        simulator.nextStep();
        Hospital actualVisitedHospital1 = country.getCurrentVisitedHospital();

        boolean actualHasNextStep2 = simulator.hasNextStep();
        simulator.nextStep();
        Hospital actualVisitedHospital2 = country.getCurrentVisitedHospital();

        boolean actualHasNextStep3 = simulator.hasNextStep();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> simulator.nextStep());
        Hospital actualVisitedHospital3 = country.getCurrentVisitedHospital();
        boolean actualHasNextStep4 = simulator.hasNextStep();

        assertEquals("There is not available beds in any hospital", exception.getMessage());
        assertNull(country.getCurrentVisitedHospital());
        assertTrue(actualHasNextStep1);
        assertTrue(actualHasNextStep2);
        assertTrue(actualHasNextStep3);
        assertFalse(actualHasNextStep4);
        assertEquals(h1, actualVisitedHospital1);
        assertEquals(h2, actualVisitedHospital2);
        assertNull(actualVisitedHospital3);
    }

    @Test
    public void shouldSimulateCorrectlyWhenMethodsAreCalledCorrectly() {
        Country country = new Country();
        Patient patient1 = new Patient(1, new Point2D(-10.5, -10));
        Patient patient2 = new Patient(2, new Point2D(15, 26.5));
        Hospital h1 = new Hospital(1, "H1", new Point2D(0, 0), 10, 1);
        Hospital h2 = new Hospital(2, "H2", new Point2D(100, 0), 10, 0);
        Hospital h3 = new Hospital(3, "H3", new Point2D(100, 100), 10, 10);
        Distance d12 = new Distance(1, h1, h2, 10);
        Distance d23 = new Distance(2, h2, h3, 10);

        h1.addNode(h2, d12);
        h2.addNode(h1, d12);
        h2.addNode(h3, d23);
        h3.addNode(h2, d23);
        country.addHospital(h1);
        country.addHospital(h2);
        country.addHospital(h3);
        country.addDistance(d12);
        country.addDistance(d23);

        simulator.setCountry(country);

        simulator.setHandledPatient(patient1);
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertEquals(h1, country.getCurrentVisitedHospital());
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertNull(country.getCurrentVisitedHospital());
        assertFalse(simulator.hasNextStep());

        simulator.setHandledPatient(patient2);
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertEquals(h1, country.getCurrentVisitedHospital());
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertEquals(h2, country.getCurrentVisitedHospital());
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertEquals(h3, country.getCurrentVisitedHospital());
        assertTrue(simulator.hasNextStep());
        simulator.nextStep();
        assertNull(country.getCurrentVisitedHospital());
        assertFalse(simulator.hasNextStep());
    }
}
