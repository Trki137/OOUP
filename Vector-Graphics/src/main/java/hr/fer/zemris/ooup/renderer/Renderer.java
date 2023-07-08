package hr.fer.zemris.ooup.renderer;

import hr.fer.zemris.ooup.util.Point;

public interface Renderer {
    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);
}
