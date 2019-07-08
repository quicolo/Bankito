/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito;

import com.bankito.aplicacion.UsuarioCont;
import com.bankito.util.AppConfiguration;
import java.io.IOException;

/**
 *
 * @author Kike
 */
public class Main {
    public static void main (String args[]) throws IOException{
        AppConfiguration.loadProperties();
        
        UsuarioCont usuCont = new UsuarioCont();
        usuCont.principal();
    }
    
}
