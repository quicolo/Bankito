/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.dominio.Movimiento;
import com.bankito.dominio.exceptions.ClienteNoValidoException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.CuentaVista;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.CuentaDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kike
 */
public class CuentaCont {

    private ServicioBancario sb;

    public CuentaCont() {
        sb = ServicioBancarioFactory.create();
    }

    public void accionPrincipal() {
        // Llama la vista que presenta el menú
        // y según sea la opción llama a otros métodos de esta misma clase
        // que alternan entre vista-controlador-servicio
        int opc;
        do {
            opc = CuentaVista.menuPrincipal();
            switch (opc) {
                case CuentaVista.COD_LISTAR:
                    accionListaCuentas();
                    break;
                case CuentaVista.COD_ALTA:
                    accionAltaCuenta();
                    break;
                case CuentaVista.COD_BAJA:
                    accionBajaCuenta();
                    break;
                case CuentaVista.COD_INGRESO:
                    accionIngresoCuenta();
                    break;
                case CuentaVista.COD_RETIRADA:
                    accionRetiradaCuenta();
                    break;
                case CuentaVista.COD_TRANSFERENCIA:
                    accionTransferenciaCuenta();
                    break;
                case CuentaVista.COD_VER_MOVIMIENTOS:
                    accionVisualizaMovimientosCuenta();
                    break;
                case CuentaVista.COD_BUSCAR_CC_CLIENTE:
                    accionBuscarCuentasCliente();
                    break;
            }
            if (opc != CuentaVista.COD_SALIR)
                accionPausar();
        } while (opc != CuentaVista.COD_SALIR);
    }

    private void accionListaCuentas() {
        List<CuentaDto> lista = sb.listaCuentas();
        CuentaVista.listaCuentas(lista);
    }

    private void accionAltaCuenta() {
        UsuarioCont usuCont = new UsuarioCont();
        UsuarioDto usu = usuCont.accionBuscarPorNifUsuario();
        if (usu != UsuarioDto.NOT_FOUND) {
            try {
                CuentaDto cuenta = sb.nuevaCuenta(usu);
                CuentaVista.muestraMsgOperacionOK();
                CuentaVista.muestraDatosCuenta(cuenta);
            } catch (CuentaNoValidaException e) {
                CuentaVista.muestraMsgCuentaNoValida();
            }
        }
    }

    private List<CuentaDto> accionBuscarCuentasCliente() {
        ClienteCont cliCont = new ClienteCont();
        ClienteDto cli = cliCont.accionBuscarPorNifCliente();
        List<CuentaDto> listaCue = new ArrayList();

        if (cli != ClienteDto.NOT_FOUND) {
            listaCue = sb.buscaCuentaPorCliente(cli);
            if (listaCue.isEmpty()) {
                CuentaVista.muestraMsgCuentaNoEncontrada();
            } else {
                CuentaVista.muestraListaCuentas(listaCue);
            }
        }
        return listaCue;
    }

    private void accionBajaCuenta() {
        List<CuentaDto> listaCue = accionBuscarCuentasCliente();

        if (listaCue.isEmpty()) {
            CuentaVista.muestraMsgCuentaNoEncontrada();
        } else {
            CuentaDto cue = CuentaVista.muestraYEligeListaCuentas(listaCue);
            boolean confirma = CuentaVista.confirmaBajaCuenta();
            if (confirma) {
                boolean result = false;
                result = sb.eliminaCuenta(cue);

                if (result) {
                    CuentaVista.muestraMsgOperacionOK();
                } else {
                    CuentaVista.muestraMsgOperacionError();
                }
            }

        }
    }

    private void accionPausar() {
        CuentaVista.pausar();
    }

    private void accionIngresoCuenta() {
        List<CuentaDto> listaCue = accionBuscarCuentasCliente();

        if (listaCue.isEmpty()) {
            CuentaVista.muestraMsgCuentaNoEncontrada();
        } else {
            CuentaDto cue = CuentaVista.muestraYEligeListaCuentas(listaCue);
            float importe = CuentaVista.solicitaImporteIngreso();
            String concepto = CuentaVista.solicitaConceptoIngreso();
            boolean result = sb.ingresoEnCuenta(concepto, importe, cue);

            if (result) {
                CuentaVista.muestraMsgOperacionOK();
            } else {
                CuentaVista.muestraMsgOperacionError();
            }
        }
    }

    private void accionRetiradaCuenta() {
        List<CuentaDto> listaCue = accionBuscarCuentasCliente();

        if (listaCue.isEmpty()) {
            CuentaVista.muestraMsgCuentaNoEncontrada();
        } else {
            CuentaDto cue = CuentaVista.muestraYEligeListaCuentas(listaCue);
            float importe = CuentaVista.solicitaImporteRetirada();
            String concepto = CuentaVista.solicitaConceptoRetirada();
            boolean result = sb.retiradaDeCuenta(concepto, importe, cue);

            if (result) {
                CuentaVista.muestraMsgOperacionOK();
            } else {
                CuentaVista.muestraMsgOperacionError();
            }
        }
    }

    private void accionVisualizaMovimientosCuenta() {
        List<CuentaDto> listaCue = accionBuscarCuentasCliente();

        if (listaCue.isEmpty()) {
            CuentaVista.muestraMsgCuentaNoEncontrada();
        } else {
            CuentaDto cue = CuentaVista.muestraYEligeListaCuentas(listaCue);
            List<Movimiento> lista = cue.getListaMov();
            if (lista.isEmpty()) {
                CuentaVista.muestraMsgMovimientoNoEncontrado();
            } else {
                CuentaVista.muestraListaMovimientos(lista);
            }
        }
    }

    private void accionTransferenciaCuenta() {
        List<CuentaDto> listaCue = accionBuscarCuentasCliente();

        if (listaCue.isEmpty()) {
            CuentaVista.muestraMsgCuentaNoEncontrada();
        } else {
            CuentaVista.muestraMsgEligeCuentaOrigen();
            CuentaDto cueOrig = CuentaVista.muestraYEligeListaCuentas(listaCue);

            int numEntidad = CuentaVista.solicitaNumEntidadDestino();
            int numSucursal = CuentaVista.solicitaNumSucursalDestino();
            int numDc = CuentaVista.solicitaNumDcDestino();
            int numCuenta = (int) CuentaVista.solicitaNumCuentaDestino();

            CuentaDto cueDest = sb.buscaCuentaPorNumCuenta(numEntidad, numSucursal, numDc, numCuenta);

            if (cueDest == CuentaDto.NOT_FOUND) {
                CuentaVista.muestraMsgCuentaNoEncontrada();
            } else {
                float importe = CuentaVista.solicitaImporteTransferencia();
                String concepto = CuentaVista.solicitaConceptoTransferencia();
                boolean result = sb.transferencia(concepto, importe, cueOrig, cueDest);
                
                if (result) {
                    CuentaVista.muestraMsgOperacionOK();
                } else {
                    CuentaVista.muestraMsgOperacionError();
                }
            }
        }
    }
}
