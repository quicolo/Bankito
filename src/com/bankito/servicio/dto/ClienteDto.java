/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio.dto;

import com.bankito.dominio.Cliente;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Kike
 */
public class ClienteDto {

    public static final ClienteDto NOT_FOUND = new ClienteDto("vacio", "vacio", "vacio", "vacio", "vacio", -1);
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    protected int idCliente = 0;
    protected String nombre = "";
    protected String apellido1 = "";
    protected String apellido2 = "";
    protected String nif = "";
    protected String direccionCompleta = "";
    protected Date fechaCreacion;
    protected Date fechaModificacion;
    protected int usuarioIdUsuario = 0;

    public ClienteDto(Cliente cli) {
        this.idCliente = cli.getIdCliente();
        this.nombre = cli.getNombre();
        this.apellido1 = cli.getApellido1();
        this.apellido2 = cli.getApellido2();
        this.nif = cli.getNif();
        this.direccionCompleta = cli.getDireccionCompleta();
        this.fechaCreacion = new Date(cli.getFechaCreacion().getTime());
        this.fechaModificacion = new Date(cli.getFechaModificacion().getTime());
        this.usuarioIdUsuario = cli.getUsuarioIdUsuario();
    }

    public ClienteDto(String nombre, String apellido1, String apellido2, String nif, String direc, int idUsuario) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del cliente no puede ser nulo");
        this.apellido1 = Objects.requireNonNull(apellido1, "El apellido1 del cliente no puede ser nulo");
        this.apellido2 = Objects.requireNonNull(apellido2, "El apellido2 del cliente no puede ser nulo");;
        this.nif = Objects.requireNonNull(nif, "El NIF del cliente no puede ser nulo");
        this.direccionCompleta = Objects.requireNonNull(direc, "La dirección del cliente no puede ser nula");
        this.fechaCreacion = new Date(0);
        this.fechaModificacion = new Date(0);
        this.usuarioIdUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getNif() {
        return nif;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public Date getFechaCreacion() {
        return new Date(fechaCreacion.getTime());
    }

    public Date getFechaModificacion() {
        return new Date(fechaModificacion.getTime());
    }

    public int getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        StringBuffer ret = new StringBuffer();
        ret.append("idCliente=" + idCliente);
        ret.append(", nombre=" + nombre);
        ret.append(", apellido1=" + apellido1);
        ret.append(", apellido2=" + apellido2);
        ret.append(", nif=" + nif);
        ret.append(", direccionCompleta=" + direccionCompleta);
        ret.append(", fechaCreacion=" + format.format(fechaCreacion));
        ret.append(", fechaModificacion=" + format.format(fechaModificacion));
        ret.append(", usuarioIdUsuario=" + usuarioIdUsuario);
        return ret.toString();
    }
    
    public String toJsonString() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        StringBuffer ret = new StringBuffer();
        ret.append("Cod. Cliente: " + idCliente);
        ret.append("\nNombre: " + nombre);
        ret.append("\nApellido 1: " + apellido1);
        ret.append("\nApellido 2: " + apellido2);
        ret.append("\nNIF: " + nif);
        ret.append("\nDireccion postal: " + direccionCompleta);
        ret.append("\nFecha alta: " + format.format(fechaCreacion)+"\n");
        return ret.toString();
    }
}
