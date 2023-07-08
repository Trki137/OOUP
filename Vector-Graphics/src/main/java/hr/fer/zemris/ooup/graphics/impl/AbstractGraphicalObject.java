package hr.fer.zemris.ooup.graphics.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.listener.GraphicalObjectListener;
import hr.fer.zemris.ooup.util.GeometryUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private final Point[] hotPoints;
    private final boolean[] hotPointsSelected;
    private boolean selected;
    private final List<GraphicalObjectListener> listeners = new ArrayList<>();
    private int z_index = 0;

    public AbstractGraphicalObject(Point[] hotPoints){
        this.hotPoints = hotPoints;
        this.hotPointsSelected = new boolean[hotPoints.length];
    }

    @Override
    public Point getHotPoint(int index) {
        if(index < 0 || index >= hotPoints.length)
            throw new IllegalArgumentException();

        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        if(index < 0 || index >= hotPoints.length)
            throw new IllegalArgumentException();

        hotPoints[index] = point;
        notifyListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        if(index < 0 || index >= hotPoints.length)
            throw new IllegalArgumentException();

        return GeometryUtil.distanceFromPoint(hotPoints[index],mousePoint);
    }

    @Override
    public boolean isHotPointSelected(int index) {
        if(index < 0 || index >= hotPoints.length)
            throw new IllegalArgumentException();

        return hotPointsSelected[index];
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        if(index < 0 || index >= hotPoints.length)
            throw new IllegalArgumentException();

        hotPointsSelected[index] = selected;
    }

    @Override
    public void translate(Point delta) {
        for(int i = 0; i < hotPoints.length; i++){
            setHotPoint(i,hotPoints[i].translate(delta));
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    @Override
    public int getZIndex(){
        return z_index;
    }
    @Override
    public void setZIndex(int z_index){
        this.z_index = z_index;
    }

    protected void notifyListeners(){
        listeners.forEach(l -> l.graphicalObjectChanged(this));
    }

    protected void notifySelectionListeners(){
        listeners.forEach(l -> l.graphicalObjectSelectionChanged(this));
    }
}
