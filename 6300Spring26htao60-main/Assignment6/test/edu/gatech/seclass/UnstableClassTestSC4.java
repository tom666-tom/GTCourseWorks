package edu.gatech.seclass;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnstableClassTestSC4 {

    @Test
    public void testPathA_ANotEqualB_ConditionTrue() {
        assertEquals(1, UnstableClass.unstableMethod4(true, false, 0, 1, -1));
    }

    @Test
    public void testPathB_ANotEqualB_ConditionFalse() {
        assertEquals(2, UnstableClass.unstableMethod4(true, false, 1, 1, 1));
    }

    @Test
    public void testPathC_AEqualB() {
        assertEquals(3, UnstableClass.unstableMethod4(true, true, 0, 0, 0));
    }
}