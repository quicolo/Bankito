package com.bankito;

import com.bankito.aplicacion.consolaGUI.MainCont;
import com.bankito.util.AppConfiguration;
import java.io.IOException;

/**
 *
 * @author Kike
 */
public class MainConsole {
    public static void main (String args[]) throws IOException{
        AppConfiguration.loadProperties();
        
        MainCont mainCont = new MainCont();
        mainCont.accionPrincipal();
    }
    
}
