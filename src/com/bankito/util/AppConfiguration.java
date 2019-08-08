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
    
    public static String getProperty(String nombre, String valorPorDefecto) {
        return props.getProperty(nombre, valorPorDefecto);
    }
    
}
