/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

import com.bankito.dominio.exceptions.ClienteDuplicadoException;
import com.bankito.dominio.exceptions.ClienteNoValidoException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.dominio.exceptions.DominioException;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.CuentaDto;
import com.bankito.servicio.dto.PerfilUsuarioDto;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.util.List;

/**
 * <h1>ServicioBancario</h1>
 * Esta interfaz especifica el conjunto de métodos que se deben desarrollar para 
 * proporcionar un servicio bancario. La clase que implemente esta interfaz 
 * utilizará las clases de la capa de Dominio para desarrollar su funcionalidad.
 * Asimimo, también establece un conjunto de constantes públicas que identifican
 * cada una de las operaciones posibles en la base de datos, de este modo los
 * métodos que lleven asociados una de estas constantes deberán implementar una
 * comprobación sobre los permisos que posee el usuario logado para realizar
 * dicha operación y elevar una excepción si no se tuvieran suficientes privilegios.
 * <p>
 * La clase que implemente esta interfaz debe ser null-safe.
 * <p>
 * Se recomienda utilizar una clase de factoría para la creación de los objetos
 * que implementan esta interfaz para ganar versatilidad y mantenibilidad en 
 * el código que utilice el ServicioBancario
 * 
 * @author Enrique Royo Sánchez
 */
public interface ServicioBancario {
    public static final String OPER_NUEVO_USUARIO = "NUEVO_USUARIO";
    public static final String OPER_ELIMINA_USUARIO = "ELIMINA_USUARIO";
    public static final String OPER_LISTA_USUARIOS = "LISTA_USUARIOS";
    public static final String OPER_BUSCA_USUARIO_POR_NIF = "BUSCA_USUARIO_POR_NIF";
    public static final String OPER_BUSCA_USUARIO_POR_ID = "BUSCA_USUARIO_POR_ID";
    public static final String OPER_BUSCA_USUARIO_POR_NOMBRE = "BUSCA_USUARIO_POR_NOMBRE";
    
    public static final String OPER_LISTA_PERFILES = "LISTA_PERFILES";
    public static final String OPER_BUSCA_PERFIL_POR_ID = "BUSCA_PERFIL_POR_ID";
    public static final String OPER_BUSCA_PERFIL_POR_NOMBRE = "BUSCA_PERFIL_POR_NOMBRE";
    
    public static final String OPER_NUEVO_CLIENTE = "NUEVO_CLIENTE";
    public static final String OPER_ELIMINA_CLIENTE = "ELIMINA_CLIENTE";
    public static final String OPER_LISTA_CLIENTES = "LISTA_CLIENTES";
    public static final String OPER_BUSCA_CLIENTE_POR_NIF = "BUSCA_CLIENTE_POR_NIF";
    public static final String OPER_BUSCA_CLIENTE_POR_ID = "BUSCA_CLIENTE_POR_ID";
    public static final String OPER_BUSCA_CLIENTE_POR_ID_USUARIO = "BUSCA_CLIENTE_POR_ID_USUARIO";
    
    public static final String OPER_NUEVA_CUENTA = "NUEVA_CUENTA";
    public static final String OPER_ELIMINA_CUENTA = "ELIMINA_CUENTA";
    public static final String OPER_LISTA_CUENTAS = "LISTA_CUENTAS";
    public static final String OPER_INGRESO_EN_CUENTA = "INGRESO_EN_CUENTA";
    public static final String OPER_RETIRADA_DE_CUENTA = "RETIRADA_DE_CUENTA";
    public static final String OPER_TRANSFERENCIA = "TRANSFERENCIA";
    public static final String OPER_BUSCA_CUENTA_POR_USUARIO = "BUSCA_CUENTA_POR_USUARIO";
    public static final String OPER_BUSCA_CUENTA_POR_CLIENTE = "BUSCA_CUENTA_POR_CLIENTE";
    public static final String OPER_BUSCA_CUENTA_POR_NUM_CUENTA = "BUSCA_CUENTA_POR_NUM_CUENTA";
    public static final String OPER_BUSCA_CUENTA_POR_ID_CUENTA = "BUSCA_CUENTA_POR_ID_CUENTA";
    
