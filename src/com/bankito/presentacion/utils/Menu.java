/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Kike
 */
public class Menu {

    private String title = "MENU";
    private String subtittle = "";
    private String optionMsg = "Selecciona una opción: ";
    private String optionErrorMsg = "Opcion incorrecta ";
    private List<MenuElement> lista = new ArrayList();

    public Menu() {
    }

    public Menu setTitle(String title) {
        this.title = Objects.requireNonNull(title, "El título del menú no puede ser nulo");
        return this;
    }

    public Menu setSubtitle(String subTitle) {
        this.title = Objects.requireNonNull(subTitle, "El título del menú no puede ser nulo");
        return this;
    }

    public Menu setOptionMsg(String optionText) {
        this.optionMsg = Objects.requireNonNull(optionText, "El título del menú no puede ser nulo");
        return this;
    }

    public Menu setInvalidOptionMsg(String optionErrorMsg) {
        this.optionErrorMsg = Objects.requireNonNull(optionErrorMsg, "El título del menú no puede ser nulo");
        return this;
    }

    public Menu addElement(MenuElement e) {
        lista.add(e);
        return this;
    }

    public MenuElement showAndSelect() {
        System.out.println("");
        System.out.println(getStringDecorated(title));
        if (!subtittle.equals("")) {
            System.out.println(subtittle);
        }

        for (int i = 0; i < lista.size(); i++) {
            MenuElement e = lista.get(i);
            System.out.println(i + " - " + e.getTexto());
        }
        System.out.println();
        ScannerWrapper sc = new ScannerWrapper().setQuestionText(optionMsg)
                .setErrorText(optionErrorMsg).setOutOfBoundsText("Opción fuera del rango permitido ");
        int opcion = sc.getInt(0, lista.size() - 1);
        
        System.out.println("");
        System.out.println(getStringDecorated(lista.get(opcion).getTexto()));
        
        return lista.get(opcion);
    }

    private String getStringDecorated(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length()+4; i++) {
            sb.append('*');
        }
        sb.append("\n* ");
        sb.append(s);
        sb.append(" *\n");
        for (int i = 0; i < s.length()+4; i++) {
            sb.append('*');
        }
        sb.append("\n");
        return sb.toString();
    }

}
