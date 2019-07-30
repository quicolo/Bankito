/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.servicio;

import com.bankito.dominio.Cuenta;
import com.bankito.dominio.PerfilUsuario;
import com.bankito.dominio.Usuario;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kike
 */
public class ServicioBancarioImplTest {

    public ServicioBancarioImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nuevoUsuario method, of class ServicioBancarioImpl.
     */
    @Test(expected = UsuarioNoValidoException.class)
    public void testNuevoUsuarioNoValido() throws Exception {
        System.out.println("nuevoUsuario");
        String nombre = "";
        String password = "";
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto result = instance.nuevoUsuario(nombre, password, perfilDto);
        
        instance.eliminaUsuario(result);
    }

    /**
     * Test of nuevoUsuario method, of class ServicioBancarioImpl.
     */
    @Test(expected = UsuarioDuplicadoException.class)
    public void testNuevoUsuarioDuplicado() throws Exception {
        System.out.println("nuevoUsuario");
        String nombre = "Test2"; // Usuario de prueba existente
        String password = "contraseña";
        ServicioBancario instance = ServicioBancarioFactory.create();
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto result = instance.nuevoUsuario(nombre, password, perfilDto);
        instance.eliminaUsuario(result);
    }

    /**
     * Test of nuevoUsuario method, of class ServicioBancarioImpl.
     */
    @Test
    public void testNuevoUsuarioValido() throws Exception {
        System.out.println("nuevoUsuario");
        String nombre = "Prueba";
        String password = "Prueba";
        ServicioBancario instance = ServicioBancarioFactory.create();
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto result = instance.nuevoUsuario(nombre, password, perfilDto);
        assertEquals("Prueba", result.getNombre());
        instance.eliminaUsuario(result);
    }

    /**
     * Test of loginLogoutUsuario methods, of class ServicioBancarioImpl
     * throws
     * UsuarioEncodePasswordException .
     */
    @Test
    public void testLoginLogoutUsuario() throws UsuarioEncodePasswordException, UsuarioDuplicadoException, UsuarioNoValidoException, DominioException, ServicioException {
        System.out.println("loginLogoutUsuario");
        String nombre = "Prueba";
        String password = "Prueba";
        ServicioBancario instance = ServicioBancarioFactory.create();
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto usu = instance.nuevoUsuario(nombre, password, perfilDto);

        UsuarioDto result = instance.loginUsuario(nombre, password);
        assertEquals(usu.getIdUsuario(), result.getIdUsuario());
        assertEquals(usu.getNombre(), result.getNombre());
        
        boolean resLogout = instance.logoutUsuario();
        assertTrue(resLogout);
        instance.eliminaUsuario(usu);

        result = instance.loginUsuario(nombre, "Otra password");
        assertEquals(UsuarioDto.NOT_FOUND, result);

        result = instance.loginUsuario("No existe", "Otra password");
        assertEquals(UsuarioDto.NOT_FOUND, result);

        resLogout = instance.logoutUsuario();
        assertFalse(resLogout);
        
    }

    
    
    
    /**
     * Test of eliminaUsuario method, of class ServicioBancarioImpl.
     */
    @Test
    public void testEliminaUsuarioBorradoValido() throws UsuarioDuplicadoException, UsuarioNoValidoException, ServicioException {
        System.out.println("eliminaUsuario");
        String nombre = "Prueba";
        String password = "Prueba";
        ServicioBancario instance = ServicioBancarioFactory.create();
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto result = instance.nuevoUsuario(nombre, password, perfilDto);
        boolean borrado = instance.eliminaUsuario(result);
        assertTrue(borrado);
        Usuario usu = Usuario.findByNombre("Prueba");
        assertEquals(Usuario.NOT_FOUND, usu);
    }

