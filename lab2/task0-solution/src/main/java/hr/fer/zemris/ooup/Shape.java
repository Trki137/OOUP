package hr.fer.zemris.ooup;

public abstract class Shape {

    private final Point center;

    protected Shape(Point center) {
        this.center = center;
    }

    public Point getCenter() {
        return center;
    }

    public abstract void draw();
}
