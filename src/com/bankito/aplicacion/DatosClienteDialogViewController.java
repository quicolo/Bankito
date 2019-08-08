package com.bankito.aplicacion;

import com.bankito.MainFX;
import com.bankito.presentacion.ResourcePath;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
* <h1>DatosClienteDialogViewController</h1>
* Esta clase representa al controlador de la vista que se encarga de mostrar
* una nueva ventana (diálogo) que muestra los datos del cliente logado. 
* <p>
* Esta clase presenta propiedades de distintos tipos. Según su funcionalidad las
* podemos clasificar en: 
* - Propiedades asociadas a la interfaz gráfica: están anotadas con @FXML y se 
*   asocian a objetos definidos en el archivo de interfaz gráfica .fxml.<br> 
* - Propiedades asocidas a la aplicación: son referencias a la aplicación 
*   principal (mainApp) y al objeto de ServicioBancario necesarias para poder
*   cambiar las vistas y utilizar los servicios de las capas inferiores. 
* <p>
* Entre los métodos de la clase destacamos los siguientes:
* - initialize: este método se llama automáticamente después de carga la vista
*   FXML en pantalla y permite asociar valores iniciales a los controles de la vista<br>
* - setMainApp: sirve para inyectar la referencia al objeto MainFX, que a su
*   vez nos permitirá obtener la referencia al objeto ServicioBancario.<br>
* - initializeAfterSettingMain: este método permite inicializar el modelo de datos
*   que se utilizará en la vista una vez se tenga cargada la referencia a MainFX 
*   y al ServicioBancario
*
* @author  Enrique Royo Sánchez
*/
public class DatosClienteDialogViewController implements Initializable {

    @FXML
    private Label nombreLbl, apellido1Lbl, apellido2Lbl, nifLbl, direccionLbl,
            fechaAltaLbl, nombreUsuarioLbl;
    private MainFX mainApp;
    private ServicioBancario sb;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setMainApp(MainFX main) {
        mainApp = main;
    }

    public void initializeAfterSettingMain() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ClienteDto cli = null;
        try {
            sb = mainApp.getServicioBancario();
            UsuarioDto user = sb.getUsuarioLogado();
            cli = sb.buscaClientePorIdUsuario(user.getIdUsuario());
            nombreLbl.setText(cli.getNombre());
            apellido1Lbl.setText(cli.getApellido1());
            apellido2Lbl.setText(cli.getApellido2());
            nifLbl.setText(cli.getNif());
            direccionLbl.setText(cli.getDireccionCompleta());
            fechaAltaLbl.setText(formatter.format(cli.getFechaCreacion()));
            nombreUsuarioLbl.setText(user.getNombre());
        } catch (ServicioException ex) {
            Logger.getLogger(DatosClienteDialogViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
