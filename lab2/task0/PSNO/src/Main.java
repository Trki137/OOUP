import java.util.Random;


//TODO: RJEŠENJE S PREDAVANJA IMPLEMENTIRATI
public class Main {
    public static void main(String[] args) {
        Shape[] shapes = new Shape[]{
                new Circle(2, new Point(1, 1)),
                new Square(2, new Point(-2, -2)),
                new Square(2, new Point(3, 3)),
                new Circle(4, new Point(2, -1)),
                new Rhomb(new Point(1,1), new Point(0,0),1)
        };

        drawShapes(shapes,4);
        for(int i = 0; i < shapes.length; i++){
            moveShapes(
                    shapes[i],
                    new Random().nextInt(10),
                    new Random().nextInt(10)
            );
        }
    }

    public static void moveShapes(Shape shape, int moveX,int moveY){
           if(shape instanceof Circle circle){
               System.out.printf("Circle center before (%d , %d)\n",circle.getCenter().x(),circle.getCenter().y());
               circle.setCenter(new Point(
                       circle.getCenter().x() + moveX,
                       circle.getCenter().y() + moveY
               ));
               System.out.printf("Circle center after (%d , %d)\n",circle.getCenter().x(),circle.getCenter().y());
           } else if (shape instanceof Square square) {

               System.out.printf("Square center before (%d , %d)\n",square.getCenter().x(),square.getCenter().y());

               square.setCenter(new Point(
                       square.getCenter().x() + moveX,
                       square.getCenter().y() + moveY
               ));

               System.out.printf("Square center after (%d , %d)\n",square.getCenter().x(),square.getCenter().y());

           }else{
               throw new IllegalArgumentException();
           }
    }

    public static void drawShapes(Shape[] shapes,int n){
        for(int i = 0; i < n; i++){
            if(shapes[i] instanceof Square){
                drawSquare((Square)shapes[i]);
                continue;
            }else if(shapes[i] instanceof Circle){
                drewCircle((Circle)shapes[i]);
                continue;
            }else if (shapes[i] instanceof Rhomb){
                drawRhomb((Rhomb)shapes[i]);
                continue;
            }else {
                System.out.println("Program terminated");
                throw new IllegalArgumentException();
            }
        }
    }

    private static void drawRhomb(Rhomb shape) {
        System.out.println("InDrawRhomb");
    }

    private static void drewCircle(Circle shape) {
        System.out.println("InDrawCircle");
    }

    private static void drawSquare(Square shape) {
        System.out.println("InDrawSquare");
    }
}