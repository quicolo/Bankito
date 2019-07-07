package com.bankito.persistencia.dto;

import com.bankito.dominio.Cliente;
import java.io.Serializable;
import java.util.Date;

public class ClienteEntidad implements Serializable
{
	/** 
	 * This attribute maps to the column id_cliente in the cliente table.
	 */
	protected int idCliente;

	/** 
	 * This attribute maps to the column nombre in the cliente table.
	 */
	protected String nombre;

	/** 
	 * This attribute maps to the column apellido1 in the cliente table.
	 */
	protected String apellido1;

	/** 
	 * This attribute maps to the column apellido2 in the cliente table.
	 */
	protected String apellido2;

	/** 
	 * This attribute maps to the column NIF in the cliente table.
	 */
	protected String nif;

	/** 
	 * This attribute maps to the column direccion_completa in the cliente table.
	 */
	protected String direccionCompleta;

	/** 
	 * This attribute maps to the column fecha_creacion in the cliente table.
	 */
	protected Date fechaCreacion;

	/** 
	 * This attribute maps to the column fecha_modificacion in the cliente table.
	 */
	protected Date fechaModificacion;

	/** 
	 * This attribute maps to the column usuario_id_usuario in the cliente table.
	 */
	protected int usuarioIdUsuario;

	/**
	 * Method 'ClienteEntidad'
	 * 
	 */
	public ClienteEntidad()
	{
	}

	/**
	 * Method 'getIdCliente'
	 * 
	 * @return int
	 */
	public int getIdCliente()
	{
		return idCliente;
	}

	/**
	 * Method 'setIdCliente'
	 * 
	 * @param idCliente
	 */
	public void setIdCliente(int idCliente)
	{
		this.idCliente = idCliente;
	}

	/**
	 * Method 'getNombre'
	 * 
	 * @return String
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Method 'setNombre'
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Method 'getApellido1'
	 * 
	 * @return String
	 */
	public String getApellido1()
	{
		return apellido1;
	}

	/**
	 * Method 'setApellido1'
	 * 
	 * @param apellido1
	 */
	public void setApellido1(String apellido1)
	{
		this.apellido1 = apellido1;
	}

	/**
	 * Method 'getApellido2'
	 * 
	 * @return String
	 */
	public String getApellido2()
	{
		return apellido2;
	}

	/**
	 * Method 'setApellido2'
	 * 
	 * @param apellido2
	 */
	public void setApellido2(String apellido2)
	{
		this.apellido2 = apellido2;
	}

	/**
	 * Method 'getNif'
	 * 
	 * @return String
	 */
	public String getNif()
	{
		return nif;
	}

	/**
	 * Method 'setNif'
	 * 
	 * @param nif
	 */
	public void setNif(String nif)
	{
		this.nif = nif;
	}

	/**
	 * Method 'getDireccionCompleta'
	 * 
	 * @return String
	 */
	public String getDireccionCompleta()
	{
		return direccionCompleta;
	}

	/**
	 * Method 'setDireccionCompleta'
	 * 
	 * @param direccionCompleta
	 */
	public void setDireccionCompleta(String direccionCompleta)
	{
		this.direccionCompleta = direccionCompleta;
	}

	/**
	 * Method 'getFechaCreacion'
	 * 
	 * @return Date
	 */
	public Date getFechaCreacion()
	{
		return fechaCreacion;
	}

