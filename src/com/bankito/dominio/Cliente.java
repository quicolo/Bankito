package com.bankito.dominio;

import com.bankito.util.ObjectMapper;
import com.bankito.dominio.exceptions.*;
import com.bankito.persistencia.dao.ClienteEntidadDao;
import com.bankito.persistencia.dto.ClienteEntidad;
import com.bankito.persistencia.dto.ClienteEntidadPk;
import com.bankito.persistencia.exceptions.CuentaEntidadDaoException;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.factory.ClienteEntidadDaoFactory;
import com.bankito.persistencia.jdbc.ResourceManager;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
* <h1>Cliente</h1>
* Esta clase representa la funcionalidad de un objeto Cliente de la capa de dominio. 
* <p>
* Un objeto de esta clase presenta el siguiente comportamiento:<br>
* - save: es capaz de salvar (insertar/actualizar) su estado en la base de datos<br>
* - delete: es capaz de borrar su registro asociado en la base de datos<br>
* - isValid: es capaz de analizar si el objeto está en un estado válido<br>
* - equals: es capaz de compararse con otro objeto para ver si son iguales<br>
* - toString: compone una cadena de texto con sus características<br>
* - getters: obtienen el valor de las propiedades<br>
* - setters: establecen el valor de la propiedades<br>
* <p>
* Esta clase es null-safe, es decir, evita a toda costa que se pueda hacer un manejo
* de sus instancias de forma que alguno de los campos pueda contener un null 
* indeseado.
* <p>
* Esta clase posee un campo público estático y final llamado NOT_FOUND que es un objeto
* de la clase Cliente que servirá para representar una consulta a la base de datos
* que no devuelve resultado alguno.
* <p>
* Además, posee un campo público estático final llamado NULL_DATE que servirá
* para inicializar los campos Date a un valor por defecto que es el 01/01/1970 
* a las 00:00:00 horas. 
* <p>
* Esta clase presenta los siguientes comportamientos como métodos estáticos:<br>
* - findByIdCliente: recupera un objeto de la clase de la BD por su IdCliente<br>
* - findByIdUsuario: recupera un objeto de la clase de la BD por su IdUsuario<br>
* - findAll: recupera todos los objetos de la clase de la BD<br>
*
* @author  Enrique Royo Sánchez
*/
public class Cliente implements Serializable
{
        public static final Cliente NOT_FOUND = new Cliente("vacio", "vacio", "vacio", "vacio", "vacio");
        public static final Date NULL_DATE = new Date(0);
        private static final String MSG_NOMBRE_NULO = "El nombre del cliente no puede ser nulo";
        private static final String MSG_APELLIDO1_NULO = "El apellido1 del cliente no puede ser nulo";
        private static final String MSG_APELLIDO2_NULO = "El apellido2 del cliente no puede ser nulo";
        private static final String MSG_NIF_NULO = "El NIF del cliente no puede ser nulo";
        private static final String MSG_DIRECCION_COMPLETA_NULO = "La dirección del cliente no puede ser nulo";
        private static final String MSG_FECHA_CREACION_NULO="La fecha de creación del cliente no puede ser nula";
        private static final String MSG_FECHA_MODIFICACION_NULO="La fecha de modificación del cliente no puede ser nula";
        /**
         * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado en la BD
         */
        private int idCliente=0;
        private String nombre="";
        private String apellido1="";
        private String apellido2="";
        private String nif="";
        private String direccionCompleta="";
        private Date fechaCreacion;
        private Date fechaModificacion;
        private int usuarioIdUsuario=0;
        /**
        * Si esta propiedad no es nula entonces se habrá establecido una conexión a la BD utilizando el método
        * setConnetion(Connection c) para utilizar los métodos de un objeto Cliente de forma transaccional.
        * Si esta propiedad es nula entonces se creará y cerrará una conexión nueva en cada operación.
        */
        private Connection conExterna;
        
        
        
	public Cliente() {
            this.fechaCreacion = Cliente.NULL_DATE;
            this.fechaModificacion = Cliente.NULL_DATE;
	}

