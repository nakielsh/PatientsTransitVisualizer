package ptv.models.data;

public class Distance {
    private final int id;
    private final Node firstNode;
    private final Node secondNode;
    private final long dist;

    public Distance(int id, Node firstNode, Node secondNode, double dist) {
        this.id = id;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.dist = Math.round(dist * 100) / 100;
    }

    public int getId() {
        return id;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public double getDist() {
        return dist;
    }
}
