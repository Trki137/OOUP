package hr.fer.zemris.ooup.texteditor.model;

public class LocationRange {
    private Location start;
    private Location end;

    public LocationRange(){
        this(new Location(),new Location());
    }


    public LocationRange(Location start, Location end){
        this.start = start;
        this.end = end;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format("From: %s, to %s", start,end);
    }
}