        public Cliente(String nombre, String apellido1, String apellido2, String nif, String direccionCompleta) {
            this.nombre = Objects.requireNonNull(nombre, MSG_NOMBRE_NULO);
            this.apellido1 = Objects.requireNonNull(apellido1, MSG_APELLIDO1_NULO);
            this.apellido2 = Objects.requireNonNull(apellido2, MSG_APELLIDO1_NULO);
            this.nif = Objects.requireNonNull(nif, MSG_NIF_NULO).toUpperCase();
            this.direccionCompleta = Objects.requireNonNull(direccionCompleta, MSG_DIRECCION_COMPLETA_NULO);
            this.fechaCreacion = Cliente.NULL_DATE;
            this.fechaModificacion = Cliente.NULL_DATE;
        }
        
        public boolean isValid() {        
            if (this == Cliente.NOT_FOUND)
                return false;

            // Si alguno de los campos obligatorios es nulo o vacío no lo consideramos válido
            if (nombre==null || apellido1==null || apellido2 == null || nif == null || direccionCompleta == null)
                return false;
            else
                if (nombre.equals("") || apellido1.equals("") || nif.equals("") || direccionCompleta.equals(""))
                    return false;
                else
                    return true;
            
        }

        
        public void save() throws ClienteDuplicadoException, ClienteNoValidoException {
            if(this.isValid()==false)
                throw new ClienteNoValidoException("Cliente no salvado por estado no válido: "+toString());
                        
            Connection conn=null;
            boolean hayConexionExterna = (conExterna!=null);
            try {
                if (!hayConexionExterna)
                    conn = ResourceManager.getConnection();
                else
                    conn = conExterna;
            
                ClienteEntidadDao dao = ClienteEntidadDaoFactory.create(conn);
                if(idCliente == 0) // Si no está inicializado entonces inserta en la BD
                {
                    if (existsClienteByNif(this.nif))
                        throw new ClienteDuplicadoException("El cliente con NIF="+this.nif+" ya existe");

                    this.fechaCreacion = new Date();
                    ClienteEntidad cli = new ClienteEntidad();
                    ObjectMapper.copyProperties(this, cli);
                    ClienteEntidadPk pk = dao.insert(cli);                
                    this.idCliente = pk.getIdCliente();
                }
                else  // modificamos el registro existente
                {
                    this.fechaModificacion = new Date();
                    ClienteEntidad cli = new ClienteEntidad();
                    ObjectMapper.copyProperties(this, cli);
                    dao.update(cli.createPk(), cli);
                }
            } catch (DaoException ex) {
                try {
                    conn.rollback();
                    throw new DaoException("Error al salvar el cliente "+this.getIdCliente()+" -> Deshacemos los cambios con un rollback"+ex.getMessage());
                } 
                catch (SQLException ex1) {
                    throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar el cliente "+this.getIdCliente()+" "+ex1.getMessage());
                }
            } catch (SQLException ex) {
                throw new CuentaEntidadDaoException("Error al conectar con la base de datos"+ex.getMessage());
            }
            finally {
                if (!hayConexionExterna)
                    ResourceManager.close(conn);
            } 
            
        }
        
        public void delete() throws ClienteNoValidoException{
            if(this.isValid()==false)
                throw new ClienteNoValidoException("Cliente no borrado por estado no válido: "+toString());
            
            Connection conn=null;
            boolean hayConexionExterna = (conExterna!=null);
            try {
                if (!hayConexionExterna)
                    conn = ResourceManager.getConnection();
                else
                    conn = conExterna;
            
                ClienteEntidadDao dao = ClienteEntidadDaoFactory.create(conn);
                ClienteEntidad cli = new ClienteEntidad();
                ObjectMapper.copyProperties(this, cli);
                dao.delete(cli.createPk());
            } catch (DaoException ex) {
                try {
                    conn.rollback();
                    throw new DaoException("Error al borrar el cliente "+this.getIdCliente()+" -> Deshacemos los cambios con un rollback"+ex.getMessage());
                } 
                catch (SQLException ex1) {
                    throw new DaoException("Error al intentar deshacer los cambios cuando falló al borrar el cliente "+this.getIdCliente()+" "+ex1.getMessage());
                }
            } catch (SQLException ex) {
                throw new CuentaEntidadDaoException("Error al conectar con la base de datos"+ex.getMessage());
            }
            finally {
                if (!hayConexionExterna)
                    ResourceManager.close(conn);
            } 
            
        }
        
        
        public static Cliente findByIdCliente(int idCliente) {
            ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
            ClienteEntidad cliEnt = dao.findByPrimaryKey(idCliente);
            Cliente cli = new Cliente();
            
            if (cliEnt != null) {            
                ObjectMapper.copyProperties(cliEnt, cli);
                return cli;
            }
            else
                return Cliente.NOT_FOUND;
        }
        
