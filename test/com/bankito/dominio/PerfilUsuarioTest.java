/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.DominioException;
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
public class PerfilUsuarioTest {
    
    public PerfilUsuarioTest() {
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
        PerfilUsuario instance = new PerfilUsuario(null, "descripcion");
    }
    
    /**
     * Test of Usuario constructor method, of class Usuario.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorPasswordNula() {
        PerfilUsuario instance = new PerfilUsuario("nombre", null);
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidNotFoundObject() {
        System.out.println("isValid - Not found object test");
        PerfilUsuario instance = PerfilUsuario.NOT_FOUND;
        assertFalse(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidCampoCadenaVacia() {
        System.out.println("isValid - One empty string field test");
        PerfilUsuario instance = new PerfilUsuario("nombre", "");
        instance.isValid();
        assertFalse(instance.isValid());
        
        instance = new PerfilUsuario("", "descripcion");
        assertFalse(instance.isValid());

    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidObjetoValido() {
        System.out.println("isValid - Valid Object");
        PerfilUsuario instance = new PerfilUsuario("perfil", "descripcion");
        assertTrue(instance.isValid());
    }
    
    @Test
    public void testCRUD() throws SQLException, DominioException {
        
        // CREATE/INSERT: Creamos dos usuarios de prueba
        PerfilUsuario instance1 = new PerfilUsuario("Perfil1", "Descripcion1");
        PerfilUsuario instance2 = new PerfilUsuario("Perfil2", "Descripcion2");
        instance1.save();
        instance2.save();

        // READ: leemos de la BD y comparamos con lo que tenemos
        PerfilUsuario result1 = PerfilUsuario.findByIdPerfilUsuario(instance1.getIdPerfilUsuario());
        assertEquals(instance1, result1);
        
        PerfilUsuario result2 = PerfilUsuario.findByIdPerfilUsuario(instance2.getIdPerfilUsuario());
        assertEquals(instance2, result2);

        // UPDATE: Cambiamos el valor algún campo y volvemos a salvar
        instance1.setNombre("Perfil1Modificada");
        instance1.setDescripcion("Descripcion1Modificada");
        instance2.setNombre("Perfil2Modificada");
        instance2.setDescripcion("Descripcion2Modificada");
        instance1.save();
        instance2.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        result1 = PerfilUsuario.findByIdPerfilUsuario(instance1.getIdPerfilUsuario());
        assertEquals(instance1, result1);
        
        result2 = PerfilUsuario.findByIdPerfilUsuario(instance2.getIdPerfilUsuario());
        assertEquals(instance2, result2);

        // DELETE: borramos y comprobamos que se ha borrado
        int idPerfilUsuario1 = instance1.getIdPerfilUsuario();
        int idPerfilUsuario2 = instance2.getIdPerfilUsuario();
        instance1.delete();
        instance2.delete();
        
        // READ: leemos y comparamos
        result1 = PerfilUsuario.findByIdPerfilUsuario(idPerfilUsuario1);
        result2 = PerfilUsuario.findByIdPerfilUsuario(idPerfilUsuario2);
        assertEquals(result1, PerfilUsuario.NOT_FOUND);
        assertEquals(result2, PerfilUsuario.NOT_FOUND);
        
    }    
}
