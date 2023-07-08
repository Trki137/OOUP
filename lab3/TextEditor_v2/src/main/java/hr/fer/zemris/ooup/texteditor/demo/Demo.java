package hr.fer.zemris.ooup.texteditor.demo;


import hr.fer.zemris.ooup.texteditor.command.*;
import hr.fer.zemris.ooup.texteditor.component.ClipboardStack;
import hr.fer.zemris.ooup.texteditor.component.TextEditor;
import hr.fer.zemris.ooup.texteditor.manager.ManagerInfo;
import hr.fer.zemris.ooup.texteditor.manager.UndoManager;
import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;
import hr.fer.zemris.ooup.texteditor.observer.UndoManagerObserver;
import hr.fer.zemris.ooup.texteditor.plugin.Plugin;
import hr.fer.zemris.ooup.texteditor.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serial;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Demo extends JFrame {

    private final TextEditor txtComponent;
    private final TextEditorModel textEditorModel;
    private final UndoManager undoManager = UndoManager.getInstance();
    private final HashMap<String, Plugin> myPlugins = new HashMap<>();
    private Path path;
    private volatile boolean clockRuns = true;
    private final JLabel clock = new JLabel();
    private final JLabel length = new JLabel();
    private final JLabel line = new JLabel();
    private final JLabel column = new JLabel();
    private final JLabel selected = new JLabel();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Demo(String[] plugins) {
        this.textEditorModel = new TextEditorModel("This is first line.\nThis is second line\nDean rj. OOUP", this);
        this.txtComponent = new TextEditor(textEditorModel);

        WindowListener wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clockRuns = false;
                dispose();
            }
        };
        addWindowListener(wl);

        if(plugins.length > 0)
            getPlugins(plugins);

        setTitle("OOUP Text editor");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        initGui();

        setVisible(true);
    }

    private void getPlugins(String[] plugins){
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Path.of("src/plugin.properties").toAbsolutePath().normalize()));
        }catch (Exception ignored){}

        for(String plugin : plugins){
            String fcqn = properties.getProperty(plugin);
            if(fcqn == null) continue;

            Plugin plugin1 = getPlugin(fcqn);
            if(plugin1 == null) continue;

            myPlugins.put(plugin1.getName(),plugin1);
        }
    }
    private Plugin getPlugin(String fcqn) {

        try {
            Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fcqn);
            Constructor<?> constructor = referenceToClass.getConstructor();
            return (Plugin) constructor.newInstance();
        } catch (Exception e) {
            return null;
        }

    }

    private void initGui() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        initActions();
        initListeners();
        createComponents(c);
        createStatusBar(c);
        createMenuBar();
    }

    private final Action openFileAction = new AbstractAction("Open") {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to open");

            int returnValue = fileChooser.showOpenDialog(Demo.this);


            if (returnValue != JFileChooser.APPROVE_OPTION) return;

            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.toPath();

            List<String> lines = null;

            try {
                lines = Files.readAllLines(path);
                textEditorModel.setLines(lines);
            } catch (Exception ignored) {
            }

            if (lines == null) {
                JOptionPane.showMessageDialog(
                        Demo.this,
                        "An error occurred while reading file " + selectedFile.getName(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    };

    private final Action saveFileAction = new AbstractAction("Save") {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(path != null){
                save();
                return;
            }

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setDialogTitle("Specify a file to save");

            int returnValue = fileChooser.showSaveDialog(Demo.this);

            if (returnValue != JFileChooser.APPROVE_OPTION) return;

            File file = fileChooser.getSelectedFile();
            path = file.toPath();

            if (file.exists()) {
                String[] options = new String[]{"Yes", "No"};

                int res = JOptionPane.showOptionDialog(
                        Demo.this,
                        String.format("%s %s %s", "File", file.getName(), "already_exists"),
                        "Close tab",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (res != 0) return;
            }
            try{
                Files.createFile(path);
            }catch (Exception ignored){}

            save();
        }
    };

    public void save() {
        StringBuilder sb = new StringBuilder();
        for (String line : textEditorModel.getLines()) {
            sb.append(line).append("\n");
        }
        try {
            Files.writeString(path, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ignored) {
        }
    }


    private final Action exitFileAction = new AbstractAction("Exit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            clockRuns = false;
            dispose();
        }
    };

    private final Action undoAction = new AbstractAction("Undo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            undoManager.undo();
        }
    };

    private final Action redoAction = new AbstractAction("Redo") {
        @Override
        public void actionPerformed(ActionEvent e) {
            undoManager.redo();
        }
    };
    private final Action cutAction = new AbstractAction("Cut") {
        @Override
        public void actionPerformed(ActionEvent e) {
            txtComponent.cut();
        }
    };

    private final Action copyAction = new AbstractAction("Copy") {
        @Override
        public void actionPerformed(ActionEvent e) {
            txtComponent.copy(false);
        }
    };


    private final Action pasteAction = new AbstractAction("Paste") {
        @Override
        public void actionPerformed(ActionEvent e) {
            txtComponent.paste();
        }
    };

    private final Action pasteAndTakeAction = new AbstractAction("Paste and Take") {
        @Override
        public void actionPerformed(ActionEvent e) {
            txtComponent.pasteRemoveFromStack();
        }
    };

    private final Action deleteAction = new AbstractAction("Delete section") {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditAction deleteAction = new DeleteBeforeAction(textEditorModel);
            deleteAction.execute_do();
            undoManager.push(deleteAction);
        }
    };

    private final Action clearDocumentAction = new AbstractAction("Clear documnet") {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditAction clearAction = new ClearAction(textEditorModel);
            clearAction.execute_do();
            undoManager.push(clearAction);
        }
    };

    private final Action cursorToStartAction = new AbstractAction("Cursor to document start") {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditAction cursorStartAction = new CursorMoveStart(textEditorModel);
            cursorStartAction.execute_do();
            undoManager.push(cursorStartAction);
        }
    };

    private final Action cursorToEndAction = new AbstractAction("Cursor to document end") {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditAction cursorEndAction = new CursorMoveEnd(textEditorModel);
            cursorEndAction.execute_do();
            undoManager.push(cursorEndAction);
        }
    };


    private void createComponents(Container c) {
        c.add(txtComponent, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(openFileAction);
        fileMenu.add(saveFileAction);
        fileMenu.add(exitFileAction);

        JMenu editMenu = new JMenu("Save");

        editMenu.add(undoAction);
        editMenu.add(redoAction);
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(pasteAndTakeAction);
        editMenu.add(deleteAction);
        editMenu.add(clearDocumentAction);

        JMenu moveMenu = new JMenu("Move");

        moveMenu.add(cursorToStartAction);
        moveMenu.add(cursorToEndAction);

        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(moveMenu);

        System.out.println(myPlugins);
        if(!myPlugins.isEmpty()){
            JMenu pluginMenu = new JMenu("Plugins");
            for(var pluginEntry : myPlugins.entrySet()){
                pluginMenu.add(new AbstractAction(pluginEntry.getKey()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        pluginEntry.getValue().execute(textEditorModel,undoManager,txtComponent.getClipboardStack());
                    }
                });
            }

            menu.add(pluginMenu);
        }

        this.setJMenuBar(menu);
    }

    private void createStatusBar(Container c) {
        JToolBar statusBar = new JToolBar();
        statusBar.setFloatable(false);
        statusBar.setLayout(new GridLayout(1, 2));

        JPanel infoPanel = new JPanel();
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1, 3));
        infoPanel.setLayout(new GridLayout(1, 2));

        setColumnLabel("0");
        setLineLabel("0");
        setSelectedLabel("-");
        setRowsLabel("0");

        panel.add(line);
        panel.add(column);
        panel.add(selected);

        infoPanel.add(length);
        infoPanel.add(panel);

        statusBar.add(infoPanel);

        clock.setText(formatter.format(LocalDateTime.now()));

        clock.setHorizontalAlignment(SwingUtilities.RIGHT);
        statusBar.add(clock);

        clock();

        c.add(statusBar, BorderLayout.SOUTH);
    }

    private void initActions() {
        openFileAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O
        );

        saveFileAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S
        );

        exitFileAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_E
        );

        undoAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_Z
        );
        undoAction.setEnabled(false);

        redoAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_R
        );
        redoAction.setEnabled(false);

        cutAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_X
        );
        cutAction.setEnabled(false);

        copyAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_C
        );
        copyAction.setEnabled(false);

        pasteAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_P
        );
        pasteAction.setEnabled(false);

        pasteAndTakeAction.setEnabled(false);

        deleteAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_D
        );
        deleteAction.setEnabled(false);

        clearDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_T
        );

        cursorToStartAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_M
        );

        cursorToEndAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_N
        );
    }

    public void initListeners() {
        textEditorModel.addCursorListener((loc) -> {
            setColumnLabel(Integer.toString(loc.getCoordinateX() + 1));
            setLineLabel(Integer.toString(loc.getCoordinateY()));
            if (textEditorModel.getSelectionRange() != null) {
                cutAction.setEnabled(true);
                copyAction.setEnabled(true);
                deleteAction.setEnabled(true);
                setSelectedLabel(calculateSelected());
            } else {
                cutAction.setEnabled(false);
                copyAction.setEnabled(false);
                deleteAction.setEnabled(false);
                setSelectedLabel("-");
            }
        });

        textEditorModel.addTextListener(() -> {
            setRowsLabel(String.valueOf(textEditorModel.getLines().size()));
        });

        txtComponent.getClipboardStack().addClipboardListener(() -> {
            ClipboardStack stack = txtComponent.getClipboardStack();

            pasteAction.setEnabled(stack.hasText());
            pasteAndTakeAction.setEnabled(stack.hasText());
        });

        UndoManagerObserver observer = new UndoManagerObserver() {
            @Override
            public void undoStackInfo(ManagerInfo managerInfoConsumer) {
                undoAction.setEnabled(managerInfoConsumer == ManagerInfo.NOT_EMPTY);
            }

            @Override
            public void redoStackInfo(ManagerInfo managerInfoConsumer) {
                redoAction.setEnabled(managerInfoConsumer == ManagerInfo.NOT_EMPTY);
            }
        };

        undoManager.addUndoListener(observer);
        undoManager.addRedoListener(observer);
    }

    private String calculateSelected() {
        List<String> lines = textEditorModel.getLines();
        LocationRange range = textEditorModel.getSelectionRange();

        Location rightEnd = range.getStart().compareTo(range.getEnd()) > 0 ? range.getStart() : range.getEnd();
        Location leftEnd = range.getStart().compareTo(range.getEnd()) < 0 ? range.getStart() : range.getEnd();

        if (leftEnd.getCoordinateY() == rightEnd.getCoordinateY()) {
            String highlighted = Util.getHighlightedText(leftEnd, rightEnd, lines.get(leftEnd.getCoordinateY()));
            return String.valueOf(highlighted.length());
        }

        int total = 0;

        int currentRow = leftEnd.getCoordinateY();
        int firstRow = leftEnd.getCoordinateY();
        int lastRow = rightEnd.getCoordinateY();

        for (Iterator<String> it = textEditorModel.linesRange(leftEnd.getCoordinateY(), rightEnd.getCoordinateY()); it.hasNext(); ) {
            String line = it.next();

            if (currentRow == firstRow)
                total += Util.getHighlightedText(leftEnd, new Location(lines.get(currentRow).length() - 1,leftEnd.getCoordinateY()), line).length();
            if (currentRow == lastRow) total += Util.getSubstringForCursor(rightEnd, line).length();
            if (currentRow > firstRow && currentRow < lastRow) total += line.length();

            currentRow++;
        }
        return String.valueOf(total);
    }

    private void clock() {
        updateTime();

        Thread t = new Thread(() -> {
            while (clockRuns) {
                try {
                    Thread.sleep(500);
                    txtComponent.setShow(!txtComponent.getShow());
                } catch (InterruptedException ignored) {
                }
                SwingUtilities.invokeLater(this::updateTime);
            }
        });

        t.start();
    }

    private void updateTime() {
        clock.setText(formatter.format(LocalDateTime.now()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Demo(args));
    }

    private void setRowsLabel(String value) {
        length.setText("length : " + value);
    }

    private void setLineLabel(String value) {
        line.setText("ln : " + value);
    }

    private void setColumnLabel(String value) {
        column.setText("col : " + value);
    }

    private void setSelectedLabel(String value) {
        selected.setText("sel : " + value);
    }


}
