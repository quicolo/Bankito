/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.utils;

/**
 *
 * @author Kike
 */
public class PasswordChecker {

    //number of chars : the Higher the Better("password must be greater than 8 chars"){done}
    //combination of uppercase and lowercase{done}
    //contains numbers{done}
    //non repeated characters (every char is different ascii char){done}
    //is not a consecutive password ie 123456789 or 987654321{done}
    //is not blank ("[space]"){done} 


    int pLength;
    final int MAX_FORTALEZA = 10;
    int fortaleza = 0;
    String pass;

    public PasswordChecker(String pwd){
        pass = pwd;
        pLength = pwd.length();

    }

    public int rateSecurity(){
        if(pass.isEmpty()){
            System.out.println("No se permiten las contraseñas vacías");
            return 0;
        }
        if(pLength >= 8){
            fortaleza++;
            if(pLength >= 12){
                fortaleza++;
                if(pLength >= 16){
                    fortaleza++;
                }
            }
        }
        if(hasUpperCase(pass) && hasLowerCase(pass)){
            fortaleza+=2;
        }
        if(containsNumbers(pass)){
            fortaleza+=2;
        }
        if(hasNoRepeats(pass)){
            fortaleza+=2;
        }
        if(!containsConsecutiveNums(pass)){
            fortaleza++;
        }

        System.out.println("La calidad de la password es de " + fortaleza +" sobre " + MAX_FORTALEZA);
        
        return fortaleza;

    }


    //Component Methods

    public boolean hasUpperCase(String str){
        for(int i = 0; i<pLength; i++){
            if(Character.isUpperCase(str.charAt(i))){
                return true;
            }
        }
        return false;

    }

    public boolean hasLowerCase(String str){
        for(int i  = 0; i<pLength; i++){
            if(Character.isUpperCase(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    public boolean containsNumbers(String str){
        for(int i = 0; i<pLength; i++){
            if(Character.isDigit(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    public boolean hasNoRepeats(String str){
        for(int i = 0; i<pLength; i++)
            if(containsChar(str, str.charAt(i))){
                return false;
            }
        return true;
    }


    public boolean containsChar(String s, char search) {
        if (s.length() == 0)
            return false;
        else
            return s.charAt(0) == search || containsChar(s.substring(1), search);
    }

    public boolean containsConsecutiveNums(String str){
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