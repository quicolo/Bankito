package com.bankito.presentacion.modelos;

import com.bankito.aplicacion.ClienteViewController;
import java.util.Iterator;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
* <h1>SituacionGlobalModelo</h1>
* Esta clase agrupa todos los campos que podrán ser consultados por la vista
* principal de los usuarios con perfil 'Cliente'.
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
* la propiedad 'private IntegerProperty numeroCuentas':<br>
* - public int getNumeroCuentas() --&gt; Accede al valor que envuelve a la Property<br>
* - public void setNumeroCuentas(int numCuentas) --&gt; Establece el valor<br>
* - public IntegerProperty getNumeroCuentasProperty() --&gt; devuelve la Property<br>
* - public void setNumeroCuentasProperty(IntegerProperty numCuenta) --&gt; establece la Property<br>
* <p>
* Además, esto modelo contiene una referencia a un objeto ClienteViewController
* que le permitirá llamar al método showSituacionGlobal() del controlador para
* que se actualice la vista tras una actualización de datos en el modelo.
* <p>
* Por último, presenta un método recalcula() que puede ser invocado cada vez
* que haya habido un cambio en el modelo y que recalcula el valor de aquellos 
* campos cuyo valor depende de otros. Este método puede ser llamado desde los
* objetos contenidos en la 'ObservableList &lt;CuentaModelo&gt; listaCuentas' cuando
* éstos hayan sufrido algún cambio, de este modo, se notifica a un objeto de la
* clase SituacionGlobalModelo que ha habido cambios.
* 
* @author  Enrique Royo Sánchez
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
