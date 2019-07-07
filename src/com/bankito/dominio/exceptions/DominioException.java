/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio.exceptions;

/**
 *
 * @author Kike
 */
public class DominioException extends Exception {
    public DominioException (String mensaje) {
        super(mensaje);
    }
    
}
