package ptv.models.data;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrahamScan {
    private List<Point2D> allPoints;
    private List<Point2D> polygon;

    public GrahamScan() {
        this.allPoints = new ArrayList<>();
        this.polygon = new ArrayList<>();
    }

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
    private Comparator<Point2D> getGrahamComparator(Point2D p0) {
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

    public void countGrahamHull() {
        if (this.allPoints.size() < 1) {
            throw new IllegalArgumentException("There must be at least 1 point");
        }
        if (this.allPoints.size() <= 3) {
            this.polygon = this.allPoints;
            return;
        }
        Point2D p0 = this.allPoints.remove(findP0(this.allPoints));
        this.allPoints.sort(getGrahamComparator(p0));

        for (Point2D point : this.allPoints) {
            while (this.polygon.size() > 1 && (ccw(this.polygon.get(1), this.polygon.get(0), point) <= 0)) {
                this.polygon.remove(0);
            }
            this.polygon.add(0, point);
        }
        this.polygon.add(0, p0);
    }

    public static List<Point2D> createPointsList(List<Hospital> hospitals, List<Facility> facilities) {
        List<Point2D> objectsPoints = new ArrayList<>();
        if (hospitals.isEmpty() || facilities.isEmpty()) {
            throw new IllegalArgumentException("List can't be empty");
        }
        for (Hospital hospital : hospitals) {
            objectsPoints.add(hospital.getCoordinates());
        }
        for (Facility facility : facilities) {
            objectsPoints.add(facility.getCoordinates());
        }
        return objectsPoints;
    }

    public void setAllPoints(List<Point2D> points) {
        this.allPoints = points;
    }

    public List<Point2D> getPolygon() {
        return this.polygon;
    }

}
