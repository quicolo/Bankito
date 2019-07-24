package com.bankito.dominio;

import com.bankito.util.ObjectMapper;
import com.bankito.dominio.exceptions.MovimientoNoValidoException;
import com.bankito.persistencia.dao.*;
import com.bankito.persistencia.dto.MovimientoEntidad;
import com.bankito.persistencia.dto.MovimientoEntidadPk;
import com.bankito.persistencia.factory.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
* <h1>Movimiento</h1>
* Esta clase representa la funcionalidad de un objeto Movimiento de la capa de dominio.
* El movimiento siempre debe pertenecer a una cuenta y siempre estará en función de las
* necesidades de ésta.
* <p>
* Un objeto de esta clase presenta el siguiente comportamiento:<br>
* - save: es capaz de salvar (insertar) un movimiento en la la BD. Recibe un objeto conexión para realizarlo transaccionalmente<br>
* - delete: es capaz de borrar su registro asociado en la BD. Recibe un objeto conexión para realizarlo transaccionalmente<br>
* - isValid: es capaz de analizar si el objeto y sus movimientos asociados están en un estado válido<br>
* - equals: es capaz de compararse con otro objeto para ver si son iguales<br>
* - toString: compone una cadena de texto con sus características<br>
* - getters: obtienen el valor de las propiedades<br>
* - setters: establecen el valor de la propiedades<br>
* <p>
* * Esta clase es null-safe, es decir, evita a toda costa que se pueda hacer un manejo
* de sus instancias de forma que alguno de los campos pueda contener un null 
* indeseado.
* <p>
* Posee un campo público estático final llamado NULL_DATE que servirá
* para inicializar los campos Date a un valor por defecto que es el 01/01/1970 
* a las 00:00:00 horas.
* <p>
* Además las clase define dos cadenas estáticas del siguiente modo:<br>
* - String TIPO_MOV_ENTRADA = "Entrada"
* - String TIPO_MOV_SALIDA = "Salida";
* <p>
* Esta clase presenta los siguientes comportamientos como métodos estáticos:<br>
* - findAllByIdCuenta: recupera todos los movimientos de la BD por su IdCuenta<br>
* <p>
* @author  Enrique Royo Sánchez
*/
public class Movimiento implements Serializable
{
	public static final String TIPO_MOV_ENTRADA = "Entrada";
        public static final String TIPO_MOV_SALIDA = "Salida";
        public static final Date NULL_DATE = new Date(0);
        
        private int idMovimiento;
	private String concepto;
	private String tipo;
	private float importe;
	private Date fechaCreacion;
	private int cuentaIdCuenta;
        

	/**
	 * Method 'MovimientoEntidad'
	 * 
	 */
	public Movimiento()
	{
            concepto="";
            tipo="";
            fechaCreacion = Movimiento.NULL_DATE;
	}

        public Movimiento(String concepto, String tipo, float importe) {
            this.concepto = Objects.requireNonNull(concepto, "El concepto del movimiento no puede ser nulo");
            this.tipo = Objects.requireNonNull(tipo, "El tipo de movimiento no puede ser nulo");
            if(importe >= 0.0f)
                this.importe = importe;
            this.fechaCreacion = Movimiento.NULL_DATE;
        }

        public Movimiento(String concepto, String tipo, double importe) {
            this(concepto, tipo, (float) importe);
        }
        
        public boolean isValid() {
            if (!(this.tipo.equals(TIPO_MOV_ENTRADA) || this.tipo.equals(TIPO_MOV_SALIDA))) 
                return false;
            if (this.importe <= 0.0 || cuentaIdCuenta == 0)
                return false;
            else
                return true;
        }
        

        public void save(Connection conn) throws MovimientoNoValidoException {
            if(this.isValid()==false)
                throw new MovimientoNoValidoException("El movimiento no es válido y no se salvó");
            
            MovimientoEntidadDao dao = MovimientoEntidadDaoFactory.create(conn);
            
            // Solo salvamos los movimientos nuevos
            if(idMovimiento == 0) 
            {
                this.fechaCreacion = new Date();
                MovimientoEntidad mov = new MovimientoEntidad();
                ObjectMapper.copyProperties(this, mov);
                MovimientoEntidadPk pk = dao.insert(mov);
                this.idMovimiento = pk.getIdMovimiento();
            }
            else  // Entendemos que no se deben actualizar movimientos que ya estaban grabados previamente
                ;
        }
        
        public void delete(Connection conn) throws MovimientoNoValidoException {
            if(this.isValid()==false)
                throw new MovimientoNoValidoException("El movimiento no es válido y no se borró");
            
            MovimientoEntidadDao movDao = MovimientoEntidadDaoFactory.create(conn);
            MovimientoEntidad movEnt = new MovimientoEntidad();
            ObjectMapper.copyProperties(this, movEnt);
            movDao.delete(movEnt.createPk());
        }
        
