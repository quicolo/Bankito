/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.Main;
import com.bankito.dominio.exceptions.DominioException;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Kike
 */
public class ClienteViewController implements Initializable {
    
    private Main mainApp;
    private ServicioBancario sb;

    @FXML
    private ImageView cerrarSesionBtn;
    @FXML
    private Label bienvenidoLbl;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void initializeWithMainLoaded() {
        
        cargaSaludoBienvenida();
    }
    
    @FXML
    public void accionLogout(javafx.event.Event event) {
        try {
            ServicioBancario sb = mainApp.getServicioBancario();
            
            boolean result = sb.logoutUsuario();
            
            if(result == true)           
                mainApp.goToLogin();
            else
                System.out.println("Error raro");
        } catch (DominioException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServicioException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setMainApp(Main aplicacion) {
        this.mainApp = aplicacion;
    }

    private void cargaSaludoBienvenida() {
        String nombre = "";
        try {
            sb = mainApp.getServicioBancario();
            UsuarioDto user = sb.getUsuarioLogado();
            ClienteDto cli = sb.buscaClientePorIdUsuario(user.getIdUsuario());
            if (cli != ClienteDto.NOT_FOUND)
                nombre = " "+cli.getNombre();
        } catch (ServicioException ex) {
            Logger.getLogger(ClienteViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bienvenidoLbl.setText("Hola"+nombre+", bienvenid@ a Bankito");
    }

    
}
