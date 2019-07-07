/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.DominioException;
import com.bankito.dominio.exceptions.UsuarioDuplicadoException;
import com.bankito.dominio.exceptions.UsuarioEncodePasswordException;
import com.bankito.dominio.exceptions.UsuarioNoValidoException;
import java.sql.SQLException;
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
public class UsuarioTest {
    
    public UsuarioTest() {
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
     * Test of Usuario constructor method, of class Cliente.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNombreNulo() {
        Usuario instance = new Usuario(null, "password");
    }
    
    /**
     * Test of Usuario constructor method, of class Usuario.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorPasswordNula() {
        Usuario instance = new Usuario("nombre", null);
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsStateValidNotFoundObject() {
        System.out.println("isStateValid - Not found object test");
        Usuario instance = Usuario.NOT_FOUND;
        assertFalse(instance.isValid());
    }

    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsStateValidCampoCadenaVacia() {
        System.out.println("isStateValid - One empty string field test");
        Usuario instance = new Usuario("nombre", "");
        instance.isValid();
        assertFalse(instance.isValid());
        
        instance = new Usuario("", "password");
        assertFalse(instance.isValid());

    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsStateValidObjetoValido() {
        System.out.println("isStateValid - Valid Object");
        Usuario instance = new Usuario("juan", "supercontraseña");
        assertTrue(instance.isValid());
    }
    
    @Test
    public void testCRUD() throws SQLException, DominioException {
        
        // CREATE/INSERT: Creamos dos usuarios de prueba
        Usuario instance1 = new Usuario("Prueba1", "Prueba1");
        Usuario instance2 = new Usuario("Prueba2", "Prueba2");
        instance1.save();
        instance2.save();

        // READ: leemos de la BD y comparamos con lo que tenemos
        Usuario result1 = Usuario.findByIdUsuario(instance1.getIdUsuario());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(instance1.getFechaCreacion());
        result1.setFechaModificacion(instance1.getFechaModificacion());
        assertEquals(instance1, result1);
        
        Usuario result2 = Usuario.findByIdUsuario(instance2.getIdUsuario());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result2.setFechaCreacion(instance2.getFechaCreacion());
        result2.setFechaModificacion(instance2.getFechaModificacion());
        assertEquals(instance2, result2);

        // UPDATE: Cambiamos el valor algún campo y volvemos a salvar
        instance1.setNombre("Prueba1Modificada");
        instance1.setPassword("Prueba1Modificada");
        instance2.setNombre("Prueba2Modificada");
        instance2.setPassword("Prueba2Modificada");
        instance1.save();
        instance2.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        result1 = Usuario.findByIdUsuario(instance1.getIdUsuario());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(instance1.getFechaCreacion());
        result1.setFechaModificacion(instance1.getFechaModificacion());
        System.out.println(instance1.toString());
        System.out.println(result1.toString());
        assertEquals(instance1, result1);
        
        result2 = Usuario.findByIdUsuario(instance2.getIdUsuario());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result2.setFechaCreacion(instance2.getFechaCreacion());
        result2.setFechaModificacion(instance2.getFechaModificacion());
        assertEquals(instance2, result2);

        // DELETE: borramos y comprobamos que se ha borrado
        int idUsuario1 = instance1.getIdUsuario();
        int idUsuario2 = instance2.getIdUsuario();
        instance1.delete();
        instance2.delete();
        
        // READ: leemos y comparamos
        result1 = Usuario.findByIdUsuario(idUsuario1);
        result2 = Usuario.findByIdUsuario(idUsuario2);
        assertEquals(result1, Usuario.NOT_FOUND);
        assertEquals(result2, Usuario.NOT_FOUND);
        
    }
    
    
    /**
     * Test of tryLogin method, of class Usuario.
     */
    @Test
    public void testTryLogin() throws UsuarioDuplicadoException, UsuarioNoValidoException, UsuarioEncodePasswordException {
        System.out.println("tryLogin");
        String nombre = "NombreUsuario";
        String password = "supercontraseña";
        Usuario instance = new Usuario(nombre, password);
        instance.save();
        
        Usuario result = Usuario.tryLogin(nombre, password);
        assertTrue(result != Usuario.NOT_FOUND);
        
        result = Usuario.tryLogin(nombre, "Otrapassword");
        assertTrue(result == Usuario.NOT_FOUND);
        
        result = Usuario.tryLogin("OtroNombre", password);
        assertTrue(result == Usuario.NOT_FOUND);
                
        instance.delete();
    }


}
