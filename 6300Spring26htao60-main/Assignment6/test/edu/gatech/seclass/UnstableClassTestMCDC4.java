package edu.gatech.seclass;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnstableClassTestMCDC4 {
    // 1. (T, T, F) -> inner decision true
    @Test
    public void testInner_TTF() {
        assertEquals(1, UnstableClass.unstableMethod4(true, false, 0, 1, 1));
    }

    // 2. (F, T, F) -> inner decision false
    @Test
    public void testInner_FTF() {
        assertEquals(2, UnstableClass.unstableMethod4(true, false, 1, 1, 1));
    }

    // 3. (T, F, F) -> inner decision false
    @Test
    public void testInner_TFF() {
        assertEquals(2, UnstableClass.unstableMethod4(true, false, 0, -1, 1));
    }

    // 4. (F, F, F) -> inner decision false
    @Test
    public void testInner_FFF() {
        assertEquals(2, UnstableClass.unstableMethod4(true, false, 1, -1, 1));
    }

    // 5. (F, F, T) -> inner decision true
    @Test
    public void testInner_FFT() {
        assertEquals(1, UnstableClass.unstableMethod4(true, false, 1, -1, -1));
    }

    // 6. Outer false branch (a == b)
    @Test
    public void testOuterFalse() {
        assertEquals(3, UnstableClass.unstableMethod4(true, true, 0, 1, 1));
    }
}