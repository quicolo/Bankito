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
* <h1>CuentaModelo</h1>
* Esta clase agrupa todos los campos que podrán ser consultados por las distintas
* vistas de la aplicación que necesiten visualizar datos de una cuenta correinte. 
* Es una clase que las vistas usan como Modelo, siguiendo el patrón de diseño MVC.
* <p>
* La mayoría de las propiedades de la clase provienen del paquete javafx.beans.property
* y son clases del tipo XXXProperty. Este tipo de datos envuelve (wrap) un tipo de
* datos básico (int, float...) o una lista (List) aportándole la funcionalidad
* de ser objetos 'Observables'. Esto significa que puede haber otros objetos que
* se suscriban o escuchen los cambios que se produzcan en estos objetos para que
* así puedan actualizarse de forma automática. Este mecanismo se utiliza para que
* la vista permanezca sincronizada a los valores del modelo, así un cambio en éste
* último provoca de forma automática la actualización de la vista vinculada.
* <p>
* Las clases XXXProperty son javabeans que exigen que los campos de la clase deban
* ser acompañados de un conjunto de métodos que cumplan con una convención de 
* nombrado. De esta forma, se garantiza que dichos campos puedan ser accedidos
* por otras clases que quieran interactuar con este tipo de javabeans.
* <p>
* Sirva como ejemplo el siguiente conjunto de métodos que permite el acceso a 
* la propiedad 'private IntegerProperty idCuenta':<br>
* - public int getIdCuenta() --&gt; Accede al valor que envuelve a la Property<br>
* - public void setIdCuenta(int idCuenta) --&gt; Establece el valor<br>
* - public IntegerProperty getIdCuentaProperty() --&gt; devuelve la Property<br>
* - public void setIdCuentaProperty(IntegerProperty idCuenta) --&gt; establece la Property<br>
* <p>
* Además, esto modelo contiene una referencia a un objeto SituacionGlobalModelo
* que contendrá una lista observable de objetos CuentaModelo. De este modo, 
* cuando cambie el valor de una propiedad de un objeto de CuentaModelo, este
* podrá notificar a su contenedor que ha habido cambios.
* <p>
* Presenta dos versiones de constructor, una de ellas tomando como parámetro
* un objeto del tipo CuentaDto tal y como lo devuelve la capa de servicio.
* <p>
* Por último, presenta un método recalcula() que puede ser invocado cada vez
* que haya habido un cambio en el modelo y que recalcula el valor de aquellos 
* campos cuyo valor depende de otros. Además, si el objeto CuentaModelo tiene
* una referencia no nula a un objeto del tipo SituacionGlobalModelo, entonces
* el método recalcula llamará al método recalcula de este objeto para que se
* propaguen los cambios.
* 
* @author  Enrique Royo Sánchez
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
            if(m.getTipo().equals(Movimiento.TIPO_MOV_ENTRADA))
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
    
    // getters y setters de values
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
    
    
    // getters y setters de Properties
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
