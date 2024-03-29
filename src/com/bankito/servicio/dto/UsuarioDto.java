/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio.dto;

import com.bankito.dominio.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Kike
 */
public class UsuarioDto {

    public static final UsuarioDto NOT_FOUND = new UsuarioDto("vacio");
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    protected final int idUsuario;
    protected final String nombre;
    protected final Date fechaCreacion;
    protected final Date fechaModificacion;

    public UsuarioDto(Usuario usu) {
        idUsuario = usu.getIdUsuario();
        nombre = usu.getNombre();
        fechaCreacion = new Date(usu.getFechaCreacion().getTime());
        fechaModificacion = new Date(usu.getFechaModificacion().getTime());
    }

    public UsuarioDto(String nombre) {
        idUsuario = 0;
        this.nombre = Objects.requireNonNull(nombre, "El nombre de usuario no puede ser nulo");
        fechaCreacion = new Date(0);
        fechaModificacion = new Date(0);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("idUsuario=" + idUsuario);
        ret.append(", nombre=" + nombre);
        ret.append(", fechaCreacion=" + fechaCreacion);
        ret.append(", fechaModificacion=" + fechaModificacion);
        return ret.toString();
    }

    public String toJsonString() {
        StringBuffer ret = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        ret.append("Cod. Usuario: " + idUsuario);
        ret.append("\nNombre: " + nombre);
        ret.append("\nFecha alta: " + format.format(fechaCreacion)+"\n");
        return ret.toString();
    }
    
}
