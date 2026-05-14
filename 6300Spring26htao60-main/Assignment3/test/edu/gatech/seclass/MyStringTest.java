package edu.gatech.seclass;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Timeout.ThreadMode;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Junit test class created for use in Georgia Tech CS6300.
 * <p>
 * This class is provided to interpret your grades via junit tests
 * and as a reminder, should NOT be posted in any public repositories,
 * even after the class has ended.
 */

@Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
public class MyStringTest {

    private MyStringInterface myString;

    @BeforeEach
    public void setUp() {
        myString = new MyString();
    }

    @AfterEach
    public void tearDown() {
        myString = null;
    }

    @Test
    // Description: First count number example in the interface documentation
    public void testCountAlphabeticWords1() {
        myString.setString("My numbers are 11, 96, and thirteen");
        assertEquals(5, myString.countAlphabeticWords());
    }

    @Test
    // Description: Test with mixed alphanumeric and special characters
    public void testCountAlphabeticWords2() {
        myString.setString("My# numb!ers are, 11");
        int result = myString.countAlphabeticWords();

        assertEquals(4, result);
    }

    @Test
    // Description: Test with numbers and alphabetic words
    public void testCountAlphabeticWords3() {
        myString.setString("My numbers are 11");
        int result = myString.countAlphabeticWords();

        assertEquals(3, result);
    }

    @Test
    // Description: Test do not set string
    public void testCountAlphabeticWords4() {
        assertThrows(NullPointerException.class, () -> myString.countAlphabeticWords());
    }

    @Test
    // Description: Test set string with numbers and alphabetic
    public void testSetString1() {
        myString.setString("My numbers are 11");

        assertEquals("My numbers are 11", myString.getString());
    }

    @Test
    // Description: Sample encryption 1
    public void testEncrypt1() {
        myString.setString("Cat & 5 DogS");
        assertEquals("tdK & O ylHL", myString.encrypt(5, 3));
    }

    @Test
    // Description: encrypt without set the string
    public void testEncrypt2() {
        assertThrows(NullPointerException.class, () -> myString.encrypt(5, 3));
    }

    @Test
    // Description: test argument 1 is not prime
    public void testEncrypt3() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(2, 3));
    }

    @Test
    // Description: test argument 1 > 62
    public void testEncrypt4() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(63, 3));
    }

    @Test
    // Description: test argument 2 > 62
    public void testEncrypt5() {
        myString.setString("Cat & 5 DogS");
        assertThrows(IllegalArgumentException.class, () -> myString.encrypt(3, 63));
    }

    @Test
    // Description: Sample encryption 2
    public void testEncrypt6() {
        myString.setString("AAABBBCCC");
        assertEquals("JJJIIIHHH", myString.encrypt(61, 61));
    }

    @Test
    // Description: First convert digits example in the interface documentation
    public void testConvertDigitsToNamesInSubstring1() {
        myString.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        myString.convertDigitsToNamesInSubstring(17, 23);
        assertEquals("I'd b3tt3r put sZerome dOneSix1ts in this 5tr1n6, right?", myString.getString());
    }


    @Test
    // Description: test MyIndexOutOfBoundsException
    public void testConvertDigitsToNamesInSubstring2() {
        myString.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertThrows(MyIndexOutOfBoundsException.class, () -> myString.convertDigitsToNamesInSubstring(1, 100));
    }

    @Test
    // Description: test argument 1 > argument 2
    public void testConvertDigitsToNamesInSubstring3() {
        myString.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertThrows(IllegalArgumentException.class, () -> myString.convertDigitsToNamesInSubstring(5, 1));

    }

    @Test
    // Description: test convert without set the string
    public void testConvertDigitsToNamesInSubstring4() {
        assertThrows(NullPointerException.class, () -> myString.convertDigitsToNamesInSubstring(1, 100));

    }

    @Test
    // Description: Second convert digits example
    public void testConvertDigitsToNamesInSubstring5() {
        myString.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        myString.convertDigitsToNamesInSubstring(1, 45);
        assertEquals("I'd bThreettThreer put sZerome dOneSixOnets in this FivetrOnenSix, right?", myString.getString());

    }
}
