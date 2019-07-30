/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.SesionNoValidaException;
import com.bankito.persistencia.dao.SesionEntidadDao;
import com.bankito.persistencia.dto.SesionEntidad;
import com.bankito.persistencia.dto.SesionEntidadPk;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.exceptions.UsuarioEntidadDaoException;
import com.bankito.persistencia.factory.SesionEntidadDaoFactory;
import com.bankito.persistencia.jdbc.ResourceManager;
import com.bankito.util.ObjectMapper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

/**
 * <h1>Sesion</h1>
 * Esta clase representa la funcionalidad de un objeto Sesion de la capa de
 * dominio.
 * <p>
 * Un objeto de esta clase presenta el siguiente comportamiento:<br>
 * - save: es capaz de salvar (insertar/actualizar) su estado en la base de
 * datos<br>
 * - delete: es capaz de borrar su registro asociado en la base de datos<br>
 * - isValid: es capaz de analizar si el objeto está en un estado válido<br>
 * - equals: es capaz de compararse con otro objeto para ver si son iguales<br>
 * - toString: compone una cadena de texto con sus características<br>
 * - getters: obtienen el valor de las propiedades<br>
 * - setters: establecen el valor de la propiedades<br>
 * <p>
 * Esta clase es null-safe, es decir, evita a toda costa que se pueda hacer un
 * manejo de sus instancias de forma que alguno de los campos pueda contener un
 * null indeseado.
 * <p>
 * Esta clase posee un campo público estático y final llamado NOT_FOUND que es
 * un objeto de la clase Sesion que servirá para representar una consulta a la
 * base de datos que no devuelve resultado alguno.
 * <p>
 * Además, posee un campo público estático final llamado NULL_DATE que servirá
 * para inicializar los campos Date a un valor por defecto que es el 01/01/1970
 * a las 00:00:00 horas.
 * <p>
 * Esta clase presenta los siguientes comportamientos como métodos
 * estáticos:<br>
 - findLastSesionByIdUsuario: recupera el objeto más reciente desde la BD por su
 IdUsuario. Devuelve una referencia a NOT_FOUND si no se encuentra.<br>
 *
 * @author Enrique Royo Sánchez
 */
public class Sesion implements Serializable {

    public static final Date NULL_DATE = new Date(0);
    public static final Sesion NOT_FOUND = new Sesion("ACCIÓN NULA", 0);
    public static final String LOGIN_OK_ACTION = "Login OK";
    public static final String LOGIN_CLOSING_PREVIOUS_ACTION = "Login OK. Previous open logins were closed";
    public static final String LOGOUT_ACTION = "Logout";
    
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    private int idSesion;
    private String accion;
    private Date fechaCreacion;
    private int usuarioIdUsuario;
    /**
     * Si esta propiedad no es nula entonces se habrá establecido una conexión a
     * la BD utilizando el método setConnetion(Connection c) para utilizar los
     * métodos de un objeto Usuario de forma transaccional. Si esta propiedad es
     * nula entonces se creará y cerrará una conexión nueva en cada operación.
     */
    private Connection conExterna;

    public Sesion() {
        this.accion = "";
        this.fechaCreacion = NULL_DATE;
    }
    
    public Sesion(String accion, int usuarioIdUsuario) {
        this.accion = Objects.requireNonNull(accion, "La acción de la sesión no puede ser nula");
        this.fechaCreacion = NULL_DATE;
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    public boolean isValid() {
        if (this == Sesion.NOT_FOUND) {
            return false;
        }

        if (accion == null || accion.equals("")|| usuarioIdUsuario == 0 || fechaCreacion == null) {
            return false;
        } else if (this == Sesion.NOT_FOUND) {
            return false;
        } else {
            return true;
        }
    }
    
    public void save() throws SesionNoValidaException {
        if (this.isValid() == false) {
            throw new SesionNoValidaException("Sesión no salvada por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            SesionEntidadDao dao = SesionEntidadDaoFactory.create(conn);
            
            if (idSesion == 0) // Si no está inicializado entonces inserta en la BD
            {
                this.fechaCreacion = new Date();
                SesionEntidad sesEnt = new SesionEntidad();
                ObjectMapper.copyProperties(this, sesEnt);
                SesionEntidadPk pk = dao.insert(sesEnt);
                this.idSesion = pk.getIdSesion();
            } else // modificamos el registro existente
            {
                SesionEntidad sesEnt = new SesionEntidad();
                ObjectMapper.copyProperties(this, sesEnt);
                dao.update(sesEnt.createPk(), sesEnt);
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar la sesión de usuario " + this.getIdSesion() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar la sesión " + this.getIdSesion() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }
    
    public static Sesion findLastSesionByIdUsuario(int UsuarioIdUsuario) {
        SesionEntidadDao dao = SesionEntidadDaoFactory.create();
        SesionEntidad sesEnt = dao.findLastSesionByUsuario(UsuarioIdUsuario);

        if (sesEnt != null) {
            Sesion sesion = new Sesion();
            ObjectMapper.copyProperties(sesEnt, sesion);
            return sesion;
        } else {
            return Sesion.NOT_FOUND;
        }
    }
    
    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    /**
     * Method 'getAccion'
     *
     * @return String
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Method 'setAccion'
     *
     * @param accion
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Method 'getFechaCreacion'
     *
     * @return Date
     */
    public Date getFechaCreacion() {
        return new Date(fechaCreacion.getTime());
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Method 'getUsuarioIdUsuario'
     *
     * @return int
     */
    public int getUsuarioIdUsuario() {
        return this.usuarioIdUsuario;
    }

    /**
     * Method 'setUsuarioIdUsuario'
     *
     * @param usuario
     */
    public void setUsuarioIdUsuario(int usuarioIdUsuario) {
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }

        if (_other == this) {
            return true;
        }

        if (!(_other instanceof Sesion)) {
            return false;
        }

        final Sesion _cast = (Sesion) _other;
        if (idSesion != _cast.idSesion) {
            return false;
        }

        if (accion == null ? _cast.accion != accion : !accion.equals(_cast.accion)) {
            return false;
        }

        if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals(_cast.fechaCreacion)) {
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
    public int hashCode() {
        int _hashCode = 0;
        _hashCode = 29 * _hashCode + idSesion;

        if (accion != null) {
            _hashCode = 29 * _hashCode + accion.hashCode();
        }

        if (fechaCreacion != null) {
            _hashCode = 29 * _hashCode + fechaCreacion.hashCode();
        }

        _hashCode = 29 * _hashCode + usuarioIdUsuario;
        return _hashCode;
    }

    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.bankito.dominio.Sesion: ");
        ret.append("idSesion=" + idSesion);
        ret.append(", accion=" + accion);
        ret.append(", fechaCreacion=" + fechaCreacion);
        ret.append(", usuarioIdUsuario=" + usuarioIdUsuario);
        return ret.toString();
    }

}
