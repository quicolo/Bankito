/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

/**
 *
 * @author Kike
 */
public class ServicioBancarioFactory {
    public static ServicioBancario create() {
        return new ServicioBancarioImpl();
    }
}
