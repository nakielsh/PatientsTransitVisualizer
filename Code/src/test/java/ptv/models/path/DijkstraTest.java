package ptv.models.path;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import ptv.models.data.Distance;
import ptv.models.data.Hospital;
import ptv.models.data.Junction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DijkstraTest {

    @Test
    public void shouldReturnNearestHospitalWhenEveryHospitalHasAvailableBeds(){
        Hospital hospital1 = new Hospital(1, "H1", new Point2D(10, 10), 5, 0);
        Junction junction1 = new Junction(-1, new Point2D(10, 10));
        Hospital hospital2 = new Hospital(2, "H1", new Point2D(10, 10), 5, 5);
        Hospital hospital3 = new Hospital(3, "H1", new Point2D(10, 10), 5, 5);
        Distance d1 = new Distance(1, hospital1, junction1, 1);
        Distance d2 = new Distance(2, hospital1, hospital2, 3);
        Distance d3 = new Distance(3, hospital2, hospital3, 1);
        Distance d4 = new Distance(4, junction1, hospital3, 1);

        hospital1.addNode(junction1, d1);
        hospital1.addNode(hospital2, d2);
        junction1.addNode(hospital1, d1);
        junction1.addNode(hospital3, d4);
        hospital2.addNode(hospital1, d2);
        hospital2.addNode(hospital3, d3);
        hospital3.addNode(junction1, d4);
        hospital3.addNode(hospital2, d3);

        Hospital expectedNearestHospital = hospital3;
        Hospital actualNearestHospital = new Dijkstra().findNearestHospitalWithAvailableBeds(hospital1);

        assertEquals(expectedNearestHospital.getId(), actualNearestHospital.getId());
    }
}
