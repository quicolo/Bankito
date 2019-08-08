package com.bankito.servicio;

import com.bankito.dominio.Cliente;
import com.bankito.dominio.Cuenta;
import com.bankito.dominio.Movimiento;
import com.bankito.dominio.Operacion;
import com.bankito.dominio.PerfilUsuario;
import com.bankito.dominio.Sesion;
import com.bankito.dominio.Transferencia;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.dominio.Usuario;
import com.bankito.dominio.exceptions.ClienteDuplicadoException;
import com.bankito.dominio.exceptions.ClienteNoValidoException;
import com.bankito.dominio.exceptions.CuentaDuplicadaException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.dominio.exceptions.DominioException;
import com.bankito.dominio.exceptions.MovimientoNoValidoException;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.CuentaDto;
import com.bankito.servicio.dto.PerfilUsuarioDto;
import com.bankito.servicio.exceptions.OperationNotAllowedException;
import com.bankito.servicio.exceptions.ServicioException;
import com.bankito.servicio.exceptions.UserNotLoggedInException;
import com.bankito.util.AppConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * <h1>ServicioBancarioImpl</h1>
 * Esta clase implementa la interfaz ServicioBancario desarrollando los distintos
 * métodos que contiene. También utiliza las constantes definidas en la interfaz
 * para establecer una comprobación de privilegios del usuario antes de realizar 
 * cada una de las operaciones.
 * 
 * @author Enrique Royo Sánchez
 */
public class ServicioBancarioImpl implements ServicioBancario {
    
    public static final int COD_ENTIDAD_CONFIG = Integer.parseInt(AppConfiguration.getProperty("COD_ENTIDAD", "101"));
    public static final int COD_SUCURSAL_CONFIG = Integer.parseInt(AppConfiguration.getProperty("COD_SUCURSAL", "202"));
    
    private UsuarioDto loggedUser = null;
    private boolean isLoggedUser = false;
    
    @Override
    public UsuarioDto nuevoUsuario(String nombre, String password, PerfilUsuarioDto perfil) 
           throws UsuarioDuplicadoException, UsuarioNoValidoException, ServicioException {
        checkPermissions(ServicioBancario.OPER_NUEVO_USUARIO);
        
        Usuario usu = new Usuario(nombre, password, new PerfilUsuario(perfil));
        usu.save();
        UsuarioDto dto = new UsuarioDto(usu);
        return dto;
    }
    
    @Override
    public UsuarioDto loginUsuario(String nombre, String password) throws UsuarioEncodePasswordException, DominioException, ServicioException {
        Sesion sesion;
        Usuario usu = Usuario.tryLogin(nombre, password);
        
        if(usu == Usuario.NOT_FOUND) {
            return UsuarioDto.NOT_FOUND;
        }
        else {
            if(isLoggedUser == true) {
                sesion = new Sesion(Sesion.LOGOUT_ACTION, this.loggedUser.getIdUsuario());
                sesion.save();
            }
            sesion = new Sesion(Sesion.LOGIN_OK_ACTION, usu.getIdUsuario());
            sesion.save();
            this.loggedUser = new UsuarioDto(usu);
            this.isLoggedUser = true;
            return new UsuarioDto(usu);
        }
    }
    
    @Override
    public boolean logoutUsuario() throws DominioException, ServicioException {
        if (isLoggedUser == true) {
            Sesion sesion = new Sesion(Sesion.LOGOUT_ACTION, loggedUser.getIdUsuario());
            sesion.save();
            this.loggedUser = null;
            this.isLoggedUser = false;
            return true;
        }
        else
            return false;
    }
    
    @Override
    public boolean hayUsuarioLogado() {
        return isLoggedUser;
    }
    
    @Override
    public UsuarioDto getUsuarioLogado() {
        return loggedUser;
    }
    
    @Override
    public boolean eliminaUsuario(UsuarioDto usu) throws UsuarioNoValidoException, ServicioException {
        checkPermissions(ServicioBancario.OPER_ELIMINA_USUARIO);
        Usuario u = Usuario.findByIdUsuario(usu.getIdUsuario());
        if (u == Usuario.NOT_FOUND)
           return false;
        else {
            u.delete();
            return true;
        }
    }

