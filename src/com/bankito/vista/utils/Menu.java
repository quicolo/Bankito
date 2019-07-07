/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.vista.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Kike
 */
public class Menu {
    private String titulo="";
    private String subtitulo="";
    private String textoOpcion="";
    private List<MenuElement> lista = new ArrayList();

    public Menu(String titulo, String subtitulo, String textoOpcion) {
        this.titulo = Objects.requireNonNull(titulo, "El título del menú no puede ser nulo");
        this.subtitulo = Objects.requireNonNull(subtitulo, "El subtítulo del menú no puede ser nulo");
        this.textoOpcion = Objects.requireNonNull(textoOpcion, "El texto de elegir opción del menú no puede ser nulo");
    }
    
    public void addElement(MenuElement e) {
        lista.add(e);
    }
    
    public MenuElement showAndSelect() {
        System.out.println(titulo);
        System.out.println();
        if(!subtitulo.equals(""))
            System.out.println(subtitulo);
        
        for(int i=0; i < lista.size();i++) {
            MenuElement e = lista.get(i);
            System.out.println(i+" - "+e.getTexto());
        }
        System.out.println();
        System.out.print(textoOpcion+": ");
        ScanIntegerInRange scan = new ScanIntegerInRange(0, lista.size());
        int opcion = scan.read();
        return lista.get(opcion);
    }

}
