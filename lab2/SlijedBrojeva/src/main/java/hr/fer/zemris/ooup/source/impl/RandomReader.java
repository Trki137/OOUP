package hr.fer.zemris.ooup.source.impl;

import hr.fer.zemris.ooup.source.NumberSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomReader implements NumberSource {

    private final int size;
    private int currentIndex = 0;
    private final List<Integer> integers;

    public RandomReader(int size) {
        this.size = size;
        this.integers = generateRandom();
    }

    private List<Integer> generateRandom() {
        final List<Integer> integers = new ArrayList<>();
        final Random random = new Random();
        for(int i = 0; i < size; i++){
            integers.add(random.nextInt(100));
        }

        return integers;
    }

    @Override
    public Integer getNext() {
        if(currentIndex >= size) return -1;

        return integers.get(currentIndex++);
    }
}