    @Override
    public List<UsuarioDto> listaUsuarios() throws ServicioException {
        checkPermissions(ServicioBancario.OPER_LISTA_USUARIOS);
        List<Usuario> listaUsu = Usuario.findAll();
        List<UsuarioDto> listaDto = new ArrayList();
        
        for(Usuario u : listaUsu) {
            UsuarioDto dto = new UsuarioDto(u);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public UsuarioDto buscaUsuarioPorNif(String nif) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_USUARIO_POR_NIF);
        Cliente cli = Cliente.findByNif(nif);
        
        if (cli == Cliente.NOT_FOUND)
           return UsuarioDto.NOT_FOUND;
        else {
            Usuario usu = Usuario.findByIdUsuario(cli.getUsuarioIdUsuario());
            if (usu == Usuario.NOT_FOUND)
                return UsuarioDto.NOT_FOUND;
            else 
                return new UsuarioDto(usu);
        }
           
    }

    @Override
    public UsuarioDto buscaUsuarioPorId(int idUsuario) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_USUARIO_POR_ID);
        Usuario usu = Usuario.findByIdUsuario(idUsuario);
        if (usu == Usuario.NOT_FOUND)
            return UsuarioDto.NOT_FOUND;
        else
            return new UsuarioDto(usu);
    }

    @Override
    public UsuarioDto buscaUsuarioPorNombre(String nombre) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_USUARIO_POR_NOMBRE);
        Usuario usu = Usuario.findByNombre(nombre);
        
        if (usu == Usuario.NOT_FOUND)
            return UsuarioDto.NOT_FOUND;
        else
            return new UsuarioDto(usu);
    }
    
    @Override
    public List<PerfilUsuarioDto> listaPerfilesUsuarios() throws ServicioException {
        checkPermissions(ServicioBancario.OPER_LISTA_PERFILES);
        List<PerfilUsuario> listaPer = PerfilUsuario.findAll();
        List<PerfilUsuarioDto> listaDto = new ArrayList();
        
        for(PerfilUsuario u : listaPer) {
            PerfilUsuarioDto dto = new PerfilUsuarioDto(u);
            listaDto.add(dto);
        }
        return listaDto;
    }
    
    @Override
    public PerfilUsuarioDto buscaPerfilUsuarioPorId(int idPerfilUsuario) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_PERFIL_POR_ID);
        PerfilUsuario per = PerfilUsuario.findByIdPerfilUsuario(idPerfilUsuario);
        
        if (per == PerfilUsuario.NOT_FOUND)
            return PerfilUsuarioDto.NOT_FOUND;
        else
            return new PerfilUsuarioDto(per);
    }
    
    @Override
    public PerfilUsuarioDto buscaPerfilUsuarioPorNombre(String nombre) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_PERFIL_POR_NOMBRE);
        PerfilUsuario per = PerfilUsuario.findByNombre(nombre);
        
        if (per == PerfilUsuario.NOT_FOUND)
            return PerfilUsuarioDto.NOT_FOUND;
        else
            return new PerfilUsuarioDto(per);
    }
    
    
    
    @Override
    public ClienteDto nuevoCliente(String nombre, String apellido1, 
            String apellido2, String nif, String direc, UsuarioDto uDto)
            throws ClienteDuplicadoException, UsuarioNoValidoException, ClienteNoValidoException,
                   ServicioException {
        checkPermissions(ServicioBancario.OPER_NUEVO_CLIENTE);
        // Comprueba si ya existe el cliente
        Objects.requireNonNull(nif,"El NIF del cliente no puede ser nulo");
        Cliente busqueda = Cliente.findByNif(nif);
        
        if(busqueda == Cliente.NOT_FOUND) {
            Usuario usu = Usuario.findByIdUsuario(uDto.getIdUsuario());
            if(usu == Usuario.NOT_FOUND || !usu.isValid())
                throw new UsuarioNoValidoException("No es un usuario válido para asociarlo a un cliente");
            else{
                Cliente cli = new Cliente(nombre, apellido1, apellido2, nif, direc);
                cli.setUsuarioIdUsuario(usu.getIdUsuario());
                cli.save();
                return new ClienteDto(cli);
            }   
        }
        else
            throw new ClienteDuplicadoException("El cliente con NIF "+nif+" ya existe");
        
    }
    
    @Override
    public boolean eliminaCliente(ClienteDto cliDto) throws ClienteNoValidoException, ServicioException {
        checkPermissions(ServicioBancario.OPER_ELIMINA_CLIENTE);
        Objects.requireNonNull(cliDto, "El objeto ClienteDto no puede ser nulo");
        Cliente cli = Cliente.findByIdCliente(cliDto.getIdCliente());
        if(cli == Cliente.NOT_FOUND) 
            return false;
        else {
            cli.delete();
            return true;
        }
    }
    
    @Override
    public List<ClienteDto> listaClientes() throws ServicioException {
        checkPermissions(ServicioBancario.OPER_LISTA_CLIENTES);
        List<Cliente> listaCli = Cliente.findAll();
        List<ClienteDto> listaDto = new ArrayList();
        
        for(Cliente u : listaCli) {
            ClienteDto dto = new ClienteDto(u);
            listaDto.add(dto);
        }
        return listaDto;
    }
    
    @Override
    public ClienteDto buscaClientePorNif(String nif) throws ServicioException{
        checkPermissions(ServicioBancario.OPER_BUSCA_CLIENTE_POR_NIF);
        Objects.requireNonNull(nif, "El NIF del cliente no puede ser nulo");
        Cliente cli = Cliente.findByNif(nif);
        if (cli == Cliente.NOT_FOUND) 
            return ClienteDto.NOT_FOUND;
        else
            return new ClienteDto(cli);
    }
    
    @Override
    public ClienteDto buscaClientePorId(int idCliente) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CLIENTE_POR_ID);
        Cliente cli = Cliente.findByIdCliente(idCliente);
        if (cli == Cliente.NOT_FOUND) 
            return ClienteDto.NOT_FOUND;
        else
            return new ClienteDto(cli);
    }
    
    @Override
    public ClienteDto buscaClientePorIdUsuario(int idUsuario) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CLIENTE_POR_ID_USUARIO);
        Cliente cli = Cliente.findByIdUsuario(idUsuario);
        if (cli == Cliente.NOT_FOUND) 
            return ClienteDto.NOT_FOUND;
        else
            return new ClienteDto(cli);
    }
    
    @Override
    public CuentaDto nuevaCuenta(UsuarioDto usuDto) throws CuentaNoValidaException, ServicioException {
        checkPermissions(ServicioBancario.OPER_NUEVA_CUENTA);
        Cuenta cue;
        Usuario usu = Usuario.findByIdUsuario(usuDto.getIdUsuario());
        boolean terminado = false;
        final int maxIntentos = 5;
        int intento = 0;
        
        if(usu == Usuario.NOT_FOUND ) 
            return CuentaDto.NOT_FOUND;
        else {
            do {
                int num_dc = generaAleatorio(99);
                int num_cuenta = generaNumCuenta();
                
                cue = new Cuenta(COD_ENTIDAD_CONFIG, COD_SUCURSAL_CONFIG, num_dc, num_cuenta, usuDto.getIdUsuario());
                try {
                    cue.save();
                    terminado = true;
                } catch (CuentaDuplicadaException | MovimientoNoValidoException e) { 
                    ;  // Volveremos a generar otro número aleatorio
                }
                intento++;
            } while(!terminado && intento <= maxIntentos);
            
            if (terminado)
                return new CuentaDto(cue);
            else
                return CuentaDto.NOT_FOUND;
        }
    }
    
    private int generaAleatorio(int tope) throws ServicioException {
        Random aleatorio = new Random(System.currentTimeMillis());
        
        return aleatorio.nextInt(tope+1);
    }
    
    private int generaNumCuenta() throws ServicioException{
        return Cuenta.findMaxNumCuenta(COD_ENTIDAD_CONFIG, COD_SUCURSAL_CONFIG)+1;
    }
    
    @Override
    public boolean eliminaCuenta(CuentaDto c)  throws ServicioException {
        checkPermissions(ServicioBancario.OPER_ELIMINA_CUENTA);
        Objects.requireNonNull(c, "La cuenta a eliminar no puede ser nula");
        
        if (c == CuentaDto.NOT_FOUND)
            return false;
        else {
            Cuenta cue = Cuenta.findByIdCuenta(c.getIdCuenta());
            try {
                cue.delete();
                return true;
            } catch (CuentaNoValidaException | MovimientoNoValidoException ex) {
                return false;
            }
        }
    }

    @Override
    public List<CuentaDto> listaCuentas() throws ServicioException {
        checkPermissions(ServicioBancario.OPER_LISTA_CUENTAS);
        List<Cuenta> lista = Cuenta.findAll();
        List<CuentaDto> listaDto = new ArrayList();
        
        for(Cuenta c : lista) {
            CuentaDto dto = new CuentaDto(c);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public boolean ingresoEnCuenta(String concepto, float cantidad, CuentaDto destino) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_INGRESO_EN_CUENTA);
        Objects.requireNonNull(concepto, "El concepto del ingreso no puede ser nulo");
        Objects.requireNonNull(destino, "La cuenta del ingreso no puede ser nula");
        
        Cuenta cue = Cuenta.findByIdCuenta(destino.getIdCuenta());
        if (cue != Cuenta.NOT_FOUND) {
            Movimiento m = new Movimiento(concepto, Movimiento.TIPO_MOV_ENTRADA, cantidad);
            cue.addMovimiento(m);
            try {
                cue.save();
                return true;
            } catch (DominioException ex) {
                return false;
            }
        }
        else
            return false;
        
    }

    @Override
    public boolean retiradaDeCuenta(String concepto, float cantidad, CuentaDto origen) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_RETIRADA_DE_CUENTA);
        Objects.requireNonNull(concepto, "El concepto de la retirada no puede ser nulo");
        Objects.requireNonNull(origen, "La cuenta de la retirada no puede ser nula");
        
        Cuenta cue = Cuenta.findByIdCuenta(origen.getIdCuenta());
        if (cue != Cuenta.NOT_FOUND) {
            Movimiento m = new Movimiento(concepto, Movimiento.TIPO_MOV_SALIDA, cantidad);
            cue.addMovimiento(m);
            try {
                cue.save();
                return true;
            } catch (DominioException ex) {
                return false;
            }
        }
        else
            return false;
    }

    @Override
    public boolean transferencia(String concepto, float cantidad, CuentaDto origen, CuentaDto destino) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_TRANSFERENCIA);
        Cuenta cueOrig = Cuenta.findByIdCuenta(origen.getIdCuenta());
        Cuenta cueDest = Cuenta.findByIdCuenta(destino.getIdCuenta());
        
        if (cueOrig != Cuenta.NOT_FOUND && cueDest != Cuenta.NOT_FOUND ) {
            
            Transferencia t = new Transferencia(cueOrig, cueDest, cantidad, concepto);
            try {
                t.execute();
                return true;
            } catch (DominioException ex) {
                return false;
            }
            
        }
        else
            return false;
    }

    @Override
    public List<CuentaDto> buscaCuentaPorUsuario(UsuarioDto u) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CUENTA_POR_USUARIO);
        Objects.requireNonNull(u, "El usuario para el que se van a buscar sus cuentas no puede ser nulo");
        
        List<Cuenta> lista = Cuenta.findByIdUsuario(u.getIdUsuario());
        List<CuentaDto> listaDto = new ArrayList();
        for(Cuenta c : lista) {
            CuentaDto dto = new CuentaDto(c);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public List<CuentaDto> buscaCuentaPorCliente(ClienteDto c) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CUENTA_POR_CLIENTE);
        Objects.requireNonNull(c, "El cliente para el que se van a buscar sus cuentas no puede ser nulo");
        
        List<Cuenta> lista = Cuenta.findByIdCliente(c.getIdCliente());
        List<CuentaDto> listaDto = new ArrayList();
        for(Cuenta cue : lista) {
            CuentaDto dto = new CuentaDto(cue);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public CuentaDto buscaCuentaPorNumCuenta(int entidad, int sucursal, int dc, int numCuenta) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CUENTA_POR_NUM_CUENTA);
        
        Cuenta c = Cuenta.findByNumCuenta(entidad, sucursal, dc, numCuenta);
        if (c == Cuenta.NOT_FOUND)
            return CuentaDto.NOT_FOUND;
        else
            return new CuentaDto(c);
    }

    @Override
    public CuentaDto buscaCuentaPorIdCuenta(int idCuenta) throws ServicioException {
        checkPermissions(ServicioBancario.OPER_BUSCA_CUENTA_POR_ID_CUENTA);
        Cuenta c = Cuenta.findByIdCuenta(idCuenta);
        if (c == Cuenta.NOT_FOUND)
            return CuentaDto.NOT_FOUND;
        else
            return new CuentaDto(c);
    }
    
    private boolean isOperationAllowed (UsuarioDto usuDto, String nombreCortoOperacion) {
        Objects.requireNonNull(usuDto, "El usuario a consultar sus permisos no puede ser nulo");
        Objects.requireNonNull(nombreCortoOperacion, "El nombre de la operación no puede ser nulo");
        
        Usuario usu = Usuario.findByIdUsuario(usuDto.getIdUsuario());
        
        PerfilUsuario perfil = PerfilUsuario.findByIdPerfilUsuario(usu.getPerfilUsuarioIdPerfil());
        for(Operacion oper: perfil.getOperPermitidas()) {
            if(nombreCortoOperacion.equals(oper.getNombreCorto()))
                return true;
        }
        return false;        
    }

    private void checkPermissions(String nombreOperacion) throws UserNotLoggedInException, OperationNotAllowedException {
        Objects.requireNonNull(nombreOperacion, "El nombre de la operación no puede ser nulo");
        
        if (this.isLoggedUser == false)
            throw new UserNotLoggedInException("El usuario debe estar logado para realizar esta operación");
        else {
            if (!isOperationAllowed(this.loggedUser, nombreOperacion))
                throw new OperationNotAllowedException("El usuario "+this.loggedUser.getNombre()+" no tiene permisos para realizar la operación "+nombreOperacion);
        }
            
    }
    
}
