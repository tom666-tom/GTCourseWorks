package edu.gatech.seclass.transmutetxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MainTest {
    private final String usageStr =
            "Usage: transmutetxt [ -g | -f style substring | -r old new | -n | -t num | -x ] FILE"
                    + System.lineSeparator();

    @TempDir Path tempDirectory;

    @RegisterExtension OutputCapture capture = new OutputCapture();

    /* ----------------------------- Test Utilities ----------------------------- */

    /**
     * Returns path of a new "input.txt" file with specified contents written into it. The file will
     * be created using {@link TempDir TempDir}, so it is automatically deleted after test
     * execution.
     *
     * @param contents the text to include in the file
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents) {
        return createFile(contents, "input.txt");
    }

    /**
     * Returns path to newly created file with specified contents written into it. The file will be
     * created using {@link TempDir TempDir}, so it is automatically deleted after test execution.
     *
     * @param contents the text to include in the file
     * @param fileName the desired name for the file to be created
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents, String fileName) {
        Path file = tempDirectory.resolve(fileName);
        try {
            Files.writeString(file, contents);
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Takes the path to some file and returns the contents within.
     *
     * @param file the path to some file
     * @return the contents of the file as a String, or null if there was an issue reading the file
     */
    private String getFileContent(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ------------------------------- Test Cases ------------------------------- */

    @Test
    public void exampleTest1() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest2() {
        String input = System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-n", "-t", "50", inputFile.toString()};
        Main.main(args);

        String expected = "1 " + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest3() {
        String input = "Hello, world!" + System.lineSeparator()
                + System.lineSeparator()
                + "How are you?" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-t", "-10", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest4() {
        String input = "Okay, let’s start counting. " + System.lineSeparator()
                + "One and... " + System.lineSeparator()
                + "Two and... " + System.lineSeparator()
                + "Three and... " + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-t", "5", inputFile.toString()};
        Main.main(args);

        String expected = "Okay," + System.lineSeparator()
                + "One a" + System.lineSeparator()
                + "Two a" + System.lineSeparator()
                + "Three" + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest5() {
        String input = "Line 1" + System.lineSeparator()
                + "Line 2" + System.lineSeparator()
                + System.lineSeparator()
                + System.lineSeparator()
                + "Line k" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-x", inputFile.toString()};
        Main.main(args);

        String expected = "Line 1" + System.lineSeparator()
                + "Line 2" + System.lineSeparator()
                + "Line k" + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest6() {
        String input = "This is a normal text file." + System.lineSeparator()
                + "Perhaps too normal." + System.lineSeparator()
                + "Or not Normal at all." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-r", "normal", "special", inputFile.toString()};
        Main.main(args);

        String expected = "This is a special text file." + System.lineSeparator()
                + "Perhaps too special." + System.lineSeparator()
                + "Or not Normal at all." + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest7() {
        String input = "This is a normal text file." + System.lineSeparator()
                + "Perhaps too normal." + System.lineSeparator()
                + "Or not Normal at all." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-f", "bold", "normal", inputFile.toString()};
        Main.main(args);

        String expected = "This is a **normal** text file." + System.lineSeparator()
                + "Perhaps too **normal**." + System.lineSeparator()
                + "Or not Normal at all." + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest8() {
        String input = "Hello World. Hello Java." + System.lineSeparator()
                + System.lineSeparator()
                + "hello World. Hello Java." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-g", "-f", "italic", "Hello", inputFile.toString()};
        Main.main(args);

        String expected = "*Hello* World. *Hello* Java." + System.lineSeparator()
                + System.lineSeparator()
                + "hello World. *Hello* Java." + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest9() {
        String input = "Line 1." + System.lineSeparator()
                + "Line 2." + System.lineSeparator()
                + "Line 3." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-n", inputFile.toString()};
        Main.main(args);

        String expected = "1 Line 1." + System.lineSeparator()
                + "2 Line 2." + System.lineSeparator()
                + "3 Line 3." + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest10() {
        String input = "Line 1." + System.lineSeparator()
                + System.lineSeparator()
                + "Line 2." + System.lineSeparator()
                + System.lineSeparator()
                + "Line 3." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-x", "-n", inputFile.toString()};
        Main.main(args);

        String expected = "1 Line 1." + System.lineSeparator()
                + "2 " + System.lineSeparator()
                + "3 Line 2." + System.lineSeparator()
                + "4 " + System.lineSeparator()
                + "5 Line 3." + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest11() {
        String input = "Hello World. Hello Java." + System.lineSeparator()
                + System.lineSeparator()
                + "hello World. Hello Java." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = { "-t", "10", "-x", "-f", "code", "Hello", "-g", "-n", inputFile.toString()};
        Main.main(args);

        String expected = "1 `Hello` " + System.lineSeparator()
                + "2 " + System.lineSeparator()
                + "3 hello Wo" + System.lineSeparator();

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
}
