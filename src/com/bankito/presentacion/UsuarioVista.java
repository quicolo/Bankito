/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion;

import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.presentacion.utils.*;
import java.util.List;

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
    public static final int COD_LOGIN = 3;
    public static final String TXT_LOGIN = "Login de usuario";
    public static final int COD_BAJA = 4;
    public static final String TXT_BAJA = "Baja de usuario";
    public static final int COD_BUSCA_NIF = 5;
    public static final String TXT_BUSCA_NIF = "Busca un usuario por su NIF";
    private static final int NOTA_MINIMA = 4;

    public static int menuPrincipal() {
        Menu m = new Menu();
        m.setTitle("MENU USUARIO");

        m.addElement(new MenuElement(COD_SALIR, TXT_SALIR));
        m.addElement(new MenuElement(COD_LISTAR, TXT_LISTAR));
        m.addElement(new MenuElement(COD_ALTA, TXT_ALTA));
        m.addElement(new MenuElement(COD_LOGIN, TXT_LOGIN));
        m.addElement(new MenuElement(COD_BAJA, TXT_BAJA));
        m.addElement(new MenuElement(COD_BUSCA_NIF, TXT_BUSCA_NIF));

        MenuElement e = m.showAndSelect();
        return e.getCodigo();
    }

    public static void listaUsuarios(List<UsuarioDto> lista) {
        for (UsuarioDto u : lista) {
            System.out.println(u.toString());
        }
    }

    public static String solicitaNombre() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el nombre de usuario: ");
        return sc.getString(3, 10);
    }

    public static String solicitaPasswordValida(String mensaje) {
        PasswordChecker pc = PasswordCheckerFactory.create();
        PasswordInput passwordInput = new PasswordInput();
        passwordInput.setQuestionText(mensaje)
                     .setMinLength(6)
                     .setMaxLength(20)
                     .setMinAcceptedQuality(4)
                     .setPasswordChecker(pc);
        return passwordInput.getValidatedPassword();
    }

    public static String solicitaPassword(String mensaje) {
        PasswordChecker pc = PasswordCheckerFactory.create();
        PasswordInput passwordInput = new PasswordInput();
        passwordInput.setQuestionText(mensaje);
        return passwordInput.getUnvalidatedPassword();
    }

    public static void muestraMsgOperacionOK() {
        System.out.println("La operación se realizó correctamente");
    }

    public static void muestraMsgUsuarioDuplicado() {
        System.out.println("Ya existe un usuario con ese nombre");
    }

    public static void muestraMsgUsuarioNoValido() {
        System.out.println("Los datos de usuario no son válidos");
    }

    public static void muestraMsgLoginOK() {
        System.out.println("Autenticación correcta de usuario");
    }

    public static void muestraMsgLoginError() {
        System.out.println("Autenticación incorrecta de usuario");
    }

    public static void muestraMsgUsuarioNoEncontrado() {
        System.out.println("El usuario no se encuentra");
    }

    public static boolean confirmaBajaUsuario() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Estás seguro de eliminar el usuario? (S/N): ");
        return sc.getStringOfStringArgs("S", "N").equals("S");
    }

    public static void muestraMsgOperacionError() {
        System.out.println("Se produjo un error al borrar al usuario");
    }

    public static String solicitaNif() {
        ScannerWrapper sc = new ScannerWrapper().setQuestionText("Introduce el NIF del usuario (formato 00000000A): ");
        return sc.getString(9, 9);
    }

    public static void muestraDatosUsuario(UsuarioDto usu) {
        System.out.println(usu);
    }

    public static void pausar() {
        ScannerWrapper sc = new ScannerWrapper();
        sc.pause();
    }
}
