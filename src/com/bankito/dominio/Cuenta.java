package com.bankito.dominio;

import com.bankito.util.ObjectMapper;
import com.bankito.dominio.exceptions.CuentaDuplicadaException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.dominio.exceptions.MovimientoNoValidoException;
import com.bankito.persistencia.dao.ClienteEntidadDao;
import com.bankito.persistencia.dao.CuentaEntidadDao;
import com.bankito.persistencia.dto.ClienteEntidad;
import com.bankito.persistencia.dto.CuentaEntidad;
import com.bankito.persistencia.dto.CuentaEntidadPk;
import com.bankito.persistencia.exceptions.CuentaEntidadDaoException;
import com.bankito.persistencia.exceptions.DaoException;
import com.bankito.persistencia.exceptions.MovimientoEntidadDaoException;
import com.bankito.persistencia.factory.ClienteEntidadDaoFactory;
import com.bankito.persistencia.factory.CuentaEntidadDaoFactory;
import com.bankito.persistencia.jdbc.ResourceManager;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <h1>Cuenta</h1>
 * Esta clase representa la funcionalidad de un objeto Cuenta de la capa de
 * dominio. La cuenta lleva asociado su conjunto de movimientos.
 * <p>
 * Un objeto de esta clase presenta el siguiente comportamiento:<br>
 * - save: es capaz de salvar (insertar/actualizar) su estado en la base de
 * datos así como el de todos sus movimientos. Esta operación se realiza
 * transaccionalmente.<br>
 * - delete: es capaz de borrar su registro asociado en la base de datos así
 * como los registros de movimientos asociados. Esta operación se realiza
 * transaccionalmente.<br>
 * - isValid: es capaz de analizar si el objeto y sus movimientos asociados
 * están en un estado válido<br>
 * - addMovimiento: añade un movimiento a la lista de movimientos de la cuenta
 * actualizando además el saldo de la cuenta<br>
 * - toStringNumCuenta: devuelve una cadena formateada con el número de cuenta
 * bancaria<br>
 * - equals: es capaz de compararse con otro objeto para ver si son iguales<br>
 * - toString: compone una cadena de texto con sus características<br>
 * - setConnection: establece una conexión a la BD gestionada de forma externa
 * para poder hacer control transaccional - resetConnection: elimina la conexión
 * externa a la BD - getConnection: devuelve la conexión externa a la BD que se
 * esté utilizando - getters: obtienen el valor de las propiedades<br>
 * - setters: establecen el valor de la propiedades<br>
 * <p>
 * Esta clase es null-safe, es decir, evita a toda costa que se pueda hacer un
 * manejo de sus instancias de forma que alguno de los campos pueda contener un
 * null indeseado.
 * <p>
 * Esta clase posee un campo público estático y final llamado NOT_FOUND que es
 * un objeto de la clase Cliente que servirá para representar una consulta a la
 * base de datos que no devuelve resultado alguno.
 * <p>
 * Posee un campo público estático final llamado NULL_DATE que servirá para
 * inicializar los campos Date a un valor por defecto que es el 01/01/1970 a las
 * 00:00:00 horas.
 * <p>
 * Además, posee un campo private estático final llamado DELTA que indica el
 * margen de error admisible al comparar dos saldos con equals.
 * <p>
 * Esta clase presenta los siguientes comportamientos como métodos
 * estáticos:<br>
 * - findByNumCuenta: recupera un objeto Cuenta de la BD por su número de
 * cuenta<br>
 * - findByIdUsuario: recupera un objeto Cuenta de la BD por su IdUsuario<br>
 * - findByIdCliente: recupera un objeto Cuenta de la BD por su IdCliente<br>
 * - findByIdCuenta: recupera un objeto Cuenta de la BD por su IdCuenta<br>
 * - findAll: recupera todos los objetos Cuenta de la BD<br>
 *
 * @author Enrique Royo Sánchez
 */
public class Cuenta implements Serializable {

