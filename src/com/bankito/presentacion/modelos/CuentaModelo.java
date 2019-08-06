/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.modelos;

import com.bankito.dominio.Movimiento;
import com.bankito.servicio.dto.CuentaDto;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kike
 */
public final class CuentaModelo {

    private IntegerProperty idCuenta;
    private IntegerProperty numEntidad;
    private IntegerProperty numSucursal;
    private IntegerProperty numDigitoControl;
    private LongProperty numCuenta;
    private FloatProperty saldo;
    private ObjectProperty<Date> fechaCreacion;
    private ObservableList<Movimiento> listaMov;
    private StringProperty numCuentaConFormato;
    private IntegerProperty numMovimientos;
    private FloatProperty ingresosTotal, gastosTotal;
    
    private SituacionGlobalModelo global = null;

    public CuentaModelo() {
        idCuenta = new SimpleIntegerProperty();
        numEntidad = new SimpleIntegerProperty();
        numSucursal = new SimpleIntegerProperty();
        numDigitoControl = new SimpleIntegerProperty();
        numCuenta = new SimpleLongProperty();
        saldo = new SimpleFloatProperty();
        fechaCreacion = new SimpleObjectProperty();
        listaMov = FXCollections.observableArrayList();
        numCuentaConFormato = new SimpleStringProperty();
        numMovimientos = new SimpleIntegerProperty();
        ingresosTotal = new SimpleFloatProperty();
        gastosTotal = new SimpleFloatProperty();
    }

    public CuentaModelo(CuentaDto dto) {
        this();
        this.setIdCuenta(dto.getIdCuenta());
        this.setNumEntidad(dto.getNumEntidad());
        this.setNumSucursal(dto.getNumSucursal());
        this.setNumDigitoControl(dto.getNumDigitoControl());
        this.setNumCuenta(dto.getNumCuenta());
        this.setSaldo(dto.getSaldo());
        this.setFechaCreacion(dto.getFechaCreacion());
        this.setListaMov(dto.getListaMov());
        this.numCuentaConFormato.set(String.format("%04d %04d %02d %010d",
                dto.getNumEntidad(), dto.getNumSucursal(), dto.getNumDigitoControl(), dto.getNumCuenta()));
        recalcula();
    }
    
    public void recalcula() {
        //System.out.println("Llamada a cuentaModelo recalcula");
        this.numMovimientos.set(listaMov.size());
        
        float ingresos = 0;
        float gastos = 0;
        Iterator<Movimiento> itera = listaMov.iterator();
        while(itera.hasNext()) {
            Movimiento m = itera.next();
            if(m.getTipo() == Movimiento.TIPO_MOV_ENTRADA)
                ingresos = ingresos + m.getImporte();
            else
                gastos = gastos + m.getImporte();
        }
        
        this.ingresosTotal.set(ingresos);
        this.gastosTotal.set(gastos);
        
        if(global != null) {
            //System.out.println("Llamada a global recalcula");
            global.recalcula();
        }
        
    }
    
    public void setSituacionGlobalModelo(SituacionGlobalModelo situacion) {
        this.global = situacion;
    }

    public int getIdCuenta() {
        return idCuenta.get();
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta.set(idCuenta);
    }

    public int getNumEntidad() {
        return numEntidad.get();
    }

    public void setNumEntidad(int numEntidad) {
        this.numEntidad.set(numEntidad);
    }

    public int getNumSucursal() {
        return numSucursal.get();
    }

    public void setNumSucursal(int numSucursal) {
        this.numSucursal.set(numSucursal);
    }

    public int getNumDigitoControl() {
        return numDigitoControl.get();
    }

    public void setNumDigitoControl(int numDigitoControl) {
        this.numDigitoControl.set(numDigitoControl);
    }

    public long getNumCuenta() {
        return numCuenta.get();
    }

    public void setNumCuenta(long numCuenta) {
        this.numCuenta.set(numCuenta);
    }

    public float getSaldo() {
        return saldo.get();
    }

    public void setSaldo(float saldo) {
        this.saldo.set(saldo);
    }

    public Date getFechaCreacion() {
        return fechaCreacion.get();
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion.set(fechaCreacion);
    }

    public void setListaMov(List<Movimiento> listaMov) {
        this.listaMov.setAll(listaMov);
        this.numMovimientos.set(listaMov.size());
    }

    public String getNumCuentaConFormato() {
        return this.numCuentaConFormato.get();
    }
    
    public int getNumMovimientos() {
        return this.numMovimientos.get();
    }
    
    public float getIngresosTotal() {
        return this.ingresosTotal.get();
    }

    public float getGastosTotal() {
        return this.gastosTotal.get();
    }
    
    
    
    public StringProperty getNumCuentaConFormatoProperty() {
        return this.numCuentaConFormato;
    }
    
    public IntegerProperty getIdCuentaProperty() {
        return idCuenta;
    }

    public void setIdCuentaProperty(IntegerProperty idCuenta) {
        this.idCuenta = idCuenta;
    }
    
    public IntegerProperty getNumEntidadProperty() {
        return numEntidad;
    }    
    
    public void setNumEntidadProperty(IntegerProperty numEntidad) {
        this.numEntidad = numEntidad;
    }

    public IntegerProperty getNumSucursalProperty() {
        return numSucursal;
    }

    public void setNumSucursalProperty(IntegerProperty numSucursal) {
        this.numSucursal = numSucursal;
    }

    public IntegerProperty getNumDigitoControlProperty() {
        return numDigitoControl;
    }

    public void setNumDigitoControlProperty(IntegerProperty numDigitoControl) {
        this.numDigitoControl = numDigitoControl;
    }

    public LongProperty getNumCuentaProperty() {
        return numCuenta;
    }

    public void setNumCuentaProperty(LongProperty numCuenta) {
        this.numCuenta = numCuenta;
    }

    public FloatProperty getSaldoProperty() {
        return saldo;
    }

    public void setSaldoProperty(FloatProperty saldo) {
        this.saldo = saldo;
    }

    public ObjectProperty<Date> getFechaCreacionProperty() {
        return fechaCreacion;
    }

    public void setFechaCreacionProperty(ObjectProperty<Date> fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ObservableList<Movimiento> getListaMovProperty() {
        return listaMov;
    }

    public void setListaMovProperty(ObservableList<Movimiento> listaMov) {
        this.listaMov = listaMov;
    }
    
    public IntegerProperty getNumMovimientosProperty() {
        return this.numMovimientos;
    }

    public FloatProperty getIngresosTotalProperty() {
        return this.ingresosTotal;
    }

    public FloatProperty getGastosTotalProperty() {
        return this.gastosTotal;
    }
    
}
