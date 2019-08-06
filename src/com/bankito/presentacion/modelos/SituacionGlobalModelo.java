/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.presentacion.modelos;

import com.bankito.presentacion.modelos.CuentaModelo;
import com.bankito.aplicacion.ClienteViewController;
import java.util.Iterator;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kike
 */
public class SituacionGlobalModelo {

    private final ClienteViewController controlador;
    private ObservableList<CuentaModelo> listaCuentas;
    private IntegerProperty numeroCuentas, numTotalMovimientos;
    private FloatProperty saldoTotal, ingresosTotal, gastosTotal;

    public SituacionGlobalModelo(ClienteViewController controlador) {
        listaCuentas = FXCollections.observableArrayList();
        numeroCuentas = new SimpleIntegerProperty();
        numTotalMovimientos = new SimpleIntegerProperty();
        saldoTotal = new SimpleFloatProperty();
        ingresosTotal = new SimpleFloatProperty();
        gastosTotal = new SimpleFloatProperty();
        this.controlador = controlador;
    }

    public void recalcula() {
        this.numeroCuentas.set(listaCuentas.size());
        
        int numMovim = 0;
        float saldo = 0;
        float ingresos = 0;
        float gastos = 0;
        Iterator<CuentaModelo> itera = listaCuentas.iterator();
        while(itera.hasNext()) {
            CuentaModelo c = itera.next();
            numMovim = numMovim + c.getNumMovimientos();
            saldo = saldo + c.getSaldo();
            ingresos = ingresos + c.getIngresosTotal();
            gastos = gastos + c.getGastosTotal();
        }
        this.numTotalMovimientos.set(numMovim);
        this.saldoTotal.set(saldo);
        this.ingresosTotal.set(ingresos);
        this.gastosTotal.set(gastos);
        
        controlador.showSituacionGlobal();
    }
    
    public ObservableList<CuentaModelo> getListaCuentasProperty() {
        return listaCuentas;
    }

    public void setListaCuentasProperty(ObservableList<CuentaModelo> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public IntegerProperty getNumeroCuentasProperty() {
        return numeroCuentas;
    }

    public void setNumeroCuentasProperty(IntegerProperty numeroCuentas) {
        this.numeroCuentas = numeroCuentas;
    }

    public FloatProperty getSaldoTotalProperty() {
        return saldoTotal;
    }

    public void setSaldoTotalProperty(FloatProperty saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public FloatProperty getIngresosTotalProperty() {
        return ingresosTotal;
    }

    public void setIngresosTotalProperty(FloatProperty ingresosTotal) {
        this.ingresosTotal = ingresosTotal;
    }

    public FloatProperty getGastosTotalProperty() {
        return gastosTotal;
    }

    public void setGastosTotalProperty(FloatProperty gastosTotal) {
        this.gastosTotal = gastosTotal;
    }

    public void setListaCuentas(ObservableList<CuentaModelo> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public int getNumeroCuentas() {
        return numeroCuentas.get();
    }

    public void setNumeroCuentas(IntegerProperty numeroCuentas) {
        this.numeroCuentas = numeroCuentas;
    }

    public float getSaldoTotal() {
        return saldoTotal.get();
    }

    public void setSaldoTotal(FloatProperty saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public FloatProperty getIngresosTotal() {
        return ingresosTotal;
    }

    public void setIngresosTotal(FloatProperty ingresosTotal) {
        this.ingresosTotal = ingresosTotal;
    }

    public FloatProperty getGastosTotal() {
        return gastosTotal;
    }

    public void setGastosTotal(FloatProperty gastosTotal) {
        this.gastosTotal = gastosTotal;
    }

}
