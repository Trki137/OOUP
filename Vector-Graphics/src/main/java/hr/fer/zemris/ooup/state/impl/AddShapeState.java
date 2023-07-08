package hr.fer.zemris.ooup.state.impl;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.model.DocumentModel;
import hr.fer.zemris.ooup.state.State;
import hr.fer.zemris.ooup.util.Point;

public class AddShapeState extends IdleState {
    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(DocumentModel model, GraphicalObject prototype){
        this.model = model;
        this.prototype = prototype;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject duplicate = prototype.duplicate();
        duplicate.translate(mousePoint);
        model.addGraphicalObject(duplicate);
    }

}
