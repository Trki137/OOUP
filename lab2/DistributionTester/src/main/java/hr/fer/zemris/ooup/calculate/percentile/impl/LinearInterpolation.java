package hr.fer.zemris.ooup.calculate.percentile.impl;

import hr.fer.zemris.ooup.calculate.percentile.PercentileCalculate;

import java.util.Arrays;
import java.util.List;

public class LinearInterpolation implements PercentileCalculate {

    @Override
    public Integer calculate(int percentile, List<Integer> interval) {
        interval.sort(Integer::compareTo);

        int N = interval.size();

        for(int i = 0;  i < N; i++){
            int v_i = interval.get(i);

            double p_v_i = 100*(i+0.5)/N;
            double p_v_i_next = 100*(i+1.5)/N;

            if(i == 0 && percentile <= p_v_i) return interval.get(i);
            if(i == N - 1 && percentile >= p_v_i ) return interval.get(i);

            if(percentile >= p_v_i && percentile <= p_v_i_next){
                int v_i_next = interval.get(i+1);
                return (int) (v_i + N * ((percentile - p_v_i)*(v_i_next - v_i))/100);
            }
        }

        return 0;
    }
}
