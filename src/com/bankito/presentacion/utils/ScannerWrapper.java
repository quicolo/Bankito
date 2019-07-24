package com.bankito.presentacion.utils;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Wrapper class for java.util.Scanner that provides a simpler interface for user input of
 * primitives from System.in
 * @author Daniel Shevtsov (SID: 200351253)
 */
public class ScannerWrapper {
    private Scanner scanner;
    private String questionMsg = "Entrada: ";
    private String errorMsg = "Error en la entrada de datos";
    private String outOfBoundsMsg = "Valor fuera del rango permitido";
    private String pauseMsg = "Pulsa ENTER para continuar...";
    

    /**
     * Constructor. Creates a new ScannerWrapper object
     */
    public ScannerWrapper() {
        scanner = new Scanner(System.in, "ISO-8859-1");
    }
    
    public ScannerWrapper setQuestionText(String question) {
        questionMsg = Objects.requireNonNull(question, "El texto no puede ser nulo");
        return this;
    }

    public ScannerWrapper setErrorText(String error) {
        errorMsg = Objects.requireNonNull(error, "El texto no puede ser nulo");
        return this;
    }
    
    public ScannerWrapper setOutOfBoundsText(String error) {
        outOfBoundsMsg = Objects.requireNonNull(error, "El texto no puede ser nulo");
        return this;
    }
    
    public ScannerWrapper setPauseText(String pause) {
        pauseMsg = Objects.requireNonNull(pause, "El texto del mensaje de pausa no puede ser nulo");
        return this;
    }
    
    /**
     * Get an int of input from a Scanner object
     * @return int input from Scanner
     */
    public int getInt() {
        int userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextInt();
                correctInput = true;
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }

    /**
     * Get an int of input from a Scanner object
     * @param lLimit int Lower limit (user input >= lower limit)
     * @param uLimit int Upper limit (user input <= upper limit)
     * @return int input from Scanner
     */
    public int getInt(int lLimit, int uLimit) {
        int userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextInt();
                if(userInput >= lLimit && userInput <= uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" ("+lLimit+","+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get an int of input from a Scanner object
     * @param lLimit int Lower limit (user input > lower limit)
     * @return int input from Scanner
     */
    public int getIntGreaterThan(int lLimit) {
        int userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextInt();
                if(userInput > lLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor>"+lLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get an int of input from a Scanner object
     * @param uLimit int Upper limit (user input < upper limit)
     * @return int input from Scanner
     */
    public int getIntLessThan(int uLimit) {
        int userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextInt();
                if(userInput < uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor<"+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
        /**
     * Get an long of input from a Scanner object
     * @return long input from Scanner
     */
    public long getLong() {
        long userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLong();
                correctInput = true;
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }

    /**
     * Get an long of input from a Scanner object
     * @param lLimit long Lower limit (user input >= lower limit)
     * @param uLimit long Upper limit (user input <= upper limit)
     * @return long input from Scanner
     */
    public long getLong(long lLimit, long uLimit) {
        long userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLong();
                if(userInput >= lLimit && userInput <= uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" ("+lLimit+","+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get an long of input from a Scanner object
     * @param lLimit long Lower limit (user input > lower limit)
     * @return long input from Scanner
     */
    public long getLongGreaterThan(long lLimit) {
        long userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLong();
                if(userInput > lLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor>"+lLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get an long of input from a Scanner object
     * @param uLimit long Upper limit (user input < upper limit)
     * @return long input from Scanner
     */
    public long getLongLessThan(long uLimit) {
        long userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLong();
                if(userInput < uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor<"+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    
    
    
    /**
     * Get a double of input from a Scanner object
     * @return double input from Scanner
     */
    public double getDouble() {
        double userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextDouble();
                correctInput = true;
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }

    /**
     * Get a double of input from a Scanner object
     * @param lLimit double Lower limit (user input >= lower limit)
     * @param uLimit double Upper limit (user input >= upper limit)
     * @return double input from Scanner
     */
    public double getDouble(double lLimit, double uLimit) {
        double userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextDouble();
                if(userInput >= lLimit && userInput <= uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" ("+lLimit+","+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }

    /**
     * Get an double of input from a Scanner object
     * @param lLimit long Lower limit (user input > lower limit)
     * @return long input from Scanner
     */
    public double getDoubleGreaterThan(double lLimit) {
        double userInput = 0.0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextDouble();
                if(userInput > lLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor>"+lLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get an double of input from a Scanner object
     * @param uLimit long Upper limit (user input < upper limit)
     * @return long input from Scanner
     */
    public double getDoubleLessThan(double uLimit) {
        double userInput = 0;
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextDouble();
                if(userInput < uLimit)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (valor<"+uLimit+")");
                scanner.nextLine();
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get a char of input from a Scanner object
     * @return first char from single line of user input from Scanner
     */
    public char getChar() {
        char userInput = '\0';
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.next().charAt(0);
                correctInput = true;
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }

    /**
     * Get a String line of input from a Scanner object
     * @return String from single line of user input from Scanner
     */
    public String getString() {
        String userInput = "";
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLine();
                correctInput = true;
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get a String line of input from a Scanner object
     * @param minLength min length limit (user input length >= min length)
     * @param maxLength max length limit (user input length >= max length)
     * @return String from single line of user input from Scanner
     */
    public String getString(int minLength, int maxLength) {
        String userInput = "";
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLine();
                if (userInput.length() >= minLength && userInput.length() <= maxLength)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" ("+minLength+" <= número caracteres <= "+maxLength+")");
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get a String line of input from a Scanner object
     * @param minLength min length limit (user input length > min length)
     * @return String from single line of user input from Scanner
     */
    public String getStringLengthGreaterThan(int minLength) {
        String userInput = "";
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLine();
                if (userInput.length() > minLength)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (número caracteres >"+minLength+")");
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    /**
     * Get a String line of input from a Scanner object
     * @param maxLength max length limit (user input length < max length)
     * @return String from single line of user input from Scanner
     */
    public String getStringLengthLessThan(int maxLength) {
        String userInput = "";
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLine();
                if (userInput.length() < maxLength)
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" (número caracteres <"+maxLength+")");
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    public String getStringOfStringArgs(String... validValues) {
        String userInput = "";
        boolean correctInput = false;

        do {
            System.out.print(questionMsg);
            try {
                userInput = scanner.nextLine();
                if (isValidInput(userInput, validValues))
                    correctInput = true;
                else
                    System.out.println(outOfBoundsMsg+" ("+getStringOfValidValues(validValues)+")");
            } catch(InputMismatchException e) {
                System.out.println(errorMsg);
                scanner.nextLine();
            }
        } while(!correctInput);

        return userInput;
    }
    
    private boolean isValidInput(String userInput, String... validValues) {
        
        for (String validValue : validValues) {
            if (userInput.equals(validValue)) {
                return true;
            }
        }
        return false;
    }
    
    private String getStringOfValidValues(String... validValues) {
        StringBuilder sb = new StringBuilder();
        
        for (int i=0; i<validValues.length; i++) {
            sb.append(validValues[i]);
            if(i+1<validValues.length)
                sb.append(", ");
        }
        
        return sb.toString();
    }
    
    public void pause() {
        System.out.println("");
        System.out.println(pauseMsg);
        scanner.nextLine();
    }
}