package hr.fer.zemris.ooup.model;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.listener.DocumentModelListener;
import hr.fer.zemris.ooup.listener.GraphicalObjectListener;
import hr.fer.zemris.ooup.util.GeometryUtil;
import hr.fer.zemris.ooup.util.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;
    private List<GraphicalObject> objects = new ArrayList<>();
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    private final List<DocumentModelListener> listeners = new ArrayList<>();
    private final List<GraphicalObject> selectedObjects = new ArrayList<>();
    private final List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if(go.isSelected() && !selectedObjects.contains(go)) selectedObjects.add(go);
            else selectedObjects.remove(go);

            notifyListeners();
        }
    };

    public DocumentModel() {

    }
    public void clear() {
        objects.forEach(go -> go.removeGraphicalObjectListener(goListener));
        objects.clear();
        notifyListeners();
    }

    public void addGraphicalObject(GraphicalObject obj) {
        obj.addGraphicalObjectListener(goListener);
        objects.add(obj);
        if(obj.isSelected()) selectedObjects.add(obj);
        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        obj.removeGraphicalObjectListener(goListener);
        objects.remove(obj);
        if(obj.isSelected()) selectedObjects.remove(obj);
        notifyListeners();
    }
    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        listeners.forEach(DocumentModelListener::documentChanged);
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void increaseZ(GraphicalObject go) {
        go.setZIndex(go.getZIndex() + 1);
        objects = objects.stream().sorted((o1,o2) -> {
            int result = Integer.compare(o1.getZIndex(),o2.getZIndex());
            if(go == o1 && result == 0) return 1;
            if(go == o2 && result == 0) return -1;
            return result;
        }).toList();
        roObjects = Collections.unmodifiableList(objects);
        notifyListeners();
    }

    public void decreaseZ(GraphicalObject go) {
        go.setZIndex(go.getZIndex() - 1);
        objects = objects.stream().sorted((o1,o2) -> {
            int result = Integer.compare(o1.getZIndex(),o2.getZIndex());
            if(go == o1 && result == 0) return -1;
            if(go == o2 && result == 0) return 1;
            return result;
        }).toList();
        roObjects = Collections.unmodifiableList(objects);
        notifyListeners();
    }

    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        double minDistance = -1;
        GraphicalObject found = null;
        for (GraphicalObject graphicalObject : objects){
            double distance = graphicalObject.selectionDistance(mousePoint);
            if(found == null || distance < minDistance){
                minDistance = distance;
                found = graphicalObject;
            }
        }

        return minDistance < SELECTION_PROXIMITY ? found : null;
    }


    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int index = -1;
        double minDistance = -1;
        for(int i = 0; i < object.getNumberOfHotPoints(); i++){
            Point hotPoint = object.getHotPoint(i);
            double distance = GeometryUtil.distanceFromPoint(hotPoint,mousePoint);
            if(distance < SELECTION_PROXIMITY){
                if(index == -1 || minDistance > distance){
                    index = i;
                    minDistance = distance;
                }
            }
        }
        return index;
    }

    public void clearSelected() {
        List<GraphicalObject> selectedCopy = new ArrayList<>(selectedObjects);
        selectedCopy.forEach(o -> o.setSelected(false));
        notifyListeners();
    }

    public void clearForGroup() {
        new ArrayList<>(selectedObjects).forEach(this::removeGraphicalObject);
    }

    public void deleteObjects(List<Point> points) {
        for(Point p : points){
            List<GraphicalObject> deletedObjects = new ArrayList<>();
            for(GraphicalObject object: objects){
                double value = object.selectionDistance(p);
                if(value < SELECTION_PROXIMITY) deletedObjects.add(object);
            }
            if(deletedObjects.size() > 0)
                deletedObjects.forEach(this::removeGraphicalObject);
        }

    }
}