        public static Cliente findByIdUsuario(int idUsuario) {
            ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
            ClienteEntidad cliEnt[] = dao.findByUsuario(idUsuario);
            Cliente cli = new Cliente();
            
            if (cliEnt.length != 0) {
                ObjectMapper.copyProperties(cliEnt[0], cli);
                return cli;
            }
            else
                return Cliente.NOT_FOUND;
        }
        /**
         * findByIdNif busca un Cliente en la BD por su Nif y lo devuelve,
         * si lo encuentra. En caso contrario, devuelve una referencia a 
         * Cliente.NOT_FOUND.
         * @param nif
         * @return Cliente
         */
        public static Cliente findByNif(String nif) {
            ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
            ClienteEntidad cliEnt[] = dao.findWhereNifEquals(nif);
            Cliente cli = new Cliente();
            
            if (cliEnt.length != 0) {
                ObjectMapper.copyProperties(cliEnt[0], cli);
                return cli;
            }
            else
                return Cliente.NOT_FOUND;
        }
        
        
        public static boolean existsClienteByNif(String nif) {
            ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
            Cliente cli = findByNif(nif);
            return cli.isValid();
        }
        
        public static List<Cliente> findAll() {
            ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
            ClienteEntidad[] listaCliEnt = dao.findAll();
            List<Cliente> listaCli = new ArrayList();
            ObjectMapper.copyProperties(listaCliEnt, listaCli);
            return listaCli;
        }
        
        
	/**
	 * Method 'getIdCliente'
	 * 
	 * @return int 
	 */
	public int getIdCliente()
	{
		return idCliente;
	}

	/**
	 * Method 'setIdCliente'
	 * 
	 * @param idCliente
	 */
	public void setIdCliente(int idCliente)
	{
		this.idCliente = idCliente;
	}

	/**
	 * Method 'getNombre'
	 * 
	 * @return String 
	 */
	public String getNombre()
	{
            return nombre;
	}

	/**
	 * Method 'setNombre'
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = Objects.requireNonNull(nombre, MSG_NOMBRE_NULO);
	}

	/**
	 * Method 'getApellido1'
	 * 
	 * @return String 
	 */
	public String getApellido1()
	{
            return apellido1;
	}

	/**
	 * Method 'setApellido1'
	 * 
	 * @param apellido1
	 */
	public void setApellido1(String apellido1)
	{
		this.apellido1 = Objects.requireNonNull(apellido1, MSG_APELLIDO1_NULO);
	}

	/**
	 * Method 'getApellido2'
	 * 
	 * @return String
	 */
	public String getApellido2()
	{
            return apellido2;
	}

	/**
	 * Method 'setApellido2'
	 * 
	 * @param apellido2
	 */
	public void setApellido2(String apellido2)
	{
            this.apellido2 = Objects.requireNonNull(apellido2, MSG_APELLIDO2_NULO);
	}

	/**
	 * Method 'getNif'
	 * 
	 * @return String 
	 */
	public String getNif()
	{
            return nif;
	}

	/**
	 * Method 'setNif'
	 * 
	 * @param nif
	 */
	public void setNif(String nif)
	{
            this.nif = Objects.requireNonNull(nif, MSG_NIF_NULO).toUpperCase();
	}

	/**
	 * Method 'getDireccionCompleta'
	 * 
	 * @return String 
	 */
	public String getDireccionCompleta()
	{
            return direccionCompleta;
	}

	/**
	 * Method 'setDireccionCompleta'
	 * 
	 * @param direccionCompleta
	 */
	public void setDireccionCompleta(String direccionCompleta)
	{
            this.direccionCompleta = Objects.requireNonNull(direccionCompleta, MSG_DIRECCION_COMPLETA_NULO);
	}

	/**
	 * Method 'getFechaCreacion'
	 * 
	 * @return Date devuelve una copia de la fecha de creación del objeto
	 */
	public Date getFechaCreacion()
	{
            return new Date(fechaCreacion.getTime());
	}

