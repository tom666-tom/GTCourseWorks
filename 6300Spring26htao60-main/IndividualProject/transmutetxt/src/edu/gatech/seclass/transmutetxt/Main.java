package edu.gatech.seclass.transmutetxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private record Options(
            List<String> fileContent,
            boolean truncate,
            Integer truncateNum,
            boolean removeEmpty,
            boolean replace,
            String replaceOld,
            String replaceNew,
            boolean format,
            String formatStyle,
            String formatSubstring,
            boolean global,
            boolean addLineNumber
    ) {}

    /**
     * Reads the file content and returns a list of lines.
     * Returns null if the file does not exist, cannot be read,
     * or does not end with a newline when non‑empty.
     */
    private static List<String> readFileLines(String fileName) {
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

        // Split into lines (preserve empty lines)
        String[] lineList = fileContent.split(lineSeparator, -1);
        if (fileContent.endsWith(System.lineSeparator())) {
            lineList = Arrays.copyOf(lineList, lineList.length - 1);
        }

        return List.of(lineList);
    }

    private static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean isValidStyle(String style) {
        return style.equals("bold") || style.equals("italic") || style.equals("code");
    }

    /**
     * Parses command line arguments and returns an Options record.
     * Returns null if any argument is invalid or missing.
     */
    private static Options parseArguments(String[] args) {
        if (args.length == 0) return null;

        boolean truncate = false;
        Integer truncateNum = null;
        boolean removeEmpty = false;
        boolean replace = false;
        String replaceOld = null;
        String replaceNew = null;
        boolean format = false;
        String formatStyle = null;
        String formatSubstring = null;
        boolean global = false;
        boolean addLineNumber = false;

        // Last argument is the file name
        String fileName = args[args.length - 1];
        if (fileName.startsWith("-")) return null;

        List<String> fileContent = readFileLines(fileName);
        if (fileContent==null) return null;

        // Parse options before the file name
        int i = 0;
        while (i < args.length - 1) {
            String token = args[i];
            switch (token) {
                case "-t":
                    if (i + 1 >= args.length - 1) return null; // no truncate number
                    int num = parseInt(args[i + 1]);
                    if (num < 0 || num > 100) return null;
                    truncateNum = num;
                    truncate = true;
                    i += 2;
                    break;

                case "-x":
                    removeEmpty = true;
                    i++;
                    break;

                case "-r":
                    if (i + 2 >= args.length - 1) return null; // no replace string
                    replaceOld = args[i + 1];
                    replaceNew = args[i + 2];
                    if (replaceOld.isEmpty()) return null;
                    replace = true;
                    i += 3;
                    break;

                case "-f":
                    if (i + 2 >= args.length - 1) return null; // no format string
                    formatStyle = args[i + 1];
                    formatSubstring = args[i + 2];
                    if (formatSubstring.isEmpty()) return null;
                    if (!isValidStyle(formatStyle)) return null;
                    format = true;
                    i += 3;
                    break;

                case "-g":
                    global = true;
                    i++;
                    break;

                case "-n":
                    addLineNumber = true;
                    i++;
                    break;

                default:
                    return null; // unrecognized option
            }
        }

        // Validate mutual exclusivity
        if (replace && format) return null;
        if (global && !format) return null;

        return new Options(fileContent, truncate, truncateNum, removeEmpty,
                replace, replaceOld, replaceNew, format, formatStyle,
                formatSubstring, global, addLineNumber);
    }

    /**
     * Applies the transformations to the given list of lines according to the options.
     * The order of operations is:
     *   1. Replace or format
     *   2. Add line numbers
     *   3. Truncate
     *   4. Remove empty lines
     */
    private static List<String> applyTransformations(Options opts) {
        List<String> result = new ArrayList<>();
        int lineNumber = 1;

        for (String line : opts.fileContent) {
            // Step 1: Replace or format
            if (opts.replace) {
                int idx = line.indexOf(opts.replaceOld);
                if (idx != -1) {
                    line = line.substring(0, idx) + opts.replaceNew + line.substring(idx + opts.replaceOld.length());
                }
            } else if (opts.format) {
                String formatted;
                switch (opts.formatStyle) {
                    case "bold":
                        formatted = "**" + opts.formatSubstring + "**";
                        break;
                    case "italic":
                        formatted = "*" + opts.formatSubstring + "*";
                        break;
                    case "code":
                        formatted = "`" + opts.formatSubstring + "`";
                        break;
                    default:
                        formatted = opts.formatSubstring; // unreachable
                }
                if (opts.global) {
                    line = line.replace(opts.formatSubstring, formatted);
                } else {
                    int idx = line.indexOf(opts.formatSubstring);
                    if (idx != -1) {
                        line = line.substring(0, idx) + formatted + line.substring(idx + opts.formatSubstring.length());
                    }
                }
            }

            // Step 2: Add line numbers
            if (opts.addLineNumber) {
                line = lineNumber + " " + line;
                lineNumber++;
            }

            // Step 3: Truncate
            if (opts.truncate && line.length() > opts.truncateNum) {
                line = line.substring(0, opts.truncateNum);
            }

            result.add(line);
        }

        // Step 4: Remove empty lines
        if (opts.removeEmpty) {
            result.removeIf(String::isEmpty);
        }

        return result;
    }

    public static void main(String[] args) {
        Options options = parseArguments(args);
        if (options == null) {
            usage();
            return;
        }

        List<String> transformedLines = applyTransformations(options);
        if (!transformedLines.isEmpty()) {
            System.out.print(String.join(System.lineSeparator(), transformedLines));
            System.out.print(System.lineSeparator());
        }
    }

    private static void usage() {
        System.err.println(
                "Usage: transmutetxt [ -g | -f style substring | -r old new | -n | -t num | -x ] FILE");
    }
}
