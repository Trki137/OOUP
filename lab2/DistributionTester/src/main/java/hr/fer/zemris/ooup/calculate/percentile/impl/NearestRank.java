package hr.fer.zemris.ooup.calculate.percentile.impl;

import hr.fer.zemris.ooup.calculate.percentile.PercentileCalculate;

import java.util.List;

public class NearestRank implements PercentileCalculate {

    @Override
    public Integer calculate(int percentile, List<Integer> interval) {
        interval.sort(Integer::compareTo);

        double n_p = (percentile * interval.size() * 1.0)/ 100 + 0.5;
        int index = (int) Math.ceil(n_p) - 1;

        return interval.get(index);
    }
}