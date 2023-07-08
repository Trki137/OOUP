package hr.fer.zemris.ooup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell {
    private String exp;
    private Integer value;
    private final String name;
    private final List<CellListener> listeners;

    public Cell(String name) {
        this.name = name;
        this.listeners = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setExp(String exp) {
        String pattern = "-?\\d+(\\\\.\\\\d+)?";
        if(exp.matches(pattern))
            setValue(Integer.valueOf(exp));

        this.exp = exp;
    }


    public void setValue(Integer value) {
        Objects.requireNonNull(value);

        this.value = value;
        notifyListeners();
    }

    public String getExp() {
        return exp;
    }

    public Integer getValue() {
        return value;
    }

    public void addListener(CellListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(CellListener listener){
        this.listeners.remove(listener);
    }

    public void notifyListeners(){
        this.listeners.forEach(CellListener::updateCell);
    }
}
