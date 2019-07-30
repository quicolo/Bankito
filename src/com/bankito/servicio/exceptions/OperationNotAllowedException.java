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
public class OperationNotAllowedException extends ServicioException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
    
}
