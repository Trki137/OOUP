package hr.fer.zemris.ooup.texteditor.component;

import hr.fer.zemris.ooup.texteditor.command.DeleteAfterAction;
import hr.fer.zemris.ooup.texteditor.command.DeleteBeforeAction;
import hr.fer.zemris.ooup.texteditor.command.EditAction;
import hr.fer.zemris.ooup.texteditor.command.InsertAction;
import hr.fer.zemris.ooup.texteditor.manager.UndoManager;
import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;
import hr.fer.zemris.ooup.texteditor.observer.CursorObserver;
import hr.fer.zemris.ooup.texteditor.observer.TextObserver;
import hr.fer.zemris.ooup.texteditor.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.List;

public class TextEditor extends JComponent implements CursorObserver, TextObserver {
    private final TextEditorModel textEditorModel;
    private final ClipboardStack clipboardStack;
    private final UndoManager undoManager;
    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private boolean show = true;
    private boolean shiftPressed = false;
    private final List<Integer> cursorActionList = List.of(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    private final List<Integer> delete = List.of(KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE);

    public TextEditor(TextEditorModel textEditorModel) {
        this.textEditorModel = textEditorModel;
        this.undoManager = UndoManager.getInstance();
        this.clipboardStack = new ClipboardStack();
        this.textEditorModel.addTextListener(this);
        this.textEditorModel.addCursorListener(this);
        initKeyListener();
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(Color.BLACK);

        drawText(g2d, fontMetrics);
        if(show)
            drawCursor(g2d, fontMetrics);
        drawHighlighted(g2d,fontMetrics);
    }

    private void drawCursor(Graphics2D g2d, FontMetrics fontMetrics) {
        List<String> lines = textEditorModel.getLines();
        Location cursorLocation = textEditorModel.getCursorLocation();

        int row = cursorLocation.getCoordinateY();
        String text = lines.size() > 0 ? lines.get(row): "";

        String result = Util.getSubstringForCursor(cursorLocation,text);

        int width = fontMetrics.stringWidth(result);
        int height = fontMetrics.getHeight();

        g2d.drawRect(width,height * row,1,height);
    }

    private void drawText(Graphics2D g2d, FontMetrics fontMetrics) {
        int yOffset = fontMetrics.getAscent();
        for (String line : textEditorModel) {
            g2d.drawString(line, 0, yOffset);
            yOffset += fontMetrics.getHeight();
        }
    }

    private void drawHighlighted(Graphics2D g2d, FontMetrics fontMetrics){
        LocationRange range = textEditorModel.getSelectionRange();
        List<String> lines = textEditorModel.getLines();
        if(range == null) return;

        Location rightEnd = range.getStart().compareTo(range.getEnd()) > 0 ? range.getStart() : range.getEnd();
        Location leftEnd = range.getStart().compareTo(range.getEnd()) < 0 ? range.getStart() : range.getEnd();

        int row = leftEnd.getCoordinateY();

        if(leftEnd.equals(rightEnd)) return;

        Color selectedColor = new Color(33, 150, 243, 99);
        g2d.setColor(selectedColor);
        int height = fontMetrics.getHeight();

        int currentLine = row;
        int lastRow = rightEnd.getCoordinateY();

        if(row == lastRow){
            highlight(g2d,fontMetrics,leftEnd,rightEnd,row);
            return;
        }

        for (Iterator<String> it = textEditorModel.linesRange(leftEnd.getCoordinateY(), rightEnd.getCoordinateY()); it.hasNext(); ) {
            String line = it.next();

            if(currentLine == row || currentLine == rightEnd.getCoordinateY()){
                String leftSideNotHighlighted = Util.getSubstringForCursor(currentLine == row ? leftEnd : rightEnd,lines.get(row));
                int startingWidth = fontMetrics.stringWidth(leftSideNotHighlighted);

                String highlightedText = Util.getHighlightedText(leftEnd,new Location(lines.get(row).length() - 1,rightEnd.getCoordinateY()),lines.get(row));
                int highlightedTextWidth = fontMetrics.stringWidth(highlightedText);

                if(currentLine == row)
                    g2d.fillRect(startingWidth, currentLine * height, highlightedTextWidth, height);
                else g2d.fillRect(0, currentLine * height, startingWidth, height);

                currentLine++;
                continue;
            }

            g2d.fillRect(0, currentLine * height, fontMetrics.stringWidth(line), height);
            currentLine++;
        }

    }

    private void highlight(Graphics2D g2d,FontMetrics fontMetrics, Location leftEnd,Location rightEnd, int row){
        List<String> lines = textEditorModel.getLines();
        int height = fontMetrics.getHeight();

        String leftSideNotHighlighted = Util.getSubstringForCursor(leftEnd,lines.get(row));
        int startingWidth = fontMetrics.stringWidth(leftSideNotHighlighted);

        String highlightedText = Util.getHighlightedText(leftEnd,rightEnd,lines.get(row));
        int highlightedTextWidth = fontMetrics.stringWidth(highlightedText);

        g2d.fillRect(startingWidth, row * height, highlightedTextWidth, height);
    }



    private void initKeyListener(){
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == KeyEvent.VK_SHIFT && !shiftPressed)
                    shiftPressed = true;



                if(cursorActionList.contains(keyCode) || delete.contains(keyCode)){
                    switch (keyCode) {
                        case KeyEvent.VK_UP -> {
                            textEditorModel.moveCursorUp();
                            if(shiftPressed){
                                mark();
                            }
                        }
                        case KeyEvent.VK_DOWN ->  {
                            textEditorModel.moveCursorDown();
                            if(shiftPressed){
                                mark();
                            }
                        }
                        case KeyEvent.VK_LEFT -> {
                            textEditorModel.moveCursorLeft();
                            if(shiftPressed){
                                mark();
                            }
                        }
                        case KeyEvent.VK_RIGHT -> {
                            textEditorModel.moveCursorRight();
                            if(shiftPressed){
                                mark();
                            }
                        }
                        case KeyEvent.VK_DELETE -> {
                            EditAction deleteAction = new DeleteAfterAction(textEditorModel);
                            deleteAction.execute_do();
                            undoManager.push(deleteAction);
                        }
                        case KeyEvent.VK_BACK_SPACE ->{
                            EditAction deleteAction = new DeleteBeforeAction(textEditorModel);
                            deleteAction.execute_do();
                            undoManager.push(deleteAction);
                        }
                    }
                }

                char c = e.getKeyChar();
                if(Character.isSpaceChar(c) || Character.isDigit(c) || Character.isLetter(c) || c == 10){
                    EditAction insertAction = new InsertAction(textEditorModel,String.valueOf(c));
                    insertAction.execute_do();
                    undoManager.push(insertAction);
                }

                if((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0 && !shiftPressed){
                    boolean shouldDelete = keyCode == KeyEvent.VK_CONTROL || (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 || keyCode == KeyEvent.VK_DELETE ||  keyCode == KeyEvent.VK_BACK_SPACE;
                    if(!shouldDelete) textEditorModel.setSelectionRange(null);
                }

                if((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
                    switch (keyCode){
                        case KeyEvent.VK_C -> copy(false);
                        case KeyEvent.VK_X -> cut();
                        case KeyEvent.VK_V -> {
                            if(shiftPressed) pasteRemoveFromStack();
                            else paste();
                        }
                        case KeyEvent.VK_Z -> undoManager.undo();
                        case KeyEvent.VK_R -> undoManager.redo();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == KeyEvent.VK_SHIFT && shiftPressed) shiftPressed = false;
            }
        };

        addKeyListener(keyListener);
    }

    private void mark() {
        Location cursor = textEditorModel.getCursorLocation();
        Location prevCursor = textEditorModel.getPrevCursorLocation();

        if(prevCursor == null) return;

        LocationRange range = textEditorModel.getSelectionRange();

        if(range == null){
            range = new LocationRange(
                    new Location(prevCursor.getCoordinateX(), prevCursor.getCoordinateY()),
                    new Location(cursor.getCoordinateX(), cursor.getCoordinateY()));
        }else range.setEnd(new Location(cursor.getCoordinateX(), cursor.getCoordinateY()));

        textEditorModel.setSelectionRange(range);
    }
    public void setShow(boolean show) {
        this.show = show;
        SwingUtilities.invokeLater(this::repaint);
    }

    public boolean getShow() {
        return show;
    }

    public void paste(){
        if(!clipboardStack.hasText()) return;

        EditAction insertAction = new InsertAction(textEditorModel,clipboardStack.peekText());
        insertAction.execute_do();
        undoManager.push(insertAction);
    }

    public void pasteRemoveFromStack(){
        if(!clipboardStack.hasText()) return;

        EditAction insertAction = new InsertAction(textEditorModel,clipboardStack.popText());
        insertAction.execute_do();
        undoManager.push(insertAction);
    }

    public void copy(boolean delete){
        LocationRange range = textEditorModel.getSelectionRange();
        if(range == null) return;

        List<String> lines = textEditorModel.getLines();
        Location rightEnd = range.getStart().compareTo(range.getEnd()) > 0 ? range.getStart() : range.getEnd();
        Location leftEnd = range.getStart().compareTo(range.getEnd()) < 0 ? range.getStart() : range.getEnd();

        if(leftEnd.equals(rightEnd)) return;

        int currentLine = leftEnd.getCoordinateY();
        int firstRow = leftEnd.getCoordinateY();
        int lastRow = rightEnd.getCoordinateY();

        if(firstRow == lastRow){
            clipboardStack.pushText(Util.getHighlightedText(leftEnd,rightEnd,lines.get(firstRow)));
            if(delete) {
                EditAction deleteAction = new DeleteBeforeAction(textEditorModel);
                deleteAction.execute_do();
                undoManager.push(deleteAction);
            }
            return;
        }
        StringBuilder copy = new StringBuilder();
        for (Iterator<String> it = textEditorModel.linesRange(leftEnd.getCoordinateY(), rightEnd.getCoordinateY()); it.hasNext(); ) {
            String line = it.next();

            if(currentLine == firstRow){
                copy.append(Util.getHighlightedText(leftEnd, new Location(line.length() - 1, currentLine), line));
            }else if (currentLine == lastRow){
                copy.append(Util.getHighlightedText(new Location(-1, currentLine), rightEnd, line));
            }else copy.append(line);

            copy.append("\n");
            currentLine++;
        }

        if(delete){
            EditAction deleteAction = new DeleteBeforeAction(textEditorModel);
            deleteAction.execute_do();
            undoManager.push(deleteAction);
        }
        clipboardStack.pushText(copy.toString());
    }
    public void cut(){
        copy(true);
    }

    public ClipboardStack getClipboardStack(){
        return clipboardStack;
    }
    @Override
    public void updateCursorLocation(Location loc) {
        textEditorModel.setCursorLocation(loc);
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    public void updateText() {
        SwingUtilities.invokeLater(this::repaint);
    }
}