    // Nuevo usuario
    public UsuarioDto nuevoUsuario(String nombre, String password, PerfilUsuarioDto perfil) throws UsuarioDuplicadoException, UsuarioNoValidoException, ServicioException;
    // Login usuario
    public UsuarioDto loginUsuario(String nombre, String password) throws UsuarioEncodePasswordException, DominioException, ServicioException;
    // Logout usuario
    public boolean logoutUsuario() throws DominioException, ServicioException;
    // Devuelve si hay usuario logado
    public boolean hayUsuarioLogado(); 
    // Devuelve el usuario logado
    public UsuarioDto getUsuarioLogado();
    // Elimina usuario
    public boolean eliminaUsuario(UsuarioDto usu) throws UsuarioNoValidoException, ServicioException;
    // Listar todos los usuarios
    public List<UsuarioDto> listaUsuarios() throws ServicioException;
    // Buscar usuario por NIF, idusuario, nombre de usuario
    public UsuarioDto buscaUsuarioPorNif(String nif)  throws ServicioException;
    public UsuarioDto buscaUsuarioPorId(int idUsuario)  throws ServicioException;
    public UsuarioDto buscaUsuarioPorNombre(String nombre)  throws ServicioException;

    // Listar todos los perfiles de usuarios
    public List<PerfilUsuarioDto> listaPerfilesUsuarios()  throws ServicioException;
    // Buscar usuario por NIF, idusuario, nombre de usuario
    public PerfilUsuarioDto buscaPerfilUsuarioPorId(int idPerfilUsuario)  throws ServicioException;
    public PerfilUsuarioDto buscaPerfilUsuarioPorNombre(String nombre)  throws ServicioException;
    

    // Nuevo cliente
    public ClienteDto nuevoCliente(String nombre, String apellido1, 
            String apellido2, String nif, String direc, UsuarioDto usu) 
            throws ClienteDuplicadoException, UsuarioNoValidoException, ClienteNoValidoException,
                   ServicioException;
    // Elimina cliente
    public boolean eliminaCliente(ClienteDto cli) throws ClienteNoValidoException, ServicioException;
    // Listar todos los clientes
    public List<ClienteDto> listaClientes() throws ServicioException;
    // Buscar cliente por NIF, idcliente, por idUsuario
    public ClienteDto buscaClientePorNif(String nif) throws ServicioException;
    public ClienteDto buscaClientePorId(int idCliente) throws ServicioException;
    public ClienteDto buscaClientePorIdUsuario(int idUsuario) throws ServicioException;

    // Nueva cuenta
    public CuentaDto nuevaCuenta(UsuarioDto usuDto) throws CuentaNoValidaException, ServicioException;
    // Elimina cuenta
    public boolean eliminaCuenta(CuentaDto c) throws ServicioException;
    // Listar todas las cuentas
    public List<CuentaDto> listaCuentas() throws ServicioException;
    // Ingreso en cuenta
    public boolean ingresoEnCuenta(String concepto, float cantidad, CuentaDto destino) throws ServicioException;
    // Retirada de cuenta
    public boolean retiradaDeCuenta(String concepto, float cantidad, CuentaDto origen) throws ServicioException;
    // Transferencia a otra cuenta
    public boolean transferencia(String concepto, float cantidad, CuentaDto origen, CuentaDto destino) throws ServicioException;
    // Buscar cuenta por usuario, cliente, por numeración, por idCuenta
    public List<CuentaDto> buscaCuentaPorUsuario(UsuarioDto u) throws ServicioException;
    public List<CuentaDto> buscaCuentaPorCliente(ClienteDto c) throws ServicioException;
    public CuentaDto buscaCuentaPorNumCuenta(int entidad, int sucursal, int dc, int numCuenta) throws ServicioException;
    public CuentaDto buscaCuentaPorIdCuenta(int idCuenta) throws ServicioException;
}
