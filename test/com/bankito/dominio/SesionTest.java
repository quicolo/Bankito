/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.DominioException;
import java.sql.SQLException;
import java.util.Date;
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
public class SesionTest {
    
    public SesionTest() {
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
        Sesion instance = new Sesion(null, 0);
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidNotFoundObject() {
        System.out.println("isValid - Not found object test");
        Sesion instance = Sesion.NOT_FOUND;
        assertFalse(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidCampoCadenaVacia() {
        System.out.println("isStateValid - One empty string field test");
        Sesion instance = new Sesion("", 10);
        instance.isValid();
        assertFalse(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidObjetoValido() {
        System.out.println("isStateValid - Valid Object");
        Sesion instance = new Sesion("accion", 1);
        assertTrue(instance.isValid());
    }
    
    @Test
    public void testCRUD() throws SQLException, DominioException {
        
        // CREATE/INSERT: Creamos dos usuarios de prueba
        Sesion instance1 = new Sesion(Sesion.LOGIN_OK_ACTION, 1);
        Sesion instance2 = new Sesion(Sesion.LOGIN_OK_ACTION, 2);
        instance1.save();
        instance2.save();
        
        instance1 = new Sesion(Sesion.LOGOUT_ACTION, 1);
        instance2 = new Sesion(Sesion.LOGOUT_ACTION, 2);
        instance1.save();
        instance2.save(); 
    }
    
    

    /**
     * Test of findLastSesionByIdUsuario method, of class Sesion.
     */
    @Test
    public void testFindLastSesionByIdUsuario() throws SQLException, DominioException {
        System.out.println("findLastSesionByIdUsuario");
        
        int usuarioIdUsuario = 1;
        
        Sesion expResult = new Sesion(Sesion.LOGIN_OK_ACTION, usuarioIdUsuario);
        expResult.save();
        
        Sesion result = Sesion.findLastSesionByIdUsuario(usuarioIdUsuario);
        assertEquals(expResult.getIdSesion(), result.getIdSesion());

        usuarioIdUsuario = -1;
        result = Sesion.findLastSesionByIdUsuario(usuarioIdUsuario);
        assertTrue(result == Sesion.NOT_FOUND);
        
    }
    
}
