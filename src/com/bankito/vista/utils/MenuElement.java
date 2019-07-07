/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.vista.utils;

import java.util.Objects;

/**
 *
 * @author Kike
 */
public class MenuElement {
    private int codigo;
    private String texto;
    
    public MenuElement(int codigo, String texto) {
        this.codigo = codigo;
        this.texto = Objects.requireNonNull(texto, "El texto del elemento del menú no puede ser nulo");
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTexto() {
        return texto;
    }
    
}
