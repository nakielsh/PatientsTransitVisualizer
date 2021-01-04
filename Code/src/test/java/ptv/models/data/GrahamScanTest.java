package ptv.models.data;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GrahamScanTest {

    @Test
    public void shouldFindP0() {
        List<Point2D> given1 = new ArrayList<>();
        given1.add(new Point2D(4,7));
        given1.add(new Point2D(8,3));
        given1.add(new Point2D(14,8));

        GrahamScan grahamScan = new GrahamScan();
        assertEquals(1, grahamScan.findP0(given1));

        given1.add(new Point2D(4, -2));
        assertEquals(3, grahamScan.findP0(given1));

        given1.add(new Point2D(2, -2));
        assertEquals(4, grahamScan.findP0(given1));
    }

    @Test
    public void shouldReturnTangens() {
        Point2D p1 = new Point2D(4, 7);
        Point2D p2 = new Point2D(1, 1);
        Point2D p3 = new Point2D(10, 10);
        Point2D p4 = new Point2D(2, 7);

        GrahamScan grahamScan = new GrahamScan();
        assertEquals(0.5, grahamScan.tan(p1, p2));
    }

    @Test
    public void comparatorTest() {
        List<Point2D> given = new ArrayList<>();
        given.add(new Point2D(6, 4));
        given.add(new Point2D(2, 3));
        given.add(new Point2D(3, 6));
        given.add(new Point2D(7, 2));
        given.add(new Point2D(8, 6));
        given.add(new Point2D(5, 5));


        List<Point2D> sorted = new ArrayList<>();
        sorted.add(new Point2D(7, 2));
        sorted.add(new Point2D(8, 6));
        sorted.add(new Point2D(6, 4));
        sorted.add(new Point2D(5, 5));
        sorted.add(new Point2D(3, 6));
        sorted.add(new Point2D(2, 3));


        GrahamScan grahamScan = new GrahamScan();
        Comparator<Point2D> comparator = grahamScan.getComparator(new Point2D(4, 2));
        given.sort(comparator);
        Collections.reverse(given);
        assertEquals(sorted, given);

    }

    @Test
    public void shouldReturnIfPointsAreCCW() {
        Point2D p1 = new Point2D(4, 7);
        Point2D p2 = new Point2D(1, 1);
        Point2D p3 = new Point2D(10, 10);
        Point2D p4 = new Point2D(2, 7);

        GrahamScan grahamScan = new GrahamScan();
        assertEquals(-1, grahamScan.ccw(p1, p2, p3));
        assertEquals(-1, grahamScan.ccw(p3, p1, p2));
    }

    @Test
    public void shouldReturnHull() {
        List<Point2D> given = new ArrayList<>();
        List<Point2D> got;
        given.add(new Point2D(4,2));
        given.add(new Point2D(2,3));
        given.add(new Point2D(5,5));
        given.add(new Point2D(3,6));
        given.add(new Point2D(8,6));
        given.add(new Point2D(6,4));
        given.add(new Point2D(7,2));

        GrahamScan grahamScan = new GrahamScan();
        got = grahamScan.returnHull(given);
        System.out.println(got);
        assertEquals(5, got.size());
    }



}