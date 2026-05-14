package edu.gatech.seclass;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnstableClassTestBC2 {

    @Test(expected = ArithmeticException.class)
    public void testXEqualsZero() {
        UnstableClass.unstableMethod2(0);
    }

    @Test
    public void testXPositive() {
        UnstableClass.unstableMethod2(1);
    }

    @Test
    public void testXNegative() {
        UnstableClass.unstableMethod2(-1);
    }
}