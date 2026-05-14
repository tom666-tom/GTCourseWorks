package edu.gatech.seclass;

import org.junit.Test;

public class UnstableClassTestBC1 {
    @Test
    public void testWithXGreaterThanZero() {
        UnstableClass.unstableMethod1(1);
    }

    @Test
    public void testWithXLessThanZero() {
        UnstableClass.unstableMethod1(-1);
    }
}