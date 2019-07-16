/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.utils;

import java.util.Objects;

/**
 *
 * @author Kike
 */
public class PasswordCheckerImpl implements PasswordChecker {

    //number of chars : the Higher the Better("password must be greater than 8 chars"){done}
    //combination of uppercase and lowercase{done}
    //contains numbers{done}
    //non repeated characters (every char is different ascii char){done}
    //is not a consecutive password ie 123456789 or 987654321{done}
    //is not blank ("[space]"){done} 


    private int pLength;
    private final int MIN_QUALITY = 0;
    private final int MAX_QUALITY = 10;
    private int quality = 0;
    private String pass;

    public PasswordCheckerImpl(){}

    @Override
    public int getQuality(String password){
        pass = Objects.requireNonNull(password, "La password no puede ser nula");
        pLength = password.length();
        
        if(pass.isEmpty()){
            System.out.println("No se permiten las contraseñas vacías");
            return MIN_QUALITY;
        }
        if(pLength >= 8){
            quality++;
            if(pLength >= 12){
                quality++;
                if(pLength >= 16){
                    quality++;
                }
            }
        }
        if(hasUpperCase(pass) && hasLowerCase(pass)){
            quality+=2;
        }
        if(containsNumbers(pass)){
            quality+=2;
        }
        if(hasNoRepeats(pass)){
            quality+=2;
        }
        if(!containsConsecutiveNums(pass)){
            quality++;
        }

        //System.out.println("La calidad de la password es de " + quality +" sobre " + MAX_QUALITY);
        
        return quality;

    }

    @Override
    public int getLowerLimitQuality() {
        return MIN_QUALITY;
    }

    @Override
    public int getUpperLimitQuality() {
        return MAX_QUALITY;
    }

    

    //Component Methods

    private boolean hasUpperCase(String str){
        for(int i = 0; i<pLength; i++){
            if(Character.isUpperCase(str.charAt(i))){
                return true;
            }
        }
        return false;

    }

    private boolean hasLowerCase(String str){
        for(int i  = 0; i<pLength; i++){
            if(Character.isUpperCase(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    private boolean containsNumbers(String str){
        for(int i = 0; i<pLength; i++){
            if(Character.isDigit(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    private boolean hasNoRepeats(String str){
        for(int i = 0; i<pLength; i++)
            if(containsChar(str, str.charAt(i))){
                return false;
            }
        return true;
    }


    private boolean containsChar(String s, char search) {
        if (s.length() == 0)
            return false;
        else
            return s.charAt(0) == search || containsChar(s.substring(1), search);
    }

    private boolean containsConsecutiveNums(String str){
        for(int i = 0; i<pLength; i++){
            if(Character.isDigit(str.charAt(i))){
                if(str.charAt(i)-1 == str.charAt(i-1) || str.charAt(i)+1 == str.charAt(i+1)){
                    return true;
                }
            }
        }
        return false;
    }
}