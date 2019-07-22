/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.servicio.ServicioBancario;
import com.bankito.presentacion.MainVista;

/**
 *
 * @author Kike
 */
public class MainCont {

    private ServicioBancario sb;

    public MainCont() {
    }

    public void accionPrincipal() {
        // Llama la vista que presenta el menú
        // y según sea la opción llama a otros métodos de esta misma clase
        // que alternan entre vista-controlador-servicio
        int opc;
        do {
            opc = MainVista.menuPrincipal();
            switch (opc) {
                case MainVista.COD_USUARIO:
                    accionMenuUsuario();
                    break;
                case MainVista.COD_CLIENTE:
                    accionMenuCliente();
                    break;
                case MainVista.COD_CUENTA:
                    accionMenuCuenta();
                    break;
            }
        } while (opc != MainVista.COD_SALIR);
    }

    private void accionMenuUsuario() {
        UsuarioCont uc = new UsuarioCont();
        uc.accionPrincipal();
    }

    private void accionMenuCliente() {
        ClienteCont cc = new ClienteCont();
        cc.accionPrincipal();
    }

    private void accionMenuCuenta() {
    
    }

}
