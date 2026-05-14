package edu.gatech.seclass.transmutetxt;

/**
 * Interface created for use in Georgia Tech CS6300.
 *
 * <p>IMPORTANT: This interface should NOT be altered in any way.
 */
public interface TransmuteTxtInterface {

    enum Style {
        bold,
        italic,
        code
    }

    /** Reset the TransmuteTxt object to its initial state, for reuse. */
    void reset();

    /**
     * Sets the path of the input file. This method has to be called before invoking the {@link
     * #transmutetxt()} methods.
     *
     * @param filepath The file path to be set.
     */
    void setFilepath(String filepath);

    /**
     * Set to replace all occurrences of the formatted substring in each line. Used with -f
     * flag only. This method has to be called before invoking the {@link #transmutetxt()} method.
     *
     * @param formatGlobal Flag to toggle functionality
     */
    void setFormatGlobal(boolean formatGlobal);

    /**
     * Set to format the text based on the style parameter. This method has to be called before
     * invoking the {@link #transmutetxt()} method.
     *
     * @param style The style to be applied
     * @param strToFormat The string to be formatted
     */
    void setFormatText(Style style, String strToFormat);

    /**
     * Set to replace the first instance of string old in each line with string new. The search is
     * case-sensitive. This method has to be called before invoking the {@link #transmutetxt()}
     * method.
     *
     * @param oldString The string to be replaced
     * @param newString The new string replacing oldString
     */
    void setReplaceText(String oldString, String newString);

    /**
     * Set to add line numbers to each line, unpadded starting from 1. This method has to be called
     * before invoking the {@link #transmutetxt()} method.
     *
     * @param addLineNumber Flag to toggle functionality
     */
    void setAddLineNumber(boolean addLineNumber);

    /**
     * Set to truncate each line to the given length. This method has to be called before invoking
     * the {@link #transmutetxt()} method.
     *
     * @param maxLength The length to truncate the line to
     */
    void setTruncateLine(Integer maxLength);

    /**
     * Set to remove all empty lines from the input file. This method has to be called before
     * invoking the {@link #transmutetxt()} method.
     *
     * @param removeEmptyLines Flag to toggle functionality
     */
    void setRemoveEmptyLines(boolean removeEmptyLines);

    /**
     * Outputs a System.lineSeparator() delimited string that contains selected parts of the lines
     * in the file specified using {@link #setFilepath} and according to the current configuration,
     * which is set through calls to the other methods in the interface.
     *
     * <p>It throws a {@link TransmuteTxtException} if an error condition occurs (e.g., when the
     * specified file does not exist).
     *
     * @throws TransmuteTxtException thrown if an error condition occurs
     */
    void transmutetxt() throws TransmuteTxtException;
}
