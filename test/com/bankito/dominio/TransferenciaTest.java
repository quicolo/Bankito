/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.dominio;

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
public class TransferenciaTest {
    
    public TransferenciaTest() {
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
     * Test of isValid method, of class Transferencia.
     */
    @Test
    public void testIsValid() {
        System.out.println("isStateValid");
        Cuenta error = new Cuenta(0, 0, 0, 0, 0);
        Cuenta origen = new Cuenta(9999, 9999, 99, 9999999, 10);
        Cuenta destino = new Cuenta(9999, 9999, 99, 9999900, 20);
        
        Transferencia instance = new Transferencia(error, destino, 120, "concepto");
        assertFalse(instance.isValid());
        
        instance = new Transferencia(origen, destino, 120, "");
        assertFalse(instance.isValid());
        
        instance = new Transferencia(origen, destino, -120, "concepto");
        assertFalse(instance.isValid());
        
        instance = new Transferencia(origen, destino, 0, "concepto");
        assertFalse(instance.isValid());

    }

    /**
     * Test of execute method, of class Transferencia.
     */
    @Test
    public void testExecute() throws Exception {
        float DELTA_ERROR = 0.001f;
        
        System.out.println("execute");
        // Creamos un usuario auxiliar de prueba
        PerfilUsuario perfil = PerfilUsuario.findByNombre("Cliente");
        Usuario usuario = new Usuario("prueba", "prueba", perfil);
        // Lo insertamos en la base de datos
        usuario.save();
        
        // Creamos una cuenta de pruebas
        Cuenta origen = new Cuenta(9999, 9999, 99, 999999999, usuario.getIdUsuario());
        // La insertamos en la base de datos
        origen.save();
        // Creamos otra cuenta de pruebas y le añadimos movimientos antes de salvarla
        Cuenta destino = new Cuenta(9999, 9999, 00, 999999900, usuario.getIdUsuario());
        destino.save();
        
        // UPDATE con movimientos
        Movimiento m1 = new Movimiento("Ingreso1", Movimiento.TIPO_MOV_ENTRADA, 190.30);
        Movimiento m2 = new Movimiento("Ingreso2", Movimiento.TIPO_MOV_ENTRADA, 50.30);
        origen.addMovimiento(m1);
        origen.addMovimiento(m2);
        origen.save();
        
        // En este punto las cuentas existen y la de origen tiene saldo
        // Creamos la transferencia y la ejecutamos
        Transferencia t = new Transferencia(origen, destino, 20, "prueba1");
        assertTrue(t.isValid());
        t.execute();
        
        t = new Transferencia(origen, destino, 30, "prueba2");
        assertTrue(t.isValid());
        t.execute();
        
        // Probamos si se han insertado los movimientos
        List<Movimiento> listaOrigen = origen.getMovimientos();
        List<Movimiento> listaDestino = destino.getMovimientos();
        
        // Comprobamos el resultado de la primera transferencia
        Movimiento mOrigen = listaOrigen.get(listaOrigen.size()-2);
        Movimiento mDestino = listaDestino.get(listaDestino.size()-2);
        assertEquals("prueba1", mOrigen.getConcepto());
        assertEquals("prueba1", mDestino.getConcepto());
        assertEquals(mDestino.getImporte(), mOrigen.getImporte(), DELTA_ERROR);
        assertEquals(Movimiento.TIPO_MOV_SALIDA, mOrigen.getTipo());
        assertEquals(Movimiento.TIPO_MOV_ENTRADA, mDestino.getTipo());
        
        // Y ahora la segunda transferencia
        mOrigen = listaOrigen.get(listaOrigen.size()-1);
        mDestino = listaDestino.get(listaDestino.size()-1);
        assertEquals("prueba2", mOrigen.getConcepto());
        assertEquals("prueba2", mDestino.getConcepto());
        assertEquals(mDestino.getImporte(), mOrigen.getImporte(), DELTA_ERROR);
        assertEquals(Movimiento.TIPO_MOV_SALIDA, mOrigen.getTipo());
        assertEquals(Movimiento.TIPO_MOV_ENTRADA, mDestino.getTipo());
        
        
        // DELETE: borramos y comprobamos que se ha borrado
        origen.delete();
        destino.delete();
        
        // Borramos el usuario auxiliar que creamos
        usuario.delete();
    }

}
