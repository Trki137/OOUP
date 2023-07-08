package hr.fer.zemris.ooup;

import hr.fer.zemris.ooup.listeners.NumberSequenceListener;
import hr.fer.zemris.ooup.source.NumberSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SlijedBrojeva {
    private final List<Integer> integerList;
    private final List<NumberSequenceListener> listeners;
    private final NumberSource source;
    public SlijedBrojeva(NumberSource source){
        Objects.requireNonNull(source);

        this.listeners = new ArrayList<>();
        this.integerList = new ArrayList<>();
        this.source = source;
    }

    public void kreni(){

        while(true){
            int nextNumber = source.getNext();

            if(nextNumber < 0) return;

            integerList.add(nextNumber);
            notifyListeners();
            waitSecond();
        }
    }

    private void waitSecond() {
        while(true){
            try{
                Thread.sleep(1000);
                return;
            }catch (InterruptedException ignored){}
        }
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void notifyListeners(){
        listeners.forEach(NumberSequenceListener::update);
    }

    public void addListener(NumberSequenceListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(NumberSequenceListener listener){
        this.listeners.remove(listener);
    }


}