        public static List<Movimiento> findAllByIdCuenta(int idCuenta) {
            MovimientoEntidadDao dao = MovimientoEntidadDaoFactory.create();
            MovimientoEntidad[] listaMovEnt = dao.findByCuenta(idCuenta);
            List<Movimiento> listaMov = new ArrayList();
            ObjectMapper.copyProperties(listaMovEnt, listaMov);
            return listaMov;
        }
        
        
	/**
	 * Method 'getIdMovimiento'
	 * 
	 * @return int
	 */
	public int getIdMovimiento()
	{
		return idMovimiento;
	}

	/**
	 * Method 'setIdMovimiento'
	 * 
	 * @param idMovimiento
	 */
	public void setIdMovimiento(int idMovimiento)
	{
		this.idMovimiento = idMovimiento;
	}

	/**
	 * Method 'getConcepto'
	 * 
	 * @return String devuelve la copia del campo concepto
	 */
	public String getConcepto()
	{
            return concepto;
	}

	/**
	 * Method 'setConcepto'
	 * 
	 * @param concepto
	 */
	public void setConcepto(String concepto)
	{
            this.concepto = Objects.requireNonNull(concepto, "El concepto del movimiento no puede ser nulo");
	}

	/**
	 * Method 'getTipo'
	 * 
	 * @return String devuelve un duplicado del campo tipo
	 */
	public String getTipo()
	{
            return tipo;
	}

	/**
	 * Method 'setTipo'
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo)
	{
            this.tipo = Objects.requireNonNull(tipo, "El tipo del movimiento no puede ser nulo");
	}

	/**
	 * Method 'getImporte'
	 * 
	 * @return float
	 */
	public float getImporte()
	{
		return importe;
	}

	/**
	 * Method 'setImporte'
	 * 
	 * @param importe
	 */
	public void setImporte(float importe)
	{
            if(importe > 0)
                this.importe = importe;
	}

	/**
	 * Method 'getFechaCreacion'
	 * 
	 * @return Date devuelve un duplicado de la fecha de creación
	 */
	public Date getFechaCreacion()
	{
            return new Date(fechaCreacion.getTime());
	}

	/**
	 * Method 'setFechaCreacion'
	 * 
	 * @param fechaCreacion
	 */
	public void setFechaCreacion(Date fechaCreacion)
	{
            this.fechaCreacion = Objects.requireNonNull(fechaCreacion,"La fecha de creación del movimiento no puede ser nula");
	}

	/**
	 * Method 'getCuentaIdCuenta'
	 * 
	 * @return int
	 */
	public int getCuentaIdCuenta()
	{
		return cuentaIdCuenta;
	}

	/**
	 * Method 'setCuentaIdCuenta'
	 * 
	 * @param cuentaIdCuenta
	 */
	public void setCuentaIdCuenta(int cuentaIdCuenta)
	{
		this.cuentaIdCuenta = cuentaIdCuenta;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof Movimiento)) {
			return false;
		}
		
		final Movimiento _cast = (Movimiento) _other;
		if (idMovimiento != _cast.idMovimiento) {
			return false;
		}
		
		if (concepto == null ? _cast.concepto != concepto : !concepto.equals( _cast.concepto )) {
			return false;
		}
		
		if (tipo == null ? _cast.tipo != tipo : !tipo.equals( _cast.tipo )) {
			return false;
		}
		
		if (importe != _cast.importe) {
			return false;
		}
		
		if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals( _cast.fechaCreacion )) {
			return false;
		}
		
		if (cuentaIdCuenta != _cast.cuentaIdCuenta) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		_hashCode = 29 * _hashCode + idMovimiento;
		if (concepto != null) {
			_hashCode = 29 * _hashCode + concepto.hashCode();
		}
		
		if (tipo != null) {
			_hashCode = 29 * _hashCode + tipo.hashCode();
		}
		
		_hashCode = 29 * _hashCode + Float.floatToIntBits(importe);
		if (fechaCreacion != null) {
			_hashCode = 29 * _hashCode + fechaCreacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + cuentaIdCuenta;
		return _hashCode;
	}

        /**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            StringBuffer ret = new StringBuffer();
            ret.append( "Cod. Mov: " + idMovimiento );
            ret.append( ", Concepto: " + concepto );
            ret.append( ", Tipo: " + tipo );
            if(tipo.equals(TIPO_MOV_SALIDA))
                ret.append( ", Importe: -" + importe );
            else
                ret.append( ", Importe: +" + importe );
            ret.append( ", Fecha alta: " + format.format(fechaCreacion));
            return ret.toString();
	}

}
