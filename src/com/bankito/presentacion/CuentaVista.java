/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion;

import com.bankito.dominio.Movimiento;
import com.bankito.presentacion.utils.*;
import com.bankito.servicio.dto.CuentaDto;
import java.util.List;

/**
 *
 * @author Kike
 */
public class CuentaVista {

    public static final int COD_SALIR = 0;
    public static final String TXT_SALIR = "Volver al menú anterior";
    
    public static final int COD_LISTAR = 1;
    public static final String TXT_LISTAR = "Listar todas las cuentas";

    public static final int COD_ALTA = 2;
    public static final String TXT_ALTA = "Alta de cuenta";
    
    public static final int COD_BAJA = 3;
    public static final String TXT_BAJA = "Baja de cuenta";
    
    public static final int COD_INGRESO = 4;
    public static final String TXT_INGRESO = "Ingreso de efectivo en cuenta";
    
    public static final int COD_RETIRADA = 5;
    public static final String TXT_RETIRADA = "Retirada de efectivo en cuenta";
    
    public static final int COD_TRANSFERENCIA = 6;
    public static final String TXT_TRANSFERENCIA = "Transferencia entre dos cuentas";
      
    public static final int COD_BUSCAR_CC_CLIENTE = 7;
    public static final String TXT_BUSCAR_CC_CLIENTE = "Buscar cuentas de un cliente";
    
    public static final int COD_VER_MOVIMIENTOS = 8;
    public static final String TXT_VER_MOVIMIENTOS = "Visualiza movimientos de una cuenta";


    public static int menuPrincipal() {
        Menu m = new Menu();
        m.setTitle("MENU CUENTA");

        m.addElement(new MenuElement(COD_SALIR, TXT_SALIR));
        m.addElement(new MenuElement(COD_LISTAR, TXT_LISTAR));
        m.addElement(new MenuElement(COD_ALTA, TXT_ALTA));
        m.addElement(new MenuElement(COD_BAJA, TXT_BAJA));
        m.addElement(new MenuElement(COD_INGRESO, TXT_INGRESO));
        m.addElement(new MenuElement(COD_RETIRADA, TXT_RETIRADA));
        m.addElement(new MenuElement(COD_TRANSFERENCIA, TXT_TRANSFERENCIA));
        m.addElement(new MenuElement(COD_VER_MOVIMIENTOS, TXT_VER_MOVIMIENTOS));
        m.addElement(new MenuElement(COD_BUSCAR_CC_CLIENTE, TXT_BUSCAR_CC_CLIENTE));

        MenuElement e = m.showAndSelect();
        return e.getCodigo();
    }

    public static void listaCuentas(List<CuentaDto> lista) {
        for (CuentaDto u : lista) {
            System.out.println(u.toJsonString());
        }
    }

    public static String solicitaNombre() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el nombre del cliente: ");
        return sc.getString(1, 30);
    }

    public static void muestraMsgOperacionOK() {
        System.out.println("La operación se realizó correctamente");
    }

    public static void muestraMsgClienteDuplicado() {
        System.out.println("Ya existe un cliente con ese NIF");
    }

    public static void muestraMsgCuentaNoValida() {
        System.out.println("Los datos de la cuenta no son válidos. La operación no se realizó correctamente.");
    }
    
    public static void muestraMsgUsuarioNoValido() {
        System.out.println("Los datos del usuario no son válidos");
    }

    public static void muestraMsgUsuarioNoEncontrado() {
        System.out.println("El usuario no se encuentra");
    }

    public static boolean confirmaBajaCuenta() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Estás seguro de eliminar la cuenta seleccionada? (S/N): ");
        return sc.getStringOfStringArgs("S", "N").equals("S");
    }

    public static void muestraMsgOperacionError() {
        System.out.println("Se produjo un error al borrar al cliente");
    }

    public static void muestraDatosCuenta(CuentaDto cue) {
        System.out.println(cue.toJsonString());
    }    
    
    public static void muestraMsgDatosUsuario() {
        System.out.println("A continuación debes introducir los datos del usuario de la aplicación: ");
    }

    public static void muestraMsgClienteNoEncontrado() {
        System.out.println("No se encuentra el cliente indicado");
    }
    
    public static void pausar() {
        ScannerWrapper sc = new ScannerWrapper();
        sc.pause();
    }

    public static void muestraListaCuentas(List<CuentaDto> listaCue) {
        for(CuentaDto c : listaCue) {
            muestraDatosCuenta(c);
        }
        
    }

    public static CuentaDto muestraYEligeListaCuentas(List<CuentaDto> listaCue) {
        Menu cueMenu = new Menu().setTitle("Escoge una cuenta");
        
        for(int i=0; i<listaCue.size();i++)
            cueMenu.addElement(new MenuElement(i+1,toStringNumCuenta(listaCue.get(i))));
        
        MenuElement elem = cueMenu.showAndSelect();
        return listaCue.get(elem.getCodigo()-1);
        
    }
    
    public static void muestraMsgCuentaNoEncontrada() {
        System.out.println("No existe ninguna cuenta asociada.");
    }
    
    public static String toStringNumCuenta(CuentaDto cue) {
        return String.format("%04d %04d %02d %010d", cue.getNumCuenta(), 
                cue.getNumSucursal(), cue.getNumDigitoControl(), cue.getNumCuenta());
    }

    public static float solicitaImporteIngreso() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el importe a ingresar: ");
        return (float) sc.getDoubleGreaterThan(0.0);
    }
    
    public static String solicitaConceptoIngreso() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el concepto del ingreso: ");
        return sc.getString(3, 50);
    }

    public static float solicitaImporteRetirada() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el importe a retirar: ");
        return (float) sc.getDoubleGreaterThan(0.0);
    }

    public static String solicitaConceptoRetirada() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el concepto de la retirada de efectivo: ");
        return sc.getString(3, 50);
    }

    public static float solicitaImporteTransferencia() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el importe a transferir: ");
        return (float) sc.getDoubleGreaterThan(0.0);
    }
    
    public static String solicitaConceptoTransferencia() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el concepto de la transferencia: ");
        return sc.getString(3, 50);
    }
    
    public static void muestraListaMovimientos(List<Movimiento> lista) {
        for(Movimiento m: lista)
            System.out.println(m.toString());
    }

    public static void muestraMsgMovimientoNoEncontrado() {
        System.out.println("La cuenta no tiene movimientos");
    }
    
    public static void muestraMsgEligeCuentaOrigen() {
        System.out.println("Elige la cuenta origen: ");
    }

    public static int solicitaNumEntidadDestino() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el código de entidad de la cuenta destino: ");
        return sc.getInt(0, 9999);
    }

    public static int solicitaNumSucursalDestino() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el código de sucursal de la cuenta destinol: ");
        return sc.getInt(0, 9999);
    }

    public static int solicitaNumDcDestino() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce los dígitos de control de la cuenta destino: ");
        return sc.getInt(0, 99);
    }

    public static long solicitaNumCuentaDestino() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el código de la cuenta de la cuenta destino: ");
        return sc.getLong(0, 999999999);
    }

    public static void muestraMsgUsuarioNoLogado() {
        System.out.println("Debes hacer login para realizar la operación");
    }

    public static void muestraErrorSesionPermisos() {
        System.out.println("Debes estar logado y tener permisos suficientes para realizar la operación escogida");
    }

    public static void muestraMsgOperacionNoPermitida() {
        System.out.println("Tu usuario no tiene permisos para realizar la operación escogida");
    }
    
    
}
