package hr.fer.zemris.ooup.state.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.model.DocumentModel;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.Point;

import java.util.ArrayList;
import java.util.List;

public class EraseState extends IdleState{

    private final DocumentModel model;
    private final List<Point> points;

    public EraseState(DocumentModel model){
        this.model = model;
        this.points = new ArrayList<>();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        points.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        model.deleteObjects(points);

        points.clear();
        model.notifyListeners();
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        points.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if(points.size() == 0) return;

        for(int i = 0; i < points.size() - 1; i++){
            r.drawLine(points.get(i), points.get(i+1));
        }
    }
}
