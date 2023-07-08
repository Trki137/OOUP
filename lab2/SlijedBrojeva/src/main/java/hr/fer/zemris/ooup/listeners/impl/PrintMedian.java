package hr.fer.zemris.ooup.listeners.impl;

import hr.fer.zemris.ooup.SlijedBrojeva;
import hr.fer.zemris.ooup.listeners.NumberSequenceListener;

import java.util.List;
import java.util.Objects;

public class PrintMedian implements NumberSequenceListener {
    private final SlijedBrojeva slijedBrojeva;

    public PrintMedian(SlijedBrojeva slijedBrojeva){
        Objects.requireNonNull(slijedBrojeva);

        this.slijedBrojeva = slijedBrojeva;
    }
    @Override
    public void update() {
        List<Integer> integers = slijedBrojeva.getIntegerList();
        integers.sort(Integer::compareTo);

        int size = integers.size();
        if(size == 0) {
            System.out.println("List is empty");
            return;
        }

        if(size % 2 == 0){
            double result = (integers.get(size/2) + integers.get(size/2 - 1))/2.0;
            System.out.println("Median: "+ result);
        }else System.out.println("Median: "+integers.get(size/2)*1.0);

    }
}
