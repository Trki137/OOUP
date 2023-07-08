package hr.fer.zemris.ooup.renderer.impl;

import hr.fer.zemris.ooup.renderer.Renderer;
import hr.fer.zemris.ooup.util.Point;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SVGRendererImpl implements Renderer {
    private final List<String> lines = new ArrayList<>();
    private final String fileName;

    public SVGRendererImpl(String fileName){
        this.fileName = fileName;
        lines.add("""
                <svg  xmlns="http://www.w3.org/2000/svg"
                      xmlns:xlink="http://www.w3.org/1999/xlink">
                """);
    }

    public void close() throws IOException{
        Path filePath = Path.of(fileName);

        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        lines.add("</svg>");

        for(String line: lines){
            Files.writeString(filePath, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }

    }

    @Override
    public void drawLine(Point s, Point e) {
        int x1 = s.getX();
        int x2 = e.getX();
        int y1 = s.getY();
        int y2 = e.getY();

        String hexColor = "#0000ff";

        String line = String.format("\n<line x1=\"%d\"  y1=\"%d\" x2=\"%d\"   y2=\"%d\" style=\"stroke:%s;\"/>\n",x1,y1,x2,y2,hexColor);
        lines.add(line);
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder sb = new StringBuilder("\n<polygon points=\"");
        for(int i = 0; i < points.length; i++){
            sb.append(points[i]);
            if (i != points.length - 1) sb.append(" ");
        }

        String fillHex = "#0000ff";
        String strokeHex = "#ff0000";
        sb.append(String.format("\" style=\"stroke:%s; fill:%s;\"/>\n", strokeHex,fillHex));

        lines.add(sb.toString());
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       lines.forEach(sb::append);
       return sb.toString();
    }
}
