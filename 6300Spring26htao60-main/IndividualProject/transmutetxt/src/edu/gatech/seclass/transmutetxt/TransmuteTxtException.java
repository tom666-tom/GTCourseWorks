package edu.gatech.seclass.transmutetxt;

/** Signals that an error occurred when running TransmuteTxt. */
public class TransmuteTxtException extends Exception {
    /**
     * Constructs a TransmuteTxtException with the specified message describing the error.
     *
     * @param s the error message
     */
    TransmuteTxtException(String s) {
        super(s);
    }
}
