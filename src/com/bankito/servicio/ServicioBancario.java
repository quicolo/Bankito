/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

import com.bankito.dominio.exceptions.ClienteDuplicadoException;
import com.bankito.dominio.exceptions.ClienteNoValidoException;
import com.bankito.dominio.exceptions.CuentaNoValidaException;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.CuentaDto;
import com.bankito.servicio.dto.UsuarioDto;
import java.util.List;

/**
 *
 * @author Kike
 */
public interface ServicioBancario {
    // Nuevo usuario
    public UsuarioDto nuevoUsuario(String nombre, String password) throws UsuarioDuplicadoException, UsuarioNoValidoException;
    // Login usuario
    public UsuarioDto loginUsuario(String nombre, String password) throws UsuarioEncodePasswordException;
    // Elimina usuario
    public boolean eliminaUsuario(UsuarioDto usu) throws UsuarioNoValidoException;
    // Listar todos los usuarios
    public List<UsuarioDto> listaUsuarios();
    // Buscar usuario por NIF, idusuario, nombre de usuario
    public UsuarioDto buscaUsuarioPorNif(String nif);
    public UsuarioDto buscaUsuarioPorId(int idUsuario);
    public UsuarioDto buscaUsuarioPorNombre(String nombre);


    // Nuevo cliente
    public ClienteDto nuevoCliente(String nombre, String apellido1, 
            String apellido2, String nif, String direc, UsuarioDto usu) 
            throws ClienteDuplicadoException, UsuarioNoValidoException, ClienteNoValidoException;
    // Elimina cliente
    public boolean eliminaCliente(ClienteDto cli) throws ClienteNoValidoException;
    // Listar todos los clientes
    public List<ClienteDto> listaClientes();
    // Buscar cliente por NIF, idcliente
    public ClienteDto buscaClientePorNif(String nif);
    public ClienteDto buscaClientePorId(int idCliente);
   
    
    // Nueva cuenta
    public CuentaDto nuevaCuenta(UsuarioDto usuDto) throws CuentaNoValidaException;
    // Elimina cuenta
    public boolean eliminaCuenta(CuentaDto c);
    // Listar todas las cuentas
    public List<CuentaDto> listaCuentas();
    // Ingreso en cuenta
    public boolean ingresoEnCuenta(String concepto, float cantidad, CuentaDto destino);
    // Retirada de cuenta
    public boolean retiradaDeCuenta(String concepto, float cantidad, CuentaDto origen);
    // Transferencia a otra cuenta
    public boolean transferencia(String concepto, float cantidad, CuentaDto origen, CuentaDto destino);
    // Buscar cuenta por usuario, cliente, por numeración, por idCuenta
    public List<CuentaDto> buscaCuentaPorUsuario(UsuarioDto u);
    public List<CuentaDto> buscaCuentaPorCliente(ClienteDto c);
    public CuentaDto buscaCuentaPorNumCuenta(int entidad, int sucursal, int dc, int numCuenta);
    public CuentaDto buscaCuentaPorIdCuenta(int idCuenta);
}
