package hr.fer.zemris.ooup.demo;

import hr.fer.zemris.ooup.canvas.Canvas;
import hr.fer.zemris.ooup.graphics.GraphicalObject;
import hr.fer.zemris.ooup.graphics.impl.CompositeShape;
import hr.fer.zemris.ooup.graphics.impl.LineSegment;
import hr.fer.zemris.ooup.graphics.impl.Oval;
import hr.fer.zemris.ooup.model.DocumentModel;
import hr.fer.zemris.ooup.renderer.impl.SVGRendererImpl;
import hr.fer.zemris.ooup.state.State;
import hr.fer.zemris.ooup.state.impl.AddShapeState;
import hr.fer.zemris.ooup.state.impl.EraseState;
import hr.fer.zemris.ooup.state.impl.IdleState;
import hr.fer.zemris.ooup.state.impl.SelectShapeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;

public class Demo extends JFrame {

    private final Canvas canvas;
    private final DocumentModel model;
    private State currentState = new IdleState();
    private final Map<String, GraphicalObject> prototypeMap = new HashMap<>();

    public Demo(List<GraphicalObject> objects) {
        objects.forEach(o -> prototypeMap.put(o.getShapeID(),o));

        model = new DocumentModel();
        this.canvas = new Canvas(model, currentState);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(800, 500));
        WindowListener wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        };

        addWindowListener(wl);


        initGui();
        setVisible(true);
    }

    private final Action openFileAction = new AbstractAction("Učitaj") {
        @Override
        public void actionPerformed(ActionEvent e) {
            File f = chooseFile();

            if (f == null) return;

            List<String> rows = new ArrayList<>();
            try{
                rows = Files.readAllLines(Path.of(f.getAbsolutePath()));
            }catch (IOException exception){
                errorMsg("Couldn't open file " + f.getAbsolutePath());
            }

            if(rows.size() == 0) return;

            model.clear();

            Stack<GraphicalObject> stack = new Stack<>();
            for(String row: rows){
                String id = row.substring(0,row.indexOf(" ")).trim();
                String data = row.substring(row.indexOf(" ")).trim();
                GraphicalObject o = prototypeMap.get(id);
                if(o == null){
                    errorMsg("Invalid format.");
                    return;
                }

                o.load(stack,data);
            }

            while(!stack.isEmpty()){
                model.addGraphicalObject(stack.pop());
            }
        }
    };

    private final Action saveFileAction = new AbstractAction("Spremi") {
        @Override
        public void actionPerformed(ActionEvent e) {
            File f = chooseFile();

            if (f == null) return;

            List<String> rows = new ArrayList<>();

            for (GraphicalObject g : model.list())
                g.save(rows);

            Path savePath = Path.of(f.getAbsolutePath());

            try {
                Files.deleteIfExists(savePath);
                Files.createFile(savePath);

                for (String row : rows)
                    Files.writeString(savePath, row, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            } catch (IOException exception) {
                errorMsg("Couldn't save file to " + f.getAbsolutePath());
            }
        }
    };

    private void errorMsg(String message) {
        JOptionPane.showMessageDialog(
                Demo.this,
                message,
                "Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private final Action svgExportAction = new AbstractAction("SVG export") {
        @Override
        public void actionPerformed(ActionEvent e) {

            File f = chooseFile();

            if (f == null) return;

            SVGRendererImpl svgRenderer = new SVGRendererImpl(f.getAbsolutePath());
            for (GraphicalObject object : model.list()) {
                object.render(svgRenderer);
            }
            try {
                svgRenderer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    private File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Odaberi mjesto svg datoteke");

        int returnValue = fileChooser.showSaveDialog(Demo.this);

        if (returnValue != JFileChooser.APPROVE_OPTION) return null;

        return fileChooser.getSelectedFile();
    }

    private final Action selectAction = new AbstractAction("Selektiraj") {
        @Override
        public void actionPerformed(ActionEvent e) {
            setState(new SelectShapeState(model));
        }
    };

    private final Action deleteAction = new AbstractAction("Brisalo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            setState(new EraseState(model));
        }
    };

    private void setState(State state) {
        currentState.onLeaving();
        currentState = state;
        canvas.setCurrentState(state);
    }


    private void initGui() {
        Container c = getContentPane();

        c.setLayout(new BorderLayout());
        c.add(canvas, BorderLayout.CENTER);

        initToolbar(c);
    }

    private void initToolbar(Container c) {
        JToolBar toolBar = new JToolBar();

        toolBar.add(openFileAction);
        toolBar.add(saveFileAction);
        toolBar.add(svgExportAction);
        prototypeMap.forEach((name, object) -> {
            if(object.getShapeName() == null) return;
            toolBar.add(new AbstractAction(object.getShapeName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setState(new AddShapeState(model, object.duplicate()));
                }
            });
        });
        toolBar.add(selectAction);
        toolBar.add(deleteAction);

        c.add(toolBar, BorderLayout.PAGE_START);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<GraphicalObject> prototypes = new ArrayList<>();

            prototypes.add(new Oval());
            prototypes.add(new LineSegment());
            prototypes.add(new CompositeShape(new ArrayList<>()));

            new Demo(prototypes);
        });
    }
}
