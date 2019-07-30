/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.util;

import com.bankito.dominio.Cliente;
import com.bankito.dominio.Cuenta;
import com.bankito.dominio.Movimiento;
import com.bankito.dominio.Operacion;
import com.bankito.dominio.PerfilUsuario;
import com.bankito.dominio.Sesion;
import com.bankito.dominio.Usuario;
import com.bankito.persistencia.dto.ClienteEntidad;
import com.bankito.persistencia.dto.CuentaEntidad;
import com.bankito.persistencia.dto.MovimientoEntidad;
import com.bankito.persistencia.dto.OperacionEntidad;
import com.bankito.persistencia.dto.PerfilUsuarioEntidad;
import com.bankito.persistencia.dto.SesionEntidad;
import com.bankito.persistencia.dto.UsuarioEntidad;
import com.bankito.persistencia.exceptions.MovimientoEntidadDaoException;
import java.util.List;

/**
 *
 * @author Kike
 */
public class ObjectMapper {
    public static void copyProperties(Cliente orig, ClienteEntidad dest) {
        dest.setIdCliente(orig.getIdCliente());
        dest.setNombre(orig.getNombre());
        dest.setApellido1(orig.getApellido1());
        dest.setApellido2(orig.getApellido2());
        dest.setDireccionCompleta(orig.getDireccionCompleta());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setFechaModificacion(orig.getFechaModificacion());
        dest.setNif(orig.getNif());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
    }
    
    public static void copyProperties(ClienteEntidad orig, Cliente dest) {
        dest.setIdCliente(orig.getIdCliente());
        dest.setNombre(orig.getNombre());
        dest.setApellido1(orig.getApellido1());
        dest.setApellido2(orig.getApellido2());
        dest.setDireccionCompleta(orig.getDireccionCompleta());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setFechaModificacion(orig.getFechaModificacion());
        dest.setNif(orig.getNif());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
    }
    
    public static void copyProperties(ClienteEntidad[] orig, Cliente[] dest) {
        for(int i=0; i<orig.length; i++) {
            copyProperties(orig[i],dest[i]);
        }
    }
    
    public static void copyProperties(Cliente[] orig, ClienteEntidad[] dest) {
        for(int i=0; i<orig.length; i++) {
            copyProperties(orig[i],dest[i]);
        }
    }
    
