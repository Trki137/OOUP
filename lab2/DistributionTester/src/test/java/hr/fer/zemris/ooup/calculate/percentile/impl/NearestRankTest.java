package hr.fer.zemris.ooup.calculate.percentile.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class NearestRankTest {
    @Test
    public void nearestRankTest(){
        List<Integer> integers = Arrays.asList(1,10,50);
        NearestRank linearInterpolation = new NearestRank();
        int result = linearInterpolation.calculate(80,integers);

        assertEquals(50,result);
    }
}

