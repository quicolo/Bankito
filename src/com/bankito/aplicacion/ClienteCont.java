/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.dominio.exceptions.ClienteDuplicadoException;
import com.bankito.dominio.exceptions.ClienteNoValidoException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.ClienteVista;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.exceptions.OperationNotAllowedException;
import com.bankito.servicio.exceptions.ServicioException;
import com.bankito.servicio.exceptions.UserNotLoggedInException;
import java.util.List;

/**
 *
 * @author Kike
 */
public class ClienteCont {

    private ServicioBancario sb;

    public ClienteCont(ServicioBancario sb) {
        this.sb = sb;
    }

    public void accionPrincipal() {
        // Llama la vista que presenta el menú
        // y según sea la opción llama a otros métodos de esta misma clase
        // que alternan entre vista-controlador-servicio
        int opc;
        do {
            opc = ClienteVista.menuPrincipal();
            switch (opc) {
                case ClienteVista.COD_LISTAR:
                    accionListaClientes();
                    break;
                case ClienteVista.COD_ALTA:
                    accionAltaCliente();
                    break;
                case ClienteVista.COD_BAJA:
                    accionBajaCliente();
                    break;
                case ClienteVista.COD_BUSCA_NIF:
                    accionBuscarPorNifCliente();
                    break;

            }
            if (opc != ClienteVista.COD_SALIR) {
                accionPausar();
            }
        } while (opc != ClienteVista.COD_SALIR);
    }

    private void accionListaClientes() {
        try {
            List<ClienteDto> lista = sb.listaClientes();
            ClienteVista.listaClientes(lista);
        } catch (UserNotLoggedInException ex) {
            ClienteVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            ClienteVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            ClienteVista.muestraErrorSesionPermisos();
        }
    }

    private void accionAltaCliente() {
        String nombre = ClienteVista.solicitaNombre();
        String apellido1 = ClienteVista.solicitaApellido1();
        String apellido2 = ClienteVista.solicitaApellido2();
        String nif = ClienteVista.solicitaNif();
        String direc = ClienteVista.solicitaDireccion();
        ClienteVista.muestraMsgDatosUsuario();

        UsuarioCont usuCont = new UsuarioCont(sb);
        UsuarioDto usu = usuCont.accionAltaUsuario();
        try {
            sb.nuevoCliente(nombre, apellido1, apellido2, nif, direc, usu);
            ClienteVista.muestraMsgOperacionOK();
        } catch (ClienteDuplicadoException ex) {
            ClienteVista.muestraMsgClienteDuplicado();
        } catch (ClienteNoValidoException e) {
            ClienteVista.muestraMsgClienteNoValido();
        } catch (UsuarioNoValidoException e) {
            ClienteVista.muestraMsgUsuarioNoValido();
        } catch (UserNotLoggedInException ex) {
            ClienteVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            ClienteVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            ClienteVista.muestraErrorSesionPermisos();
        }
    }

    ClienteDto accionBuscarPorNifCliente() {
        ClienteDto cli = ClienteDto.NOT_FOUND;
        try {
            String nif = ClienteVista.solicitaNif();
            cli = sb.buscaClientePorNif(nif);
            if (cli == ClienteDto.NOT_FOUND) {
                ClienteVista.muestraMsgClienteNoEncontrado();
            } else {
                ClienteVista.muestraDatosCliente(cli);
            } 
        } catch (UserNotLoggedInException ex) {
            ClienteVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            ClienteVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            ClienteVista.muestraErrorSesionPermisos();
        }
        return cli;
    }

    private void accionBajaCliente() {
        try {
            String nif = ClienteVista.solicitaNif();
            ClienteDto cli = sb.buscaClientePorNif(nif);
            if (cli == ClienteDto.NOT_FOUND) {
                ClienteVista.muestraMsgUsuarioNoEncontrado();
            } else {
                boolean confirma = ClienteVista.confirmaBajaCliente();
                if (confirma) {
                    boolean result = false;
                    try {
                        result = sb.eliminaCliente(cli);
                    } catch (ClienteNoValidoException ex) {
                        result = false;
                    }
                    if (result) {
                        ClienteVista.muestraMsgOperacionOK();
                    } else {
                        ClienteVista.muestraMsgOperacionError();
                    }
                }

            }
        } catch (UserNotLoggedInException ex) {
            ClienteVista.muestraMsgUsuarioNoLogado();
        } catch (OperationNotAllowedException ex) {
            ClienteVista.muestraMsgOperacionNoPermitida();
        } catch (ServicioException ex) {
            ClienteVista.muestraErrorSesionPermisos();
        }
    }

    private void accionPausar() {
        ClienteVista.pausar();
    }
}