	/**
	 * Method 'setFechaCreacion'
	 * 
	 * @param fechaCreacion
	 */
	public void setFechaCreacion(Date fechaCreacion)
	{
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Method 'getFechaModificacion'
	 * 
	 * @return Date
	 */
	public Date getFechaModificacion()
	{
		return fechaModificacion;
	}

	/**
	 * Method 'setFechaModificacion'
	 * 
	 * @param fechaModificacion
	 */
	public void setFechaModificacion(Date fechaModificacion)
	{
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * Method 'getUsuarioIdUsuario'
	 * 
	 * @return int
	 */
	public int getUsuarioIdUsuario()
	{
		return usuarioIdUsuario;
	}

	/**
	 * Method 'setUsuarioIdUsuario'
	 * 
	 * @param usuarioIdUsuario
	 */
	public void setUsuarioIdUsuario(int usuarioIdUsuario)
	{
		this.usuarioIdUsuario = usuarioIdUsuario;
	}
        
        
        public Cliente toCliente() {
            Cliente cli = new Cliente();
            cli.setIdCliente(idCliente);
            cli.setNombre(nombre);
            cli.setNif(nif);
            cli.setApellido1(apellido1);
            cli.setApellido2(apellido2);
            cli.setFechaCreacion(fechaCreacion);
            cli.setFechaModificacion(fechaModificacion);
            cli.setDireccionCompleta(direccionCompleta);
            cli.setUsuarioIdUsuario(usuarioIdUsuario);
            return cli;
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
		
		if (!(_other instanceof ClienteEntidad)) {
			return false;
		}
		
		final ClienteEntidad _cast = (ClienteEntidad) _other;
		if (idCliente != _cast.idCliente) {
			return false;
		}
		
		if (nombre == null ? _cast.nombre != nombre : !nombre.equals( _cast.nombre )) {
			return false;
		}
		
		if (apellido1 == null ? _cast.apellido1 != apellido1 : !apellido1.equals( _cast.apellido1 )) {
			return false;
		}
		
		if (apellido2 == null ? _cast.apellido2 != apellido2 : !apellido2.equals( _cast.apellido2 )) {
			return false;
		}
		
		if (nif == null ? _cast.nif != nif : !nif.equals( _cast.nif )) {
			return false;
		}
		
		if (direccionCompleta == null ? _cast.direccionCompleta != direccionCompleta : !direccionCompleta.equals( _cast.direccionCompleta )) {
			return false;
		}
		
		if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals( _cast.fechaCreacion )) {
			return false;
		}
		
		if (fechaModificacion == null ? _cast.fechaModificacion != fechaModificacion : !fechaModificacion.equals( _cast.fechaModificacion )) {
			return false;
		}
		
		if (usuarioIdUsuario != _cast.usuarioIdUsuario) {
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
		_hashCode = 29 * _hashCode + idCliente;
		if (nombre != null) {
			_hashCode = 29 * _hashCode + nombre.hashCode();
		}
		
		if (apellido1 != null) {
			_hashCode = 29 * _hashCode + apellido1.hashCode();
		}
		
		if (apellido2 != null) {
			_hashCode = 29 * _hashCode + apellido2.hashCode();
		}
		
		if (nif != null) {
			_hashCode = 29 * _hashCode + nif.hashCode();
		}
		
		if (direccionCompleta != null) {
			_hashCode = 29 * _hashCode + direccionCompleta.hashCode();
		}
		
		if (fechaCreacion != null) {
			_hashCode = 29 * _hashCode + fechaCreacion.hashCode();
		}
		
		if (fechaModificacion != null) {
			_hashCode = 29 * _hashCode + fechaModificacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + usuarioIdUsuario;
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return ClienteEntidadPk
	 */
	public ClienteEntidadPk createPk()
	{
		return new ClienteEntidadPk(idCliente);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.bankito.persistencia.dto.ClienteEntidad: " );
		ret.append( "idCliente=" + idCliente );
		ret.append( ", nombre=" + nombre );
		ret.append( ", apellido1=" + apellido1 );
		ret.append( ", apellido2=" + apellido2 );
		ret.append( ", nif=" + nif );
		ret.append( ", direccionCompleta=" + direccionCompleta );
		ret.append( ", fechaCreacion=" + fechaCreacion );
		ret.append( ", fechaModificacion=" + fechaModificacion );
		ret.append( ", usuarioIdUsuario=" + usuarioIdUsuario );
		return ret.toString();
	}

}
