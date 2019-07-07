/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.vista.utils;

import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Kike
 */
public class ScanIntegerInRange {
    private String textoError = "Debes escoger un número entero entre ";
    private int minimo, maximo;
    
    public ScanIntegerInRange(int minimo, int maximo) {
        this.maximo = maximo;
        this.minimo = minimo;
    }
    
    public ScanIntegerInRange(int minimo, int maximo, String textoError) {
        this.maximo = maximo;
        this.minimo = minimo;
        this.textoError = Objects.requireNonNull(textoError, "El texto de error no puede ser nulo");
    }
    
    public int read() {
        int valor = Integer.MAX_VALUE;
        Scanner teclado = new Scanner(System.in);
        String respuesta;
        
        while(valor < minimo || valor > maximo) {
            try {
                respuesta = teclado.nextLine();
                valor = Integer.parseInt(respuesta);
            } catch(NumberFormatException e) {
                System.out.println(textoError+minimo+" y "+maximo);
            }
            if (valor < minimo || valor > maximo)
                System.out.println(textoError+minimo+" y "+maximo);
        } 
        return valor;
    }
}
