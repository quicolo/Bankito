package com.bankito.persistencia.jdbc;

import com.bankito.util.AppConfiguration;
import java.sql.*;

public class ResourceManager
{
    private static String JDBC_DRIVER   = "";
    private static String JDBC_URL      = "";

    private static String JDBC_USER     = "";
    private static String JDBC_PASSWORD = "";

    private static Driver driver = null;

    public static synchronized Connection getConnection()
	throws SQLException
    {
        if (driver == null)
        {
            try
            {
                JDBC_DRIVER = AppConfiguration.getProperty("JDBC_DRIVER", "com.mysql.jdbc.Driver");
                JDBC_URL = AppConfiguration.getProperty("JDBC_URL","jdbc:mysql://localhost/bankito?useSSL=false");
                JDBC_USER = AppConfiguration.getProperty("JDBC_USER", "bankitoadmin");
                JDBC_PASSWORD = AppConfiguration.getProperty("JDBC_PASSWORD", "admin");
                
                Class jdbcDriverClass = Class.forName( JDBC_DRIVER );
                driver = (Driver) jdbcDriverClass.newInstance();
                DriverManager.registerDriver( driver );
            }
            catch (Exception e)
            {
                System.out.println( "Failed to initialise JDBC driver" );
                e.printStackTrace();
            }
        }

        return DriverManager.getConnection(
                JDBC_URL,
                JDBC_USER,
                JDBC_PASSWORD
        );
    }


	public static void close(Connection conn)
	{
		try {
			if (conn != null) {
                            conn.close();
                        }        
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt)
	{
		try {
			if (stmt != null) stmt.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs)
	{
		try {
			if (rs != null) rs.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

}
