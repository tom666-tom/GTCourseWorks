package edu.gatech.seclass;

public class MyString implements MyStringInterface {

    private String currentString;

    private static final String[] DIGIT = {
            "Zero", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine"
    };

    private static final String CIPHER_ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    public String getString() {
        return currentString;
    }

    private boolean containsLetterOrDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setString(String string) {
        if (string == null || string.isEmpty() || string.equals(easterEgg)) {
            throw new IllegalArgumentException();
        }

        if (!containsLetterOrDigit(string)){
            throw new IllegalArgumentException();
        }

        currentString = string;
    }

    @Override
    public int countAlphabeticWords() {
        if (currentString == null) {
            throw new NullPointerException();
        }

        int count = 0;
        boolean isWord = false;

        for (char c : currentString.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!isWord) {
                    count++;
                    isWord = true;
                }
            } else {
                isWord = false;
            }
        }

        return count;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    @Override
    public String encrypt(int arg1, int arg2) {
        if (currentString == null) {
            throw new NullPointerException();
        }

        if (arg1 <= 0 || arg1 >= 62 || gcd(arg1, 62) != 1 || arg2 < 1 || arg2 >= 62) {
            throw new IllegalArgumentException();
        }

        if (currentString.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (!containsLetterOrDigit(currentString)){
            throw new IllegalArgumentException();
        }

        StringBuilder encrypted = new StringBuilder();

        for (char c : currentString.toCharArray()) {
            int index = CIPHER_ALPHABET.indexOf(c);

            if (index >= 0) {
                int encryptedIndex = (arg1 * index + arg2) % 62;
                encrypted.append(CIPHER_ALPHABET.charAt(encryptedIndex));
            } else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();
    }

    @Override
    public void convertDigitsToNamesInSubstring(int firstPosition, int finalPosition) {
        if (currentString == null) {
            throw new NullPointerException();
        }

        if (firstPosition < 1 || firstPosition > finalPosition) {
            throw new IllegalArgumentException();
        }

        if (finalPosition > currentString.length()) {
            throw new MyIndexOutOfBoundsException("Out of bounds");
        }

        int startIdx = firstPosition - 1;
        int endIdx = finalPosition - 1;

        StringBuilder result = new StringBuilder();

        if (startIdx > 0) {
            result.append(currentString, 0, startIdx);
        }

        for (int i = startIdx; i <= endIdx; i++) {
            char c = currentString.charAt(i);
            if (Character.isDigit(c)) {
                int digit = c - '0';
                result.append(DIGIT[digit]);
            } else {
                result.append(c);
            }
        }

        if (endIdx < currentString.length() - 1) {
            result.append(currentString.substring(endIdx + 1));
        }

        currentString = result.toString();
    }
}
