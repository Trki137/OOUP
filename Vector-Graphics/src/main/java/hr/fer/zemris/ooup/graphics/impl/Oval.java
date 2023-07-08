package hr.fer.zemris.ooup.graphics.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.GeometryUtil;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.util.Rectangle;

import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject{

    public Oval(){
        this(new Point(10,0), new Point(0,10));
    }
    public Oval(Point rightHotPoint, Point southHotPoint){
        super(new Point[]{rightHotPoint,southHotPoint});
    }
    private Oval(Oval oval){
        this(
                oval.getHotPoint(0),
                oval.getHotPoint(1)
        );
    }

    @Override
    public Rectangle getBoundingBox() {
        Point bottomRight = new Point(getHotPoint(0).getX(), getHotPoint(1).getY());
        int width =(int)(2 * GeometryUtil.distanceFromPoint(bottomRight, getHotPoint(1)));
        int height = (int) (2 * GeometryUtil.distanceFromPoint(bottomRight, getHotPoint(0)));
        int x = getHotPoint(1).getX() - width / 2;
        int y = getHotPoint(0).getY() - height / 2;

        return new Rectangle(x,y,width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point rightHotPoint = getHotPoint(0);
        Point southHotPoint = getHotPoint(1);

        Point center = new Point(southHotPoint.getX(), rightHotPoint.getY());

        int width = rightHotPoint.getX() - southHotPoint.getX();
        int height = southHotPoint.getY() - rightHotPoint.getY();

        boolean isInOval = ((Math.pow(mousePoint.getX() - center.getX(),2))/(Math.pow(Math.max(width,height),2)) +
                (Math.pow(mousePoint.getY() - center.getY(), 2))/(Math.pow(Math.min(width,height),2))) <= 1;

        return isInOval ? 0.0 : Math.min(GeometryUtil.distanceFromPoint(mousePoint,getHotPoint(0)),GeometryUtil.distanceFromPoint(mousePoint,getHotPoint(1)));
    }

    @Override
    public void render(Renderer r) {
        Point rightHotPoint = getHotPoint(0);
        Point southHotPoint = getHotPoint(1);

        Point center = new Point(southHotPoint.getX(), rightHotPoint.getY());

        int width = rightHotPoint.getX() - southHotPoint.getX();
        int height = southHotPoint.getY() - rightHotPoint.getY();

        int semiMajorAxis = Math.max(width,height);
        int semiMinorAxis = Math.min(width,height);
        int numberOfPoints = 10000;

        Point[] points = new Point[numberOfPoints];

        for(int i = 0; i < numberOfPoints; i++){
            double theta = 2 * Math.PI * i / numberOfPoints;
            int x = center.getX() + (int)(semiMajorAxis * Math.cos(theta));
            int y = center.getY() + (int)(semiMinorAxis * Math.sin(theta));
            points[i] = new Point(x,y);
        }

        r.fillPolygon(points);
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(this);
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] split = data.split(" ");
        if(split.length != 4) return;

        GraphicalObject o = duplicate();

        int hotPointIndex = 0;
        for(int i = 0; i < split.length; i+=2){
            o.setHotPoint(hotPointIndex++, new Point(Integer.parseInt(split[i]),Integer.parseInt(split[i+1])));
        }

        stack.push(o);
    }

    @Override
    public void save(List<String> rows) {
        Point right = getHotPoint(0);
        Point down = getHotPoint(1);

        int x1 = right.getX();
        int y1 = right.getY();
        int x2 = down.getX();
        int y2 = down.getY();

        String ovalSave = String.format("%s %d %d %d %d\n",getShapeID(),x1,y1,x2,y2);
        rows.add(ovalSave);
    }

}
