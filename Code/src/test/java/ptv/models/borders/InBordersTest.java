package ptv.models.borders;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InBordersTest {

    @Test
    public void isInside() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(4, 2));
        given.add(new Point2D(7, 2));
        given.add(new Point2D(8, 6));
        given.add(new Point2D(3, 6));
        given.add(new Point2D(2, 3));

        Point2D patientIn = new Point2D(5, 4);
        Point2D patientOut = new Point2D(10, 10);

        InBorders inBorders = new InBorders(given);
        assertTrue(inBorders.isInside(patientIn));
        assertFalse(inBorders.isInside(patientOut));
    }

    @Test
    public void shouldPatientBeInTheSamePositionAsPointWhenPolygonHasOnePoint(){
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(4,4));

        Point2D patientIn = new Point2D(4,4);
        Point2D patientOut = new Point2D(4,5);

        InBorders inBorders = new InBorders(given);

        assertTrue(inBorders.isInside(patientIn));
        assertFalse(inBorders.isInside(patientOut));


    }
}