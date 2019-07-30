/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.DominioException;
import java.sql.SQLException;
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
public class ClienteTest {
    
    public ClienteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Se ejecuta después de la ejecución de todos los métodos de prueba
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        // Se ejecuta después de cada llamada a los métodos de prueba
    }

    /**
     * Test of isValid method, of class Cliente.
     */
    @Test
    public void testIsStateValidNotFoundObject() {
        System.out.println("isStateValid - Not found object test");
        Cliente instance = Cliente.NOT_FOUND;
        boolean result = instance.isValid();
        assertFalse(result);
    }
    
    
    /**
     * Test of Cliente constructor method, of class Cliente.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorCampoNulo() {
        Cliente instance = new Cliente("Pedro", "Sánchez", "Sánchez", null, "Una dirección");
    }
    
    /**
     * Test of isValid method, of class Cliente.
     */
    @Test
    public void testIsStateValidCampoCadenaVacia() {
        System.out.println("isStateValid - One empty string field test");
        Cliente instance = new Cliente("Pedro", "Sánchez", "Sánchez", "", "Una dirección");
        assertFalse(instance.isValid());
        
        instance = new Cliente("", "Sánchez", "Sánchez" , "28748654E", "Una dirección");
        assertFalse(instance.isValid());
        
        // Este caso sí se permite porque el apellido2 es opcional
        instance = new Cliente("Pedro", "Sánchez", "" , "28748654E", "Una dirección");
        assertTrue(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Cliente.
     */
    @Test
    public void testIsStateValidObjetoValido() {
        System.out.println("isStateValid - Valid Object");
        Cliente instance = new Cliente("Pedro", "Sánchez", "Sánchez", "12345678T", "Una dirección");
        assertTrue(instance.isValid());
    }
    
    /**
     * Test of save, delete and find... methods, of class Cliente.
     * @throws com.bankito.dominio.exceptions.DominioException
     * @throws java.sql.SQLException
     */
    @Test
    public void testCRUD() throws DominioException, SQLException {
        // Creamos un usuario auxiliar de prueba
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        Usuario usuario = new Usuario("prueba", "prueba", perfil);
        // Lo insertamos en la base de datos
        usuario.save();
        
        // CREATE/INSERT: Creamos dos clientes de prueba y los asignamos al usuario anterior
        Cliente instance1 = new Cliente("Prueba1", "Prueba1", "Prueba1", "NIFPrueba1", "Prueba1");
        Cliente instance2 = new Cliente("Prueba2", "Prueba2", "Prueba2", "NIFPrueba2", "Prueba2");
        instance1.setUsuarioIdUsuario(usuario.getIdUsuario());
        instance2.setUsuarioIdUsuario(usuario.getIdUsuario());
        instance1.save();
        instance2.save();

        // READ: leemos de la BD y comparamos con lo que tenemos
        Cliente result1 = Cliente.findByIdCliente(instance1.getIdCliente());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(instance1.getFechaCreacion());
        assertEquals(instance1, result1);
        
        Cliente result2 = Cliente.findByIdCliente(instance2.getIdCliente());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result2.setFechaCreacion(instance2.getFechaCreacion());
        assertEquals(instance2, result2);

        // UPDATE: Cambiamos el valor algún campo y volvemos a salvar
        instance1.setApellido1("Test");
        instance1.setNombre("Test");
        instance2.setApellido2("Test");
        instance2.setNif("Test");
        instance1.save();
        instance2.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        result1 = Cliente.findByIdCliente(instance1.getIdCliente());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(instance1.getFechaCreacion());
        result1.setFechaModificacion(instance1.getFechaModificacion());
        System.out.println(instance1.toString());
        System.out.println(result1.toString());
        assertEquals(instance1, result1);
        
        result2 = Cliente.findByIdCliente(instance2.getIdCliente());
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result2.setFechaCreacion(instance2.getFechaCreacion());
        result2.setFechaModificacion(instance2.getFechaModificacion());
        assertEquals(instance2, result2);

        // DELETE: borramos y comprobamos que se ha borrado
        int idCliente1 = instance1.getIdCliente();
        int idCliente2 = instance2.getIdCliente();
        instance1.delete();
        instance2.delete();
        
        // READ: leemos y comparamos
        result1 = Cliente.findByIdCliente(idCliente1);
        result2 = Cliente.findByIdCliente(idCliente2);
        assertEquals(result1, Cliente.NOT_FOUND);
        assertEquals(result2, Cliente.NOT_FOUND);
        
        // Borramos el usuario auxiliar que creamos
        usuario.delete();
        
  }   
}
