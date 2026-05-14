package edu.gatech.seclass;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnstableClassTestSC1 {
    @Test(expected = ArithmeticException.class)
    public void testWithXEqualsZero() {
        UnstableClass.unstableMethod1(0);
    }
}