	/**
	 * Method 'setFechaCreacion'
	 * 
	 * @param fechaCreacion
	 */
	public void setFechaCreacion(Date fechaCreacion)
	{
            this.fechaCreacion = Objects.requireNonNull(fechaCreacion, MSG_FECHA_CREACION_NULO);
	}

	/**
	 * Method 'getFechaModificacion' 
	 * 
	 * @return Date devuelve una copia de la fecha de modificación del objeto
	 */
	public Date getFechaModificacion()
	{
            return new Date(fechaModificacion.getTime());
	}

	/**
	 * Method 'setFechaModificacion'
	 * 
	 * @param fechaModificacion
	 */
	public void setFechaModificacion(Date fechaModificacion)
	{
            this.fechaModificacion = Objects.requireNonNull(fechaModificacion, MSG_FECHA_MODIFICACION_NULO);
	}

	/**
	 * Method 'getUsuarioIdUsuario'
	 * 
	 * @return int
	 */
	public int getUsuarioIdUsuario()
	{
		return usuarioIdUsuario;
	}

	/**
	 * Method 'setUsuarioIdUsuario'
	 * 
	 * @param usuarioIdUsuario
	 */
	public void setUsuarioIdUsuario(int usuarioIdUsuario)
	{
		this.usuarioIdUsuario = usuarioIdUsuario;
	}

        public void setConnection(Connection conn) {
            this.conExterna = conn;
        }
        
        public Connection getConnection () {
            return conExterna;
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
		
		if (!(_other instanceof Cliente)) {
			return false;
		}
		
		final Cliente _cast = (Cliente) _other;
		if (idCliente != _cast.idCliente) {
			return false;
		}
		
		if (nombre == null ? _cast.nombre != nombre : !nombre.equals( _cast.nombre )) {
			return false;
		}
		
		if (apellido1 == null ? _cast.apellido1 != apellido1 : !apellido1.equals( _cast.apellido1 )) {
			return false;
		}
		
		if (apellido2 == null ? _cast.apellido2 != apellido2 : !apellido2.equals( _cast.apellido2 )) {
			return false;
		}
		
		if (nif == null ? _cast.nif != nif : !nif.equals( _cast.nif )) {
			return false;
		}
		
		if (direccionCompleta == null ? _cast.direccionCompleta != direccionCompleta : !direccionCompleta.equals( _cast.direccionCompleta )) {
			return false;
		}
		
		if (fechaCreacion == null ? _cast.fechaCreacion != fechaCreacion : !fechaCreacion.equals( _cast.fechaCreacion )) {
			return false;
		}
		
		if (fechaModificacion == null ? _cast.fechaModificacion != fechaModificacion : !fechaModificacion.equals( _cast.fechaModificacion )) {
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
	public int hashCode()
	{
		int _hashCode = 0;
		_hashCode = 29 * _hashCode + idCliente;
		if (nombre != null) {
			_hashCode = 29 * _hashCode + nombre.hashCode();
		}
		
		if (apellido1 != null) {
			_hashCode = 29 * _hashCode + apellido1.hashCode();
		}
		
		if (apellido2 != null) {
			_hashCode = 29 * _hashCode + apellido2.hashCode();
		}
		
		if (nif != null) {
			_hashCode = 29 * _hashCode + nif.hashCode();
		}
		
		if (direccionCompleta != null) {
			_hashCode = 29 * _hashCode + direccionCompleta.hashCode();
		}
		
		if (fechaCreacion != null) {
			_hashCode = 29 * _hashCode + fechaCreacion.hashCode();
		}
		
		if (fechaModificacion != null) {
			_hashCode = 29 * _hashCode + fechaModificacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + usuarioIdUsuario;
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
		ret.append( "com.bankito.dominio.Cliente: " );
		ret.append( "idCliente=" + idCliente );
		ret.append( ", nombre=" + nombre );
		ret.append( ", apellido1=" + apellido1 );
		ret.append( ", apellido2=" + apellido2 );
		ret.append( ", nif=" + nif );
		ret.append( ", direccionCompleta=" + direccionCompleta );
		ret.append( ", fechaCreacion=" + fechaCreacion );
		ret.append( ", fechaModificacion=" + fechaModificacion );
		ret.append( ", usuarioIdUsuario=" + usuarioIdUsuario );
		return ret.toString();
	}

}
