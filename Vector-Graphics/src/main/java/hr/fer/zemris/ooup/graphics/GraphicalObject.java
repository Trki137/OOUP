package hr.fer.zemris.ooup.graphics;

import hr.fer.zemris.ooup.listener.GraphicalObjectListener;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.util.Rectangle;

import java.util.List;
import java.util.Stack;

public interface GraphicalObject {
    boolean isSelected();
    void setSelected(boolean selected);
    int getNumberOfHotPoints();
    Point getHotPoint(int index);
    void setHotPoint(int index, Point point);
    boolean isHotPointSelected(int index);
    void setHotPointSelected(int index, boolean selected);
    double getHotPointDistance(int index, Point mousePoint);
    void translate(Point delta);
    Rectangle getBoundingBox();
    double selectionDistance(Point mousePoint);
    void render(Renderer r);
    void addGraphicalObjectListener(GraphicalObjectListener l);
    void removeGraphicalObjectListener(GraphicalObjectListener l);
    String getShapeName();
    GraphicalObject duplicate();
    int getZIndex();
    void setZIndex(int z_index);
    String getShapeID();
    void load(Stack<GraphicalObject> stack, String data);
    void save(List<String> rows);



}