    /**
     * Test of eliminaUsuario method, of class ServicioBancarioImpl.
     */
    @Test
    public void testEliminaUsuarioBorradoNoEncontrado() throws UsuarioDuplicadoException, UsuarioNoValidoException, ServicioException {
        System.out.println("eliminaUsuario");
        ServicioBancario instance = ServicioBancarioFactory.create();
        UsuarioDto result = new UsuarioDto("No Existe");
        boolean borrado = instance.eliminaUsuario(result);
        assertFalse(borrado);
        Usuario usu = Usuario.findByNombre("No Existe");
        assertEquals(Usuario.NOT_FOUND, usu);
    }

    /**
     * Test of listaUsuarios method, of class ServicioBancarioImpl.
     */
    @Test
    public void testListaUsuarios() throws ServicioException {
        System.out.println("listaUsuarios");
        ServicioBancario instance = ServicioBancarioFactory.create();

        List<UsuarioDto> result = instance.listaUsuarios();
        assertTrue(result.size() >= 2);

        for (UsuarioDto u : result) {
            System.out.println(u);
        }
    }

    /**
     * Test of buscaUsuarioPorNif method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaUsuarioPorNif() throws ServicioException {
        System.out.println("buscaUsuarioPorNif");
        String nif = "12345678A";
        ServicioBancario instance = ServicioBancarioFactory.create();

        UsuarioDto result = instance.buscaUsuarioPorNif(nif);
        assertEquals("Test2", result.getNombre());
        assertEquals(2, result.getIdUsuario());

        result = instance.buscaUsuarioPorNif("NO EXISTE");
        assertEquals(UsuarioDto.NOT_FOUND, result);

    }

    /**
     * Test of buscaUsuarioPorId method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaUsuarioPorId() throws UsuarioDuplicadoException, UsuarioNoValidoException, ServicioException {
        System.out.println("buscaUsuarioPorId");
        int idUsuario = 0;
        ServicioBancario instance = ServicioBancarioFactory.create();
        UsuarioDto expResult = UsuarioDto.NOT_FOUND;
        UsuarioDto result = instance.buscaUsuarioPorId(idUsuario);
        assertEquals(expResult, result);

        String nombre = "Prueba";
        String password = "Prueba";
        
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        
        UsuarioDto insertado = instance.nuevoUsuario(nombre, password, perfilDto);
        assertEquals("Prueba", insertado.getNombre());

        UsuarioDto buscado = instance.buscaUsuarioPorId(insertado.getIdUsuario());
        assertEquals(insertado.getIdUsuario(), buscado.getIdUsuario());
        assertEquals(insertado.getNombre(), buscado.getNombre());
        instance.eliminaUsuario(insertado);

    }

    /**
     * Test of buscaUsuarioPorNombre method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaUsuarioPorNombre() throws ServicioException {
        System.out.println("buscaUsuarioPorNombre");
        String nombre = "Test1";
        ServicioBancario instance = ServicioBancarioFactory.create();

        UsuarioDto result = instance.buscaUsuarioPorNombre(nombre);
        assertEquals("Test1", result.getNombre());
        assertEquals(1, result.getIdUsuario());

        result = instance.buscaUsuarioPorNombre("NO EXISTE");
        assertEquals(UsuarioDto.NOT_FOUND, result);

    }

    /**
     * Test of listaPerfilesUsuarios method, of class ServicioBancarioImpl.
     */
    @Test
    public void testListaPerfilesUsuarios() throws ServicioException {
        System.out.println("listaPerfilesUsuarios");
        ServicioBancario instance = ServicioBancarioFactory.create();

        List<PerfilUsuarioDto> result = instance.listaPerfilesUsuarios();
        assertTrue(result.size() >= 2);

        for (PerfilUsuarioDto u : result) {
            System.out.println(u);
        }
    }
    
    
    /**
     * Test of buscaPerfilUsuarioPorId method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaPerfilUsuarioPorId() throws ServicioException {
        System.out.println("buscaPerfilUsuarioPorId");
        String nombre = "Cliente";
        ServicioBancario instance = ServicioBancarioFactory.create();

        PerfilUsuarioDto result = instance.buscaPerfilUsuarioPorId(3);
        assertEquals(nombre, result.getNombre());
        assertEquals(3, result.getIdPerfilUsuario());

        result = instance.buscaPerfilUsuarioPorNombre("NO EXISTE");
        assertEquals(PerfilUsuarioDto.NOT_FOUND, result);

    }
    
    
    /**
     * Test of buscaPerfilUsuarioPorNombre method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaPerfilUsuarioPorNombre() throws ServicioException {
        System.out.println("buscaPerfilUsuarioPorNombre");
        String nombre = "Cliente";
        ServicioBancario instance = ServicioBancarioFactory.create();

        PerfilUsuarioDto result = instance.buscaPerfilUsuarioPorNombre(nombre);
        assertEquals(nombre, result.getNombre());
        assertEquals(3, result.getIdPerfilUsuario());

        result = instance.buscaPerfilUsuarioPorNombre("NO EXISTE");
        assertEquals(PerfilUsuarioDto.NOT_FOUND, result);

    }
    
    
    
    /**
     * Test of testNuevoCliente method, of class ServicioBancarioImpl.
     */
    @Test
    public void testNuevoCliente() throws UsuarioDuplicadoException, UsuarioNoValidoException, ClienteDuplicadoException, ClienteNoValidoException, ServicioException {
        System.out.println("nuevoCliente");
        ServicioBancario instance = ServicioBancarioFactory.create();

        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        PerfilUsuarioDto perfilDto = new PerfilUsuarioDto(perfil);
        UsuarioDto usuDto = instance.nuevoUsuario("Probando1", "Probando1", perfilDto);
        ClienteDto cliDto = instance.nuevoCliente("Probando1Nombre", "Probando1Ape1", "Probando1Ape2", "NIF", "Direct", usuDto);

        assertEquals("Probando1Nombre", cliDto.getNombre());
        assertEquals("Probando1Ape1", cliDto.getApellido1());
        assertEquals("Probando1Ape2", cliDto.getApellido2());
        assertEquals("NIF", cliDto.getNif());
        assertEquals("Direct", cliDto.getDireccionCompleta());
        assertEquals(usuDto.getIdUsuario(), cliDto.getUsuarioIdUsuario());

        UsuarioDto usuDto2 = instance.nuevoUsuario("Probando2", "Probando2", perfilDto);
        ClienteDto cliDto2 = instance.nuevoCliente("Probando2", "Probando2", "Probando", "NIF2", "Direct", usuDto2);

        instance.eliminaCliente(cliDto);
        instance.eliminaUsuario(usuDto);

        instance.eliminaCliente(cliDto2);
        instance.eliminaUsuario(usuDto2);
    }

