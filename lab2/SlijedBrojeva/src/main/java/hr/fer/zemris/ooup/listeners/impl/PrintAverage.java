package hr.fer.zemris.ooup.listeners.impl;

import hr.fer.zemris.ooup.SlijedBrojeva;
import hr.fer.zemris.ooup.listeners.NumberSequenceListener;

import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

public class PrintAverage implements NumberSequenceListener {

    private final SlijedBrojeva slijedBrojeva;

    public PrintAverage(SlijedBrojeva slijedBrojeva){
        Objects.requireNonNull(slijedBrojeva);

        this.slijedBrojeva = slijedBrojeva;
    }
    @Override
    public void update() {
        List<Integer> integers = slijedBrojeva.getIntegerList();

        OptionalDouble optionalDouble = integers.stream().mapToDouble(Integer::intValue).average();
        if(optionalDouble.isEmpty()) System.out.println("No elements in list");
        else System.out.println("Average: "+optionalDouble.getAsDouble());
    }
}