    public static void copyProperties(ClienteEntidad[] orig, List<Cliente> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            Cliente cli = new Cliente();
            copyProperties(orig[i],cli);
            dest.add(cli);
        }
    }
    
    public static void copyProperties(Cliente[] orig, List<ClienteEntidad> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            ClienteEntidad cli = new ClienteEntidad();
            copyProperties(orig[i],cli);
            dest.add(cli);
        }
    }
    
    public static void copyProperties(Usuario orig, UsuarioEntidad dest) {
        dest.setIdUsuario(orig.getIdUsuario());
        dest.setNombre(orig.getNombre());
        dest.setPassword(orig.getPassword());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setFechaModificacion(orig.getFechaModificacion());
        dest.setPerfilUsuarioIdPerfil(orig.getPerfilUsuarioIdPerfil());
    }
    
    public static void copyProperties(UsuarioEntidad orig, Usuario dest) {
        dest.setIdUsuario(orig.getIdUsuario());
        dest.setNombre(orig.getNombre());
        dest.setPasswordNoEncoding(orig.getPassword());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setFechaModificacion(orig.getFechaModificacion());
        dest.setPerfilUsuarioIdPerfil(orig.getPerfilUsuarioIdPerfil());
    }
    
    public static void copyProperties(UsuarioEntidad[] orig, List<Usuario> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            Usuario cli = new Usuario();
            copyProperties(orig[i],cli);
            dest.add(cli);
        }
    }
    
    public static void copyProperties(Usuario[] orig, List<UsuarioEntidad> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            UsuarioEntidad cli = new UsuarioEntidad();
            copyProperties(orig[i],cli);
            dest.add(cli);
        }
    }
    
    public static void copyProperties(Cuenta orig, CuentaEntidad dest) {
        dest.setIdCuenta(orig.getIdCuenta());
        dest.setNumEntidad(orig.getNumEntidad());
        dest.setNumSucursal(orig.getNumSucursal());
        dest.setNumDigitoControl(orig.getNumDigitoControl());
        dest.setNumCuenta(orig.getNumCuenta());
        dest.setSaldo(orig.getSaldo());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
        dest.setFechaCreacion(orig.getFechaCreacion());
        
    }
    
    public static void copyProperties(CuentaEntidad orig, Cuenta dest) {
        dest.setIdCuenta(orig.getIdCuenta());
        dest.setNumEntidad(orig.getNumEntidad());
        dest.setNumSucursal(orig.getNumSucursal());
        dest.setNumDigitoControl(orig.getNumDigitoControl());
        dest.setNumCuenta(orig.getNumCuenta());
        dest.setSaldo(orig.getSaldo());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
        dest.setFechaCreacion(orig.getFechaCreacion());
    }
    
    public static void copyProperties(CuentaEntidad[] orig, List<Cuenta> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            Cuenta cue = new Cuenta(0);
            copyProperties(orig[i],cue);
            dest.add(cue);
        }
    }
    
    public static void copyProperties(Movimiento orig, MovimientoEntidad dest) {
        dest.setIdMovimiento(orig.getIdMovimiento());
        dest.setConcepto(orig.getConcepto());
        dest.setCuentaIdCuenta(orig.getCuentaIdCuenta());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setImporte(orig.getImporte());
        dest.setTipo(orig.getTipo());
        
    }
    
    public static void copyProperties(MovimientoEntidad orig, Movimiento dest) throws MovimientoEntidadDaoException {
        dest.setIdMovimiento(orig.getIdMovimiento());
        dest.setConcepto(orig.getConcepto());
        dest.setCuentaIdCuenta(orig.getCuentaIdCuenta());
        dest.setFechaCreacion(orig.getFechaCreacion());
        dest.setTipo(orig.getTipo());
        dest.setImporte(orig.getImporte());
    }
    
    public static void copyProperties(MovimientoEntidad[] orig, List<Movimiento> dest) throws MovimientoEntidadDaoException{
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            Movimiento mov = new Movimiento();
            copyProperties(orig[i],mov);
            dest.add(mov);
        }
    }

    public static void copyProperties(PerfilUsuarioEntidad orig, PerfilUsuario dest) {
        dest.setIdPerfilUsuario(orig.getIdPerfilUsuario());
        dest.setNombre(orig.getNombre());
        dest.setDescripcion(orig.getDescripcion());
        dest.setOperPermitidas(PerfilUsuario.findOperacionesPermitidas(dest));

    }

    public static void copyProperties(PerfilUsuario orig, PerfilUsuarioEntidad dest) {
        dest.setIdPerfilUsuario(orig.getIdPerfilUsuario());
        dest.setNombre(orig.getNombre());
        dest.setDescripcion(orig.getDescripcion());
    }

    public static void copyProperties(PerfilUsuarioEntidad[] orig, List<PerfilUsuario> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            PerfilUsuario perfil = new PerfilUsuario();
            copyProperties(orig[i],perfil);
            dest.add(perfil);
        }
    }

    public static void copyProperties(Operacion orig, OperacionEntidad dest) {
        dest.setIdOperacion(orig.getIdOperacion());
        dest.setNombre(orig.getNombre());
        dest.setNombreCorto(orig.getNombreCorto());
        dest.setDescripcion(orig.getDescripcion());
    }

    public static void copyProperties(OperacionEntidad orig, Operacion dest) {
        dest.setIdOperacion(orig.getIdOperacion());
        dest.setNombre(orig.getNombre());
        dest.setNombreCorto(orig.getNombreCorto());
        dest.setDescripcion(orig.getDescripcion());
    }

    public static void copyProperties(OperacionEntidad[] orig, List<Operacion> dest) {
        dest.clear();
        for(int i=0; i<orig.length; i++) {
            Operacion perfil = new Operacion();
            copyProperties(orig[i],perfil);
            dest.add(perfil);
        }
    }

    public static void copyProperties(Sesion orig, SesionEntidad dest) {
        dest.setIdSesion(orig.getIdSesion());
        dest.setAccion(orig.getAccion());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
        dest.setFechaCreacion(orig.getFechaCreacion());
    }

    public static void copyProperties(SesionEntidad orig, Sesion dest) {
        dest.setIdSesion(orig.getIdSesion());
        dest.setAccion(orig.getAccion());
        dest.setUsuarioIdUsuario(orig.getUsuarioIdUsuario());
        dest.setFechaCreacion(orig.getFechaCreacion());
    }

}
