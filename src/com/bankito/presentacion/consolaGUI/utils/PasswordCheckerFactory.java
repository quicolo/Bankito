/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.consolaGUI.utils;

/**
 *
 * @author Kike
 */
public class PasswordCheckerFactory {
    public static PasswordChecker create() {
        return new PasswordCheckerImpl();
    }
}
