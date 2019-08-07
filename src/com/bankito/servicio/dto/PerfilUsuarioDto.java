/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio.dto;

import com.bankito.dominio.Operacion;
import com.bankito.dominio.PerfilUsuario;
import java.util.List;
import java.util.Objects;

/**
 * <h1>PerfilUsuarioDto</h1>
 * Esta clase funciona como DTO (Data Transfer Object) dentro de la capa de 
 * servicio de la aplicación. Los objetos DTO simplemente transportan datos de 
 * una capa a otra ofreciendo normalmente los getters y setters. Normalmente 
 * también presentan varias versiones de los constructores por versatilidad. 
 * Nos ayudan a ocultar la complejidad de los objetos de la capa de Dominio
 * utilizados en la capa de servicio.
 * <p>
 * Presenta una referencia a un objeto de la clase, NOT_FOUND, que sirve para 
 * indicar que no se encontraron datos en una búsqueda o interacción con la
 * capa de servicio. De este modo, nos evitamos utilizar valores nulos.
 *
 * @author Enrique Royo Sánchez
 */
public class PerfilUsuarioDto {
    public static final PerfilUsuarioDto NOT_FOUND = new PerfilUsuarioDto("vacio", "vacio");
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    private int idPerfilUsuario;
    private String nombre;
    private String descripcion;
    private List<Operacion> operPermitidas;
    
    public PerfilUsuarioDto(String nombre, String descripcion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del perfil de usuario no puede ser nulo");
        this.descripcion = Objects.requireNonNull(descripcion, "La descripcion del perfil de usuario no puede ser nula");
    }
    
    public PerfilUsuarioDto(PerfilUsuario per) {
        this.idPerfilUsuario = per.getIdPerfilUsuario();
        this.nombre = Objects.requireNonNull(per.getNombre(), "El nombre del perfil de usuario no puede ser nulo");
        this.descripcion = Objects.requireNonNull(per.getDescripcion(), "La descripcion del perfil de usuario no puede ser nula");
    }

    public int getIdPerfilUsuario() {
        return idPerfilUsuario;
    }

    public void setIdPerfilUsuario(int idPerfilUsuario) {
        this.idPerfilUsuario = idPerfilUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setOperPermitidas(List<Operacion> operPermitidas) {
        this.operPermitidas = operPermitidas;
    }

    public List<Operacion> getOperPermitidas() {
        return operPermitidas;
    }
    
    
    
}
