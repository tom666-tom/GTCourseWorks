package edu.gatech.seclass;

/**
 * This is a Georgia Tech provided code example for use in assigned
 * private GT repositories. Students and other users of this template
 * code are advised not to share it with other students or to make it
 * available on publicly viewable websites including repositories such
 * as GitHub and GitLab.  Such sharing may be investigated as a GT
 * honor code violation. Created for CS6300 Spring 2026.
 *
 * Template provided for the White-Box Testing Assignment. Follow the
 * assignment directions to either implement or provide comments for
 * the appropriate methods.
 */

public class UnstableClass {

    public static void exampleMethod1(int a) {
        // ...
        int x = a / 0; // Example of instruction that makes the method
                       // fail with an ArithmeticException
        // ...
    }

    public static void exampleMethod2() {
        // NOT POSSIBLE: This method cannot be implemented because
        // <REPLACE WITH REASON> (this is the example format for a
        // method that is not possible)
    }

    public static void unstableMethod1(int x) {
        if (x >= 0) {
            int temp = 100;
        }
        if (x <= 0) {
            int result = 100 / x;
        }
    }

    public static void unstableMethod2(int x) {
        if (x > 0) {
            int temp = 100;
        }
        if (x == 0) {
            int result = 100 / x;
        }
        if (x < 0) {
            int temp = 100;
        }
    }

    /**
     * NOT POSSIBLE: This method cannot be implemented
     *
     * Because 100% path coverage implies 100% statement coverage, any test suite
     * satisfying (2) would also satisfy 100% statement coverage without revealing
     * the fault, contradicting (1).
     */
    public static void unstableMethod3(int x, int y) {
    }

    public static int unstableMethod4(boolean a, boolean b, int c, int d, int e) { 
        int result = 0; 
        if (a != b) { 
            if ((c == 0) && (d > 0) || (e < 0)) { 
                result = 1; 
            } else { 
                result = 2; 
            } 
        } else { 
            result = 3; 
        } 
        return result; 
}  


    public static String[] unstableMethod5() {
        String a[] = new String[7];
        /*_
        public static boolean unstableMethod5(boolean a, boolean b) { 
            int x = 1;  
            int y = 1;  

            if(a)  
                x -=1;   
            else 
                y +=1;  
            if(b)  
                x -=1; 
            else   
                y +=1; 
            return (y/x < 0); 
        } 
        */
        //
        // Replace the "?" in column "output" with "T", "F", or "E":
        //
        //         | a | b |output|
        //         ================
        a[0] =  /* | T | T | <T, F, or E> (e.g., "T") */ "T";
        a[1] =  /* | T | F | <T, F, or E> (e.g., "T") */ "E";
        a[2] =  /* | F | T | <T, F, or E> (e.g., "T") */ "E";
        a[3] =  /* | F | F | <T, F, or E> (e.g., "T") */ "F";
        // ================
        //
        // Replace the "?" in the following sentences with "NEVER",
        // "SOMETIMES" or "ALWAYS":
        //
        a[4] = /* Test suites with 100% statement coverage */ "ALWAYS";
        /*reveal the fault in this method.*/
        a[5] = /* Test suites with 100% branch coverage */ "ALWAYS";
        /*reveal the fault in this method.*/
        a[6] =  /* Test suites with 100% path coverage */ "SOMETIMES";
        /*reveal the fault in this method.*/
        // ================
        return a;
    }
}
