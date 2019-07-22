/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion;

import com.bankito.presentacion.utils.*;
import com.bankito.servicio.dto.ClienteDto;
import java.util.List;

/**
 *
 * @author Kike
 */
public class MainVista {

    public static final int COD_SALIR = 0;
    public static final String TXT_SALIR = "Salir de la aplicación";
    
    public static final int COD_USUARIO = 1;
    public static final String TXT_USUARIO = "Gestión de usuarios";

    public static final int COD_CLIENTE = 2;
    public static final String TXT_CLIENTE = "Gestión de clientes";
    
    public static final int COD_CUENTA = 3;
    public static final String TXT_CUENTA = "Gestión de cuentas";
    


    public static int menuPrincipal() {
        Menu m = new Menu();
        m.setTitle("MENU PRINCIPAL");

        m.addElement(new MenuElement(COD_SALIR, TXT_SALIR));
        m.addElement(new MenuElement(COD_USUARIO, TXT_USUARIO));
        m.addElement(new MenuElement(COD_CLIENTE, TXT_CLIENTE));
        m.addElement(new MenuElement(COD_CUENTA, TXT_CUENTA));

        MenuElement e = m.showAndSelect();
        return e.getCodigo();
    }

}
