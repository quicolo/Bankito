package com.bankito.dominio;

import com.bankito.dominio.exceptions.*;
import com.bankito.persistencia.dao.*;
import com.bankito.persistencia.dto.OperacionEntidad;
import com.bankito.persistencia.dto.PerfilUsuarioEntidad;
import com.bankito.persistencia.dto.PerfilUsuarioEntidadPk;
import com.bankito.persistencia.dto.PerfilUsuarioOperacionEntidad;
import com.bankito.persistencia.factory.*;
import com.bankito.persistencia.exceptions.*;
import com.bankito.persistencia.jdbc.ResourceManager;
import com.bankito.servicio.dto.PerfilUsuarioDto;
import com.bankito.util.ObjectMapper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * <h1>PerfilUsuario</h1>
 * Esta clase representa la funcionalidad de un objeto PerfilUsuario de la capa 
 * de dominio.
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
 * un objeto de la clase PerfilUsuario que servirá para representar una consulta 
 * a la base de datos que no devuelve resultado alguno.
 * <p>
 * Esta clase presenta los siguientes comportamientos como métodos
 * estáticos:<br>
 * - findByIdPerfilUsuario: recupera un objeto de la clase desde la BD por su
 * IdPerfilUsuario<br>
 * - findByNombre: recupera un objeto de la clase desde la BD por su nombre<br>
 * - findAll: recupera todos los objetos de la clase desde la BD<br>
 *
 * @author Enrique Royo Sánchez
 */
public class PerfilUsuario implements Serializable {

    public static final PerfilUsuario NOT_FOUND = new PerfilUsuario("vacio", "vacio");
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    private int idPerfilUsuario;
    private String nombre;
    private String descripcion;
    private List<Operacion> operPermitidas;

    /**
     * Si esta propiedad no es nula entonces se habrá establecido una conexión a
     * la BD utilizando el método setConnetion(Connection c) para utilizar los
     * métodos de un objeto Usuario de forma transaccional. Si esta propiedad es
     * nula entonces se creará y cerrará una conexión nueva en cada operación.
     */
    private Connection conExterna;


    public PerfilUsuario() {
        nombre = "";
        descripcion = "";
        operPermitidas = new ArrayList();
    }
    
    public PerfilUsuario(String nombre, String descripcion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del perfil de usuario no puede ser nulo");
        this.descripcion = Objects.requireNonNull(descripcion, "La descripcion del perfil de usuario no puede ser nula");
        this.operPermitidas = new ArrayList();
    }

    public PerfilUsuario(PerfilUsuarioDto per) {
        this.idPerfilUsuario = per.getIdPerfilUsuario();
        this.nombre = Objects.requireNonNull(per.getNombre(), "El nombre del perfil de usuario no puede ser nulo");
        this.descripcion = Objects.requireNonNull(per.getDescripcion(), "La descripcion del perfil de usuario no puede ser nula");
        this.operPermitidas = PerfilUsuario.findOperacionesPermitidas(this);
    }
    
    /**
     * Method 'getIdPerfilUsuario'
     *
     * @return int
     */
    public int getIdPerfilUsuario() {
        return idPerfilUsuario;
    }

    public boolean isValid() {
        if (this == PerfilUsuario.NOT_FOUND) {
            return false;
        }

        if (nombre == null || descripcion == null) {
            return false;
        } else if (nombre.equals("") || descripcion.equals("")) {
            return false;
        } else {
            return true;
        }
    }
    
