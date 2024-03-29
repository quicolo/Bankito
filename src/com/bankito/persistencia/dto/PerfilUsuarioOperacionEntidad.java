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

public class PerfilUsuarioOperacionEntidad implements Serializable
{
	/** 
	 * This attribute maps to the column perfil_usuario_id_perfil in the perfil_usuario_operacion table.
	 */
	protected int perfilUsuarioIdPerfil;

	/** 
	 * This attribute represents whether the attribute perfilUsuarioIdPerfil has been modified since being read from the database.
	 */
	protected boolean perfilUsuarioIdPerfilModified = false;

	/** 
	 * This attribute maps to the column operacion_id_operacion in the perfil_usuario_operacion table.
	 */
	protected int operacionIdOperacion;

	/** 
	 * This attribute represents whether the attribute operacionIdOperacion has been modified since being read from the database.
	 */
	protected boolean operacionIdOperacionModified = false;

	/**
	 * Method 'PerfilUsuarioOperacionEntidad'
	 * 
	 */
	public PerfilUsuarioOperacionEntidad()
	{
	}

	/**
	 * Method 'getPerfilUsuarioIdPerfil'
	 * 
	 * @return int
	 */
	public int getPerfilUsuarioIdPerfil()
	{
		return perfilUsuarioIdPerfil;
	}

	/**
	 * Method 'setPerfilUsuarioIdPerfil'
	 * 
	 * @param perfilUsuarioIdPerfil
	 */
	public void setPerfilUsuarioIdPerfil(int perfilUsuarioIdPerfil)
	{
		this.perfilUsuarioIdPerfil = perfilUsuarioIdPerfil;
		this.perfilUsuarioIdPerfilModified = true;
	}

	/** 
	 * Sets the value of perfilUsuarioIdPerfilModified
	 */
	public void setPerfilUsuarioIdPerfilModified(boolean perfilUsuarioIdPerfilModified)
	{
		this.perfilUsuarioIdPerfilModified = perfilUsuarioIdPerfilModified;
	}

	/** 
	 * Gets the value of perfilUsuarioIdPerfilModified
	 */
	public boolean isPerfilUsuarioIdPerfilModified()
	{
		return perfilUsuarioIdPerfilModified;
	}

	/**
	 * Method 'getOperacionIdOperacion'
	 * 
	 * @return int
	 */
	public int getOperacionIdOperacion()
	{
		return operacionIdOperacion;
	}

	/**
	 * Method 'setOperacionIdOperacion'
	 * 
	 * @param operacionIdOperacion
	 */
	public void setOperacionIdOperacion(int operacionIdOperacion)
	{
		this.operacionIdOperacion = operacionIdOperacion;
		this.operacionIdOperacionModified = true;
	}

	/** 
	 * Sets the value of operacionIdOperacionModified
	 */
	public void setOperacionIdOperacionModified(boolean operacionIdOperacionModified)
	{
		this.operacionIdOperacionModified = operacionIdOperacionModified;
	}

	/** 
	 * Gets the value of operacionIdOperacionModified
	 */
	public boolean isOperacionIdOperacionModified()
	{
		return operacionIdOperacionModified;
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
		
		if (!(_other instanceof PerfilUsuarioOperacionEntidad)) {
			return false;
		}
		
		final PerfilUsuarioOperacionEntidad _cast = (PerfilUsuarioOperacionEntidad) _other;
		if (perfilUsuarioIdPerfil != _cast.perfilUsuarioIdPerfil) {
			return false;
		}
		
		if (perfilUsuarioIdPerfilModified != _cast.perfilUsuarioIdPerfilModified) {
			return false;
		}
		
		if (operacionIdOperacion != _cast.operacionIdOperacion) {
			return false;
		}
		
		if (operacionIdOperacionModified != _cast.operacionIdOperacionModified) {
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
		_hashCode = 29 * _hashCode + perfilUsuarioIdPerfil;
		_hashCode = 29 * _hashCode + (perfilUsuarioIdPerfilModified ? 1 : 0);
		_hashCode = 29 * _hashCode + operacionIdOperacion;
		_hashCode = 29 * _hashCode + (operacionIdOperacionModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return PerfilUsuarioOperacionEntidadPk
	 */
	public PerfilUsuarioOperacionEntidadPk createPk()
	{
		return new PerfilUsuarioOperacionEntidadPk(perfilUsuarioIdPerfil, operacionIdOperacion);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.bankito.persistencia.dto.PerfilUsuarioOperacionEntidad: " );
		ret.append( "perfilUsuarioIdPerfil=" + perfilUsuarioIdPerfil );
		ret.append( ", operacionIdOperacion=" + operacionIdOperacion );
		return ret.toString();
	}

}
