package hr.fer.zemris.ooup.graphics.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.GeometryUtil;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.util.Rectangle;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {


    public LineSegment() {
        this(new Point(0, 0), new Point(10, 0));
    }

    public LineSegment(Point start, Point end) {
        super(new Point[]{start, end});
    }

    private LineSegment(LineSegment lineSegment) {
        this(
                lineSegment.getHotPoint(0),
                lineSegment.getHotPoint(1)
        );
    }


    @Override
    public Rectangle getBoundingBox() {
        Point start = getHotPoint(0);
        Point end = getHotPoint(1);

        int x = Math.min(start.getX(), end.getX());
        int y = Math.min(start.getY(), end.getY());
        int width = Math.abs(start.getX() - end.getX());
        int height = Math.abs(start.getY() - end.getY());

        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point start = getHotPoint(0);
        Point end = getHotPoint(1);

        return GeometryUtil.distanceFromLineSegment(start, end, mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(this);
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] split = data.split(" ");
        if (split.length != 4) return;

        GraphicalObject o = duplicate();

        int hotPointIndex = 0;
        for (int i = 0; i < split.length; i += 2) {
            o.setHotPoint(hotPointIndex++, new Point(Integer.parseInt(split[i]), Integer.parseInt(split[i + 1])));
        }

        stack.push(o);
    }

    @Override
    public void save(List<String> rows) {
        Point start = getHotPoint(0);
        Point end = getHotPoint(1);

        int x1 = start.getX();
        int y1 = start.getY();
        int x2 = end.getX();
        int y2 = end.getY();

        String ovalSave = String.format("%s %d %d %d %d\n", getShapeID(), x1, y1, x2, y2);
        rows.add(ovalSave);

    }
}
