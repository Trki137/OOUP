public class Square extends Shape {

    private  Point center;

    private final double side;

    public Square(double side,Point center) {
        this.center = center;
        this.side = side;
    }

    public Point getCenter() {
        return center;
    }

    public double getSide() {
        return side;
    }

    public void setCenter(Point center) {
        this.center = center;
    }
}
