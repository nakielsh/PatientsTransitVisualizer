package ptv.models.data;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JunctionFinderTest {

     @Test
    public void shouldThrowIllegalArgumentExceptionWhenDistanceListIsNull(){
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new JunctionFinder().findJunctions(null));
        assertEquals("Distance list cannot be null", e.getMessage());
    }

    @Test
    public void shouldDoNothingWhenDistanceListIsEmpty(){
        List<Distance> emptyList = new ArrayList<>();
        new JunctionFinder().findJunctions(emptyList);
    }

    @Test
    public void shouldCalculateJunctionWhenTwoDistancesAreCrossed(){
        Node n11 = new Node(0, new Point2D(50, 50));
        Node n12 = new Node(1, new Point2D(150, 150));
        Node n21 = new Node(2, new Point2D(150, 50));
        Node n22 = new Node(3, new Point2D(50, 150));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n11, n12, 500));
        distances.add(new Distance(1, n21, n22, 1000));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        Junction expectedJunction = new Junction(-1, new Point2D(100, 100));
        Junction actualJunction = junctions.get(0);

        assertTrue(n11.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n12.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n21.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n22.getAdjacentNodes().containsKey(expectedJunction));
        assertEquals(4, distances.size());
        assertEquals(1, junctions.size());
        assertEquals(expectedJunction.getId(), actualJunction.getId());
        assertEquals(expectedJunction.getCoordinates(), actualJunction.getCoordinates());
    }

    @Test
    public void shouldDoNothingWhenTwoDistancesAreNotCrossed(){
        Node n11 = new Node(0, new Point2D(50, 150));
        Node n12 = new Node(1, new Point2D(150, 150));
        Node n21 = new Node(2, new Point2D(150, 50));
        Node n22 = new Node(3, new Point2D(50, 50));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n11, n12, 500));
        distances.add(new Distance(1, n21, n22, 1000));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        assertEquals(0, n11.getAdjacentNodes().size());
        assertEquals(0, n11.getAdjacentNodes().size());
        assertEquals(0, n11.getAdjacentNodes().size());
        assertEquals(0, n11.getAdjacentNodes().size());
        assertEquals(2, distances.size());
        assertEquals(0, junctions.size());
    }

    @Test
    public void shouldCalculateJunctionsWhenOneDistanceIsCrossedWithAnotherTwo(){
        Node n11 = new Node(0, new Point2D(100, 100));
        Node n12 = new Node(1, new Point2D(160, 100));
        Node n21 = new Node(2, new Point2D(100, 150));
        Node n22 = new Node(3, new Point2D(160, 150));
        Node n31 = new Node(4, new Point2D(100, 50));
        Node n32 = new Node(5, new Point2D(160, 200));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n11, n12, 600));
        distances.add(new Distance(1, n21, n22, 750));
        distances.add(new Distance(2, n31, n32, 1200));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        Junction expectedJunction1 = new Junction(-1, new Point2D(120, 100));
        Junction expectedJunction2 = new Junction(-2, new Point2D(140, 150));
        Junction actualJunction1 = junctions.get(0);

        assertTrue(n11.getAdjacentNodes().containsKey(expectedJunction1));
        assertTrue(n12.getAdjacentNodes().containsKey(expectedJunction1));
        assertTrue(n21.getAdjacentNodes().containsKey(expectedJunction2));
        assertTrue(n22.getAdjacentNodes().containsKey(expectedJunction2));
        assertTrue(n31.getAdjacentNodes().containsKey(expectedJunction1));
        assertTrue(n32.getAdjacentNodes().containsKey(expectedJunction2));
        assertTrue(actualJunction1.getAdjacentNodes().containsKey(expectedJunction2));
        assertEquals(7, distances.size());
        assertEquals(2, junctions.size());
    }

    @Test
    public void shouldDoNothingWhenThreeDistancesAreNotCrossedWitchEachOther(){
        Node n1 = new Node(1, new Point2D(60, 0));
        Node n2 = new Node(2, new Point2D(0, 60));
        Node n3 = new Node(3, new Point2D(60, 60));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n1, n2, 600));
        distances.add(new Distance(1, n2, n3, 750));
        distances.add(new Distance(2, n3, n1, 1200));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        assertEquals(0, n1.getAdjacentNodes().size());
        assertEquals(0, n2.getAdjacentNodes().size());
        assertEquals(0, n3.getAdjacentNodes().size());
        assertEquals(3, distances.size());
        assertEquals(0, junctions.size());
    }

    @Test
    public void shouldDoNothingWhenTwoDistancesAreParallelAndOverlapping(){
        Node n11 = new Node(1, new Point2D(60, 60));
        Node n12 = new Node(2, new Point2D(0, 60));
        Node n21 = new Node(3, new Point2D(120, 60));
        Node n22 = new Node(3, new Point2D(450, 60));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n11, n12, 600));
        distances.add(new Distance(1, n21, n22, 750));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        assertEquals(0, n11.getAdjacentNodes().size());
        assertEquals(0, n12.getAdjacentNodes().size());
        assertEquals(0, n21.getAdjacentNodes().size());
        assertEquals(0, n22.getAdjacentNodes().size());
        assertEquals(2, distances.size());
        assertEquals(0, junctions.size());
    }

    @Test
    public void shouldCalculateJunctionCorrectlyWhenCoordinatesAreNegative(){
        Node n11 = new Node(0, new Point2D(-50, -100));
        Node n12 = new Node(1, new Point2D(-150, -100));
        Node n21 = new Node(2, new Point2D(-100, -50));
        Node n22 = new Node(3, new Point2D(-100, -150));

        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance(0, n11, n12, 500));
        distances.add(new Distance(1, n21, n22, 1000));

        List<Junction> junctions = new JunctionFinder().findJunctions(distances);

        Junction expectedJunction = new Junction(-1, new Point2D(-100, -100));
        Junction actualJunction = junctions.get(0);

        assertTrue(n11.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n12.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n21.getAdjacentNodes().containsKey(expectedJunction));
        assertTrue(n22.getAdjacentNodes().containsKey(expectedJunction));
        assertEquals(4, distances.size());
        assertEquals(1, junctions.size());
        assertEquals(expectedJunction.getId(), actualJunction.getId());
        assertEquals(expectedJunction.getCoordinates(), actualJunction.getCoordinates());
    }
}
