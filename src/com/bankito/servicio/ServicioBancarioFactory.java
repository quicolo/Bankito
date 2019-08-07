/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

/**
 * <h1>ServicioBancarioFactory</h1>
 * Esta clase funciona como una factoría de objetos que cumplen con la interfaz
 * ServicioBancario, para ello solo desarrolla el método create()
 * 
 * @author Enrique Royo Sánchez
 */
public class ServicioBancarioFactory {
    public static ServicioBancario create() {
        return new ServicioBancarioImpl();
    }
}
