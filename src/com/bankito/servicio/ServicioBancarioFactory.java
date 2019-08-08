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
