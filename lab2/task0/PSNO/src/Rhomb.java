public class Rhomb extends Shape{
    private Point bottomLeft;

    private Point topLeft;

    private final double side;

    public Rhomb(Point bottomLeft, Point topLeft, double side) {
        this.bottomLeft = bottomLeft;
        this.topLeft = topLeft;
        this.side = side;
    }


    public double getSide() {
        return side;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public void setBottomLeft(Point bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }
}
