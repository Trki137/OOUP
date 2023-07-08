package hr.fer.zemris.ooup.graphics.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.util.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class CompositeShape extends AbstractGraphicalObject implements Iterable<GraphicalObject> {

    private final List<GraphicalObject> graphicalObjectList;

    public CompositeShape(List<GraphicalObject> graphicalObjectList) {
        super(new Point[0]);
        this.graphicalObjectList = graphicalObjectList;
    }

    @Override
    public Rectangle getBoundingBox() {
        Rectangle bb = null;

        for(GraphicalObject object : graphicalObjectList){
            Rectangle objectBB = object.getBoundingBox();
            if(bb == null) bb=objectBB;
            else bb = bb.union(objectBB);
        }

        return bb;
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Double minDistance = null;

        for(GraphicalObject object : graphicalObjectList) {
            double distance = object.selectionDistance(mousePoint);
            if (minDistance == null || minDistance > distance){
                minDistance = distance;
            }
        }

        return minDistance == null ? 0.0 : minDistance;
    }

    @Override
    public void render(Renderer r){
        graphicalObjectList.forEach(object ->object.render(r));
    }

    @Override
    public String getShapeName() {
        return null;
    }

    @Override
    public GraphicalObject duplicate() {
        return new CompositeShape(new ArrayList<>(this.graphicalObjectList));
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        int numOfElements = Integer.parseInt(data);
        if(stack.size() < numOfElements) return;

        List<GraphicalObject> objects = new ArrayList<>();
        for(int i = 0; i < numOfElements; i++){
            objects.add(stack.pop());
        }

        CompositeShape o = (CompositeShape) duplicate();
        o.graphicalObjectList.addAll(objects);

        stack.add(o);
    }

    @Override
    public void save(List<String> rows) {
        graphicalObjectList.forEach(o -> o.save(rows));
        rows.add(String.format("%s %d\n", getShapeID(),graphicalObjectList.size()));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        graphicalObjectList.forEach(s -> s.setSelected(selected));
    }

    @Override
    public void translate(Point delta) {
        for(GraphicalObject object : graphicalObjectList){
            object.translate(delta);
        }
        notifyListeners();
    }

    @Override
    public Iterator<GraphicalObject> iterator() {
        return graphicalObjectList.iterator();
    }
}
