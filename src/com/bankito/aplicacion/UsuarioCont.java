/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.UsuarioVista;
import static com.bankito.presentacion.UsuarioVista.COD_BAJA;
import static com.bankito.presentacion.UsuarioVista.COD_BUSCA_NIF;
import static com.bankito.presentacion.UsuarioVista.COD_LOGIN;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kike
 */
public class UsuarioCont {

    private ServicioBancario sb;

    public UsuarioCont() {
        sb = ServicioBancarioFactory.create();
    }

    public void accionPrincipal() {
        // Llama la vista que presenta el menú
        // y según sea la opción llama a otros métodos de esta misma clase
        // que alternan entre vista-controlador-servicio
        int opc;
        do {
            opc = UsuarioVista.menuPrincipal();
            switch (opc) {
                case UsuarioVista.COD_LISTAR:
                    accionListaUsuarios();
                    break;
                case UsuarioVista.COD_ALTA:
                    accionAltaUsuario();
                    break;
                case UsuarioVista.COD_LOGIN:
                    accionLoginUsuario();
                    break;
                case UsuarioVista.COD_BAJA:
                    accionBajaUsuario();
                    break;
                case UsuarioVista.COD_BUSCA_NIF:
                    accionBuscarPorNifUsuario();
                    break;

            }
        } while (opc != UsuarioVista.COD_SALIR);
    }

    private void accionListaUsuarios() {
        List<UsuarioDto> lista = sb.listaUsuarios();
        UsuarioVista.listaUsuarios(lista);
        accionPrincipal();
    }

    private void accionAltaUsuario() {
        String nombre = UsuarioVista.solicitaNombre();
        String password = UsuarioVista.solicitaPasswordValida("Introduce la contraseña: ");
        UsuarioDto usu;
        try {
            sb.nuevoUsuario(nombre, password);
            UsuarioVista.muestraMsgOperacionOK();
        } catch (UsuarioDuplicadoException ex) {
            UsuarioVista.muestraMsgUsuarioDuplicado();
        } catch (UsuarioNoValidoException ex) {
            UsuarioVista.muestraMsgUsuarioNoValido();
        }
    }

    private void accionBuscarPorNifUsuario() {
        String nombre = UsuarioVista.solicitaNif();
        UsuarioDto usu = sb.buscaUsuarioPorNif(nombre);
        if(usu == UsuarioDto.NOT_FOUND)
            UsuarioVista.muestraMsgUsuarioNoEncontrado();
        else 
            UsuarioVista.muestraDatosUsuario(usu);
    }

    private void accionBajaUsuario() {
        String nombre = UsuarioVista.solicitaNombre();
        UsuarioDto usu = sb.buscaUsuarioPorNombre(nombre);
        if(usu == UsuarioDto.NOT_FOUND)
            UsuarioVista.muestraMsgUsuarioNoEncontrado();
        else {
            boolean confirma = UsuarioVista.confirmaBajaUsuario();
            if (confirma) {
                boolean result=false;
                try {
                    result = sb.eliminaUsuario(usu);
                } catch (UsuarioNoValidoException ex) {
                    result=false;
                }
                if(result)
                    UsuarioVista.muestraMsgOperacionOK();
                else
                    UsuarioVista.muestraMsgOperacionError();
            }
            
        }   
    }

    private void accionLoginUsuario() {

        String nombre = UsuarioVista.solicitaNombre();
        String password = UsuarioVista.solicitaPassword("Introduce la contraseña: ");
        UsuarioDto usu;
        
        try {
            usu = sb.loginUsuario(nombre, password);
            if (usu != UsuarioDto.NOT_FOUND)
                UsuarioVista.muestraMsgLoginOK();
            else 
                UsuarioVista.muestraMsgLoginError();
        } catch (UsuarioEncodePasswordException ex) {
            UsuarioVista.muestraMsgUsuarioNoValido();
        }

    }
}


