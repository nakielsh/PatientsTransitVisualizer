package ptv.models.data;

public class Distance {
    private final int id;
    private final Node firstNode;
    private final Node secondNode;
    private final double dist;
    //private Junction junction;

    public Distance(int id, Node firstNode, Node secondNode, double dist) {
        this.id = id;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.dist = Math.round(dist* 100)/100;
    }

    /*public void setJunction(Junction junction) {
        this.junction = junction;
    }*/

    public int getId() {
        return id;
    }

    public Node getFirstNode(){
        return firstNode;
    }

    public Node getSecondNode(){
        return secondNode;
    }

    public Node getAnotherNode(Node node){
        return firstNode.equals(node) ? secondNode: firstNode;
    }

    public double getDist() {
        return dist;
    }
}
