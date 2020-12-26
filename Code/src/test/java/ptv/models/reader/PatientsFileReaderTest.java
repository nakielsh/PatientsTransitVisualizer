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
    private String path = new File("").getAbsolutePath() + "/src/test/java/ptv/models/reader/patientsDataSets/";

    @BeforeEach
    public void setUp(){ patientsFileReader = new PatientsFileReader();}

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(null));
        assertEquals("FilePath cannot be null or empty String", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFilePathIsEmptyString(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientsFileReader.readFile(""));
        assertEquals("FilePath cannot be null or empty String", exception.getMessage());
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
}
