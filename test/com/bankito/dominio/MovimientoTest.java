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

/**
 *
 * @author Kike
 */
public class MovimientoTest {
    
    public MovimientoTest() {
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
     * Test of isValid method, of class Movimiento.
     */
    @Test
    public void testIsStateValid() {
        System.out.println("isStateValid - Campos nulos");
        Movimiento instance = new Movimiento();
        assertFalse(instance.isValid());
        
        instance = new Movimiento("Prueba", "Otro tipo", 0);
        assertFalse(instance.isValid());
    
        instance = new Movimiento("Prueba", Movimiento.TIPO_MOV_ENTRADA, -1.0);
        assertFalse(instance.isValid());
        
        instance = new Movimiento("Prueba", Movimiento.TIPO_MOV_SALIDA, 0.0);
        assertFalse(instance.isValid());
        
        instance = new Movimiento("Prueba", Movimiento.TIPO_MOV_SALIDA, 10.0);
        instance.setCuentaIdCuenta(20);
        assertTrue(instance.isValid());
    }

    
    /**
     * Test of save, delete and find... methods, of class Cliente.
     * @throws com.bankito.dominio.exceptions.DominioException
     * @throws java.sql.SQLException
     */
    @Test
    public void testCRUD() throws DominioException, SQLException, CuentaEntidadDaoException, MovimientoEntidadDaoException {
        // Creamos un usuario auxiliar de prueba
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        Usuario usuario = new Usuario("prueba", "prueba", perfil);
        // Lo insertamos en la base de datos
        usuario.save();
        
        // Creamos una cuenta auxiliar de prueba
        Cuenta cuenta = new Cuenta(9999, 9999, 99, 999999999, usuario.getIdUsuario());
        // Lo insertamos en la base de datos
        cuenta.save();
        
        // CREATE/INSERT: Creamos dos movimientos de prueba y los asignamos a la cuenta anterior
        Movimiento instance1 = new Movimiento("Prueba1", Movimiento.TIPO_MOV_ENTRADA, 10.30);
        Movimiento instance2 = new Movimiento("Prueba2", Movimiento.TIPO_MOV_SALIDA, 10.30);
        cuenta.addMovimiento(instance1);
        cuenta.addMovimiento(instance2);
        cuenta.save();

        // READ: leemos de la BD y comparamos con lo que tenemos
        List<Movimiento> lista = Movimiento.findAllByIdCuenta(cuenta.getIdCuenta());
        assertEquals(2, lista.size());
        Movimiento result1 = lista.get(0);
        Movimiento result2 = lista.get(1);
        // Adaptamos el campo fechaCreacion porque al salvarse en la BD y recuperarse
        // el valore recuperado no es exactamente el mismo.
        result1.setFechaCreacion(instance1.getFechaCreacion());
        System.out.println("Instance1="+instance1);
        System.out.println("Result1="+result1);
        assertEquals(instance1, result1);
        result2.setFechaCreacion(instance2.getFechaCreacion());
        assertEquals(instance2, result2);
        assertEquals(0.0f, cuenta.getSaldo(), 0.20);
        // UPDATE: no existe para los movimientos en la BD.
        
        // DELETE: borramos y comprobamos que se ha borrado
        cuenta.delete();
        
        // READ: leemos y comparamos
        List<Cuenta> listaCuenta = Cuenta.findByIdUsuario(usuario.getIdUsuario());
        assertEquals(0, listaCuenta.size());

        // Borramos el usuario auxiliar que creamos
        usuario.delete();
        
    }
    
}
