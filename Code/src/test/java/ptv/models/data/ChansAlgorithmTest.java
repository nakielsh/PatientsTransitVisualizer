package ptv.models.data;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChansAlgorithmTest {

    @Test
    public void shouldPouints(){
        List<Point2D> given = new ArrayList<>();
        List<Point2D> got;
        given.add(new Point2D(4,7));
        given.add(new Point2D(8,9));
        given.add(new Point2D(14,8));
        given.add(new Point2D(12,12));
        given.add(new Point2D(6,13));
        given.add(new Point2D(6,10));
        given.add(new Point2D(9,10));
        given.add(new Point2D(9,8));
        given.add(new Point2D(7,7));
        given.add(new Point2D(9,6));

        ChansAlgorithm chansAlgorithm = new ChansAlgorithm();
        got = chansAlgorithm.countPolygon(given);
        assertEquals(5, got.size());
    }
}
