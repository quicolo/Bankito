/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.consolaGUI;

import com.bankito.presentacion.consolaGUI.utils.Menu;
import com.bankito.presentacion.consolaGUI.utils.MenuElement;
import com.bankito.presentacion.consolaGUI.utils.ScannerWrapper;
import com.bankito.servicio.dto.ClienteDto;
import java.util.List;

/**
 *
 * @author Kike
 */
public class ClienteVista {

    public static final int COD_SALIR = 0;
    public static final String TXT_SALIR = "Volver al menú anterior";
    
    public static final int COD_LISTAR = 1;
    public static final String TXT_LISTAR = "Listar clientes";

    public static final int COD_ALTA = 2;
    public static final String TXT_ALTA = "Alta de cliente";
    
    public static final int COD_BAJA = 3;
    public static final String TXT_BAJA = "Baja de cliente";
    
    public static final int COD_BUSCA_NIF = 4;
    public static final String TXT_BUSCA_NIF = "Busca un cliente por su NIF";


    public static int menuPrincipal() {
        Menu m = new Menu();
        m.setTitle("MENU CLIENTE");

        m.addElement(new MenuElement(COD_SALIR, TXT_SALIR));
        m.addElement(new MenuElement(COD_LISTAR, TXT_LISTAR));
        m.addElement(new MenuElement(COD_ALTA, TXT_ALTA));
        m.addElement(new MenuElement(COD_BAJA, TXT_BAJA));
        m.addElement(new MenuElement(COD_BUSCA_NIF, TXT_BUSCA_NIF));

        MenuElement e = m.showAndSelect();
        return e.getCodigo();
    }

    public static void listaClientes(List<ClienteDto> lista) {
        for (ClienteDto u : lista) {
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

    public static void muestraMsgClienteNoValido() {
        System.out.println("Los datos del cliente no son válidos");
    }
    
    public static void muestraMsgUsuarioNoValido() {
        System.out.println("Los datos del usuario no son válidos");
    }

    public static void muestraMsgUsuarioNoEncontrado() {
        System.out.println("El usuario no se encuentra");
    }

    public static boolean confirmaBajaCliente() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Estás seguro de eliminar el cliente? (S/N): ");
        return sc.getStringOfStringArgs("S", "N").equals("S");
    }

    public static void muestraMsgOperacionError() {
        System.out.println("Se produjo un error al borrar al cliente");
    }

    public static String solicitaNif() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el NIF del cliente (formato 00000000A): ");
        return sc.getString(9, 9);
    }

    public static void muestraDatosCliente(ClienteDto usu) {
        System.out.println(usu.toJsonString());
    }

    public static String solicitaApellido1() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el primer apellido del cliente: ");
        return sc.getString(1, 30);
    }

    public static String solicitaApellido2() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el segundo apellido del cliente: ");
        return sc.getString(1, 30);
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

    public static String solicitaDireccion() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce la dirección postal del cliente: ");
        return sc.getString(1, 80);
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
