/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio.dto;

import com.bankito.dominio.Cuenta;
import com.bankito.dominio.Movimiento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kike
 */
public class CuentaDto {
    
    public static final CuentaDto NOT_FOUND = new CuentaDto(0,0,0,0,0);
    protected final int idCuenta;
    protected final int numEntidad;
    protected final int numSucursal;
    protected final int numDigitoControl;
    protected final long numCuenta;
    protected final float saldo;
    protected final Date fechaCreacion;
    protected final int usuarioIdUsuario;
    protected final List<Movimiento> listaMov;
    
    public CuentaDto(int numEntidad, int numSucursal, int numDigitoControl, long numCuenta, int idUsuario) {
        this.idCuenta = 0;
        this.numEntidad = numEntidad;
        this.numSucursal = numSucursal;
        this.numDigitoControl = numDigitoControl;
        this.numCuenta = numCuenta;
        this.saldo = 0;
        this.usuarioIdUsuario = idUsuario;
        this.listaMov = new ArrayList<Movimiento>();
        this.fechaCreacion = Cuenta.NULL_DATE;
    }
    
    public CuentaDto(Cuenta c) {
        this.idCuenta = c.getIdCuenta();
        this.numEntidad = c.getNumEntidad();
        this.numSucursal = c.getNumSucursal();
        this.numDigitoControl = c.getNumDigitoControl();
        this.numCuenta = c.getNumCuenta();
        this.saldo = c.getSaldo();
        this.usuarioIdUsuario = c.getUsuarioIdUsuario();
        this.listaMov = c.getMovimientos();
        this.fechaCreacion = new Date(c.getFechaCreacion().getTime());
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public int getNumEntidad() {
        return numEntidad;
    }

    public int getNumSucursal() {
        return numSucursal;
    }

    public int getNumDigitoControl() {
        return numDigitoControl;
    }

    public long getNumCuenta() {
        return numCuenta;
    }

    public float getSaldo() {
        return saldo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public int getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public List<Movimiento> getListaMov() {
        return listaMov;
    }
    
    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("idCuenta=" + idCuenta);
        ret.append(", numEntidad=" + numEntidad);
        ret.append(", numSucursal=" + numSucursal);
        ret.append(", numDigitoControl=" + numDigitoControl);
        ret.append(", numCuenta=" + numCuenta);
        ret.append(", saldo=" + saldo);
        ret.append(", fechaCreacion=" + fechaCreacion);
        ret.append(", usuarioIdUsuario=" + usuarioIdUsuario);
        return ret.toString();
    }
    
    public String toJsonString() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        StringBuffer ret = new StringBuffer();
        ret.append("Cod. Cuenta: " + idCuenta);
        ret.append("\nEntidad: " + numEntidad);
        ret.append("\nSucursal: " + numSucursal);
        ret.append("\nDigitoControl: " + numDigitoControl);
        ret.append("\nNum. Cuenta: " + numCuenta);
        ret.append("\nSaldo: " + saldo);
        ret.append("\nFecha alta: " + format.format(fechaCreacion)+"\n");
        return ret.toString();
    }
}
