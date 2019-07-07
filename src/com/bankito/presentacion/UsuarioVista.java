/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion;

import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.utils.Menu;
import com.bankito.presentacion.utils.MenuElement;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Kike
 */
public class UsuarioVista {
    public static final int COD_SALIR = 0;
    public static final String TXT_SALIR = "Volver al menú anterior";
    public static final int COD_LISTAR = 1;
    public static final String TXT_LISTAR = "Listar usuarios";
    public static final int COD_ALTA = 2;
    public static final String TXT_ALTA = "Alta de usuario";
    
    public static int menuPrincipal() {
        Menu m = new Menu("MENU USUARIO", "", "Selecciona una opción");
        m.addElement(new MenuElement(COD_SALIR, TXT_SALIR));
        m.addElement(new MenuElement(COD_LISTAR, TXT_LISTAR));
        m.addElement(new MenuElement(COD_ALTA, TXT_ALTA));
        
        MenuElement e = m.showAndSelect();
        return e.getCodigo();
    }
    
    public static void listaUsuarios(List<UsuarioDto> lista) {
        for (UsuarioDto u : lista) {
            System.out.println(u.toString());
        }
    }
}
