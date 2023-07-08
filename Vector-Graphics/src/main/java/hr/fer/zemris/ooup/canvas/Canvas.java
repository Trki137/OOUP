package hr.fer.zemris.ooup.canvas;

import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.graphics.impl.LineSegment;
import hr.fer.zemris.ooup.model.DocumentModel;
import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.renderer.impl.G2DRendererImpl;
import hr.fer.zemris.ooup.state.State;
import hr.fer.zemris.ooup.util.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JComponent {
    private final DocumentModel model;
    private State currentState;

    public Canvas(DocumentModel model, State currentState){
        setFocusable(true);
        model.addDocumentModelListener(this::repaint);

        this.model = model;
        this.currentState = currentState;
        initKeyListeners();
        initMouseListeners();
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    private void initMouseListeners() {
        MouseListener mouseListener = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
                currentState.mouseDown(new Point(e.getX(),e.getY()), e.isShiftDown(),e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentState.mouseUp(new Point(e.getX(),e.getY()), e.isShiftDown(),e.isControlDown());
            }
        };


        MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentState.mouseDragged(new Point(e.getX(),e.getY()));
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseMotionListener);
    }

    private void initKeyListeners() {
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                currentState.keyPressed(e.getKeyCode());
            }
        };

        addKeyListener(keyListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);

        g2d.fillRect(0,0,getWidth(),getHeight());

        Renderer renderer = new G2DRendererImpl(g2d);

        for(GraphicalObject graphicalObject: model.list()){
            graphicalObject.render(renderer);
            currentState.afterDraw(renderer,graphicalObject);
        }

        currentState.afterDraw(renderer);
    }
}
