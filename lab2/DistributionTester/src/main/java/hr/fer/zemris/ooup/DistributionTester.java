package hr.fer.zemris.ooup;

import hr.fer.zemris.ooup.calculate.percentile.PercentileCalculate;
import hr.fer.zemris.ooup.generate.interval.IntervalGenerator;

import java.util.List;
import java.util.Objects;

public class DistributionTester {

    private final IntervalGenerator intervalGenerator;

    private final PercentileCalculate percentileCalculate;

    private List<Integer> generatedInterval;

    public DistributionTester(IntervalGenerator intervalGenerator, PercentileCalculate percentileCalculate) {
        this.intervalGenerator = intervalGenerator;
        this.percentileCalculate = percentileCalculate;
    }

    public void generateInterval(){
        generatedInterval = intervalGenerator.generateRandom();
    }

    public Integer calculatePercentile(Integer percentile){
        Objects.requireNonNull(percentile);

        if(percentile > 100 || percentile < 0){
            System.out.println("Invalid percentile. Value must be from range [0,100]");
            return null;
        }

        if(Objects.isNull(generatedInterval)){
            System.out.println("You need first to generate a interval");
            return null;
        }

        return percentileCalculate.calculate(percentile, generatedInterval);
    }
}
