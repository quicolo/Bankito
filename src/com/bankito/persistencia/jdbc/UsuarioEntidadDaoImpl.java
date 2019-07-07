/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */
package com.bankito.persistencia.jdbc;

import com.bankito.persistencia.dao.*;
import java.util.Date;
import com.bankito.persistencia.dto.*;
import com.bankito.persistencia.exceptions.*;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class UsuarioEntidadDaoImpl extends AbstractDAO implements UsuarioEntidadDao {

    /**
     * The factory class for this DAO has two versions of the create() method -
     * one that takes no arguments and one that takes a Connection argument. If
     * the Connection version is chosen then the connection will be stored in
     * this attribute and will be used by all calls to this DAO, otherwise a new
     * Connection will be allocated for each operation.
     */
    protected java.sql.Connection userConn;

    /**
     * All finder methods in this class use this SELECT constant to build their
     * queries
     */
    protected final String SQL_SELECT = "SELECT id_usuario, nombre, password, fecha_creacion, fecha_modificacion FROM " + getTableName() + "";

    /**
     * Finder methods will pass this value to the JDBC setMaxRows method
     */
    protected int maxRows;

    /**
     * SQL INSERT statement for this table
     */
    protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( id_usuario, nombre, password, fecha_creacion, fecha_modificacion ) VALUES ( ?, ?, ?, ?, ? )";

    /**
     * SQL UPDATE statement for this table
     */
    protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET id_usuario = ?, nombre = ?, password = ?, fecha_creacion = ?, fecha_modificacion = ? WHERE id_usuario = ?";

    /**
     * SQL DELETE statement for this table
     */
    protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE id_usuario = ?";

    /**
     * Index of column id_usuario
     */
    protected static final int COLUMN_ID_USUARIO = 1;

    /**
     * Index of column nombre
     */
    protected static final int COLUMN_NOMBRE = 2;

    /**
     * Index of column password
     */
    protected static final int COLUMN_PASSWORD = 3;

    /**
     * Index of column fecha_creacion
     */
    protected static final int COLUMN_FECHA_CREACION = 4;

    /**
     * Index of column fecha_modificacion
     */
    protected static final int COLUMN_FECHA_MODIFICACION = 5;

    /**
     * Number of columns
     */
    protected static final int NUMBER_OF_COLUMNS = 5;

    /**
     * Index of primary-key column id_usuario
     */
    protected static final int PK_COLUMN_ID_USUARIO = 1;

    /**
     * Inserts a new row in the usuario table.
     */
    public UsuarioEntidadPk insert(UsuarioEntidad dto) throws UsuarioEntidadDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setInt(index++, dto.getIdUsuario());
            stmt.setString(index++, dto.getNombre());
            stmt.setString(index++, dto.getPassword());
            stmt.setTimestamp(index++, dto.getFechaCreacion() == null ? null : new java.sql.Timestamp(dto.getFechaCreacion().getTime()));
            stmt.setDate(index++, dto.getFechaModificacion() == null ? null : new java.sql.Date(dto.getFechaModificacion().getTime()));
            System.out.println("Executing " + SQL_INSERT + " with DTO: " + dto);
            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");

            // retrieve values from auto-increment columns
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                dto.setIdUsuario(rs.getInt(1));
            }

            reset(dto);
            return dto.createPk();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new UsuarioEntidadDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }

        }

    }

    /**
     * Updates a single row in the usuario table.
     */
    public void update(UsuarioEntidadPk pk, UsuarioEntidad dto) throws UsuarioEntidadDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            System.out.println("Executing " + SQL_UPDATE + " with DTO: " + dto);
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setInt(index++, dto.getIdUsuario());
            stmt.setString(index++, dto.getNombre());
            stmt.setString(index++, dto.getPassword());
            stmt.setTimestamp(index++, dto.getFechaCreacion() == null ? null : new java.sql.Timestamp(dto.getFechaCreacion().getTime()));
            stmt.setDate(index++, dto.getFechaModificacion() == null ? null : new java.sql.Date(dto.getFechaModificacion().getTime()));
            stmt.setInt(6, pk.getIdUsuario());
            int rows = stmt.executeUpdate();
            reset(dto);
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new UsuarioEntidadDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }

        }

    }

    /**
     * Deletes a single row in the usuario table.
     */
    public void delete(UsuarioEntidadPk pk) throws UsuarioEntidadDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            System.out.println("Executing " + SQL_DELETE + " with PK: " + pk);
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, pk.getIdUsuario());
            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new UsuarioEntidadDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }

        }

    }

    /**
     * Returns the rows from the usuario table that matches the specified
     * primary-key value.
     */
    public UsuarioEntidad findByPrimaryKey(UsuarioEntidadPk pk) throws UsuarioEntidadDaoException {
        return findByPrimaryKey(pk.getIdUsuario());
    }

    /**
     * Returns all rows from the usuario table that match the criteria
     * 'id_usuario = :idUsuario'.
     */
    public UsuarioEntidad findByPrimaryKey(int idUsuario) throws UsuarioEntidadDaoException {
        UsuarioEntidad ret[] = findByDynamicSelect(SQL_SELECT + " WHERE id_usuario = ?", new Object[]{new Integer(idUsuario)});
        return ret.length == 0 ? null : ret[0];
    }

    /**
     * Returns all rows from the usuario table that match the criteria ''.
     */
    public UsuarioEntidad[] findAll() throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " ORDER BY id_usuario", null);
    }

    /**
     * Returns all rows from the usuario table that match the criteria
     * 'id_usuario = :idUsuario'.
     */
    public UsuarioEntidad[] findWhereIdUsuarioEquals(int idUsuario) throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " WHERE id_usuario = ? ORDER BY id_usuario", new Object[]{new Integer(idUsuario)});
    }

    /**
     * Returns all rows from the usuario table that match the criteria 'nombre =
     * :nombre'.
     */
    public UsuarioEntidad[] findWhereNombreEquals(String nombre) throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " WHERE nombre = ? ORDER BY nombre", new Object[]{nombre});
    }

    /**
     * Returns all rows from the usuario table that match the criteria 'password
     * = :password'.
     */
    public UsuarioEntidad[] findWherePasswordEquals(String password) throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " WHERE password = ? ORDER BY password", new Object[]{password});
    }

    /**
     * Returns all rows from the usuario table that match the criteria
     * 'fecha_creacion = :fechaCreacion'.
     */
    public UsuarioEntidad[] findWhereFechaCreacionEquals(Date fechaCreacion) throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " WHERE fecha_creacion = ? ORDER BY fecha_creacion", new Object[]{fechaCreacion == null ? null : new java.sql.Timestamp(fechaCreacion.getTime())});
    }

    /**
     * Returns all rows from the usuario table that match the criteria
     * 'fecha_modificacion = :fechaModificacion'.
     */
    public UsuarioEntidad[] findWhereFechaModificacionEquals(Date fechaModificacion) throws UsuarioEntidadDaoException {
        return findByDynamicSelect(SQL_SELECT + " WHERE fecha_modificacion = ? ORDER BY fecha_modificacion", new Object[]{fechaModificacion == null ? null : new java.sql.Timestamp(fechaModificacion.getTime())});
    }

    /**
     * Method 'UsuarioEntidadDaoImpl'
     *
     */
    public UsuarioEntidadDaoImpl() {
    }

    /**
     * Method 'UsuarioEntidadDaoImpl'
     *
     * @param userConn
     */
    public UsuarioEntidadDaoImpl(final java.sql.Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Sets the value of maxRows
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * Gets the value of maxRows
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Method 'getTableName'
     *
     * @return String
     */
    public String getTableName() {
        return "bankito.usuario";
    }

    /**
     * Fetches a single row from the result set
     */
    protected UsuarioEntidad fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            UsuarioEntidad dto = new UsuarioEntidad();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }

    }

    /**
     * Fetches multiple rows from the result set
     */
    protected UsuarioEntidad[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            UsuarioEntidad dto = new UsuarioEntidad();
            populateDto(dto, rs);
            resultList.add(dto);
        }

        UsuarioEntidad ret[] = new UsuarioEntidad[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    protected void populateDto(UsuarioEntidad dto, ResultSet rs) throws SQLException {
        dto.setIdUsuario(rs.getInt(COLUMN_ID_USUARIO));
        dto.setNombre(rs.getString(COLUMN_NOMBRE));
        dto.setPassword(rs.getString(COLUMN_PASSWORD));
        dto.setFechaCreacion(rs.getTimestamp(COLUMN_FECHA_CREACION));
        dto.setFechaModificacion(rs.getDate(COLUMN_FECHA_MODIFICACION));
    }

    /**
     * Resets the modified attributes in the DTO
     */
    protected void reset(UsuarioEntidad dto) {
    }

    /**
     * Returns all rows from the usuario table that match the specified
     * arbitrary SQL statement
     */
    public UsuarioEntidad[] findByDynamicSelect(String sql, Object[] sqlParams) throws UsuarioEntidadDaoException {
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            // construct the SQL statement
            final String SQL = sql;

            System.out.println("Executing " + SQL);
            // prepare statement
            stmt = conn.prepareStatement(SQL);
            stmt.setMaxRows(maxRows);

            // bind parameters
            for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }

            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new UsuarioEntidadDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(rs);
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }

        }

    }

    /**
     * Returns all rows from the usuario table that match the specified
     * arbitrary SQL statement
     */
    public UsuarioEntidad[] findByDynamicWhere(String sql, Object[] sqlParams) throws UsuarioEntidadDaoException {
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            // construct the SQL statement
            final String SQL = SQL_SELECT + " WHERE " + sql;

            System.out.println("Executing " + SQL);
            // prepare statement
            stmt = conn.prepareStatement(SQL);
            stmt.setMaxRows(maxRows);

            // bind parameters
            for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }

            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new UsuarioEntidadDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(rs);
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }

        }

    }

}
