/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio.exceptions;

/**
 *
 * @author Kike
 */
public class UserNotLoggedInException extends ServicioException{
    public UserNotLoggedInException (String message) {
        super(message);
    }
    
    
}
