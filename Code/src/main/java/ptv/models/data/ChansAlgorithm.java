package ptv.models.data;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChansAlgorithm {

    Point2D p0 = new Point2D(0,0);

    Comparator<Point2D> comparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Point2D vp1 = (Point2D) o1;
            Point2D vp2 = (Point2D) o2;
            int orient = orientation(p0, vp1, vp2);
            if (orient == 0) {
                return (dist(p0, vp2) >= dist(p0, vp1)) ? -1 : 1;
            }
            return (orient == 1) ? -1 : 1;
        }
    };

    private int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) * (q.getX() - p.getX()) * (r.getY() - q.getY());
        return (val > 0) ? -1 : (val < 0) ? 1 : 0;
    }

    private int compare(Point2D p0, Point2D vp1, Point2D vp2) {
        int orient = orientation(p0, vp1, vp2);
        if (orient == 0) {
            return (dist(p0, vp2) >= dist(p0, vp1)) ? -1 : 1;
        }
        return (orient == 1) ? -1 : 1;
    }

    private int tangent(List<Point2D> pointsList, Point2D p) {
        int l = 0;
        int r = pointsList.size();
        int l_before = orientation(p, pointsList.get(0), pointsList.get(pointsList.size() - 1));
        int l_after = orientation(p, pointsList.get(0), pointsList.get((l + 1) % pointsList.size()));

        while (l < r) {
            int c = ((l + r) >> 1);
            int c_before = orientation(p, pointsList.get(c), pointsList.get((c - 1) % pointsList.size()));
            int c_after = orientation(p, pointsList.get(c), pointsList.get((c + 1) % pointsList.size()));
            int c_side = orientation(p, pointsList.get(l), pointsList.get(c));

            if (c_before != -1 && c_after != -1) {
                return c;
            } else if ((c_side == 1) && (l_after == -1 || l_before == l_after) || (c_side == -1 && c_before == -1)) {
                r = c;
            } else {
                l = c + 1;
            }

            l_before = -c_after;
            l_after = orientation(p, pointsList.get(l), pointsList.get((l + 1) % pointsList.size()));
        }
        return l;
    }

    private Pair<Integer, Integer> extremeHullptPair(List<List<Point2D>> listListPoints) {
        int h = 0;
        int p = 0;

        for (int i = 0; i < listListPoints.size(); ++i) {
            int min_index = 0;
            double min_y = listListPoints.get(i).get(0).getY();
            for (int j = 1; j < listListPoints.get(i).size(); ++j) {
                if (listListPoints.get(i).get(j).getY() < min_y) {
                    min_y = listListPoints.get(i).get(j).getY();
                    min_index = j;
                }
            }
            if (listListPoints.get(i).get(min_index).getY() < listListPoints.get(h).get(p).getY()) {
                h = i;
                p = min_index;
            }
        }
        return new Pair<>(h, p);
    }

    private Pair<Integer, Integer> nextHullptPair(List<List<Point2D>> listListPoints, Pair<Integer, Integer> lpoint) {
        Point2D p = listListPoints.get(lpoint.getKey()).get(lpoint.getValue());
        Pair<Integer, Integer> next = new Pair<>(lpoint.getKey(), (lpoint.getValue() + 1) % listListPoints.get(lpoint.getKey()).size());

        for (int h = 0; h < listListPoints.size(); h++) {
            if (h != lpoint.getKey()) {
                int s = tangent(listListPoints.get(h), p);
                Point2D q = listListPoints.get(next.getKey()).get(next.getValue());
                Point2D r = listListPoints.get(h).get(s);
                int t = orientation(p, q, r);
                if (t == -1 || (t == 0) && dist(p, r) > dist(p, q)) {
                    next = new Pair<>(h, s);
                }
            }
        }
        return next;
    }

    private double dist(Point2D p1, Point2D p2) {
        return ((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())) + ((p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    private List<Point2D> keepLeft(List<Point2D> pointsList, Point2D p) {
        while (pointsList.size() > 1 && orientation(pointsList.get(pointsList.size() - 2), pointsList.get(pointsList.size() - 1), p) != 1) {
            pointsList.remove(pointsList.size() - 1);
        }
        if (pointsList.size() == 0 || pointsList.get(pointsList.size() - 1) != p) {
            pointsList.add(p);
        }
        return pointsList;
    }

    private List<Point2D> grahamScan(List<Point2D> pointsList) {
        if (pointsList.size() <= 1) {
            return pointsList;
        }
        pointsList.sort(comparator);
        List<Point2D> lower_hull = new ArrayList<>();
        for (Point2D point2D : pointsList) {
            lower_hull = keepLeft(lower_hull, point2D);
        }
        Collections.reverse(pointsList);
        List<Point2D> upper_hull = new ArrayList<>();
        for (Point2D point2D : pointsList) {
            upper_hull = keepLeft(upper_hull, point2D);
        }
        for (int i = 1; i < upper_hull.size(); ++i) {
            lower_hull.add(upper_hull.get(i));
        }
        return lower_hull;

    }

    public List<Point2D> countPolygon(List<Point2D> pointsList) {
        for (int t = 0; t < pointsList.size(); ++t) {
            for (int m = 1; m < (1 << (1 << t)); ++m) {
                List<List<Point2D>> hulls = new ArrayList<>();
                for (int i = 0; i < pointsList.size(); i += m) {
                    List<Point2D> chunk;
                    if ((i + m) > (pointsList.size() - 1)) {
                        chunk = pointsList.subList(i, i + m);
                    } else {
                        chunk = pointsList.subList(i, pointsList.size() - 1);
                    }
                    hulls.add(grahamScan(chunk));
                }
                List<Pair<Integer, Integer>> hull = new ArrayList<>();
                hull.add(extremeHullptPair(hulls));
                for (int i = 0; i < m; ++i) {
                    Pair<Integer, Integer> p = nextHullptPair(hulls, hull.get(hull.size() - 1));
                    List<Point2D> output = new ArrayList<>();
                    if (p == hull.get(0)) {
                        for (Pair<Integer, Integer> integerIntegerPair : hull) {
                            output.add(hulls.get(integerIntegerPair.getKey()).get(integerIntegerPair.getValue()));
                        }
                        return output;
                    }
                    hull.add(p);
                }
            }
        }
        return null;
    }


}

