package hr.fer.zemris.ooup.calculate.percentile;

import java.util.List;

public interface PercentileCalculate {
    Integer calculate(int percentile, List<Integer> interval);
}
