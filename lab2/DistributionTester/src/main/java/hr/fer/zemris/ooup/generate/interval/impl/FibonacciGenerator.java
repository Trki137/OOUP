package hr.fer.zemris.ooup.generate.interval.impl;

import hr.fer.zemris.ooup.generate.interval.IntervalGenerator;

import java.util.ArrayList;
import java.util.List;

public class FibonacciGenerator implements IntervalGenerator {

    private int numOfElements;

    public FibonacciGenerator(int numOfElements){
        if(numOfElements < 2) throw new IllegalArgumentException("Number of elements must be greater or equal than 2");

        this.numOfElements = numOfElements;
    }

    @Override
    public List<Integer> generateRandom() {
        final List<Integer> fibonacci = new ArrayList<>(numOfElements);

        fibonacci.add(0);
        fibonacci.add(1);

        int lastListIndex = 1;

        while (lastListIndex < numOfElements - 1){
            int newNumber = fibonacci.get(lastListIndex) + fibonacci.get(lastListIndex - 1);
            fibonacci.add(newNumber);
            lastListIndex++;
        }

        return fibonacci;
    }

    public void setNumOfElements(int numOfElements) {
        if(numOfElements < 2)
            throw new IllegalArgumentException("Number of elements must be greater or equal than 2");

        this.numOfElements = numOfElements;
    }
}
