package ptv.models.borders;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InBordersTest {

    @Test
    public void shouldReturnTrueWhenPatientIsInBorders() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(4, 2));
        given.add(new Point2D(7, 2));
        given.add(new Point2D(8, 6));
        given.add(new Point2D(3, 6));
        given.add(new Point2D(2, 3));

        Point2D point = new Point2D(5, 4);

        InBorders inBorders = new InBorders(given);

        assertTrue(inBorders.isInside(point));
    }

    @Test
    public void shouldReturnFalseWhenPatientIsOutOfBorders() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(3, 9));
        given.add(new Point2D(5, 6));
        given.add(new Point2D(2, 2));
        given.add(new Point2D(10, 3));
        given.add(new Point2D(7, 10));

        Point2D point = new Point2D(10, 10);

        InBorders inBorders = new InBorders(given);

        assertFalse(inBorders.isInside(point));
    }

    @Test
    public void shouldReturnTrueWhenPatientIsExactlyInBorders() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(3, 9));
        given.add(new Point2D(5, 6));
        given.add(new Point2D(2, 2));
        given.add(new Point2D(11, 2));
        given.add(new Point2D(7, 10));

        Point2D point = new Point2D(7, 2);

        InBorders inBorders = new InBorders(given);

        assertTrue(inBorders.isInside(point));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPointIsNull() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(3, 9));
        given.add(new Point2D(5, 6));
        given.add(new Point2D(2, 2));

        InBorders inBorders = new InBorders(given);
        String msg = "Point cannot be null";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inBorders.isInside(null));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPolygonIsNull() {
        InBorders inBorders = new InBorders(null);
        Point2D point = new Point2D(7, 2);
        String msg = "Polygon with border points cannot be null";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inBorders.isInside(point));
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void shouldPatientBeInStraightBetweenTwoPointsWhenPolygonHasOnlyTwoPoints() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(3, 3));
        given.add(new Point2D(10, 10));

        Point2D patientOut = new Point2D(4, 5);
        Point2D patientIn = new Point2D(5, 5);

        InBorders inBorders = new InBorders(given);

        assertTrue(inBorders.isInside(patientIn));
        assertFalse(inBorders.isInside(patientOut));
    }

    @Test
<<<<<<< Updated upstream
    public void shouldPatientBeInTheSamePositionAsPointWhenPolygonHasOnePoint(){
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(4,4));

        Point2D patientIn = new Point2D(4,4);
        Point2D patientOut = new Point2D(4,5);
=======
    public void shouldPatientBeInStraightBetweenTwoPointsWhenPolygonHasOnlyTwoPointsAndStraightIsVertical() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(3, 3));
        given.add(new Point2D(3, 10));

        Point2D patientOut = new Point2D(4, 5);
        Point2D patientIn = new Point2D(3, 5);
>>>>>>> Stashed changes

        InBorders inBorders = new InBorders(given);

        assertTrue(inBorders.isInside(patientIn));
        assertFalse(inBorders.isInside(patientOut));
<<<<<<< Updated upstream


=======
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPolygonIsEmpty() {
        List<Point2D> given = new ArrayList<>();

        Point2D point = new Point2D(4, 5);

        InBorders inBorders = new InBorders(given);
        String msg = "Polygon with border points cannot be empty";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inBorders.isInside(point));
        assertEquals(msg, exception.getMessage());
>>>>>>> Stashed changes
    }
}