    /**
     * Test of listaClientes method, of class ServicioBancarioImpl.
     */
    @Test
    public void listaClientes() throws ServicioException {
        System.out.println("listaClientes");
        ServicioBancario instance = ServicioBancarioFactory.create();

        List<ClienteDto> lista = instance.listaClientes();

        assertTrue(lista.size() >= 1);

        for (ClienteDto u : lista) {
            System.out.println(u);
        }

    }

    /**
     * Test of buscaClientePorNif method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaClientePorNif() throws ServicioException {
        System.out.println("buscaClientePorNif");
        ServicioBancario instance = ServicioBancarioFactory.create();

        ClienteDto cliDto = instance.buscaClientePorNif("NO EXISTE");
        assertEquals(ClienteDto.NOT_FOUND, cliDto);

        cliDto = instance.buscaClientePorNif("12345678A");
        assertEquals("Test", cliDto.getNombre());
        assertEquals("Test", cliDto.getApellido1());
        assertEquals("Test", cliDto.getApellido2());
        assertEquals(2, cliDto.getIdCliente());
        assertEquals("12345678A", cliDto.getNif());
    }

    /**
     * Test of buscaClientePorId method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaClientePorId() throws ServicioException {
        System.out.println("buscaClientePorId");
        ServicioBancario instance = ServicioBancarioFactory.create();

        ClienteDto cliDto = instance.buscaClientePorId(-1);
        assertEquals(ClienteDto.NOT_FOUND, cliDto);

        cliDto = instance.buscaClientePorId(2);
        assertEquals("Test", cliDto.getNombre());
        assertEquals("Test", cliDto.getApellido1());
        assertEquals("Test", cliDto.getApellido2());
        assertEquals(2, cliDto.getIdCliente());
        assertEquals("12345678A", cliDto.getNif());
    }

    /**
     * Test of nuevaCuenta y eliminaCuenta methods, of class
     * ServicioBancarioImpl.
     */
    @Test
    public void testNuevaCuentaYEliminaCuenta() throws CuentaNoValidaException, ServicioException {
        System.out.println("nuevaCuenta");
        ServicioBancario instance = ServicioBancarioFactory.create();

        UsuarioDto usuDto = instance.buscaUsuarioPorId(-1);
        CuentaDto cueDto = instance.nuevaCuenta(usuDto);
        assertEquals(CuentaDto.NOT_FOUND, cueDto);

        usuDto = instance.buscaUsuarioPorId(1);
        cueDto = instance.nuevaCuenta(usuDto);
        System.out.println(cueDto.toString());
        assertEquals(0.0, cueDto.getSaldo(), 0.001);
        assertEquals(1, cueDto.getUsuarioIdUsuario());

        boolean result = instance.eliminaCuenta(cueDto);
        assertTrue(result);
    }

