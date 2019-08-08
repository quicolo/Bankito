package com.bankito.dominio;

import com.bankito.dominio.exceptions.OperacionDuplicadaException;
import com.bankito.dominio.exceptions.OperacionNoValidaException;
import com.bankito.persistencia.dao.OperacionEntidadDao;
import com.bankito.persistencia.dto.OperacionEntidad;
import com.bankito.persistencia.dto.OperacionEntidadPk;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.exceptions.UsuarioEntidadDaoException;
import com.bankito.persistencia.factory.OperacionEntidadDaoFactory;
import com.bankito.persistencia.jdbc.ResourceManager;
import com.bankito.util.ObjectMapper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * <h1>Operacion</h1>
 * Esta clase representa las posibles operaciones que puede hacer un usuario
 * de un determinado PerfilUsuario de la capa de dominio.
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
 * un objeto de la clase Operacion que servirá para representar una consulta 
 * a la base de datos que no devuelve resultado alguno.
 * <p>
 * Esta clase presenta los siguientes comportamientos como métodos
 * estáticos:<br>
 * - findByIdOperacion: recupera un objeto de la clase desde la BD por su
 * IdOperacion<br>
 * - findByNombre: recupera un objeto de la clase desde la BD por su nombre<br>
 * - findAll: recupera todos los objetos de la clase desde la BD<br>
 *
 * @author Enrique Royo Sánchez
 */
public class Operacion implements Serializable {

    public static final Operacion NOT_FOUND = new Operacion("vacio", "vacio", "vacio");

    private int idOperacion;
    private String nombreCorto;
    private String nombre;
    private String descripcion;
    
    /**
     * Si esta propiedad no es nula entonces se habrá establecido una conexión a
     * la BD utilizando el método setConnetion(Connection c) para utilizar los
     * métodos de un objeto Usuario de forma transaccional. Si esta propiedad es
     * nula entonces se creará y cerrará una conexión nueva en cada operación.
     */
    private Connection conExterna;

    public Operacion() {
        nombreCorto = "";
        nombre = "";
        descripcion = "";
    }

    public Operacion(String nombreCorto, String nombre, String descripcion) {
        this.nombreCorto = Objects.requireNonNull(nombreCorto, "El nombre corto de la operación no puede ser nulo");
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la operación no puede ser nulo");;
        this.descripcion = Objects.requireNonNull(descripcion, "La descripción de la operación no puede ser nula");
    }
    
    public boolean isValid() {
        if (this == Operacion.NOT_FOUND) {
            return false;
        }

        if (nombre == null || nombreCorto == null || descripcion == null) {
            return false;
        } else if (nombre.equals("") || nombreCorto.equals("") ||descripcion.equals("")) {
            return false;
        } else {
            return true;
        }
    }
    
