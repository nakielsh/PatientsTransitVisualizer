package ptv.models.data;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrahamScan {

    private static int findP0(List<Point2D> points) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("List is to short");
        }
        double minY = points.get(0).getY();
        double minTempX = points.get(0).getX();
        int index = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getY() < minY) {
                minY = points.get(i).getY();
                minTempX = points.get(i).getX();
                index = i;
            } else if (points.get(i).getY() == minY) {
                if (points.get(i).getX() < minTempX) {
                    minY = points.get(i).getY();
                    minTempX = points.get(i).getX();
                    index = i;
                }
            }
        }
        return index;
    }

    private static double tan(Point2D p0, Point2D p1) {
        if (p0.getY() == p1.getY()) {
            throw new IllegalArgumentException("These points are collinear");
        }
        return ((p1.getX() - p0.getX())) / (p1.getY() - p0.getY());
    }

    //comparator to sort by angle
    //compares to the lowest y the most left point p0
    private Comparator<Point2D> getComparator(Point2D p0) {
        return (vp1, vp2) -> {
            if (vp1.getY() == p0.getY() || vp2.getY() == p0.getY()) {
                if (vp1.getY() == p0.getY() && vp2.getY() == p0.getY()) {
                    return Double.compare(p0.distance(vp1), p0.distance(vp2));
                }
                return vp1.getY() == p0.getY() ? 1 : -1;
            }
            if (Double.compare(tan(p0, vp1), tan(p0, vp2)) != 0) {
                return Double.compare(tan(p0, vp1), tan(p0, vp2));
            }
            return Double.compare(p0.distance(vp1), p0.distance(vp2));
        };
    }

    private static int ccw(Point2D p1, Point2D p2, Point2D p3) {
        double val = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p3.getX() - p1.getX()) * (p2.getY() - p1.getY());
        return val > 0 ? -1 : (val < 0) ? 1 : 0;
    }

    public List<Point2D> returnGrahamHull(List<Point2D> points) {
        if(points.size() < 3) {
            throw new IllegalArgumentException("There must be at least 3 points to count hull");
        }
        List<Point2D> hull = new ArrayList<>();
        Point2D p0 = points.remove(findP0(points));
        points.sort(getComparator(p0));

        for (Point2D point : points) {
            while (hull.size() > 1 && (ccw(hull.get(1), hull.get(0), point) <= 0)) {
                hull.remove(0);
            }
            hull.add(0, point);
        }
        hull.add(0, p0);
        return hull;
    }



//    public List<Point2D> returnJarvisMarchHull(List<Point2D> points) {
//        if(points.size() < 3) {
//            throw new IllegalArgumentException("There must be at least 3 points to count hull");
//        }
//        List<Point2D> hull = new ArrayList<>();
//
//    }

}