    /**
     * Test of listaCuentas method, of class ServicioBancarioImpl.
     */
    @Test
    public void testListaCuentas() throws ServicioException {

        System.out.println("listaCuentas");
        ServicioBancario instance = ServicioBancarioFactory.create();

        List<CuentaDto> lista = instance.listaCuentas();

        assertTrue(lista.size() > 0);
        // Comprobamos que las cuentas que nos devuelven existen y son válidas
        Cuenta cue;
        for (CuentaDto c : lista) {
            cue = Cuenta.findByIdCuenta(c.getIdCuenta());
            assertTrue(cue.isValid());
        }
    }
    
    /**
     * Test of ingresoEnCuenta method, of class ServicioBancarioImpl.
     */
    @Test
    public void testIngresoEnCuenta() throws CuentaNoValidaException, ServicioException {

        System.out.println("ingresoEnCuenta");
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        UsuarioDto usuDto = instance.buscaUsuarioPorId(1);
        CuentaDto cueDto = instance.nuevaCuenta(usuDto);
        boolean result = instance.ingresoEnCuenta("Ingreso Test", 320, cueDto);
        
        assertTrue(result);
        cueDto = instance.buscaCuentaPorIdCuenta(cueDto.getIdCuenta());
        assertEquals(320, cueDto.getSaldo(), 0.001);
        assertEquals(1, cueDto.getListaMov().size());
        
        instance.eliminaCuenta(cueDto);
    }
    
    /**
     * Test of retiradaDeCuenta method, of class ServicioBancarioImpl.
     */
    @Test
    public void testRetiradaDeCuenta() throws CuentaNoValidaException, ServicioException {

        System.out.println("retiradaDeCuenta");
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        UsuarioDto usuDto = instance.buscaUsuarioPorId(1);
        CuentaDto cueDto = instance.nuevaCuenta(usuDto);
        instance.ingresoEnCuenta("Ingreso Test", 320, cueDto);
        boolean result = instance.retiradaDeCuenta("Retirada Test", 20, cueDto);
        
        assertTrue(result);
        cueDto = instance.buscaCuentaPorIdCuenta(cueDto.getIdCuenta());
        assertEquals(300, cueDto.getSaldo(), 0.001);
        assertEquals(2, cueDto.getListaMov().size());
        
        instance.eliminaCuenta(cueDto);
    }
    
