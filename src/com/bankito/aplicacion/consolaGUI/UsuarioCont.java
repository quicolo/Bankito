/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion.consolaGUI;

import com.bankito.dominio.exceptions.DominioException;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.consolaGUI.UsuarioVista;
import com.bankito.servicio.dto.PerfilUsuarioDto;
import com.bankito.servicio.exceptions.OperationNotAllowedException;
import com.bankito.servicio.exceptions.ServicioException;
import com.bankito.servicio.exceptions.UserNotLoggedInException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kike
 */
public class UsuarioCont {

    private ServicioBancario sb;

    public UsuarioCont(ServicioBancario sb) {
        this.sb = sb;
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
                case UsuarioVista.COD_LOGOUT:
                    accionLogoutUsuario();
                    break;
                case UsuarioVista.COD_BAJA:
                    accionBajaUsuario();
                    break;
                case UsuarioVista.COD_BUSCA_NIF:
                    accionBuscarPorNifUsuario();
                    break;

            }
            if (opc != UsuarioVista.COD_SALIR)
                accionPausar();
        } while (opc != UsuarioVista.COD_SALIR);
    }

    private void accionListaUsuarios() {
        try {
            List<UsuarioDto> lista = sb.listaUsuarios();
            UsuarioVista.listaUsuarios(lista);
        } catch (UserNotLoggedInException ex) {
            UsuarioVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            UsuarioVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }
    }
    
    UsuarioDto accionAltaUsuario() {
        String nombre = UsuarioVista.solicitaNombre();
        String password = UsuarioVista.solicitaPasswordValida("Introduce la contraseña: ");
        UsuarioDto usu = UsuarioDto.NOT_FOUND;
        try {
            PerfilUsuarioDto per = sb.buscaPerfilUsuarioPorNombre("Cliente");
            usu = sb.nuevoUsuario(nombre, password, per);
            UsuarioVista.muestraMsgOperacionOK();
        } catch (UsuarioDuplicadoException ex) {
            UsuarioVista.muestraMsgUsuarioDuplicado();
        } catch (UsuarioNoValidoException ex) {
            UsuarioVista.muestraMsgUsuarioNoValido();
        } catch (UserNotLoggedInException ex) {
            UsuarioVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            UsuarioVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }
        
        
        return usu;
    }

    UsuarioDto accionBuscarPorNifUsuario() {
        UsuarioDto usu = UsuarioDto.NOT_FOUND;
        try {
            String nombre = UsuarioVista.solicitaNif();
            usu = sb.buscaUsuarioPorNif(nombre);
            if (usu == UsuarioDto.NOT_FOUND) {
                UsuarioVista.muestraMsgUsuarioNoEncontrado();
            } else {
                UsuarioVista.muestraDatosUsuario(usu);
            }   
        } catch (UserNotLoggedInException ex) {
            UsuarioVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            UsuarioVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }
        return usu;
    }

    private void accionBajaUsuario() {
        try {
            String nombre = UsuarioVista.solicitaNombre();
            UsuarioDto usu = sb.buscaUsuarioPorNombre(nombre);
            if (usu == UsuarioDto.NOT_FOUND) {
                UsuarioVista.muestraMsgUsuarioNoEncontrado();
            } else {
                boolean confirma = UsuarioVista.confirmaBajaUsuario();
                if (confirma) {
                    boolean result = false;
                    try {
                        result = sb.eliminaUsuario(usu);
                    } catch (UsuarioNoValidoException ex) {
                        result = false;
                    }
                    if (result) {
                        UsuarioVista.muestraMsgOperacionOK();
                    } else {
                        UsuarioVista.muestraMsgOperacionError();
                    }
                }

            }
        } catch (UserNotLoggedInException ex) {
            UsuarioVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            UsuarioVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }
    }

    private void accionLoginUsuario() {

        String nombre = UsuarioVista.solicitaNombre();
        String password = UsuarioVista.solicitaPassword("Introduce la contraseña: ");
        UsuarioDto usu;

        try {
            usu = sb.loginUsuario(nombre, password);
            if (usu != UsuarioDto.NOT_FOUND) {
                UsuarioVista.muestraMsgLoginOK();
            } else {
                UsuarioVista.muestraMsgLoginError();
            }
        } catch (UsuarioEncodePasswordException ex) {
            UsuarioVista.muestraMsgUsuarioNoValido();
        } catch (DominioException ex) {
            UsuarioVista.muestraMsgOperacionError();
        } catch (UserNotLoggedInException ex) {
            UsuarioVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            UsuarioVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }

    }

    void accionLogoutUsuario() {
        try {
            boolean logoutUsuario = sb.logoutUsuario();
            if (logoutUsuario == true)
                UsuarioVista.muestraMsgOperacionOK();
            else
                UsuarioVista.muestraMsgOperacionError();
        } catch (DominioException ex) {
            UsuarioVista.muestraMsgOperacionError();
        } catch (ServicioException ex) {
            UsuarioVista.muestraErrorSesionPermisos();
        }
    }
    
    
    private void accionPausar() {
        UsuarioVista.pausar();        
    }

    
}
