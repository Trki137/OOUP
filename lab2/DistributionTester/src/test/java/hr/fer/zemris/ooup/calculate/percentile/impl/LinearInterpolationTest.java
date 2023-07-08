package hr.fer.zemris.ooup.calculate.percentile.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class LinearInterpolationTest {
    @Test
    public void linearInterpolationTest(){
        List<Integer> integers = Arrays.asList(1,10,50);
        LinearInterpolation linearInterpolation = new LinearInterpolation();
        int result = linearInterpolation.calculate(80,integers);

        assertEquals(46,result);

        result = linearInterpolation.calculate(90,integers);

        assertEquals(50,result);
    }
}
