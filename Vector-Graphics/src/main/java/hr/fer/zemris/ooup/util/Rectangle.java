package hr.fer.zemris.ooup.util;

public class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height) {
       this.x = x;
       this.y = y;
       this.width = width;
       this.height = height;
    };

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle union(Rectangle objectBB) {
        java.awt.Rectangle bb = new java.awt.Rectangle(this.x,this.y,this.width,this.height);
        java.awt.Rectangle objectBBRectangle = new java.awt.Rectangle(objectBB.x,objectBB.y,objectBB.width,objectBB.height);
        java.awt.Rectangle unionResult = bb.union(objectBBRectangle);

        return new Rectangle(unionResult.x, unionResult.y, unionResult.width, unionResult.height);
    }
}
