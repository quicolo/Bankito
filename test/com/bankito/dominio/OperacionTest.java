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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kike
 */
public class OperacionTest {
    
    public OperacionTest() {
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
    public void testConstructorNombreCortoNulo() {
        Operacion instance = new Operacion(null, "nombre", "descripcion");
    }
    
    /**
     * Test of Usuario constructor method, of class Cliente.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNombreNulo() {
        Operacion instance = new Operacion("nombrecorto", null, "descripcion");
    }
    
    /**
     * Test of Usuario constructor method, of class Usuario.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorDescripcionNula() {
        Operacion instance = new Operacion("nombrecorto", "nombre", null);
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidNotFoundObject() {
        System.out.println("isValid - Not found object test");
        Operacion instance = Operacion.NOT_FOUND;
        assertFalse(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidCampoCadenaVacia() {
        System.out.println("isValid - One empty string field test");
        Operacion instance = new Operacion("nombrecorto", "", "descripcion");
        assertFalse(instance.isValid());
        
        instance = new Operacion("", "nombre", "descripcion");
        assertFalse(instance.isValid());
        
        instance = new Operacion("nombreCorto", "nombre", "");
        assertFalse(instance.isValid());
    }
    
    /**
     * Test of isValid method, of class Usuario.
     */
    @Test
    public void testIsValidObjetoValido() {
        System.out.println("isValid - Valid Object");
        Operacion instance = new Operacion("nombreCorto", "nombre", "descripcion");
        assertTrue(instance.isValid());
    }
    
    @Test
    public void testCRUD() throws SQLException, DominioException {
        
        // CREATE/INSERT: Creamos dos usuarios de prueba
        Operacion instance1 = new Operacion("nombreCorto1", "nombre1", "descripcion1");
        Operacion instance2 = new Operacion("nombreCorto2", "nombre2", "descripcion2");
        instance1.save();
        instance2.save();

        // READ: leemos de la BD y comparamos con lo que tenemos
        Operacion result1 = Operacion.findByIdOperacion(instance1.getIdOperacion());
        assertEquals(instance1, result1);
        
        Operacion result2 = Operacion.findByIdOperacion(instance2.getIdOperacion());
        assertEquals(instance2, result2);

        // UPDATE: Cambiamos el valor algún campo y volvemos a salvar
        instance1.setNombre("Perfil1Modificada");
        instance1.setDescripcion("Descripcion1Modificada");
        instance2.setNombre("Perfil2Modificada");
        instance2.setDescripcion("Descripcion2Modificada");
        instance1.save();
        instance2.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        result1 = Operacion.findByIdOperacion(instance1.getIdOperacion());
        assertEquals(instance1, result1);
        
        result2 = Operacion.findByIdOperacion(instance2.getIdOperacion());
        assertEquals(instance2, result2);

        // DELETE: borramos y comprobamos que se ha borrado
        int idOperacion1 = instance1.getIdOperacion();
        int idOperacion2 = instance2.getIdOperacion();
        instance1.delete();
        instance2.delete();
        
        // READ: leemos y comparamos
        result1 = Operacion.findByIdOperacion(idOperacion1);
        result2 = Operacion.findByIdOperacion(idOperacion2);
        assertEquals(result1, Operacion.NOT_FOUND);
        assertEquals(result2, Operacion.NOT_FOUND);
        
    }    
    
    
    
}