    public static final Cuenta NOT_FOUND = new Cuenta(0, 0, 0, 0, 0);
    public static final Date NULL_DATE = new Date(0);
    public static final float DELTA = 0.001f;
    /**
     * Esta propiedad valdrá 0 para los nuevos objetos que aún no se han salvado
     * en la BD.
     */
    private int idCuenta;
    private int numEntidad;
    private int numSucursal;
    private int numDigitoControl;
    private long numCuenta;
    private float saldo;
    private Date fechaCreacion;
    private int usuarioIdUsuario;
    private List<Movimiento> listaMov;
    /**
     * Si esta propiedad no es nula entonces se habrá establecido una conexión a
     * la BD utilizando el método setConnetion(Connection c) para utilizar los
     * métodos de un objeto Cuenta de forma transaccional. Si esta propiedad es
     * nula entonces se creará y cerrará una conexión nueva en cada operación.
     */
    private Connection conExterna;

    /**
     * Esta propiedad nos indica si listaMov ha sufrido cambios y hay que
     * salvarla en la BD.
     */
    private boolean isListaMovChanged;

    /**
     * Method 'CuentaEntidad'
     *
     */
    public Cuenta(int idUsuario) {
        this.listaMov = new ArrayList<Movimiento>();
        this.fechaCreacion = Cuenta.NULL_DATE;
        this.usuarioIdUsuario = idUsuario;
    }

    public Cuenta(int numEntidad, int numSucursal, int numDigitoControl, long numCuenta, int idUsuario) {
        this.numEntidad = numEntidad;
        this.numSucursal = numSucursal;
        this.numDigitoControl = numDigitoControl;
        this.numCuenta = numCuenta;
        this.usuarioIdUsuario = idUsuario;
        this.listaMov = new ArrayList<Movimiento>();
        this.fechaCreacion = Cuenta.NULL_DATE;
    }

