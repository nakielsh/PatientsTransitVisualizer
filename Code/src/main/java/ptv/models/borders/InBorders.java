package ptv.models.borders;

import javafx.geometry.Point2D;
import ptv.models.data.GrahamScan;

import java.util.ArrayList;
import java.util.List;

public class InBorders {
    private final List<Point2D> borderPoints;
    private static int INF = 10000;

    public InBorders(List<Point2D> borderPoints) {
        this.borderPoints = borderPoints;
    }

    private static boolean onSegment(Point2D p, Point2D q, Point2D r){
        return q.getX() <= Math.max(p.getX(), r.getX()) &&
                q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) &&
                q.getY() >= Math.min(p.getY(), r.getY());
    }

    private static int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) {
            return 0;
        }
        return (val > 0) ? 1 : 2;
    }

    private static boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2){
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4){
            return true;
        }
        if (o1 == 0 && onSegment(p1, p2, q1)){
            return true;
        }
        if (o2 == 0 && onSegment(p1, q2, q1)){
            return true;
        }
        if (o3 == 0 && onSegment(p2, p1, q2)){
            return true;
        }
        if (o4 == 0 && onSegment(p2, q1, q2)){
            return true;
        }
        return false;
    }

    public boolean isInside(Point2D p)
    {
        int n = borderPoints.size();
        if (n < 1)
        {
            return false;
        }
        if(n==1) {
            return p == borderPoints.get(0);
        }
        if (n == 2) {
            return onSegment(borderPoints.get(0), p, borderPoints.get(1));
        }

        Point2D extreme = new Point2D(INF, p.getY());

        int count = 0, i = 0;
        do{
            int next = (i + 1) % n;
            if (doIntersect(borderPoints.get(i), borderPoints.get(next), p, extreme)){
                if (orientation(borderPoints.get(i), p, borderPoints.get(next)) == 0) {
                    return onSegment(borderPoints.get(i), p, borderPoints.get(next));
                }
                count++;
            }
            i = next;
        } while (i != 0);
        return (count % 2 == 1);
    }



}
