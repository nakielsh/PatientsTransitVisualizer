package ptv.models.data;

public class Distance {
    private final int id;
    private final Hospital firstOM;
    private final Hospital secondOM;
    private final double dist;
    private Junction junction;

    public Distance(int id, Hospital firstOM, Hospital secondOM, double dist) {
        this.id = id;
        this.firstOM = firstOM;
        this.secondOM = secondOM;
        this.dist = dist;
    }

    public void setJunction(Junction junction) {
        this.junction = junction;
    }

    public int getId() {
        return id;
    }

    public Hospital getFirstOM() {
        return firstOM;
    }

    public Hospital getSecondOM() {
        return secondOM;
    }

    public double getDist() {
        return dist;
    }

    public Junction getJunction() {
        return junction;
    }
}
