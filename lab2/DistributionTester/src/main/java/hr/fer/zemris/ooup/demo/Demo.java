package hr.fer.zemris.ooup.demo;

import hr.fer.zemris.ooup.DistributionTester;
import hr.fer.zemris.ooup.calculate.percentile.PercentileCalculate;
import hr.fer.zemris.ooup.calculate.percentile.impl.LinearInterpolation;
import hr.fer.zemris.ooup.calculate.percentile.impl.NearestRank;
import hr.fer.zemris.ooup.generate.interval.IntervalGenerator;
import hr.fer.zemris.ooup.generate.interval.impl.FibonacciGenerator;
import hr.fer.zemris.ooup.generate.interval.impl.SequenceGenerator;

public class Demo {
    public static void main(String[] args) {
        PercentileCalculate nearestRank = new NearestRank();
        PercentileCalculate linear = new LinearInterpolation();

        IntervalGenerator fibonacciGenerator = new FibonacciGenerator(10);
        IntervalGenerator sequenceGenerator = new SequenceGenerator(10,100,2);

        DistributionTester tester1 = new DistributionTester(fibonacciGenerator,nearestRank);
        DistributionTester tester2 = new DistributionTester(sequenceGenerator,linear);


        tester1.generateInterval();
        tester2.generateInterval();

        System.out.println(tester1.calculatePercentile(80));
        System.out.println(tester2.calculatePercentile(30));
    }
}