/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Kike
 */
public class AppConfiguration {
    private static Properties props = new Properties();
    
    public static void loadProperties() throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream("config_db.properties");
        props.load(in);
        in.close();
        
        in = new FileInputStream("config_app.properties");
        props.load(in);
        in.close();
    }
    
    public static String getProperty(String nombre) {
        String result = props.getProperty(nombre);
        return result == null? "NOT_FOUND": result;
    }
    
}
