package hr.fer.zemris.ooup.texteditor.util;

import hr.fer.zemris.ooup.texteditor.model.Location;

public class Util {
    public static String getSubstringForCursor(Location location, String line){
        int column = location.getCoordinateX();
        if(column == -1) return "";
        if(column == line.length()) return line;

        return line.substring(0,column+1);
    }

    public static String removeOneCharacter(String line, int column) {
        if(column == 0) return line.substring(1);

        String left = line.substring(0,column);
        String right = line.substring(column+1);

        return left + right;
    }

    public static String getHighlightedText(Location leftEnd, Location rightEnd, String line) {
        int start = leftEnd.getCoordinateX();
        int end = rightEnd.getCoordinateX() + 1;

        if(start != -1) start++;
        if(start == -1) start = 0;

        return line.substring(start, end);
    }
}
