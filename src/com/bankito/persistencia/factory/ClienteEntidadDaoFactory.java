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

public class ClienteEntidadDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return ClienteEntidadDao
	 */
	public static ClienteEntidadDao create()
	{
		return new ClienteEntidadDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return ClienteEntidadDao
	 */
	public static ClienteEntidadDao create(Connection conn)
	{
		return new ClienteEntidadDaoImpl( conn );
	}

}
