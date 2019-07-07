/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

import com.bankito.dominio.exceptions.DominioException;
import com.bankito.persistencia.exceptions.CuentaEntidadDaoException;
import com.bankito.persistencia.exceptions.MovimientoEntidadDaoException;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Kike
 */
public class CuentaTest {
    
    public CuentaTest() {
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
     * Test of isValid method, of class Cuenta.
     */
    @Test
    public void testIsValid() {
        System.out.println("isValid");
        Cuenta instance = Cuenta.NOT_FOUND;
        assertFalse(instance.isValid());
        
        instance = new Cuenta(0, 0, 0, 0, 0);
        assertFalse(instance.isValid());
        
        instance = new Cuenta(0);
        assertFalse(instance.isValid());
        
        instance = new Cuenta(9999, 9999, 99, 9999999, 10);
        assertTrue(instance.isValid());
    }
    
    /**
     * Test of save, delete and find... methods, of class Cliente.
     * @throws com.bankito.dominio.exceptions.DominioException
     * @throws java.sql.SQLException
     */
    //@Ignore
    @Test
    public void testCRUD() throws DominioException, SQLException, CuentaEntidadDaoException, MovimientoEntidadDaoException {
        // Creamos un usuario auxiliar de prueba
        Usuario usuario = new Usuario("prueba", "prueba");
        // Lo insertamos en la base de datos
        usuario.save();
        
        // Creamos una cuenta de pruebas
        Cuenta cuenta1 = new Cuenta(9999, 9999, 99, 999999999, usuario.getIdUsuario());
        // La insertamos en la base de datos
        cuenta1.save();
        // Creamos otra cuenta de pruebas y le añadimos movimientos antes de salvarla
        Cuenta cuenta2 = new Cuenta(9999, 9999, 00, 999999900, usuario.getIdUsuario());
        cuenta2.save();
        
        // UPDATE con movimientos
        Movimiento m1 = new Movimiento("Ingreso1", Movimiento.TIPO_MOV_ENTRADA, 190.30);
        Movimiento m2 = new Movimiento("Ingreso2", Movimiento.TIPO_MOV_ENTRADA, 50.30);
        cuenta1.addMovimiento(m1);
        cuenta1.addMovimiento(m2);
        cuenta1.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        List<Cuenta> lista = Cuenta.findByIdUsuario(usuario.getIdUsuario());
        assertEquals(2, lista.size());
        Cuenta result1 = lista.get(0);
        Cuenta result2 = lista.get(1);
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(cuenta1.getFechaCreacion());
        System.out.println("Cuenta1="+cuenta1);
        System.out.println("Result1="+result1);
        assertEquals(cuenta1, result1);
        
        result2.setFechaCreacion(cuenta2.getFechaCreacion());
        assertEquals(cuenta2, result2);
        assertEquals(240.60f, cuenta1.getSaldo(), 0.20);
        assertEquals(0.0f, cuenta2.getSaldo(), 0.20);
        
        // DELETE: borramos y comprobamos que se ha borrado
        cuenta1.delete();
        cuenta2.delete();
        // READ: leemos y comparamos
        List<Cuenta> listaCuenta = Cuenta.findByIdUsuario(usuario.getIdUsuario());
        assertEquals(0, listaCuenta.size());

        // Borramos el usuario auxiliar que creamos
        usuario.delete();
        
    }
    
    /**
     * Test of save, delete and find... methods, of class Cliente.
     * @throws com.bankito.dominio.exceptions.DominioException
     * @throws java.sql.SQLException
     */
    @Test
    public void testCRUD2() throws DominioException, SQLException, CuentaEntidadDaoException, MovimientoEntidadDaoException {
        // Creamos un usuario auxiliar de prueba
        Usuario usuario = Usuario.findByIdUsuario(1);
        
        // Creamos una cuenta de pruebas
        Cuenta cuenta1 = new Cuenta(9999, 9999, 99, 999999999, usuario.getIdUsuario());
        // La insertamos en la base de datos
        cuenta1.save();
        
        
        // UPDATE con movimientos
        Movimiento m1 = new Movimiento("Ingreso1", Movimiento.TIPO_MOV_ENTRADA, 190.30);
        Movimiento m2 = new Movimiento("Ingreso2", Movimiento.TIPO_MOV_ENTRADA, 50.30);
        cuenta1.addMovimiento(m1);
        cuenta1.addMovimiento(m2);
        cuenta1.save();
        
        // READ: leemos de la BD y comparamos con lo que tenemos
        Cuenta result = Cuenta.findByIdCuenta(cuenta1.getIdCuenta());
        assertEquals(result.getIdCuenta(), cuenta1.getIdCuenta());
        assertEquals(result.getUsuarioIdUsuario(), cuenta1.getUsuarioIdUsuario()); 
        assertEquals(result.getSaldo(), cuenta1.getSaldo(), 0.001); 
        
        // DELETE: borramos
        cuenta1.delete();
        
    }
    
}
