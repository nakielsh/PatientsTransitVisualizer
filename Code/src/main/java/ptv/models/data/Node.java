package ptv.models.data;

import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;

public class Node {

    protected final int id;
    protected final Point2D coordinates;
    protected Map<Node, Distance> adjacentNodes;

    public Node(int id, Point2D coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        adjacentNodes = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public void addNode(Node node, Distance distance) {
        adjacentNodes.put(node, distance);
    }

    public Map<Node, Distance> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void removeDistance(Node node) {
        adjacentNodes.remove(node);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            return id == node.getId() && coordinates.equals(node.coordinates);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 97 * id + 89 * coordinates.hashCode();
    }

}
