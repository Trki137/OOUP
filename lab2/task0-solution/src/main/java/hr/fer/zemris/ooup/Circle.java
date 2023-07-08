package hr.fer.zemris.ooup;

public class Circle extends Shape{
    private double radius;

    public Circle(Point center, double radius) {
        super(center);

        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void draw() {
        System.out.println("Draw circle");
    }
}