    public void save() throws PerfilUsuarioDuplicadoException, PerfilUsuarioNoValidoException {
        if (this.isValid() == false) {
            throw new PerfilUsuarioNoValidoException("Perfil de usuario no salvado por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            PerfilUsuarioEntidadDao dao = PerfilUsuarioEntidadDaoFactory.create(conn);
            if (idPerfilUsuario == 0) // Si no está inicializado entonces inserta en la BD
            {
                if (findByNombre(this.nombre) != PerfilUsuario.NOT_FOUND) {
                    throw new PerfilUsuarioDuplicadoException("El perfil de usuario con nombre=" + this.nombre + " ya existe");
                }

                PerfilUsuarioEntidad perfil = new PerfilUsuarioEntidad();
                ObjectMapper.copyProperties(this, perfil);
                PerfilUsuarioEntidadPk pk = dao.insert(perfil);
                this.idPerfilUsuario = pk.getIdPerfilUsuario();
            } else // modificamos el registro existente
            {
                PerfilUsuarioEntidad perfil = new PerfilUsuarioEntidad();
                ObjectMapper.copyProperties(this, perfil);
                dao.update(perfil.createPk(), perfil);
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar el perfil de usuario " + this.getIdPerfilUsuario() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar el perfil de usuario " + this.getIdPerfilUsuario() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }
    
    public void delete() throws PerfilUsuarioNoValidoException {
        if (this.isValid() == false) {
            throw new PerfilUsuarioNoValidoException("Perfil de usuario no borrado por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            PerfilUsuarioEntidadDao dao = PerfilUsuarioEntidadDaoFactory.create(conn);

            PerfilUsuarioEntidad perfil = new PerfilUsuarioEntidad();
            ObjectMapper.copyProperties(this, perfil);
            dao.delete(perfil.createPk());

        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al borrar el usuario " + this.getIdPerfilUsuario() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al borrar el usuario " + this.getIdPerfilUsuario() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }
    
    public static PerfilUsuario findByIdPerfilUsuario(int idPerfilUsuario) {
        PerfilUsuarioEntidadDao dao = PerfilUsuarioEntidadDaoFactory.create();
        PerfilUsuarioEntidad perUsuEnt = dao.findByPrimaryKey(idPerfilUsuario);

        if (perUsuEnt != null) {
            PerfilUsuario perfil = new PerfilUsuario();
            ObjectMapper.copyProperties(perUsuEnt, perfil);
            return perfil;
        } else {
            return PerfilUsuario.NOT_FOUND;
        }
    }
    
    public static PerfilUsuario findByNombre(String nombre) {
        PerfilUsuarioEntidadDao dao = PerfilUsuarioEntidadDaoFactory.create();
        PerfilUsuarioEntidad perUsuEnt[] = dao.findWhereNombreEquals(Objects.requireNonNull(nombre, "El nombre del perfil de usuario no puede ser nulo"));

        if (perUsuEnt.length > 0) {
            PerfilUsuario perUsu = new PerfilUsuario();
            ObjectMapper.copyProperties(perUsuEnt[0], perUsu);
            return perUsu;
        } else {
            return PerfilUsuario.NOT_FOUND;
        }
    }
    
    public static List<PerfilUsuario> findAll() {
        PerfilUsuarioEntidadDao dao = PerfilUsuarioEntidadDaoFactory.create();
        PerfilUsuarioEntidad[] listaPerfiles = dao.findAll();
        List<PerfilUsuario> dest = new ArrayList();
        ObjectMapper.copyProperties(listaPerfiles, dest);
        return dest;
    }
    
    public static List<Operacion> findOperacionesPermitidas(PerfilUsuario perfil) {
        Objects.requireNonNull(perfil, "El perfil de usuario no puede ser nulo");
        List <Operacion> listaOper = new ArrayList();
        
        PerfilUsuarioOperacionEntidadDao dao = PerfilUsuarioOperacionEntidadDaoFactory.create();
        PerfilUsuarioOperacionEntidad[] listaPUOE = dao.findByPerfilUsuario(perfil.getIdPerfilUsuario());
        
        OperacionEntidadDao daoOper = OperacionEntidadDaoFactory.create();
        
        for (int i=0; i<listaPUOE.length; i++) {
            int idOperacion = listaPUOE[i].getOperacionIdOperacion();
            OperacionEntidad operEnt = daoOper.findByPrimaryKey(idOperacion);
            Operacion oper = new Operacion();
            ObjectMapper.copyProperties(operEnt, oper);
            listaOper.add(oper);
        }
        
        return listaOper;        
    }
    
    
    /**
     * Method 'setIdPerfilUsuario'
     *
     * @param idPerfilUsuario
     */
    public void setIdPerfilUsuario(int idPerfilUsuario) {
        this.idPerfilUsuario = idPerfilUsuario;
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
    
    public void setOperPermitidas(List<Operacion> lista) {
        this.operPermitidas = lista;
    }
    
    public List<Operacion> getOperPermitidas() {
        return operPermitidas;
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

        if (!(_other instanceof PerfilUsuario)) {
            return false;
        }

        final PerfilUsuario _cast = (PerfilUsuario) _other;
        if (idPerfilUsuario != _cast.idPerfilUsuario) {
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
        _hashCode = 29 * _hashCode + idPerfilUsuario;
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
        ret.append("com.bankito.dominio.PerfilUsuario: ");
        ret.append("idPerfilUsuario=" + idPerfilUsuario);
        ret.append(", nombre=" + nombre);
        ret.append(", descripcion=" + descripcion);
        return ret.toString();
    }

}
