/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

import com.bankito.dominio.Cliente;
import com.bankito.dominio.Cuenta;
import com.bankito.dominio.Movimiento;
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
import com.bankito.util.AppConfiguration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kike
 */
class ServicioBancarioImpl implements ServicioBancario {
    
    public static final int COD_ENTIDAD_CONFIG = Integer.parseInt(AppConfiguration.getProperty("COD_ENTIDAD", "101"));
    public static final int COD_SUCURSAL_CONFIG = Integer.parseInt(AppConfiguration.getProperty("COD_SUCURSAL", "202"));
    
    @Override
    public UsuarioDto nuevoUsuario(String nombre, String password) 
           throws UsuarioDuplicadoException, UsuarioNoValidoException {
        
        Usuario usu = new Usuario(nombre, password);
        usu.save();
        UsuarioDto dto = new UsuarioDto(usu);
        return dto;
    }
    
    @Override
    public UsuarioDto loginUsuario(String nombre, String password) throws UsuarioEncodePasswordException {
        Usuario usu = Usuario.tryLogin(nombre, password);
        if(usu == Usuario.NOT_FOUND)
            return UsuarioDto.NOT_FOUND;
        else
            return new UsuarioDto(usu);
    }

    @Override
    public boolean eliminaUsuario(UsuarioDto usu) throws UsuarioNoValidoException {
        Usuario u = Usuario.findByIdUsuario(usu.getIdUsuario());
        if (u == Usuario.NOT_FOUND)
           return false;
        else {
            u.delete();
            return true;
        }
    }

    @Override
    public List<UsuarioDto> listaUsuarios() {
        List<Usuario> listaUsu = Usuario.findAll();
        List<UsuarioDto> listaDto = new ArrayList();
        
        for(Usuario u : listaUsu) {
            UsuarioDto dto = new UsuarioDto(u);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public UsuarioDto buscaUsuarioPorNif(String nif) {
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
    public UsuarioDto buscaUsuarioPorId(int idUsuario) {
        Usuario usu = Usuario.findByIdUsuario(idUsuario);
        if (usu == Usuario.NOT_FOUND)
            return UsuarioDto.NOT_FOUND;
        else
            return new UsuarioDto(usu);
    }

    @Override
    public UsuarioDto buscaUsuarioPorNombre(String nombre) {
        Usuario usu = Usuario.findByNombre(nombre);
        
        if (usu == Usuario.NOT_FOUND)
            return UsuarioDto.NOT_FOUND;
        else
            return new UsuarioDto(usu);
    }
    
    @Override
    public ClienteDto nuevoCliente(String nombre, String apellido1, 
            String apellido2, String nif, String direc, UsuarioDto uDto)
            throws ClienteDuplicadoException, UsuarioNoValidoException, ClienteNoValidoException {
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
    public boolean eliminaCliente(ClienteDto cliDto) throws ClienteNoValidoException {
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
    public List<ClienteDto> listaClientes() {
        List<Cliente> listaCli = Cliente.findAll();
        List<ClienteDto> listaDto = new ArrayList();
        
        for(Cliente u : listaCli) {
            ClienteDto dto = new ClienteDto(u);
            listaDto.add(dto);
        }
        return listaDto;
    }
    
    @Override
    public ClienteDto buscaClientePorNif(String nif) {
        Objects.requireNonNull(nif, "El NIF del cliente no puede ser nulo");
        Cliente cli = Cliente.findByNif(nif);
        if (cli == Cliente.NOT_FOUND) 
            return ClienteDto.NOT_FOUND;
        else
            return new ClienteDto(cli);
    }
    
    @Override
    public ClienteDto buscaClientePorId(int idCliente) {
        Cliente cli = Cliente.findByIdCliente(idCliente);
        if (cli == Cliente.NOT_FOUND) 
            return ClienteDto.NOT_FOUND;
        else
            return new ClienteDto(cli);
    }
    
    @Override
    public CuentaDto nuevaCuenta(UsuarioDto usuDto) throws CuentaNoValidaException {
        
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
    
    private int generaAleatorio(int tope) {
        Random aleatorio = new Random(System.currentTimeMillis());
        
        return aleatorio.nextInt(tope+1);
    }
    
    private int generaNumCuenta() {
        return Cuenta.findMaxNumCuenta(COD_ENTIDAD_CONFIG, COD_SUCURSAL_CONFIG)+1;
    }
    
    @Override
    public boolean eliminaCuenta(CuentaDto c) {
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
    public List<CuentaDto> listaCuentas() {
        List<Cuenta> lista = Cuenta.findAll();
        List<CuentaDto> listaDto = new ArrayList();
        
        for(Cuenta c : lista) {
            CuentaDto dto = new CuentaDto(c);
            listaDto.add(dto);
        }
        return listaDto;
    }

    @Override
    public boolean ingresoEnCuenta(String concepto, float cantidad, CuentaDto destino) {
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
    public boolean retiradaDeCuenta(String concepto, float cantidad, CuentaDto origen) {
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
    public boolean transferencia(String concepto, float cantidad, CuentaDto origen, CuentaDto destino) {
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
    public List<CuentaDto> buscaCuentaPorUsuario(UsuarioDto u) {
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
    public List<CuentaDto> buscaCuentaPorCliente(ClienteDto c) {
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
    public CuentaDto buscaCuentaPorNumCuenta(int entidad, int sucursal, int dc, int numCuenta) {
        Cuenta c = Cuenta.findByNumCuenta(entidad, sucursal, dc, numCuenta);
        if (c == Cuenta.NOT_FOUND)
            return CuentaDto.NOT_FOUND;
        else
            return new CuentaDto(c);
    }

    @Override
    public CuentaDto buscaCuentaPorIdCuenta(int idCuenta) {
        Cuenta c = Cuenta.findByIdCuenta(idCuenta);
        if (c == Cuenta.NOT_FOUND)
            return CuentaDto.NOT_FOUND;
        else
            return new CuentaDto(c);
    }
    
}
