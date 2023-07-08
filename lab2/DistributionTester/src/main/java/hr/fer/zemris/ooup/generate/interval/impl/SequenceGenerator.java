package hr.fer.zemris.ooup.generate.interval.impl;

import hr.fer.zemris.ooup.generate.interval.IntervalGenerator;

import java.util.ArrayList;
import java.util.List;

public class SequenceGenerator implements IntervalGenerator {


    private int lowerBound;
    private int upperBound;
    private int step;

    public SequenceGenerator(int lowerBound, int upperBound, int step) {
        if(lowerBound > upperBound) throw new IllegalArgumentException("Lower bound can't be bigger than upper bound");
        if(step <= 0) throw new IllegalArgumentException("Step must be a number greater than 0");

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.step = step;
    }

    @Override
    public List<Integer> generateRandom() {
        final List<Integer> intervalGenerated = new ArrayList<>();

        int start = lowerBound;

        while (start <= upperBound){
            intervalGenerated.add(start);
            start += step;
        }

        return intervalGenerated;
    }

    public void setLowerBound(int lowerBound) {
        if(lowerBound > upperBound)
            throw new IllegalArgumentException("Lower bound can't be bigger than upper bound");

        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        if(lowerBound > upperBound)
            throw new IllegalArgumentException("Lower bound can't be bigger than upper bound");

        this.upperBound = upperBound;
    }

    public void setStep(int step) {
        if(step <= 0)
            throw new IllegalArgumentException("Step must be a number greater than 0");

        this.step = step;
    }
}
