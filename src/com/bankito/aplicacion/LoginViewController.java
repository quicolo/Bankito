/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.Main;
import com.bankito.dominio.exceptions.DominioException;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kike
 */
public class LoginViewController implements Initializable {
    
    private Main mainApp;

    @FXML
    private Button entrarBtn;
    @FXML
    private TextField usuarioTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Label errorLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLbl.setText("");
    }    
    
    @FXML
    public void accionLogin(ActionEvent event) {
        try {
            ServicioBancario sb = mainApp.getServicioBancario();
            
            UsuarioDto usu = sb.loginUsuario(usuarioTxt.getText(), passwordTxt.getText());
            
            if(usu == UsuarioDto.NOT_FOUND)           
                errorLbl.setText("Usuario o contraseña incorrectos");
            else
                mainApp.goToClienteView();
        } catch (DominioException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServicioException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void onPasswordEnter(ActionEvent event) {
        accionLogin(event);        
    }
    
    public void setMainApp(Main aplicacion) {
        this.mainApp = aplicacion;
    }
}
