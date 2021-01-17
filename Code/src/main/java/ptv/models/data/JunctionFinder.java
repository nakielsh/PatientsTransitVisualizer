package ptv.models.data;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class JunctionFinder {

    private int junctionsId;
    List<Junction> junctions;

    public JunctionFinder() {
        junctionsId = -1;
        junctions = new ArrayList<>();
    }

    public List<Junction> findJunctions(List<Distance> distances) {
        if (distances == null) {
            throw new IllegalArgumentException("Distance list cannot be null");
        }

        for (int i = 0; i < distances.size(); i++) {
            Distance firstDistance = distances.get(i);

            for (int j = i + 1; j < distances.size(); j++) {
                Distance secondDistance = distances.get(j);
                Point2D intersectionPoint = findIntersectionPoint(firstDistance, secondDistance);

                if (intersectionPoint != null) {
                    createJunction(i, j, intersectionPoint, distances, firstDistance, secondDistance);
                    junctionsId--;
                    i--;
                    break;
                }
            }
        }

        findBranchedJunctions(distances, junctions);

        return junctions;
    }

    private void createJunction(int i, int j, Point2D intersectionPoint, List<Distance> distances, Distance firstDistance, Distance secondDistance) {
        Junction junction = new Junction(junctionsId, intersectionPoint);
        junctions.add(junction);

        Distance[] newDistances = setDistances(junction, firstDistance, secondDistance);

        distances.remove(j);
        distances.remove(i);
        distances.add(newDistances[0]);
        distances.add(newDistances[1]);
        distances.add(newDistances[2]);
        distances.add(newDistances[3]);
    }

    private Point2D findIntersectionPoint(Distance dist1, Distance dist2) {
        Point2D point11 = dist1.getFirstNode().getCoordinates();
        Point2D point12 = dist1.getSecondNode().getCoordinates();
        Point2D point21 = dist2.getFirstNode().getCoordinates();
        Point2D point22 = dist2.getSecondNode().getCoordinates();

        double[] straight1Section = {Math.min(point11.getX(), point12.getX()), Math.max(point11.getX(), point12.getX())};
        double[] straight2Section = {Math.min(point21.getX(), point22.getX()), Math.max(point21.getX(), point22.getX())};
        double[] intersectionSectionX = {Math.max(straight1Section[0], straight2Section[0]), Math.min(straight1Section[1], straight2Section[1])};

        double a1 = calculateA(point11, point12);
        double a2 = calculateA(point21, point22);
        double b1 = calculateB(point11, a1);
        double b2 = calculateB(point21, a2);
        double intersectionX;

        if (a1 == a2) {
            return null;
        } else if (a1 == Double.MAX_VALUE) {
            intersectionX = point11.getX();
            if (intersectionSectionX[0] == intersectionX && intersectionSectionX[1] == intersectionX && intersectionX != point21.getX() && intersectionX != point22.getX()) {
                return new Point2D(intersectionX, a2 * intersectionX + b2);
            }
        } else if (a2 == Double.MAX_VALUE) {
            intersectionX = point21.getX();
            if (intersectionSectionX[0] == intersectionX && intersectionSectionX[1] == intersectionX && intersectionX != point11.getX() && intersectionX != point12.getX()) {
                return new Point2D(intersectionX, a1 * intersectionX + b1);
            }
        } else {
            intersectionX = (b2 - b1) / (a1 - a2);
            if (intersectionX > intersectionSectionX[0] && intersectionX < intersectionSectionX[1]) {
                return new Point2D(intersectionX, a1 * intersectionX + b1);
            }
        }

        return null;
    }

    private double calculateA(Point2D p1, Point2D p2) {
        if (p2.getX() == p1.getX()) {
            return Double.MAX_VALUE;
        } else {
            return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        }
    }

    private double calculateB(Point2D point, double a) {
        return point.getY() - a * point.getX();
    }

    private Distance[] setDistances(Junction junction, Distance distance1, Distance distance2) {
        Node n11 = distance1.getFirstNode();
        Node n12 = distance1.getSecondNode();
        Node n21 = distance2.getFirstNode();
        Node n22 = distance2.getSecondNode();

        double x11 = calculateDistanceFactor(n11.getCoordinates(), n12.getCoordinates(), junction.getCoordinates());
        double x12 = 1 - x11;
        double x21 = calculateDistanceFactor(n21.getCoordinates(), n22.getCoordinates(), junction.getCoordinates());
        double x22 = 1 - x21;

        Distance d11 = new Distance(distance1.getId(), n11, junction, x11 * distance1.getDist());
        Distance d12 = new Distance(distance1.getId(), n12, junction, x12 * distance1.getDist());
        Distance d21 = new Distance(distance2.getId(), n21, junction, x21 * distance2.getDist());
        Distance d22 = new Distance(distance2.getId(), n22, junction, x22 * distance2.getDist());

        n11.removeDistance(n12);
        n12.removeDistance(n11);
        n21.removeDistance(n22);
        n22.removeDistance(n21);

        n11.addNode(junction, d11);
        n12.addNode(junction, d12);
        n21.addNode(junction, d21);
        n22.addNode(junction, d22);

        junction.addNode(n11, d11);
        junction.addNode(n12, d12);
        junction.addNode(n21, d21);
        junction.addNode(n22, d22);

        return new Distance[]{d11, d12, d21, d22};
    }

    private double calculateDistanceFactor(Point2D node1, Point2D node2, Point2D junction) {
        double numerator = Math.pow(node1.getX() - junction.getX(), 2) + Math.pow(node1.getY() - junction.getY(), 2);
        double denominator = Math.pow(node1.getX() - node2.getX(), 2) + Math.pow(node1.getY() - node2.getY(), 2);
        return Math.sqrt(numerator / denominator);
    }

    private void findBranchedJunctions(List<Distance> distances, List<Junction> junctions) {
        int n = distances.size();

        for (int i = 0; i < n; i++) {
            Distance distance = distances.get(i);
            for (Junction junction : junctions) {
                if (isJunctionInDistance(distance, junction)) {
                    Distance[] newDistances = setDistancesInBranchedJunction(distance, junction);

                    distances.remove(i);
                    distances.add(newDistances[0]);
                    distances.add(newDistances[1]);

                    i--;
                    n--;
                }
            }
        }
    }

    private boolean isJunctionInDistance(Distance distance, Junction junction) {
        Point2D junctionPoint = junction.getCoordinates();
        Point2D point1 = distance.getFirstNode().getCoordinates();
        Point2D point2 = distance.getSecondNode().getCoordinates();
        double junctionX = junctionPoint.getX();
        double junctionY = junctionPoint.getY();

        double[] sectionX = {Math.min(point1.getX(), point2.getX()), Math.max(point1.getX(), point2.getX())};

        if (junctionX > sectionX[0] && junctionX < sectionX[1]) {
            double a = (point1.getY() - point2.getY()) / (point1.getX() - point2.getX());
            double b = point1.getY() - a * point1.getX();
            return junctionY == (a * junctionY + b);
        } else if (junctionX == sectionX[0] && junctionX == sectionX[1]) {
            return (junctionY < Math.max(point1.getY(), point2.getY()))
                    && (junctionY > Math.min(point1.getY(), point2.getY()));
        }

        return false;
    }

    private Distance[] setDistancesInBranchedJunction(Distance distance, Junction junction) {
        Node node1 = distance.getFirstNode();
        Node node2 = distance.getSecondNode();

        double x1 = calculateDistanceFactor(node1.getCoordinates(), node2.getCoordinates(), junction.getCoordinates());
        double x2 = 1 - x1;

        Distance d1 = new Distance(distance.getId(), node1, junction, x1 * distance.getDist());
        Distance d2 = new Distance(distance.getId(), node2, junction, x2 * distance.getDist());

        node1.removeDistance(node2);
        node2.removeDistance(node1);

        node1.addNode(junction, d1);
        node2.addNode(junction, d1);

        junction.addNode(node1, d1);
        junction.addNode(node2, d2);

        return new Distance[]{d1, d2};
    }
}



















