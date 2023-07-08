package hr.fer.zemris.ooup.listener;

import hr.fer.zemris.ooup.graphics.GraphicalObject;

public interface GraphicalObjectListener {
    // Poziva se kad se nad objektom promjeni bio što...
    void graphicalObjectChanged(GraphicalObject go);
    // Poziva se isključivo ako je nad objektom promjenjen status selektiranosti
    // (baš objekta, ne njegovih hot-point-a).
    void graphicalObjectSelectionChanged(GraphicalObject go);
}