    public void save() throws OperacionDuplicadaException, OperacionNoValidaException {
        if (this.isValid() == false) {
            throw new OperacionNoValidaException("Operacion no salvada por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            OperacionEntidadDao dao = OperacionEntidadDaoFactory.create(conn);
            if (idOperacion == 0) // Si no está inicializado entonces inserta en la BD
            {
                if (findByNombre(this.nombre) != Operacion.NOT_FOUND) {
                    throw new OperacionDuplicadaException("La operación con nombre=" + this.nombre + " ya existe");
                }
                OperacionEntidad oper = new OperacionEntidad();
                ObjectMapper.copyProperties(this, oper);
                OperacionEntidadPk pk = dao.insert(oper);
                this.idOperacion = pk.getIdOperacion();
            } else // modificamos el registro existente
            {
                OperacionEntidad oper = new OperacionEntidad();
                ObjectMapper.copyProperties(this, oper);
                dao.update(oper.createPk(), oper);
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar la operacion " + this.getIdOperacion() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar el usuario " + this.getIdOperacion() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }
    
    public void delete() throws OperacionNoValidaException {
        if (this.isValid() == false) {
            throw new OperacionNoValidaException("Operación no borrada por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            OperacionEntidadDao dao = OperacionEntidadDaoFactory.create(conn);

            OperacionEntidad oper = new OperacionEntidad();
            ObjectMapper.copyProperties(this, oper);
            dao.delete(oper.createPk());

        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al borrar la operación " + this.getIdOperacion() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al borrar la operación " + this.getIdOperacion() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }
    
    
    public static Operacion findByIdOperacion(int idOperacion) {
        OperacionEntidadDao dao = OperacionEntidadDaoFactory.create();
        OperacionEntidad opeEnt = dao.findByPrimaryKey(idOperacion);

        if (opeEnt != null) {
            Operacion operacion = new Operacion();
            ObjectMapper.copyProperties(opeEnt, operacion);
            return operacion;
        } else {
            return Operacion.NOT_FOUND;
        }
    }
    
    public static Operacion findByNombre(String nombre) {
        OperacionEntidadDao dao = OperacionEntidadDaoFactory.create();
        OperacionEntidad opeEnt[] = dao.findWhereNombreEquals(Objects.requireNonNull(nombre, "El nombre de la operacion no puede ser nulo"));

        if (opeEnt.length > 0) {
            Operacion operacion = new Operacion();
            ObjectMapper.copyProperties(opeEnt[0], operacion);
            return operacion;
        } else {
            return Operacion.NOT_FOUND;
        }
    }
    
    public static List<Operacion> findAll() {
        OperacionEntidadDao dao = OperacionEntidadDaoFactory.create();
        OperacionEntidad[] listaOper = dao.findAll();
        
        List<Operacion> dest = new ArrayList();
        
        ObjectMapper.copyProperties(listaOper, dest);
        return dest;
    }
    
    /**
     * Method 'getIdOperacion'
     *
     * @return int
     */
    public int getIdOperacion() {
        return idOperacion;
    }

    /**
     * Method 'setIdOperacion'
     *
     * @param idOperacion
     */
    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    /**
     * Method 'getNombreCorto'
     *
     * @return String
     */
    public String getNombreCorto() {
        return nombreCorto;
    }

    /**
     * Method 'setNombreCorto'
     *
     * @param nombreCorto
     */
    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    /**
     * Method 'getNombre'
     *
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Method 'setNombre'
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Method 'getDescripcion'
     *
     * @return String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Method 'setDescripcion'
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Method 'equals'
     *
     * @param _other
     * @return boolean
     */
    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }

        if (_other == this) {
            return true;
        }

        if (!(_other instanceof Operacion)) {
            return false;
        }

        final Operacion _cast = (Operacion) _other;
        if (idOperacion != _cast.idOperacion) {
            return false;
        }

        if (nombreCorto == null ? _cast.nombreCorto != nombreCorto : !nombreCorto.equals(_cast.nombreCorto)) {
            return false;
        }

        if (nombre == null ? _cast.nombre != nombre : !nombre.equals(_cast.nombre)) {
            return false;
        }
        if (descripcion == null ? _cast.descripcion != descripcion : !descripcion.equals(_cast.descripcion)) {
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
        _hashCode = 29 * _hashCode + idOperacion;
        if (nombreCorto != null) {
            _hashCode = 29 * _hashCode + nombreCorto.hashCode();
        }

        if (nombre != null) {
            _hashCode = 29 * _hashCode + nombre.hashCode();
        }

        if (descripcion != null) {
            _hashCode = 29 * _hashCode + descripcion.hashCode();
        }

        return _hashCode;
    }


    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.bankito.dominio.Operacion: ");
        ret.append("idOperacion=" + idOperacion);
        ret.append(", nombreCorto=" + nombreCorto);
        ret.append(", nombre=" + nombre);
        ret.append(", descripcion=" + descripcion);
        return ret.toString();
    }

}
