package hr.fer.zemris.ooup.renderer.impl;

import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.Point;

import java.awt.*;
import java.util.Arrays;

public class G2DRendererImpl implements Renderer {

    private final Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d){
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX(),s.getY(),e.getX(),e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        g2d.setColor(Color.BLUE);
        int[] pointsX = Arrays.stream(points).mapToInt(Point::getX).toArray();
        int[] pointsY = Arrays.stream(points).mapToInt(Point::getY).toArray();
        g2d.fillPolygon(pointsX,pointsY,points.length);
        g2d.setColor(Color.RED);

        for(int i = 0; i < points.length -1; i++){
            g2d.drawLine(pointsX[i], pointsY[i], pointsX[i+1], pointsY[i+1]);
        }
    }
}
