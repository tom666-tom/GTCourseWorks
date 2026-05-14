package edu.gatech.seclass.transmutetxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
            "Usage: transmutetxt [ -g | -f style substring | -r old new | -n | -t num | -x ] FILE"
                    + System.lineSeparator();

    private final String hello = "hello" + System.lineSeparator();

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

    // File existence and format errors
    @Test
    public void testFileDoesNotExist() {
        String[] args = { "nonexistent.txt" };
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    @Test
    void testMissingFileParameter() {
        String[] args = {};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    // Option parsing errors

    @Test
    public void testUnrecognizedOption() {
        Path inputFile = createFile(hello);
        String[] args = {"-a", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testTruncateMissingNum() {
        Path inputFile = createFile(hello);
        String[] args = { "-t", inputFile.toString() };
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testTruncateInvalidNum() {
        Path inputFile = createFile(hello);
        String[] args = {"-t", "abc", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testTruncateNegativeNum() {
        Path inputFile = createFile(hello);
        String[] args = {"-t", "-1", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testTruncateNumGreaterThan100() {
        Path inputFile = createFile(hello);
        String[] args = {"-t", "101", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testReplaceMissingOldNew() {
        Path inputFile = createFile(hello);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testReplaceEmptyOld() {
        Path inputFile = createFile(hello);
        String[] args = {"-r", "", "new", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testFormatMissingStyleSubstring() {
        Path inputFile = createFile(hello);
        String[] args = {"-f", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testFormatInvalidStyle() {
        Path inputFile = createFile(hello);
        String[] args = {"-f", "underline", "sub", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testFormatEmptySubstring() {
        Path inputFile = createFile(hello);
        String[] args = {"-f", "bold", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testGlobalWithoutFormat() {
        Path inputFile = createFile(hello);
        String[] args = {"-g", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    @Test
    public void testReplaceAndFormatMutuallyExclusive() {
        Path inputFile = createFile(hello);
        String[] args = {"-r", "old", "new", "-f", "bold", "hello", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(hello, getFileContent(inputFile));
    }

    // Basic functionality

    @Test
    public void testEmptyFile() {
        String input = "";
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testNoOptions() {
        String input = "line1" + System.lineSeparator() + "line2" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(input, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testTruncateToZero() {
        String input = "hello" + System.lineSeparator() + "world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "0", inputFile.toString()};
        Main.main(args);

        String expected = System.lineSeparator() + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testTruncateTo5() {
        String input = "hello world" + System.lineSeparator() + "this is a test" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "5", inputFile.toString()};
        Main.main(args);

        String expected = "hello" + System.lineSeparator() + "this " + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testRemoveEmptyLines() {
        String input = "Line 1" + System.lineSeparator() + System.lineSeparator() + "Line 2" + System.lineSeparator()
                + System.lineSeparator() + System.lineSeparator() + "Line 3" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-x", inputFile.toString()};
        Main.main(args);

        String expected = "Line 1" + System.lineSeparator() + "Line 2" + System.lineSeparator() +
                "Line 3" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testReplaceFirstOccurrence() {
        String input = "foo bar foo" + System.lineSeparator() + "foo baz" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "foo", "qux", inputFile.toString()};
        Main.main(args);

        String expected = "qux bar foo" + System.lineSeparator() + "qux baz" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testReplaceWithEmptyNew() {
        String input = "foo bar" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "foo", "", inputFile.toString()};
        Main.main(args);

        String expected = " bar" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatBoldFirstOccurrence() {
        String input = "bold text is bold" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "bold", inputFile.toString()};
        Main.main(args);

        String expected = "**bold** text is bold" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatItalicFirstOccurrence() {
        String input = "italic word" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "italic", "italic", inputFile.toString()};
        Main.main(args);

        String expected = "*italic* word" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatCodeFirstOccurrence() {
        String input = "code snippet" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "code", "code", inputFile.toString()};
        Main.main(args);

        String expected = "`code` snippet" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatGlobalReplace() {
        String input = "foo foo foo" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "bold", "foo", inputFile.toString()};
        Main.main(args);

        String expected = "**foo** **foo** **foo**" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testAddLineNumbers() {
        String input = "first" + System.lineSeparator() + "second" + System.lineSeparator() + "third" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", inputFile.toString()};
        Main.main(args);

        String expected = "1 first" + System.lineSeparator() + "2 second" + System.lineSeparator() + "3 third" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Order of operations

    @Test
    public void testLastTruncateOptionWins() {
        String input = "long line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "5", "-t", "3", inputFile.toString()};
        Main.main(args);

        String expected = "lon" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testLastReplaceOptionWins() {
        String input = "hello world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "hello", "hi", "-r", "world", "earth", inputFile.toString()};
        Main.main(args);

        String expected = "hello earth" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testLastFormatStyleWins() {
        String input = "text" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "text", "-f", "italic", "text", inputFile.toString()};
        Main.main(args);

        String expected = "*text*" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Combined options

    @Test
    public void testTruncateAndRemoveEmpty() {
        String input = "hello" + System.lineSeparator() + System.lineSeparator() + "world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "3", "-x", inputFile.toString()};
        Main.main(args);
        
        String expected = "hel" + System.lineSeparator() + "wor" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testLineNumbersThenTruncate() {
        String input = "very long line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "-t", "5", inputFile.toString()};
        Main.main(args);
        
        // "1 very long line" -> truncate to 5 -> "1 ver"
        String expected = "1 ver" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testReplaceThenLineNumbersThenTruncate() {
        String input = "a b c" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "b", "B", "-n", "-t", "4", inputFile.toString()};
        Main.main(args);
        
        // replace -> "a B c"; add numbers -> "1 a B c"; truncate to 4 -> "1 a "
        String expected = "1 a " + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatGlobalThenLineNumbersThenRemoveEmpty() {
        String input = "test" + System.lineSeparator() + "test" + System.lineSeparator() + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "code", "test", "-n", "-x", inputFile.toString()};
        Main.main(args);

        String expected = "1 `test`" + System.lineSeparator() + "2 `test`" + System.lineSeparator() + "3 " + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testAllOptions() {
        String input = "line one" + System.lineSeparator() + "line two" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "7", "-x", "-n", "-f", "bold", "line", "-g", inputFile.toString()};
        Main.main(args);
        
        // format global -> "**line** one\n**line** two\n"; add numbers -> "1 **line** one\n2 **line** two\n";
        // truncate to 7 -> "1 **lin", "2 **lin"; remove empty none
        String expected = "1 **lin" + System.lineSeparator() + "2 **lin" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Edge cases
    @Test
    public void testReplaceCaseSensitive() {
        String input = "Hello hello" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "Hello", "Hi", inputFile.toString()};
        Main.main(args);

        String expected = "Hi hello" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testFormatCaseSensitive() {
        String input = "Test test" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "test", inputFile.toString()};
        Main.main(args);

        String expected = "Test **test**" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testSpecialCharactersInReplace() {
        String input = "a*b?c" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "*b?", "@", inputFile.toString()};
        Main.main(args);

        String expected = "a@c" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testMultipleLinesWithMixedEmptyAndNumbering() {
        String input = System.lineSeparator() + System.lineSeparator() + "hello" + System.lineSeparator() + System.lineSeparator() + "world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "-x", inputFile.toString()};
        Main.main(args);

        String expected = "1 " + System.lineSeparator()+ "2 " + System.lineSeparator() + "3 hello" + System.lineSeparator()
                + "4 " + System.lineSeparator() + "5 world" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void testTruncateWithExactlyBoundary() {
        String input = "12345" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "5", inputFile.toString()};
        Main.main(args);

        String expected = "12345" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void truncateThenLineNumbers() {
        String input = "abc def gh" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "3", "-n", inputFile.toString()};
        Main.main(args);

        // First truncate: "abc", then add line number: "1 abc"
        String expected = "1 a" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void lineNumbersThenTruncate() {
        String input = "abc def gh" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "-t", "3", inputFile.toString()};
        Main.main(args);

        // Add numbers: "1 abcdefghij", then truncate to 3: "1 a"
        String expected = "1 a" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void replaceAndLineNumbers() {
        String input = "hello world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "world", "earth", "-n", inputFile.toString()};
        Main.main(args);

        // Replace: "hello earth", then add numbers: "1 hello earth"
        String expected = "1 hello earth" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void formatAndLineNumbers() {
        String input = "test line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "test", "-n", inputFile.toString()};
        Main.main(args);

        // Format: "**test** line", then add numbers: "1 **test** line"
        String expected = "1 **test** line" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
    
    @Test
    public void replaceAndRemoveEmpty() {
        String input = "foo" + System.lineSeparator() + System.lineSeparator() + "bar" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "foo", "baz", "-x", inputFile.toString()};
        Main.main(args);

        // Replace: "baz\n\nbar\n", then remove empty lines -> "baz\nbar\n"
        String expected = "baz" + System.lineSeparator() + "bar" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void formatAndRemoveEmpty() {
        String input = "code" + System.lineSeparator() + System.lineSeparator() + "test" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "code", "code", "-x", inputFile.toString()};
        Main.main(args);

        // Format: "`code`\n\n`test`\n"? Wait substring "code" only appears in first line. Actually second line is empty, third line "test" unchanged.
        // After format: "`code`\n\ntest\n", then remove empty -> "`code`\ntest\n"
        String expected = "`code`" + System.lineSeparator() + "test" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void globalFormatLineNumbersTruncate() {
        String input = "hello hello world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "italic", "hello", "-n", "-t", "10", inputFile.toString()};
        Main.main(args);

        // Global format: "*hello* *hello* world"
        // Add numbers: "1 *hello* *hello* world"
        // Truncate to 10: "1 *hello* "
        String expected = "1 *hello* " + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void repeatedRemoveEmpty() {
        String input = "a" + System.lineSeparator() + System.lineSeparator() + "b" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-x", "-x", inputFile.toString()};
        Main.main(args);

        String expected = "a" + System.lineSeparator() + "b" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    
    @Test
    public void repeatedLineNumbers() {
        String input = "x" + System.lineSeparator() + "y" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "-n", inputFile.toString()};
        Main.main(args);

        String expected = "1 x" + System.lineSeparator() + "2 y" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void veryLongLineWithNumbers() {
        String longLine = "a".repeat(100);
        String input = longLine + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "-t", "10", inputFile.toString()};
        Main.main(args);

        // Add numbers: "1 " + 200 'a's -> length 202, truncate to 10 -> "1 aaaaaaaa"? Actually "1 " (2 chars) + 8 'a's = 10
        String expected = "1 " + "aaaaaaaa" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void replaceWithSpaces() {
        String input = "hello world" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "world", "beautiful earth", inputFile.toString()};
        Main.main(args);

        String expected = "hello beautiful earth" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void formatNoGlobalMultipleOccurrences() {
        String input = "foo foo foo" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "foo", inputFile.toString()};
        Main.main(args);

        String expected = "**foo** foo foo" + System.lineSeparator();
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
}
