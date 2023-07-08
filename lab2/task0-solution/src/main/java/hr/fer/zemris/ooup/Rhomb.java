package hr.fer.zemris.ooup;

public class Rhomb extends Shape{

    protected Rhomb(Point center) {
        super(center);
    }

    @Override
    public void draw() {
        System.out.println("Draw rhomb");
    }
}
