package com.bankito.dominio;

import com.bankito.util.ObjectMapper;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.persistencia.dao.UsuarioEntidadDao;
import com.bankito.persistencia.dto.UsuarioEntidad;
import com.bankito.persistencia.dto.UsuarioEntidadPk;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.exceptions.UsuarioEntidadDaoException;
import com.bankito.persistencia.factory.UsuarioEntidadDaoFactory;
import com.bankito.persistencia.jdbc.ResourceManager;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <h1>Usuario</h1>
 * Esta clase representa la funcionalidad de un objeto Usuario de la capa de
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
 * un objeto de la clase Usuario que servirá para representar una consulta a la
 * base de datos que no devuelve resultado alguno.
 * <p>
 * Además, posee un campo público estático final llamado NULL_DATE que servirá
 * para inicializar los campos Date a un valor por defecto que es el 01/01/1970
 * a las 00:00:00 horas.
 * <p>
 * Esta clase presenta los siguientes comportamientos como métodos
 * estáticos:<br>
 * - findByIdUsuario: recupera un objeto de la clase desde la BD por su
 * IdUsuario<br>
 * - findByNombre: recupera un objeto de la clase desde la BD por su nombre<br>
 * - findAll: recupera todos los objetos de la clase desde la BD<br>
 * - tryLogin: intenta hacer login con un nombre de usuario y password. Devuelve
 * el objeto usuario si todo está correcto y NOT_FOUND en caso contrario.
 *
 * @author Enrique Royo Sánchez
 */
public class Usuario implements Serializable {

    public static final Usuario NOT_FOUND = new Usuario("vacio", "vacio", PerfilUsuario.NOT_FOUND);
    public static final Date NULL_DATE = new Date(0);
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD
     */
    private int idUsuario;
    private String nombre;
    private String password;
    private Date fechaCreacion;
    private Date fechaModificacion;
    /**
     * Esta propiedad valdrá 0 cuando no haya perfil asignado al usuario
     */
    private int perfilUsuarioIdPerfil;

    /**
     * Si esta propiedad no es nula entonces se habrá establecido una conexión a
     * la BD utilizando el método setConnetion(Connection c) para utilizar los
     * métodos de un objeto Usuario de forma transaccional. Si esta propiedad es
     * nula entonces se creará y cerrará una conexión nueva en cada operación.
     */
    private Connection conExterna;

    /**
     * Method 'UsuarioEntidad'
     *
     */
    public Usuario() {
        this.fechaCreacion = Usuario.NULL_DATE;
        this.fechaModificacion = Usuario.NULL_DATE;
    }

    public Usuario(String nombre, String password, PerfilUsuario perfil) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo");
        String pass = Objects.requireNonNull(password, "La contraseña del usuario no puede ser nula");
        Objects.requireNonNull(perfil, "El perfil del usuario no puede ser nulo");
        
