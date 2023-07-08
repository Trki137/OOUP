package hr.fer.zemris.ooup.util;

public class GeometryUtil {
    public static double distanceFromPoint(Point point1, Point point2) {
        int x1 = point1.getX();
        int x2 = point2.getX();
        int y1 = point1.getY();
        int y2 = point2.getY();
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        if(p.getX() <= s.getX()) return distanceFromPoint(s,p);
        if(p.getX() >= e.getX()) return distanceFromPoint(p,e);

        int koefA = e.getY() - s.getY();
        int koefB = e.getX() - s.getX();
        int koefC = e.getX() * s.getY() - e.getY() * s.getX();

        return Math.abs(koefA * p.getX() - koefB * p.getY() + koefC)/ Math.sqrt(koefA*koefA + koefB * koefB);
    }
}
