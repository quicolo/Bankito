/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.CuentaDuplicadaException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.dominio.exceptions.MovimientoNoValidoException;
import com.bankito.dominio.exceptions.TransferenciaNoValidaException;
import com.bankito.persistencia.exceptions.CuentaEntidadDaoException;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
* <h1>Transferencia</h1>
* Esta clase representa la funcionalidad de un objeto Transferencia de la capa de dominio.
* La transferencia contiene todos los datos para realizar dicha operación.
* <p>
* Un objeto de esta clase presenta el siguiente comportamiento:<br>
* - isValid: es capaz de analizar si las cuentas origen y destino son válidas y si el resto de parámetros también lo son.<br>
* - equals: es capaz de compararse con otro objeto para ver si son iguales<br>
* - toString: compone una cadena de texto con sus características<br>
* - getters: obtienen el valor de las propiedades<br>
* - setters: establecen el valor de la propiedades<br>
* <p>
* Esta clase es null-safe, es decir, evita a toda costa que se pueda hacer un manejo
* de sus instancias de forma que alguno de los campos pueda contener un null 
* indeseado.
*
* @author  Enrique Royo Sánchez
*/
public class Transferencia {
    private Cuenta origen, destino;
    private float importe;
    private String concepto;

    public Transferencia(Cuenta origen, Cuenta destino, double importe, String concepto) {
        this.origen = Objects.requireNonNull(origen,"La cuenta de origen de la transferencia no puede ser nula");
        this.destino = Objects.requireNonNull(destino,"La cuenta de destino de la transferencia no puede ser nula");
        this.importe = (float) importe;
        this.concepto = Objects.requireNonNull(concepto,"El concepto de la transferencia no puede ser nulo");
    }

    public boolean isValid() {
        if (importe <= 0 || (concepto == null)? true:concepto.equals("") || !origen.isValid() || !destino.isValid() || origen.getSaldo()<this.importe)
            return false;
        else
            return true;
    }
    
    public void execute() throws CuentaNoValidaException, CuentaDuplicadaException, MovimientoNoValidoException, TransferenciaNoValidaException {
        if (isValid() == false)
            throw new TransferenciaNoValidaException("La transferencia no es válida y no se ejecutará");
        
        Connection conn = null;
    
        Movimiento mOrigen = new Movimiento(this.concepto, Movimiento.TIPO_MOV_SALIDA, this.importe);
        Movimiento mDestino = new Movimiento(this.concepto, Movimiento.TIPO_MOV_ENTRADA, this.importe);
        
        try {
            conn = ResourceManager.getConnection();
            conn.setAutoCommit(false);
            origen.setConnection(conn);
            destino.setConnection(conn);
            origen.addMovimiento(mOrigen);
            destino.addMovimiento(mDestino);
            origen.save();
            destino.save();
            conn.commit();
        } catch (DaoException ex) {
            try {
                if (conn != null)
                    conn.rollback();
                throw new DaoException("Error al ejecutar la transacción "+this.toString()+" -> Deshacemos los cambios con un rollback"+ex.getMessage());
            } 
            catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al ejecutar la transacción "+this.toString()+" "+ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new CuentaEntidadDaoException("Error al conectar con la base de datos"+ex.getMessage());
        }
        finally {
            ResourceManager.close(conn);
            origen.resetConnection();
            destino.resetConnection();
        }
        
    }

    public Cuenta getOrigen() {
        return origen;
    }

    public void setOrigen(Cuenta origen) {
        this.origen = Objects.requireNonNull(origen,"La cuenta de origen de la transferencia no puede ser nula");
    }

    public Cuenta getDestino() {
        return destino;
    }

    public void setDestino(Cuenta destino) {
        this.destino = Objects.requireNonNull(destino,"La cuenta de destino de la transferencia no puede ser nula");
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        if (importe > 0.0f)
            this.importe = importe;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = Objects.requireNonNull(concepto,"El concepto de la transferencia no puede ser nulo");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.origen);
        hash = 37 * hash + Objects.hashCode(this.destino);
        hash = 37 * hash + Float.floatToIntBits(this.importe);
        hash = 37 * hash + Objects.hashCode(this.concepto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transferencia other = (Transferencia) obj;
        if (Float.floatToIntBits(this.importe) != Float.floatToIntBits(other.importe)) {
            return false;
        }
        if (!Objects.equals(this.concepto, other.concepto)) {
            return false;
        }
        if (!Objects.equals(this.origen, other.origen)) {
            return false;
        }
        if (!Objects.equals(this.destino, other.destino)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transferencia{" + "origen=" + origen + ", destino=" + destino + ", importe=" + importe + ", concepto=" + concepto + '}';
    }
    

}