        try {
            // Solo codificamos si es distinto de la cadena vacía, si no
            // dejaremos la propiedad a null y no será un usuario válido.
            if (pass.equals("") == false) {
                this.password = encodePassword(pass);
            }
        } catch (UsuarioEncodePasswordException e) {
            e.printStackTrace();
        }
        this.fechaCreacion = Usuario.NULL_DATE;
        this.fechaModificacion = Usuario.NULL_DATE;
        this.perfilUsuarioIdPerfil = perfil.getIdPerfilUsuario();
    }

    public boolean isValid() {
        if (this == Usuario.NOT_FOUND) {
            return false;
        }

        if (nombre == null || password == null) {
            return false;
        } else if (nombre.equals("") || password.equals("") || perfilUsuarioIdPerfil == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void save() throws UsuarioDuplicadoException, UsuarioNoValidoException {
        if (this.isValid() == false) {
            throw new UsuarioNoValidoException("Usuario no salvado por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }
            
            UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create(conn);
            if (idUsuario == 0) // Si no está inicializado entonces inserta en la BD
            {
                if (findByNombre(this.nombre) != Usuario.NOT_FOUND) {
                    throw new UsuarioDuplicadoException("El usuario con nombre=" + this.nombre + " ya existe");
                }

                this.fechaCreacion = new Date();
                this.fechaModificacion = new Date();
                UsuarioEntidad usu = new UsuarioEntidad();
                ObjectMapper.copyProperties(this, usu);
                UsuarioEntidadPk pk = dao.insert(usu);
                this.idUsuario = pk.getIdUsuario();
            } else // modificamos el registro existente
            {
                this.fechaModificacion = new Date();
                UsuarioEntidad usu = new UsuarioEntidad();
                ObjectMapper.copyProperties(this, usu);
                dao.update(usu.createPk(), usu);
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar el usuario " + this.getIdUsuario() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar el usuario " + this.getIdUsuario() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }

    public void delete() throws UsuarioNoValidoException {
        if (this.isValid() == false) {
            throw new UsuarioNoValidoException("Usuario no borrado por estado no válido: " + toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }
            
            UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create(conn);

            UsuarioEntidad usu = new UsuarioEntidad();
            ObjectMapper.copyProperties(this, usu);
            dao.delete(usu.createPk());

        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al borrar el usuario " + this.getIdUsuario() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al borrar el usuario " + this.getIdUsuario() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new UsuarioEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }

    private static String encodePassword(String passwordToHash) throws UsuarioEncodePasswordException {
        Objects.requireNonNull(passwordToHash, "La password no puede ser nula");
        String generatedPassword = null;
        try {
            // Creamos una instancia de la clase con el algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Añadimos la password que queremos codificar
            md.update(passwordToHash.getBytes());
            // Obtenemos los bytes codificados que me devuelve el objeto md
            byte[] bytes = md.digest();

            // bytes es un array con números que van desde -128 a 127
            // para guardarlos en la base de datos lo vamos a pasar a 
            // hexadecimal. Es decir lo vamos a forzar a que encaje en
            // este "alfabeto": 0123456789abcdef
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Obtenemos el password como cadena hexadecimal
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new UsuarioEncodePasswordException("Fallo en el algoritmo de cifrado de passwords");
        }
        return generatedPassword;
    }

    public static Usuario findByIdUsuario(int idUsuario) {
        UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create();
        UsuarioEntidad usuEnt = dao.findByPrimaryKey(idUsuario);

        if (usuEnt != null) {
            Usuario usu = new Usuario();
            ObjectMapper.copyProperties(usuEnt, usu);
            return usu;
        } else {
            return Usuario.NOT_FOUND;
        }
    }

    public static Usuario findByNombre(String nombre) {
        UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create();
        UsuarioEntidad usuEnt[] = dao.findWhereNombreEquals(Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo"));

        if (usuEnt.length > 0) {
            Usuario usu = new Usuario();
            ObjectMapper.copyProperties(usuEnt[0], usu);
            return usu;
        } else {
            return Usuario.NOT_FOUND;
        }
    }

    /**
     * Intenta hacer un login buscando el nombre de usuario en la base de datos
     * y comparando si ambos passwords coinciden. Devuelve NOT_FOUND en caso de
     * no coincidir y el objeto Usuario en caso favorable.
     *
     * @param nombre
     * @param password
     * @return Usuario
     * @throws UsuarioEncodePasswordException
     */
    public static Usuario tryLogin(String nombre, String password) throws UsuarioEncodePasswordException {
        UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create();
        UsuarioEntidad usuEnt[] = dao.findWhereNombreEquals(Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo"));

        String passEncoded = encodePassword(password);

        if (usuEnt.length > 0) {
            Usuario usu = new Usuario();
            ObjectMapper.copyProperties(usuEnt[0], usu);
            if (usu.password.equals(passEncoded)) {
                return usu;
            } else {
                return Usuario.NOT_FOUND;
            }
        } else {
            return Usuario.NOT_FOUND;
        }
    }

    public static List<Usuario> findAll() {
        UsuarioEntidadDao dao = UsuarioEntidadDaoFactory.create();
        UsuarioEntidad[] listaUsuEnt = dao.findAll();
        List<Usuario> dest = new ArrayList();
        ObjectMapper.copyProperties(listaUsuEnt, dest);
        return dest;
    }

    /**
     * Method 'getIdUsuario'
     *
     * @return int
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Method 'setIdUsuario'
     *
     * @param idUsuario
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
        this.nombre = Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo");
    }

    /**
     * Method 'getPassword'
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method 'setPassword'
     *
     * @param password
     */
    public void setPassword(String password) throws UsuarioEncodePasswordException {
        String pass = Objects.requireNonNull(password, "La contraseña del usuario no puede ser nula");
        this.password = encodePassword(pass);
    }

    /**
     * Method 'setPassword'
     *
     * @param password
     */
    public void setPasswordNoEncoding(String password) {
        this.password = Objects.requireNonNull(password, "La contraseña del usuario no puede ser nula");
    }

    /**
     * Method 'getFechaCreacion'
     *
     * @return Date devuelve una copia de la fecha de creación del objeto
     */
    public Date getFechaCreacion() {
        return new Date(fechaCreacion.getTime());
    }

    /**
     * Method 'setFechaCreacion'
     *
     * @param fechaCreacion
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación de un usuario no puede ser nula");
    }

    /**
     * Method 'getFechaModificacion'
     *
     * @return Date devuelve una copia de la fecha de modificación del objeto
     */
    public Date getFechaModificacion() {
        return new Date(fechaModificacion.getTime());
    }

    /**
     * Method 'setFechaModificacion'
     *
     * @param fechaModificacion
     */
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = Objects.requireNonNull(fechaModificacion, "La fecha de modificación de un usuario no puede ser nula");
    }

    public int getPerfilUsuarioIdPerfil() {
        return perfilUsuarioIdPerfil;
    }

    public void setPerfilUsuarioIdPerfil(int perfilUsuarioIdPerfil) {
        this.perfilUsuarioIdPerfil = perfilUsuarioIdPerfil;
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

        if (!(_other instanceof Usuario)) {
            return false;
        }

        final Usuario _cast = (Usuario) _other;
        if (idUsuario != _cast.idUsuario) {
            return false;
        }

        if (nombre == null ? _cast.nombre != nombre : !nombre.equals(_cast.nombre)) {
            return false;
        }

        if (password == null ? _cast.password != password : !password.equals(_cast.password)) {
            return false;
        }

        if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals(_cast.fechaCreacion)) {
            return false;
        }

        if (fechaModificacion == null ? _cast.fechaModificacion != fechaModificacion : !fechaModificacion.equals(_cast.fechaModificacion)) {
            return false;
        }
        
        if (perfilUsuarioIdPerfil != _cast.perfilUsuarioIdPerfil) {
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
        _hashCode = 29 * _hashCode + idUsuario + perfilUsuarioIdPerfil;
        if (nombre != null) {
            _hashCode = 29 * _hashCode + nombre.hashCode();
        }

        if (password != null) {
            _hashCode = 29 * _hashCode + password.hashCode();
        }

        if (fechaCreacion != null) {
            _hashCode = 29 * _hashCode + fechaCreacion.hashCode();
        }

        if (fechaModificacion != null) {
            _hashCode = 29 * _hashCode + fechaModificacion.hashCode();
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
        ret.append("com.bankito.dominio.Usuario: ");
        ret.append("idUsuario=" + idUsuario);
        ret.append(", nombre=" + nombre);
        ret.append(", password=" + password);
        ret.append(", fechaCreacion=" + fechaCreacion);
        ret.append(", fechaModificacion=" + fechaModificacion);
        ret.append(", perfilUsuarioIdPerfil=" + perfilUsuarioIdPerfil);
        return ret.toString();
    }

}
