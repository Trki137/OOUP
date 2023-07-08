package hr.fer.zemris.ooup.generate.interval.impl;

import hr.fer.zemris.ooup.generate.interval.IntervalGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator implements IntervalGenerator {

    private double mean;

    private double standardDeviation;

    private int numOfElements;

    public RandomGenerator(double mean, double standardDeviation, int numOfElements) {
        if(numOfElements <= 0)
            throw new IllegalArgumentException("Number of elements must be greater than 0.");

        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.numOfElements = numOfElements;
    }



    @Override
    public List<Integer> generateRandom() {
        final Random random = new Random();
        final List<Integer> integerGenerated = new ArrayList<>(numOfElements);

        int generated = 0;

        while (generated < numOfElements){
            double newNumber = random.nextGaussian(mean, standardDeviation);
            integerGenerated.add((int) newNumber);

            generated++;
        }

        return integerGenerated;
    }


    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public void setNumOfElements(int numOfElements) {
        if(numOfElements <= 0)
            throw new IllegalArgumentException("Number of elements must be greater than 0.");


        this.numOfElements = numOfElements;
    }
}
