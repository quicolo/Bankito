/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.bankito.persistencia.factory;

import java.sql.Connection;
import com.bankito.persistencia.dao.*;
import com.bankito.persistencia.jdbc.*;

public class MovimientoEntidadDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return MovimientoEntidadDao
	 */
	public static MovimientoEntidadDao create()
	{
		return new MovimientoEntidadDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return MovimientoEntidadDao
	 */
	public static MovimientoEntidadDao create(Connection conn)
	{
		return new MovimientoEntidadDaoImpl( conn );
	}

}
