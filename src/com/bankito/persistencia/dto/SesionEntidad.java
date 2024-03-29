/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.bankito.persistencia.dto;

import com.bankito.persistencia.dao.*;
import com.bankito.persistencia.factory.*;
import com.bankito.persistencia.exceptions.*;
import java.io.Serializable;
import java.util.*;
import java.util.Date;

public class SesionEntidad implements Serializable
{
	/** 
	 * This attribute maps to the column id_sesion in the sesion table.
	 */
	protected int idSesion;

	/** 
	 * This attribute represents whether the attribute idSesion has been modified since being read from the database.
	 */
	protected boolean idSesionModified = false;

	/** 
	 * This attribute maps to the column accion in the sesion table.
	 */
	protected String accion;

	/** 
	 * This attribute represents whether the attribute accion has been modified since being read from the database.
	 */
	protected boolean accionModified = false;

	/** 
	 * This attribute maps to the column fecha_creacion in the sesion table.
	 */
	protected Date fechaCreacion;

	/** 
	 * This attribute represents whether the attribute fechaCreacion has been modified since being read from the database.
	 */
	protected boolean fechaCreacionModified = false;

	/** 
	 * This attribute maps to the column usuario_id_usuario in the sesion table.
	 */
	protected int usuarioIdUsuario;

	/** 
	 * This attribute represents whether the primitive attribute usuarioIdUsuario is null.
	 */
	protected boolean usuarioIdUsuarioNull = true;

	/** 
	 * This attribute represents whether the attribute usuarioIdUsuario has been modified since being read from the database.
	 */
	protected boolean usuarioIdUsuarioModified = false;

	/**
	 * Method 'SesionEntidad'
	 * 
	 */
	public SesionEntidad()
	{
	}

	/**
	 * Method 'getIdSesion'
	 * 
	 * @return int
	 */
	public int getIdSesion()
	{
		return idSesion;
	}

	/**
	 * Method 'setIdSesion'
	 * 
	 * @param idSesion
	 */
	public void setIdSesion(int idSesion)
	{
		this.idSesion = idSesion;
		this.idSesionModified = true;
	}

	/** 
	 * Sets the value of idSesionModified
	 */
	public void setIdSesionModified(boolean idSesionModified)
	{
		this.idSesionModified = idSesionModified;
	}

	/** 
	 * Gets the value of idSesionModified
	 */
	public boolean isIdSesionModified()
	{
		return idSesionModified;
	}

	/**
	 * Method 'getAccion'
	 * 
	 * @return String
	 */
	public String getAccion()
	{
		return accion;
	}

	/**
	 * Method 'setAccion'
	 * 
	 * @param accion
	 */
	public void setAccion(String accion)
	{
		this.accion = accion;
		this.accionModified = true;
	}

	/** 
	 * Sets the value of accionModified
	 */
	public void setAccionModified(boolean accionModified)
	{
		this.accionModified = accionModified;
	}

	/** 
	 * Gets the value of accionModified
	 */
	public boolean isAccionModified()
	{
		return accionModified;
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
		this.fechaCreacionModified = true;
	}

	/** 
	 * Sets the value of fechaCreacionModified
	 */
	public void setFechaCreacionModified(boolean fechaCreacionModified)
	{
		this.fechaCreacionModified = fechaCreacionModified;
	}

	/** 
	 * Gets the value of fechaCreacionModified
	 */
	public boolean isFechaCreacionModified()
	{
		return fechaCreacionModified;
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
		this.usuarioIdUsuarioNull = false;
		this.usuarioIdUsuarioModified = true;
	}

	/**
	 * Method 'setUsuarioIdUsuarioNull'
	 * 
	 * @param value
	 */
	public void setUsuarioIdUsuarioNull(boolean value)
	{
		this.usuarioIdUsuarioNull = value;
		this.usuarioIdUsuarioModified = true;
	}

	/**
	 * Method 'isUsuarioIdUsuarioNull'
	 * 
	 * @return boolean
	 */
	public boolean isUsuarioIdUsuarioNull()
	{
		return usuarioIdUsuarioNull;
	}

	/** 
	 * Sets the value of usuarioIdUsuarioModified
	 */
	public void setUsuarioIdUsuarioModified(boolean usuarioIdUsuarioModified)
	{
		this.usuarioIdUsuarioModified = usuarioIdUsuarioModified;
	}

	/** 
	 * Gets the value of usuarioIdUsuarioModified
	 */
	public boolean isUsuarioIdUsuarioModified()
	{
		return usuarioIdUsuarioModified;
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
		
		if (!(_other instanceof SesionEntidad)) {
			return false;
		}
		
		final SesionEntidad _cast = (SesionEntidad) _other;
		if (idSesion != _cast.idSesion) {
			return false;
		}
		
		if (idSesionModified != _cast.idSesionModified) {
			return false;
		}
		
		if (accion == null ? _cast.accion != accion : !accion.equals( _cast.accion )) {
			return false;
		}
		
		if (accionModified != _cast.accionModified) {
			return false;
		}
		
		if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals( _cast.fechaCreacion )) {
			return false;
		}
		
		if (fechaCreacionModified != _cast.fechaCreacionModified) {
			return false;
		}
		
		if (usuarioIdUsuario != _cast.usuarioIdUsuario) {
			return false;
		}
		
		if (usuarioIdUsuarioNull != _cast.usuarioIdUsuarioNull) {
			return false;
		}
		
		if (usuarioIdUsuarioModified != _cast.usuarioIdUsuarioModified) {
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
		_hashCode = 29 * _hashCode + idSesion;
		_hashCode = 29 * _hashCode + (idSesionModified ? 1 : 0);
		if (accion != null) {
			_hashCode = 29 * _hashCode + accion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (accionModified ? 1 : 0);
		if (fechaCreacion != null) {
			_hashCode = 29 * _hashCode + fechaCreacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaCreacionModified ? 1 : 0);
		_hashCode = 29 * _hashCode + usuarioIdUsuario;
		_hashCode = 29 * _hashCode + (usuarioIdUsuarioNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (usuarioIdUsuarioModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return SesionEntidadPk
	 */
	public SesionEntidadPk createPk()
	{
		return new SesionEntidadPk(idSesion);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.bankito.persistencia.dto.SesionEntidad: " );
		ret.append( "idSesion=" + idSesion );
		ret.append( ", accion=" + accion );
		ret.append( ", fechaCreacion=" + fechaCreacion );
		ret.append( ", usuarioIdUsuario=" + usuarioIdUsuario );
		return ret.toString();
	}

}
