package ptv.models.reader;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ptv.models.data.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientsFileReaderTest {

    private PatientsFileReader patientsFileReader;
    private final String path = new File("").getAbsolutePath() + "/src/test/java/ptv/models/reader/patientsDataSets/";

    @BeforeEach
    public void setUp(){ patientsFileReader = new PatientsFileReader();}

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsNull(){
        String expectedMessage = "FilePath cannot be null or empty String";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(null));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsEmptyString(){
        String expectedMessage = "FilePath cannot be null or empty String";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(""));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldReadFileProperlyWhenGivenCorrectFile() throws FileNotFoundException {
        List<Patient> patientList = patientsFileReader.readFile(path + "correctData.txt");

        Patient patient1 = patientList.get(0);
        Patient patient2 = patientList.get(1);
        Patient patient3 = patientList.get(2);

        assertEquals(1, patient1.getId());
        assertEquals(new Point2D(20,20), patient1.getCoordinates());

        assertEquals(2, patient2.getId());
        assertEquals(new Point2D(99,105), patient2.getCoordinates());

        assertEquals(3, patient3.getId());
        assertEquals(new Point2D(23,40), patient3.getCoordinates());
    }

    @Test
    public void shouldReadFileProperlyWhenGivenDoubleCoords() throws FileNotFoundException {
        List<Patient> patientList = patientsFileReader.readFile(path + "doubleCoords.txt");

        Patient patient1 = patientList.get(0);
        Patient patient2 = patientList.get(1);
        Patient patient3 = patientList.get(2);

        assertEquals(1, patient1.getId());
        assertEquals(new Point2D(20.1,20.4), patient1.getCoordinates());

        assertEquals(2, patient2.getId());
        assertEquals(new Point2D(99.2,105.0), patient2.getCoordinates());

        assertEquals(3, patient3.getId());
        assertEquals(new Point2D(23.4,40.2), patient3.getCoordinates());
    }

    @Test
    public void shouldReadFileProperlyWhenGivenNegativeCoords() throws FileNotFoundException {
        List<Patient> patientList = patientsFileReader.readFile(path + "negativeCoords.txt");

        Patient patient1 = patientList.get(0);
        Patient patient2 = patientList.get(1);
        Patient patient3 = patientList.get(2);

        assertEquals(1, patient1.getId());
        assertEquals(new Point2D(-20,20), patient1.getCoordinates());

        assertEquals(2, patient2.getId());
        assertEquals(new Point2D(99,-105), patient2.getCoordinates());

        assertEquals(3, patient3.getId());
        assertEquals(new Point2D(-23,-40), patient3.getCoordinates());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsOnlyHeader(){
        String filePath = path + "headerOnly.txt";
        String expectedMessage = "File does not contain any patients.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileDoesNotContainHeader(){
        String filePath = path + "noHeader.txt";
        String expectedMessage = "Expected first line starting with '#' in file " + filePath;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsTooManySections(){
        String filePath = path + "tooManySections.txt";
        String expectedMessage = "Line: 2. Wrong number of sections. Expected 3 sections.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsTooManyHeaders(){
        String filePath = path + "tooManyHeaders.txt";
        String expectedMessage = "More than one header starting with '#' in file " + filePath + ". Expected one header.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsTheSameId(){
        String filePath = path + "sameID.txt";
        String expectedMessage = "Line: 3. Patient's id should be unique.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFileContainsNegativeId(){
        String filePath = path + "negativeID.txt";
        String expectedMessage = "Line: 2. Wrong data format. Should be:\n " +
                "id - not negative integer,\n " +
                "x and y - float number.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(filePath));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
