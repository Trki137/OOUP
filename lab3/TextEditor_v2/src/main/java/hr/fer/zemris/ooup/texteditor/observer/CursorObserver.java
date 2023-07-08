package hr.fer.zemris.ooup.texteditor.observer;

import hr.fer.zemris.ooup.texteditor.model.Location;

public interface CursorObserver {
    void updateCursorLocation(Location loc);
}