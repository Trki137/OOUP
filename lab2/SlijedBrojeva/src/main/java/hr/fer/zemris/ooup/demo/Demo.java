package hr.fer.zemris.ooup.demo;

import hr.fer.zemris.ooup.SlijedBrojeva;
import hr.fer.zemris.ooup.listeners.NumberSequenceListener;
import hr.fer.zemris.ooup.listeners.impl.FileInput;
import hr.fer.zemris.ooup.listeners.impl.PrintAverage;
import hr.fer.zemris.ooup.listeners.impl.PrintMedian;
import hr.fer.zemris.ooup.listeners.impl.PrintSize;
import hr.fer.zemris.ooup.source.NumberSource;
import hr.fer.zemris.ooup.source.impl.RandomReader;

import java.util.Arrays;

public class Demo {
    public static void main(String[] args) {
        NumberSource randomGenerated = new RandomReader(5);

        SlijedBrojeva slijedBrojeva = new SlijedBrojeva(randomGenerated);

        NumberSequenceListener[] listeners = new NumberSequenceListener[]{
                new FileInput(slijedBrojeva),
                new PrintAverage(slijedBrojeva),
                new PrintMedian(slijedBrojeva),
                new PrintSize(slijedBrojeva)
        };

        Arrays.stream(listeners).forEach(slijedBrojeva::addListener);

        slijedBrojeva.kreni();
    }
}
