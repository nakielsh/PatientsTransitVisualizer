package ptv.models.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ptv.models.data.Country;
import ptv.models.data.Distance;
import ptv.models.data.Facility;
import ptv.models.data.Hospital;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountryFileReaderTest {

    private CountryFileReader countryFileReader;
    private String path = new File("").getAbsolutePath() + "/src/test/java/ptv/models/reader/countryDataSets/";

    @BeforeEach
    public void setUp() {
        countryFileReader = new CountryFileReader();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(null));
        assertEquals("FilePath cannot be null or empty String", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(""));
        assertEquals("FilePath cannot be null or empty String", exception.getMessage());
    }

    @Test
    public void shouldReadFileProperlyWhenFileIsCorrect() throws FileNotFoundException {
        Country country = countryFileReader.readFile(path + "correctData.txt");

        List<Hospital> hospitals = country.getHospitalsList();
        List<Facility> facilities = country.getFacilitiesList();
        List<Distance> distances = country.getDistancesList();

        Hospital hospital1 = hospitals.get(0);
        Hospital hospital2 = hospitals.get(1);
        Facility facility = facilities.get(0);
        Distance distance = distances.get(0);

        assertEquals(1, hospital1.getId());
        assertEquals(2, hospital2.getId());
        assertEquals(1, hospital1.getAdjacentNodes().size());
        assertEquals(1, hospital2.getAdjacentNodes().size());
        assertEquals(1, facility.getId());
        assertEquals("Szpital Wojewódzki nr 997", hospital1.getName());
        assertEquals("Krakowski Szpital Kliniczny", hospital2.getName());
        assertEquals("Pomnik Wikipedii", facility.getName());
        assertEquals(1, distance.getId());
        assertEquals(700, distance.getDist());
        assertEquals(1, distances.size());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsHeadersOnly() {
        String filePath = path + "headersOnly.txt";
        String msg = "File does not contain any hospital";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldReadFileProperlyWhenFileDoesNotContainFacilities() throws FileNotFoundException {
        String filePath = path + "noFacilities.txt";
        Country country = countryFileReader.readFile(filePath);

        List<Hospital> hospitals = country.getHospitalsList();
        List<Facility> facilities = country.getFacilitiesList();
        List<Distance> distances = country.getDistancesList();

        Hospital hospital1 = hospitals.get(0);
        Hospital hospital2 = hospitals.get(1);
        Distance distance = distances.get(0);

        assertEquals(1, hospital1.getId());
        assertEquals(2, hospital2.getId());
        assertEquals(1, hospital1.getAdjacentNodes().size());
        assertEquals(1, hospital2.getAdjacentNodes().size());
        assertEquals(0, facilities.size());
        assertEquals("Szpital Wojewódzki nr 997", hospital1.getName());
        assertEquals("Krakowski Szpital Kliniczny", hospital2.getName());
        assertEquals(1, distance.getId());
        assertEquals(700, distance.getDist());
        assertEquals(1, distances.size());
    }

    @Test
    public void shouldReadFileProperlyWhenFileDoesNotContainDistances() throws FileNotFoundException {
        Country country = countryFileReader.readFile(path + "noDistances.txt");

        List<Hospital> hospitals = country.getHospitalsList();
        List<Facility> facilities = country.getFacilitiesList();
        List<Distance> distances = country.getDistancesList();

        Hospital hospital1 = hospitals.get(0);
        Hospital hospital2 = hospitals.get(1);
        Facility facility = facilities.get(0);

        assertEquals(1, hospital1.getId());
        assertEquals(2, hospital2.getId());
        assertEquals(0, hospital1.getAdjacentNodes().size());
        assertEquals(0, hospital2.getAdjacentNodes().size());
        assertEquals(1, facility.getId());
        assertEquals("Szpital Wojewódzki nr 997", hospital1.getName());
        assertEquals("Krakowski Szpital Kliniczny", hospital2.getName());
        assertEquals("Pomnik Wikipedii", facility.getName());
        assertEquals(0, distances.size());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileDoesNotContainEnoughHeaders() {
        String filePath = path + "notEnoughSharps.txt";
        String msg = "Line: 9. Wrong number of sections. Expected 6 sections";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileDoesContainWrongNumberOfSections() {
        String filePath = path + "wrongNumberOfSections.txt";
        String msg = "Line: 2. Wrong number of sections. Expected 6 sections";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainTooManyHeaders() {
        String filePath = path + "tooMuchSharps.txt";
        String msg = "Too much lines starting with '#' in file " + filePath + ". Expected 3 lines";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainFewSameHospitalsIds() {
        String filePath = path + "sameId.txt";
        String msg = "Line: 3. Hospital's id and name should be unique";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainFewSameFacilitiesNames() {
        String filePath = path + "sameNames.txt";
        String msg = "Line: 10. Facility's id and name should be unique";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainWrongHospitalIdInDistance() {
        String filePath = path + "wrongHospitalIdInDistance.txt";
        String msg = "Line: 15. Distance's id should be unique. Hospital's id should be real";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainNegativeId() {
        String filePath = path + "negativeId.txt";
        String msg = "Line: 3. Wrong data format. Should be:\n" +
                "id, number of beds, number of free beds - not negative integer,\n" +
                "x and y - float number";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainNegativeNumberOfBeds() {
        String filePath = path + "negativeNumberOfBeds.txt";
        String msg = "Line: 4. Wrong data format. Should be:\n" +
                "id, number of beds, number of free beds - not negative integer,\n" +
                "x and y - float number";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainFloatNumberInIntegerSectionId() {
        String filePath = path + "doubleInIntegerSection.txt";
        String msg = "Line: 5. Wrong data format. Should be:\n" +
                "id, number of beds, number of free beds - not negative integer,\n" +
                "x and y - float number";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainIdEquals1000() {
        String filePath = path + "idEquals1000.txt";
        String msg = "Line: 3. Hospital's id should be smaller than 1000";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainRoadToTheSameHospital() {
        String filePath = path + "roadToSameHospital.txt";
        String msg = "Line: 14. Road cannot lead to the same hospital.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainHospitalWithNumberOfAvailableBedsBiggerThanTotalNumberOfAllBeds() {
        String filePath = path + "freeBedsBiggerThanAll.txt";
        String msg = "Line: 3. Number of available beds cannot be bigger than total number of all beds";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> countryFileReader.readFile(filePath));
        assertEquals(msg, exception.getMessage());
    }
}
