package hr.fer.zemris.ooup.state.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.graphics.impl.CompositeShape;
import hr.fer.zemris.ooup.model.DocumentModel;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.GeometryUtil;
import hr.fer.zemris.ooup.util.Point;
import hr.fer.zemris.ooup.util.Rectangle;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectShapeState extends IdleState {

    private final DocumentModel model;

    private final int SIDE = 3;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    private Point prevPoint;

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        prevPoint = new Point(mousePoint.getX(),mousePoint.getY());
        for(GraphicalObject object :model.list()){
            if(object.selectionDistance(mousePoint) < DocumentModel.SELECTION_PROXIMITY){
                return;
            }
        }

        model.clearSelected();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject object = model.findSelectedGraphicalObject(mousePoint);

        if (object == null) return;

        if (object.isSelected()) {
            for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
                Point hotPoint = object.getHotPoint(i);
                double distance = GeometryUtil.distanceFromPoint(hotPoint, mousePoint);
                if (distance < DocumentModel.SELECTION_PROXIMITY) {
                    object.setHotPointSelected(i, true);
                    return;
                } else object.setHotPointSelected(i, false);
            }
        }
        if (ctrlDown) {
            object.setSelected(!object.isSelected());
        } else {
            model.clearSelected();
            object.setSelected(!object.isSelected());
        }

        prevPoint = null;
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (!go.isSelected()) return;

        Rectangle rectangle = go.getBoundingBox();

        Point topLeft = new Point(rectangle.getX(), rectangle.getY());
        Point topRight = new Point(rectangle.getX() + rectangle.getWidth(), rectangle.getY());
        Point bottomLeft = new Point(rectangle.getX(), rectangle.getY() + rectangle.getHeight());
        Point bottomRight = new Point(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());

        drawBoundingBox(r, topLeft, topRight, bottomRight, bottomLeft);

        if (model.getSelectedObjects().size() > 1) return;

        for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
            Point point = go.getHotPoint(i);

            topLeft = new Point(point.getX() - SIDE, point.getY() - SIDE);
            topRight = new Point(point.getX() + SIDE, point.getY() - SIDE);
            bottomLeft = new Point(point.getX() - SIDE, point.getY() + SIDE);
            bottomRight = new Point(point.getX() + SIDE, point.getY() + SIDE);

            drawBoundingBox(r, topLeft, topRight, bottomRight, bottomLeft);
        }

    }

    private void drawBoundingBox(Renderer r, Point topLeft, Point topRight, Point bottomRight, Point bottomLeft) {
        r.drawLine(topLeft, topRight);
        r.drawLine(topRight, bottomRight);
        r.drawLine(bottomRight, bottomLeft);
        r.drawLine(bottomLeft, topLeft);
    }

    @Override
    public void onLeaving() {
        model.clearSelected();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        //System.out.println(model.getSelectedObjects().size());
        if (model.getSelectedObjects().size() != 1) {
            moveObjects(mousePoint);
            return;
        };

        GraphicalObject selected = model.findSelectedGraphicalObject(mousePoint);
        //System.out.println(selected);
        if (selected == null) return;
        if (!selected.isSelected()) return;

        int hotPointIndex = model.findSelectedHotPoint(selected, mousePoint);
        if (hotPointIndex == -1) {
            moveObjects(mousePoint);
            return;
        };
        selected.setHotPoint(hotPointIndex, mousePoint);
    }

    private void moveObjects(Point mousePoint) {
        for (GraphicalObject object : model.getSelectedObjects()){
                Point diff = mousePoint.difference(prevPoint);
                object.translate(diff);
        }
        prevPoint = mousePoint;
    }

    @Override
    public void keyPressed(int keyCode) {
        if (model.getSelectedObjects().size() == 0) return;

        switch (keyCode) {
            case KeyEvent.VK_DOWN -> {
                model.getSelectedObjects().forEach(selected -> selected.translate(new Point(0, 1)));
            }
            case KeyEvent.VK_UP -> {
                model.getSelectedObjects().forEach(selected -> selected.translate(new Point(0, -1)));
            }
            case KeyEvent.VK_RIGHT -> {
                model.getSelectedObjects().forEach(selected -> selected.translate(new Point(1, 0)));
            }
            case KeyEvent.VK_LEFT -> {
                model.getSelectedObjects().forEach(selected -> selected.translate(new Point(-1, 0)));
            }
            case KeyEvent.VK_ADD  -> model.getSelectedObjects().forEach(model::increaseZ);
            case KeyEvent.VK_SUBTRACT -> model.getSelectedObjects().forEach(model::decreaseZ);
            case KeyEvent.VK_G -> group();
            case KeyEvent.VK_U -> ungroup();
        }
    }

    private void group() {
        List<GraphicalObject> group = new ArrayList<>(model.getSelectedObjects());
        model.clearForGroup();
        model.addGraphicalObject(new CompositeShape(group));
    }

    private void ungroup() {
        List<CompositeShape> compositeShapes = model.getSelectedObjects().stream().filter(object -> object instanceof CompositeShape).map(o -> (CompositeShape)o).toList();
        for(CompositeShape composite:compositeShapes){
            model.removeGraphicalObject(composite);
            composite.forEach(model::addGraphicalObject);
        }
    }
}
