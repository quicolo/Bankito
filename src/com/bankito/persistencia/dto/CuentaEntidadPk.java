/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.bankito.persistencia.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
 * This class represents the primary key of the cuenta table.
 */
public class CuentaEntidadPk implements Serializable
{
	protected int idCuenta;

	/** 
	 * This attribute represents whether the primitive attribute idCuenta is null.
	 */
	protected boolean idCuentaNull;

	/** 
	 * Sets the value of idCuenta
	 */
	public void setIdCuenta(int idCuenta)
	{
		this.idCuenta = idCuenta;
	}

	/** 
	 * Gets the value of idCuenta
	 */
	public int getIdCuenta()
	{
		return idCuenta;
	}

	/**
	 * Method 'CuentaEntidadPk'
	 * 
	 */
	public CuentaEntidadPk()
	{
	}

	/**
	 * Method 'CuentaEntidadPk'
	 * 
	 * @param idCuenta
	 */
	public CuentaEntidadPk(final int idCuenta)
	{
		this.idCuenta = idCuenta;
	}

	/** 
	 * Sets the value of idCuentaNull
	 */
	public void setIdCuentaNull(boolean idCuentaNull)
	{
		this.idCuentaNull = idCuentaNull;
	}

	/** 
	 * Gets the value of idCuentaNull
	 */
	public boolean isIdCuentaNull()
	{
		return idCuentaNull;
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
		
		if (!(_other instanceof CuentaEntidadPk)) {
			return false;
		}
		
		final CuentaEntidadPk _cast = (CuentaEntidadPk) _other;
		if (idCuenta != _cast.idCuenta) {
			return false;
		}
		
		if (idCuentaNull != _cast.idCuentaNull) {
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
		_hashCode = 29 * _hashCode + idCuenta;
		_hashCode = 29 * _hashCode + (idCuentaNull ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.bankito.persistencia.dto.CuentaEntidadPk: " );
		ret.append( "idCuenta=" + idCuenta );
		return ret.toString();
	}

}