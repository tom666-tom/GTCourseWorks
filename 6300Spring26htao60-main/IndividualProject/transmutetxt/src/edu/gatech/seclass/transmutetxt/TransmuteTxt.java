package edu.gatech.seclass.transmutetxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransmuteTxt implements TransmuteTxtInterface {
    private String filepath;
    private boolean formatGlobal;
    private Style formatStyle;
    private String formatSubstring;
    private String replaceOld;
    private String replaceNew;
    private boolean addLineNumber;
    private Integer truncateMaxLength;
    private boolean removeEmptyLines;

    // Internal flags to know which operations are active
    private boolean formatSet;
    private boolean replaceSet;

    public TransmuteTxt() {
        reset();
    }

    @Override
    public void reset() {
        filepath = null;
        formatGlobal = false;
        formatStyle = null;
        formatSubstring = null;
        replaceOld = null;
        replaceNew = null;
        addLineNumber = false;
        truncateMaxLength = null;
        removeEmptyLines = false;
        formatSet = false;
        replaceSet = false;
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void setFormatGlobal(boolean formatGlobal) {
        this.formatGlobal = formatGlobal;
    }

    @Override
    public void setFormatText(Style style, String strToFormat) {
        this.formatStyle = style;
        this.formatSubstring = strToFormat;
        this.formatSet = true;
        // Setting format does not automatically clear replace; validation will catch conflicts later
    }

    @Override
    public void setReplaceText(String oldString, String newString) {
        this.replaceOld = oldString;
        this.replaceNew = newString;
        this.replaceSet = true;
    }

    @Override
    public void setAddLineNumber(boolean addLineNumber) {
        this.addLineNumber = addLineNumber;
    }

    @Override
    public void setTruncateLine(Integer maxLength) {
        this.truncateMaxLength = maxLength;
    }

    @Override
    public void setRemoveEmptyLines(boolean removeEmptyLines) {
        this.removeEmptyLines = removeEmptyLines;
    }

    @Override
    public void transmutetxt() throws TransmuteTxtException {
        if (filepath == null) {
            throw new TransmuteTxtException("File path not set");
        }

        // Read file with the same validation as Main.readFileLines
        List<String> lines = readFileLines(filepath);
        if (lines == null) {
            throw new TransmuteTxtException("Error reading file");
        }

        // Validate option consistency (mirroring Main.parseArguments)
        if (replaceSet && formatSet) {
            throw new TransmuteTxtException("Cannot use both replace and format");
        }
        if (formatGlobal && !formatSet) {
            throw new TransmuteTxtException("Global flag only applicable with format");
        }
        if (truncateMaxLength != null && (truncateMaxLength < 0 || truncateMaxLength > 100)) {
            throw new TransmuteTxtException("Truncate length must be between 0 and 100");
        }
        if (replaceSet && (replaceOld == null || replaceOld.isEmpty())) {
            throw new TransmuteTxtException("Replace old string cannot be empty");
        }
        if (formatSet) {
            if (formatSubstring == null || formatSubstring.isEmpty()) {
                throw new TransmuteTxtException("Format substring cannot be null or empty");
            }
            if (formatStyle == null) {
                throw new TransmuteTxtException("Format style must be set");
            }
        }

        // Apply transformations in the required order
        List<String> result = applyTransformations(lines);

        // Output exactly as Main does
        if (!result.isEmpty()) {
            System.out.print(String.join(System.lineSeparator(), result));
            System.out.print(System.lineSeparator());
        }
    }

    /**
     * Reads the file exactly as Main.readFileLines does.
     * Returns null for any error (missing file, unreadable, missing trailing newline when non‑empty).
     */
    private List<String> readFileLines(String fileName) {
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            return null;
        }

        String fileContent;
        try {
            fileContent = Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }

        String lineSeparator = System.lineSeparator();
        // Non‑empty file must end with newline
        if (!fileContent.isEmpty() && !fileContent.endsWith(lineSeparator)) {
            return null;
        }

        String[] lineList = fileContent.split(lineSeparator, -1);
        if (fileContent.endsWith(lineSeparator)) {
            lineList = Arrays.copyOf(lineList, lineList.length - 1);
        }

        return List.of(lineList);
    }

    private List<String> applyTransformations(List<String> lines) {
        List<String> result = new ArrayList<>();
        int lineNumber = 1;

        for (String line : lines) {
            // Step 1: Replace or format
            if (replaceSet) {
                int idx = line.indexOf(replaceOld);
                if (idx != -1) {
                    line = line.substring(0, idx) + replaceNew + line.substring(idx + replaceOld.length());
                }
            } else if (formatSet) {
                String formatted;
                switch (formatStyle) {
                    case bold:
                        formatted = "**" + formatSubstring + "**";
                        break;
                    case italic:
                        formatted = "*" + formatSubstring + "*";
                        break;
                    case code:
                        formatted = "`" + formatSubstring + "`";
                        break;
                    default:
                        formatted = formatSubstring; // should never happen
                }
                if (formatGlobal) {
                    line = line.replace(formatSubstring, formatted);
                } else {
                    int idx = line.indexOf(formatSubstring);
                    if (idx != -1) {
                        line = line.substring(0, idx) + formatted + line.substring(idx + formatSubstring.length());
                    }
                }
            }

            // Step 2: Add line numbers
            if (addLineNumber) {
                line = lineNumber + " " + line;
                lineNumber++;
            }

            // Step 3: Truncate
            if (truncateMaxLength != null && line.length() > truncateMaxLength) {
                line = line.substring(0, truncateMaxLength);
            }

            result.add(line);
        }

        // Step 4: Remove empty lines
        if (removeEmptyLines) {
            result.removeIf(String::isEmpty);
        }

        return result;
    }
}