    /**
     * Test of transferencia method, of class ServicioBancarioImpl.
     */
    @Test
    public void testTransferencia() throws CuentaNoValidaException, ServicioException {

        System.out.println("transferencia");
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        UsuarioDto usuDto = instance.buscaUsuarioPorId(1);
        CuentaDto cueOrig = instance.nuevaCuenta(usuDto);
        CuentaDto cueDest = instance.nuevaCuenta(usuDto);
        instance.ingresoEnCuenta("Ingreso Test", 320, cueOrig);
        boolean result = instance.transferencia("Test", 300, cueOrig, cueDest);
        
        assertTrue(result);
        cueOrig = instance.buscaCuentaPorIdCuenta(cueOrig.getIdCuenta());
        assertEquals(20, cueOrig.getSaldo(), 0.001);
        assertEquals(2, cueOrig.getListaMov().size());
        
        cueDest = instance.buscaCuentaPorIdCuenta(cueDest.getIdCuenta());
        assertEquals(300, cueDest.getSaldo(), 0.001);
        assertEquals(1, cueDest.getListaMov().size());
        
        instance.eliminaCuenta(cueOrig);
        instance.eliminaCuenta(cueDest);
    }
    
    /**
     * Test of buscaCuentaPorUsuario method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaCuentaPorUsuario() throws CuentaNoValidaException, ServicioException {

        System.out.println("buscaCuentaPorUsuario");
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        UsuarioDto usuDto = instance.buscaUsuarioPorId(1);
        List<CuentaDto> lista = instance.buscaCuentaPorUsuario(usuDto);
        
        assertTrue(lista.size() > 0);
        // Comprobamos que las cuentas que nos devuelven existen y son válidas
        Cuenta cue;
        for (CuentaDto c : lista) {
            cue = Cuenta.findByIdCuenta(c.getIdCuenta());
            assertTrue(cue.isValid());
        }
        
    }
    
    /**
     * Test of buscaCuentaPorCliente method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaCuentaPorCliente() throws CuentaNoValidaException, ServicioException {

        System.out.println("buscaCuentaPorCliente");
        ServicioBancario instance = ServicioBancarioFactory.create();
        
        ClienteDto cliDto = instance.buscaClientePorId(2);
        List<CuentaDto> lista = instance.buscaCuentaPorCliente(cliDto);
        
        assertTrue(lista.size() > 0);
        // Comprobamos que las cuentas que nos devuelven existen y son válidas
        Cuenta cue;
        for (CuentaDto c : lista) {
            cue = Cuenta.findByIdCuenta(c.getIdCuenta());
            assertTrue(cue.isValid());
        }
        
    }
    
    /**
     * Test of buscaCuentaPorNumCuenta method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaCuentaPorNumCuenta() throws CuentaNoValidaException, ServicioException {

        System.out.println("buscaCuentaPorNumCuenta");
        ServicioBancario instance = ServicioBancarioFactory.create();

        CuentaDto cue = instance.buscaCuentaPorNumCuenta(212, 323, 32, 2312341);
        assertTrue(cue != CuentaDto.NOT_FOUND);
        
        cue = instance.buscaCuentaPorNumCuenta(999, 9323, 99, 999999);
        assertTrue(cue == CuentaDto.NOT_FOUND);
    }
    
    /**
     * Test of buscaCuentaPorIdCuenta method, of class ServicioBancarioImpl.
     */
    @Test
    public void testBuscaCuentaPorIdCuenta() throws CuentaNoValidaException, ServicioException {

        System.out.println("buscaCuentaPorIdCuenta");
        ServicioBancario instance = ServicioBancarioFactory.create();

        CuentaDto cue = instance.buscaCuentaPorIdCuenta(16);
        assertTrue(cue != CuentaDto.NOT_FOUND);
        
        cue = instance.buscaCuentaPorIdCuenta(4);
        assertTrue(cue == CuentaDto.NOT_FOUND);
    }
}
