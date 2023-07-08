package hr.fer.zemris.ooup;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Circle(new Point(0,0),1),
                new Square(new Point(2,2),3),
                new Rhomb(new Point(-2,-2)),
                new Square(new Point(-3,2),2)
        };

        drawShapes(shapes);
        moveShapes(shapes[0],new Point(1,1));
        moveShapes(shapes[0],new Point(0,1));
        moveShapes(shapes[0],new Point(-1,1));
        moveShapes(shapes[0],new Point(-1,-1));
    }

    private static void moveShapes(Shape shape, Point point){
        System.out.println("Center before: "+point);

        shape.getCenter().setX(shape.getCenter().getX() + point.getX());
        shape.getCenter().setY(shape.getCenter().getY() + point.getY());

        System.out.println("Center after: "+point);
    }
    private static void drawShapes(Shape[] shapes) {
        Arrays.stream(shapes).forEach(Shape::draw);
    }
}