    public boolean isValid() {
        if (this == Cuenta.NOT_FOUND) {
            return false;
        }

        if (numEntidad == 0 && numSucursal == 0 && numDigitoControl == 0 && numCuenta == 0) {
            return false;
        } else if (listaMov == null || this.usuarioIdUsuario == 0) {
            return false;
        } else {
            for (Movimiento m : listaMov) {
                if (!m.isValid()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Este método salva una cuenta y todos sus movimientos de forma
     * transaccional. Si la propiedad conExterna no es nula entonces este método
     * se estará utilizando de forma transaccional desde otra clase, así que se
     * utiliza dicha conexión a la BD pero no hace commit porque se deberá
     * realizar por el objeto "externo" que está usando este método. La primera
     * vez que se salva un objeto Cuenta debe realizarse sin añadirle
     * movimientos, si no fallará.
     */
    public void save() throws CuentaNoValidaException, CuentaDuplicadaException, MovimientoNoValidoException {
        if (this.isValid() == false) {
            throw new CuentaNoValidaException("La cuenta no es válida y no se salvó " + this.toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            conn.setAutoCommit(false);
            CuentaEntidadDao cueDao = CuentaEntidadDaoFactory.create(conn);

            if (idCuenta == 0) // Si no está inicializada entonces inserta en la BD
            {
                if (findByNumCuenta(this.numEntidad, this.numSucursal, this.numDigitoControl, this.numCuenta) != Cuenta.NOT_FOUND) {
                    throw new CuentaDuplicadaException("La cuenta no se salvará porque ya existe " + this.toString());
                }

                this.fechaCreacion = new Date();
                CuentaEntidad cue = new CuentaEntidad();
                ObjectMapper.copyProperties(this, cue);
                CuentaEntidadPk pk;
                pk = cueDao.insert(cue);
                this.idCuenta = pk.getIdCuenta();
                if (listaMov != null) {
                    for (Movimiento m : listaMov) {
                        m.setCuentaIdCuenta(idCuenta);
                    }
                }
            } else // modificamos el registro existente
            {
                CuentaEntidad cue = new CuentaEntidad();
                ObjectMapper.copyProperties(this, cue);
                cueDao.update(cue.createPk(), cue);
            }

            if (isListaMovChanged == true) {
                if (listaMov != null) {
                    for (Movimiento m : listaMov) {
                        m.save(conn);
                    }
                }
                this.isListaMovChanged = false;
            }

            // Si hemos llegado hasta aquí entonces todo ha ido bien
            // Consolidamos los cambios en la BD, solo si la conexión no era externa
            if (!hayConexionExterna) {
                conn.commit();
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar la cuenta " + this.getIdCuenta() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar la cuenta" + this.getIdCuenta() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new CuentaEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }

    /**
     * Este método borra una cuenta y todos sus movimientos de forma
     * transaccional. Si la propiedad conExterna no es nula entonces este método
     * se estará utilizando de forma transaccional desde otra clase, así que se
     * utiliza dicha conexión a la BD pero no hace commit porque se deberá
     * realizar por el objeto "externo" que está usando este método.
     */
    public void delete() throws CuentaNoValidaException, MovimientoNoValidoException {
        if (this.isValid() == false) {
            throw new CuentaNoValidaException("La cuenta no es válida y no se borró " + this.toString());
        }

        Connection conn = null;
        boolean hayConexionExterna = (conExterna != null);
        try {
            if (!hayConexionExterna) {
                conn = ResourceManager.getConnection();
            } else {
                conn = conExterna;
            }

            conn.setAutoCommit(false);
            CuentaEntidadDao cueDao = CuentaEntidadDaoFactory.create(conn);

            if (listaMov != null) {
                for (Movimiento m : listaMov) {
                    m.delete(conn);
                }
            }

            CuentaEntidad cueEnt = new CuentaEntidad();
            ObjectMapper.copyProperties(this, cueEnt);
            cueDao.delete(cueEnt.createPk());

            // Si hemos llegado hasta aquí entonces todo ha ido bien
            // Consolidamos los cambios en la BD, solo si la conexión no era externa
            if (!hayConexionExterna) {
                conn.commit();
            }
        } catch (DaoException ex) {
            try {
                conn.rollback();
                throw new DaoException("Error al salvar la cuenta " + this.getIdCuenta() + " -> Deshacemos los cambios con un rollback" + ex.getMessage());
            } catch (SQLException ex1) {
                throw new DaoException("Error al intentar deshacer los cambios cuando falló al salvar la cuenta" + this.getIdCuenta() + " " + ex1.getMessage());
            }
        } catch (SQLException ex) {
            throw new CuentaEntidadDaoException("Error al conectar con la base de datos" + ex.getMessage());
        } finally {
            if (!hayConexionExterna) {
                ResourceManager.close(conn);
            }
        }
    }

    public void addMovimiento(Movimiento m) {
        Objects.requireNonNull(m, "El movimiento a añadir no puede ser nulo");
        m.setCuentaIdCuenta(idCuenta);
        this.listaMov.add(m);
        this.isListaMovChanged = true;
        if (m.getTipo().equals(Movimiento.TIPO_MOV_ENTRADA)) {
            this.saldo = this.saldo + m.getImporte();
        } else {
            this.saldo = this.saldo - m.getImporte();
        }
    }

    /**
     * Este método devuelve una copia de la lista de movimientos de la cuenta
     *
     * @return List &lt;Movimiento&gt; devuelve una copia de los movimientos de la
     * cuenta
     */
    public List<Movimiento> getMovimientos() {
        return new ArrayList(listaMov);
    }

    public static Cuenta findByNumCuenta(int entidad, int sucursal, int dc, long cuenta) throws CuentaEntidadDaoException, MovimientoEntidadDaoException {
        CuentaEntidadDao dao = CuentaEntidadDaoFactory.create();

        Object[] parametros = new Object[4];
        parametros[0] = new Integer(entidad);
        parametros[1] = new Integer(sucursal);
        parametros[2] = new Integer(dc);
        parametros[3] = new Long(cuenta);
        CuentaEntidad[] cueEnt = dao.findByDynamicWhere("num_entidad = ? AND num_sucursal = ? AND num_digito_control = ? AND num_cuenta = ?", parametros);

        if (cueEnt.length > 0) {
            Cuenta cue = new Cuenta(0);
            ObjectMapper.copyProperties(cueEnt[0], cue);
            List<Movimiento> movRecuperados = Movimiento.findAllByIdCuenta(cue.getIdCuenta());
            cue.listaMov.addAll(movRecuperados);
            return cue;
        } else {
            return Cuenta.NOT_FOUND;
        }
    }

    public static int findMaxNumCuenta(int entidad, int sucursal) throws CuentaEntidadDaoException, MovimientoEntidadDaoException {
        CuentaEntidadDao dao = CuentaEntidadDaoFactory.create();

        Object[] parametros = new Object[2];
        parametros[0] = new Integer(entidad);
        parametros[1] = new Integer(sucursal);
        int max = dao.findMaxNumCuenta(parametros);
                
        return max;
    }
    
    public static List<Cuenta> findByIdCliente(int idCliente) throws CuentaEntidadDaoException, MovimientoEntidadDaoException {
        ClienteEntidadDao dao = ClienteEntidadDaoFactory.create();
        ClienteEntidad cliEnt = dao.findByPrimaryKey(idCliente);
        
        if (cliEnt != null) 
            return findByIdUsuario(cliEnt.getUsuarioIdUsuario());      
        else
            return new ArrayList();
    }

    public static Cuenta findByIdCuenta(int idCuenta) throws CuentaEntidadDaoException, MovimientoEntidadDaoException {
        CuentaEntidadDao dao = CuentaEntidadDaoFactory.create();
        CuentaEntidad cueEnt = dao.findByPrimaryKey(idCuenta);
        
        if (cueEnt != null) {
            Cuenta cue = new Cuenta(cueEnt.getUsuarioIdUsuario());
            ObjectMapper.copyProperties(cueEnt, cue);
            List<Movimiento> movRecuperados = Movimiento.findAllByIdCuenta(cue.getIdCuenta());
            cue.listaMov.addAll(movRecuperados);
            return cue;
        } else {
            return Cuenta.NOT_FOUND;
        }
    }
    
    public static List<Cuenta> findByIdUsuario(int idUsuario) throws CuentaEntidadDaoException, MovimientoEntidadDaoException {
        CuentaEntidadDao dao = CuentaEntidadDaoFactory.create();
        CuentaEntidad cueEnt[] = dao.findByUsuario(idUsuario);
        List<Cuenta> listaCue = new ArrayList();
        ObjectMapper.copyProperties(cueEnt, listaCue);
        for (Cuenta c : listaCue) {
            List<Movimiento> movRecuperados = Movimiento.findAllByIdCuenta(c.getIdCuenta());
            c.listaMov.addAll(movRecuperados);
        }
        return listaCue;
    }

    public static List<Cuenta> findAll() throws CuentaEntidadDaoException {
        CuentaEntidadDao dao = CuentaEntidadDaoFactory.create();
        CuentaEntidad[] listaCueEnt = dao.findAll();
        List<Cuenta> listaCue = new ArrayList();
        ObjectMapper.copyProperties(listaCueEnt, listaCue);
        return listaCue;
    }

    /**
     * Este método devuelve una cadena con número de cuenta formateado con o sin
     * espacios
     *
     * @param conEspacios Si es true entonces se dejará un espacio entre cada
     * grupo de los elementos del número de cuenta
     * @return Devuelve una cadena con el número de cuenta formateado
     * completando con ceros cada grupo de números.
     */
    public String toStringNumCuenta(boolean conEspacios) {
        if (conEspacios) {
            return String.format("%04d %04d %02d %010d", numEntidad, numSucursal, numDigitoControl, numCuenta);
        } else {
            return String.format("%04d%04d%02d%010d", numEntidad, numSucursal, numDigitoControl, numCuenta);
        }
    }

    /**
     * Method 'getIdCuenta'
     *
     * @return int
     */
    public int getIdCuenta() {
        return idCuenta;
    }

    /**
     * Method 'setIdCuenta'
     *
     * @param idCuenta
     */
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    /**
     * Method 'getNumEntidad'
     *
     * @return int
     */
    public int getNumEntidad() {
        return numEntidad;
    }

    /**
     * Method 'setNumEntidad'
     *
     * @param numEntidad
     */
    public void setNumEntidad(int numEntidad) {
        this.numEntidad = numEntidad;
    }

    /**
     * Method 'getNumSucursal'
     *
     * @return int
     */
    public int getNumSucursal() {
        return numSucursal;
    }

    /**
     * Method 'setNumSucursal'
     *
     * @param numSucursal
     */
    public void setNumSucursal(int numSucursal) {
        this.numSucursal = numSucursal;
    }

    /**
     * Method 'getNumDigitoControl'
     *
     * @return int
     */
    public int getNumDigitoControl() {
        return numDigitoControl;
    }

    /**
     * Method 'setNumDigitoControl'
     *
     * @param numDigitoControl
     */
    public void setNumDigitoControl(int numDigitoControl) {
        this.numDigitoControl = numDigitoControl;
    }

    /**
     * Method 'getNumCuenta'
     *
     * @return long
     */
    public long getNumCuenta() {
        return numCuenta;
    }

    /**
     * Method 'setNumCuenta'
     *
     * @param numCuenta
     */
    public void setNumCuenta(long numCuenta) {
        this.numCuenta = numCuenta;
    }

    /**
     * Method 'setSaldo'
     *
     * @param saldo
     */
    public void setSaldo(double saldo) {
        this.saldo = (float) saldo;
    }

    /**
     * Method 'getSaldo'
     *
     * @return float
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * Method 'getFechaCreacion'
     *
     * @return Date devuelve la copia del campo fechaCreacion
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
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación de una cuenta no puede ser nula");
    }

    /**
     * Method 'getUsuarioIdUsuario'
     *
     * @return int
     */
    public int getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public void setUsuarioIdUsuario(int usuarioIdUsuario) {
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    public void resetConnection() {
        this.conExterna = null;
    }

    public void setConnection(Connection conn) {
        this.conExterna = Objects.requireNonNull(conn, "La conexión no puede ser nula");
    }

    public Connection getConnection() {
        return conExterna;
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

        if (!(_other instanceof Cuenta)) {
            return false;
        }

        final Cuenta _cast = (Cuenta) _other;
        if (idCuenta != _cast.idCuenta) {
            return false;
        }

        if (numEntidad != _cast.numEntidad) {
            return false;
        }

        if (numSucursal != _cast.numSucursal) {
            return false;
        }

        if (numDigitoControl != _cast.numDigitoControl) {
            return false;
        }

        if (numCuenta != _cast.numCuenta) {
            return false;
        }

        if (Math.abs(saldo - _cast.saldo) >= DELTA) {
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
        _hashCode = 29 * _hashCode + idCuenta;
        _hashCode = 29 * _hashCode + numEntidad;
        _hashCode = 29 * _hashCode + numSucursal;
        _hashCode = 29 * _hashCode + numDigitoControl;
        _hashCode = 29 * _hashCode + (int) (numCuenta ^ (numCuenta >>> 32));
        _hashCode = 29 * _hashCode + Float.floatToIntBits(saldo);
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
        ret.append("com.bankito.persistencia.dto.CuentaEntidad: ");
        ret.append("idCuenta=" + idCuenta);
        ret.append(", numEntidad=" + numEntidad);
        ret.append(", numSucursal=" + numSucursal);
        ret.append(", numDigitoControl=" + numDigitoControl);
        ret.append(", numCuenta=" + numCuenta);
        ret.append(", saldo=" + saldo);
        ret.append(", fechaCreacion=" + fechaCreacion);
        ret.append(", usuarioIdUsuario=" + usuarioIdUsuario);
        return ret.toString();
    }

}
