package hr.fer.zemris.ooup.texteditor.model;

import hr.fer.zemris.ooup.texteditor.observer.CursorObserver;
import hr.fer.zemris.ooup.texteditor.observer.TextObserver;
import hr.fer.zemris.ooup.texteditor.util.Util;

import javax.swing.*;
import java.util.*;

public class TextEditorModel implements Iterable<String>{
    private final JFrame frame;
    private List<String> lines;
    private LocationRange selectionRange;
    private Location cursorLocation;
    private Location prevCursorLocation;
    private final List<CursorObserver> cursorObserverList;
    private final List<TextObserver> textObserverList;

    public TextEditorModel(String text, JFrame frame) {
        this.frame = frame;
        this.lines = new LinkedList<>();
        this.cursorObserverList = new LinkedList<>();
        this.textObserverList = new LinkedList<>();
        this.selectionRange = null;
        this.cursorLocation = new Location();

        setData(text);
    }

    private void setData(String text) {
        String[] splitText = text.split("\n");
        if(splitText.length == 0)return;

        lines.addAll(Arrays.asList(splitText));

        int row = splitText.length-1;
        int column = splitText[row].length();
        if(column == 0) column = -1;
        else column--;

        setCursorLocation(new Location(column,row));
    }

    public List<String> getLines() {
        return lines;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Location getCursorLocation() {
        return cursorLocation;
    }

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public void setSelectionRange(LocationRange selectionRange) {
        notifyAllTextListeners();
        notifyAllCursorListeners(cursorLocation);
        this.selectionRange = selectionRange;
    }

    public void setCursorLocation(Location cursorLocation) {
        prevCursorLocation = this.cursorLocation;
        this.cursorLocation = cursorLocation;
    }

    public Location getPrevCursorLocation() {
        return prevCursorLocation;
    }

    public void addCursorListener(CursorObserver observer) {
        cursorObserverList.add(observer);
    }

    public void removeCursorListener(CursorObserver observer) {
        cursorObserverList.remove(observer);
    }

    public void addTextListener(TextObserver observer) {
        textObserverList.add(observer);
    }

    public void removeTextObserver(TextObserver observer) {
        textObserverList.remove(observer);
    }


    public void notifyAllCursorListeners(Location loc) {
        cursorObserverList.forEach(observer -> observer.updateCursorLocation(loc));
    }

    public void notifyAllTextListeners() {
        textObserverList.forEach(TextObserver::updateText);
    }

    @Override
    public Iterator<String> iterator() {
        return new TextIterator(this);
    }

    public Iterator<String> allLines() {
        return iterator();
    }

    public Iterator<String> linesRange(int index1, int index2) {
        if (index1 < 0 || index2 < 0)
            throw new IllegalArgumentException("Invalid index.");
        if (index2 >= lines.size())
            throw new IllegalArgumentException("Invalid second index. Can't be greater than number of rows");

        return new TextIterator(this, index1, index2);
    }

    public void moveCursorUp() {
        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();
        if(row == 0) return;

        int nextColumn = lines.get(row-1).length() - 1;
        if(column > nextColumn) column = nextColumn;

        Location newLocation = new Location(column,row-1);
        notifyAllCursorListeners(newLocation);
    }
    public void moveCursorDown() {
        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();
        if(row == lines.size() - 1) return;

        int nextColumn = lines.get(row+1).length() - 1;
        if(column > nextColumn) column = nextColumn;

        Location newLocation = new Location(column,row+1);
        notifyAllCursorListeners(newLocation);
    }
    public void moveCursorLeft() {
        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();

        if(column == -1 && row == 0) return;

        if(column == -1){
            column = lines.get(--row).length() - 1;
        }else column--;

        Location newLocation = new Location(column,row);
        notifyAllCursorListeners(newLocation);
    }
    public void moveCursorRight() {
        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();
        if(column == lines.get(row).length() - 1 && row == lines.size() - 1) return;

        if(column == lines.get(row).length() - 1){
            column = -1;
            row++;
        }else column++;

        Location newLocation = new Location(column,row);
        notifyAllCursorListeners(newLocation);
    }

    public void deleteAfter(){
        if(selectionRange != null){
            deleteRange();
            this.selectionRange = null;
            notifyAllTextListeners();
            return;
        }

        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();
        String line = lines.get(row);

        if(column == line.length() - 1 && row == lines.size() - 1) return;

        if(column == line.length() - 1 && line.isBlank()){
            column = -1;
            lines.remove(row);
        }else if(column == line.length() - 1 && !line.isBlank()){
            String newLine = line + lines.get(row+1);
            lines.remove(row+1);
            lines.remove(row);
            lines.add(row,newLine);
        }else if(column == -1){
            String newString = Util.removeOneCharacter(line,0);
            lines.remove(line);
            lines.add(row,newString);
        }else{
            String newString = Util.removeOneCharacter(line,column+1);
            lines.remove(line);
            lines.add(row,newString);
        }

        notifyAllCursorListeners(new Location(column,row));
        notifyAllTextListeners();
    }

    public void deleteBefore(){
        if(selectionRange != null){
            deleteRange();
            notifyAllTextListeners();
            return;
        }

        int row = cursorLocation.getCoordinateY();
        int column = cursorLocation.getCoordinateX();
        String line = lines.get(row);

        if(column == -1 && row == 0) return;

        if(column == -1 && line.isBlank()){
            lines.remove(row);
            column = lines.get(--row).length() - 1;

        }else if(column == -1 && !line.isBlank()){
            String newLine = lines.get(row - 1) + line;
            column = lines.get(row - 1).length() - 1;
            lines.remove(--row);
            lines.remove(row);
            lines.add(row,newLine);
        }
        else if(column == line.length() - 1){
            column--;
            String newString = Util.getSubstringForCursor(new Location(column,row),line);
            lines.remove(line);
            lines.add(row,newString);
        }else {
            String newString = Util.removeOneCharacter(line, column);
            column--;
            lines.remove(line);
            lines.add(row,newString);
        }

        notifyAllCursorListeners(new Location(column,row));
        notifyAllTextListeners();
    }

    public void deleteRange(){
        Location rightEnd = selectionRange.getStart().compareTo(selectionRange.getEnd()) > 0 ? selectionRange.getStart() : selectionRange.getEnd();
        Location leftEnd = selectionRange.getStart().compareTo(selectionRange.getEnd()) < 0 ? selectionRange.getStart() : selectionRange.getEnd();

        if(leftEnd.equals(rightEnd)) return;

        if(leftEnd.getCoordinateY() == rightEnd.getCoordinateY()){
            String line = lines.get(leftEnd.getCoordinateY());

            String left = Util.getSubstringForCursor(leftEnd,line);
            String right = line.substring(rightEnd.getCoordinateX()+1);

            lines.remove(leftEnd.getCoordinateY());
            lines.add(leftEnd.getCoordinateY(),left+right);

            this.selectionRange = null;
            notifyAllCursorListeners(leftEnd);
            return;
        }

        List<String> newLines = new LinkedList<>();

        for (int i = 0; i < lines.size(); i++ ) {
            String line = lines.get(i);

            if(i == leftEnd.getCoordinateY()){
                newLines.add(Util.getSubstringForCursor(leftEnd,line));
            }else if(i == rightEnd.getCoordinateY()){
                newLines.add(line.substring(rightEnd.getCoordinateX()+1));
            }else if (i < leftEnd.getCoordinateY() || i > rightEnd.getCoordinateY()){
                newLines.add(line);
            }
        }

        this.lines = newLines;
        this.selectionRange = null;
        notifyAllCursorListeners(leftEnd);
    }

    public void insert(char c){
        if(selectionRange != null){
            deleteRange();
            selectionRange = null;
        }

        if(lines.size() == 0){
            lines.add(String.valueOf(c));
            notifyAllCursorListeners(new Location(0,0));
            notifyAllTextListeners();
            return;
        }

        int column = cursorLocation.getCoordinateX();
        int row = cursorLocation.getCoordinateY();
        String line = lines.get(row);

        String left = Util.getSubstringForCursor(cursorLocation,line);
        String right = line.substring(column+1);
        lines.remove(row);
        if(c == 10){

            lines.add(row,left);
            if(row == lines.size() - 1) lines.add(right);
            else lines.add(row+1,right);

            notifyAllCursorListeners(new Location(-1,row+1));
        }else {
            lines.add(row,left+c+right);
            notifyAllCursorListeners(new Location(column+1,row));
        }
        notifyAllTextListeners();
    }


    public void insert(String text){
        for(char c : text.toCharArray()) insert(c);
    }

    public void setLines(List<String> lines) {
        notifyAllTextListeners();
        this.lines = lines;
    }


    private static class TextIterator implements Iterator<String> {
        private final TextEditorModel textEditorModel;
        private final int to;
        private int current;

        public TextIterator(TextEditorModel textEditorModel) {
            this(textEditorModel, 0, textEditorModel.lines.size() - 1);
        }

        public TextIterator(TextEditorModel textEditorModel, int from, int to) {
            this.textEditorModel = textEditorModel;
            this.to = to;
            this.current = from;
        }

        @Override
        public boolean hasNext() {
            return current <= to;
        }

        @Override
        public String next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return textEditorModel.lines.get(current++);
        }
    }
}
