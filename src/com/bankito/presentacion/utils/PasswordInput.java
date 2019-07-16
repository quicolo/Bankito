/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.utils;

import java.io.Console;
import java.util.Objects;

/**
 *
 * @author Kike
 */
public class PasswordInput {
    
    private ScannerWrapper scanner=null;
    private Console console=null;
    private String questionMsg = "Introduzca la password: ";
    private String lowQualityMsg = "La contraseña es demasiado sencilla... hazla más compleja";
    private String outOfBoundsMsg = "La contraseña debe contener entre ";
    private int minQualityAccepted = 0;
    private int minLength = 1;
    private int maxLength = 50;
    private int MAX_QUALITY = 10;
    private PasswordChecker passCheck = null;
    
    public PasswordInput () {
        Console console = System.console();
        scanner = new ScannerWrapper();
        scanner.setQuestionText(questionMsg).setOutOfBoundsText(outOfBoundsMsg);
    }
    
    public PasswordInput (PasswordChecker pc) {
        Console console = System.console();
        scanner = new ScannerWrapper();
        scanner.setQuestionText(questionMsg).setOutOfBoundsText(outOfBoundsMsg);
        passCheck = Objects.requireNonNull(pc, "El objeto PasswordChecker no puede ser nulo");
        MAX_QUALITY = passCheck.getUpperLimitQuality();
    }
    
    public PasswordInput setPasswordChecker (PasswordChecker pc) {
        passCheck = Objects.requireNonNull(pc, "El objeto PasswordChecker no puede ser nulo");
        MAX_QUALITY = passCheck.getUpperLimitQuality();
        return this;
    }
    
    public PasswordInput setMinLength(int min) {
        minLength = min;
        if (minLength > maxLength)
            maxLength = minLength;
        
        return this;
    }
    
    public PasswordInput setMaxLength(int max) {
        maxLength = max;
        if (minLength > maxLength)
            minLength = maxLength;
        return this;
    }
    
    public PasswordInput setMinAcceptedQuality(int minQuality) {
        minQualityAccepted = minQuality;
        if (minQualityAccepted > MAX_QUALITY)
            minQualityAccepted = MAX_QUALITY;
        
        return this;
    }
    
    public PasswordInput setQuestionText(String msg) {
        questionMsg = Objects.requireNonNull(msg, "La pregunta de la password no puede ser nula");
        scanner.setQuestionText(questionMsg);
        return this;
    }
    
    public PasswordInput setLowQualityText (String msg) {
        lowQualityMsg = Objects.requireNonNull(msg, "El mensaje de password de poca calidad no puede ser nulo");
        return this;
    }
    
    public PasswordInput setOutOfBoundsText (String msg) {
        outOfBoundsMsg = Objects.requireNonNull(msg, "El mensaje password fuera de rango no puede ser nulo");
        scanner.setOutOfBoundsText(outOfBoundsMsg);
        return this;
    }
    
    public String getValidatedPassword() {
        Objects.requireNonNull(passCheck, "El objeto PassChecker no puede ser nulo");
        int quality = 0;
        String pass = "";
        
        do {
            if (console != null) {
                System.out.print(this.questionMsg);
                pass = new String(console.readPassword());
            } else {
                pass = scanner.getString(minLength, maxLength);
            }

            quality = passCheck.getQuality(pass);

            if (quality < minQualityAccepted) {
                System.out.println(lowQualityMsg);
            }
        } while (quality < minQualityAccepted);

        return pass;
    }

    public String getUnvalidatedPassword() {
        String pass = "";

        if (console != null) {
            System.out.print(questionMsg);
            pass = new String(console.readPassword());
        } else {
            pass = scanner.getString(minLength, maxLength);
        }

        return pass;
    }
    
}
