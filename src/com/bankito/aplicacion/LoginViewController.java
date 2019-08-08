package com.bankito.aplicacion;

import com.bankito.MainFX;
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
* <h1>LoginViewController</h1>
* Esta clase representa al controlador de la vista que se encarga de pedir el 
* login al usuario. Es la pantalla de inicio de la aplicación.
* <p>
* Esta clase presenta propiedades de distintos tipos. Según su funcionalidad las
* podemos clasificar en: 
* - Propiedades asociadas a la interfaz gráfica: están anotadas con @FXML y se 
*   asocian a objetos definidos en el archivo de interfaz gráfica .fxml.<br>
* - Propiedad asocida a la aplicación: son referencias a la aplicación 
*   principal (mainApp)
* <p>
* Entre los métodos de la clase destacamos los siguientes:
* - initialize: este método se llama automáticamente después de carga la vista
*   FXML en pantalla y permite asociar valores iniciales a los controles de la vista<br>
* - setMainApp: sirve para inyectar la referencia al objeto MainFX, que a su
*   vez nos permitirá obtener la referencia al objeto ServicioBancario.<br>
* - Los del tipo accionXXX(): se encargan de procesar un evento producido por 
*   la interfaz gráfica que pueden llaman a otro método de MainFX para cambiar
*   la vista si corresponde (método goToXXX()).
*
* @author  Enrique Royo Sánchez
*/
public class LoginViewController implements Initializable {
    
    private MainFX mainApp;

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
    
    public void setMainApp(MainFX aplicacion) {
        this.mainApp = aplicacion;
    }
}
