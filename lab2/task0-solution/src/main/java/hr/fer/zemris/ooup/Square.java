package hr.fer.zemris.ooup;

public class Square extends Shape{
    private double side;
    protected Square(Point center,double side) {
        super(center);

        this.side = side;

    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    @Override
    public void draw() {
        System.out.println("Draw square");
    }
}
