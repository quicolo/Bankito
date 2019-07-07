/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.UsuarioVista;
import java.util.List;

/**
 *
 * @author Kike
 */
public class UsuarioCont {

    public void principal() {
        // Llama la vista que presenta el menú
        // y según sea la opción llama a otros métodos de esta misma clase
        // que alternan entre vista-controlador-servicio
        int opc;
        do {
            opc = UsuarioVista.menuPrincipal();
            switch(opc) {
                case UsuarioVista.COD_LISTAR:
                    listaUsuarios();
                    break;
                case UsuarioVista.COD_ALTA:
                    altaUsuario();
                    break;
            }
        } while (opc != UsuarioVista.COD_SALIR);
    }
    
    public void listaUsuarios() {
        ServicioBancario sb = ServicioBancarioFactory.create();
        List<UsuarioDto> lista = sb.listaUsuarios();
        UsuarioVista.listaUsuarios(lista);
        // pause;
    }
    
    public void altaUsuario() {
//        ServicioBancario sb = ServicioBancarioFactory.create();
//        UsuarioVista.listaUsuarios(sb.listaUsuarios());
//        // pause;
//        principal();
    }
}
