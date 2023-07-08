package hr.fer.zemris.ooup.texteditor.model;

import java.util.Objects;

public class Location implements Comparable<Location>{
    private int coordinateX;
    private int coordinateY;

    public Location(){
        this(0,0);
    }

    public Location(int coordinateX,int coordinateY){

        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    @Override
    public String toString() {
        return String.format("(%d , %d)", coordinateX,coordinateY);
    }

    @Override
    public int compareTo(Location other) {
        int result = this.coordinateY - other.coordinateY;
        if(result != 0) return result;

        return this.coordinateX - other.coordinateX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return coordinateX == location.coordinateX && coordinateY == location.coordinateY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinateX, coordinateY);
    }
}
