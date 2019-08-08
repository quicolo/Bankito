package com.bankito.aplicacion;

import com.bankito.MainFX;
import com.bankito.presentacion.modelos.CuentaModelo;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
* <h1>DetallesCuentaDialogViewController</h1>
* Esta clase representa al controlador de la vista que se encarga de mostrar
* una nueva ventana (diálogo) que carga los detalles de la cuenta escogida. 
* <p>
* Esta clase presenta propiedades de distintos tipos. Según su funcionalidad las
* podemos clasificar en: 
* - Propiedades asociadas a la interfaz gráfica: están anotadas con @FXML y se 
*   asocian a objetos definidos en el archivo de interfaz gráfica .fxml. También
*   la propiedad 'dialogStage' que es la referencia a la nueva ventana abierta, esta
*   propiedad se necesita para cerrarla cuando se pulse el botón 'Aceptar'.
*   También se guarda una referencia al modelo de datos que necesita la vista
*   para poder rellenarse.<br>
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
*   y al ServicioBancario.<br>
* - setModel: nos permite inyectar la referencia al objeto del modelo que se 
*   mostrará en la vista.
*
* @author  Enrique Royo Sánchez
*/
public class DetallesCuentaDialogViewController implements Initializable {
    private MainFX mainApp;
    private Stage dialogStage;
    private CuentaModelo cuentaModelo;
    
    @FXML
    private Label cuentaLbl, detallesLbl, fechaCreacionLbl, 
            ingresosLbl, gastosLbl, saldoLbl, numMovimLbl;
    @FXML
    private Button aceptarBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMainApp(MainFX main) {
        mainApp = main;
    }
    
    public void setModel(CuentaModelo cuenta) {
        this.cuentaModelo = cuenta;
    }

    public void initializeAfterSettingMain() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String ahora = formatter.format(new Date());
        
        detallesLbl.setText("Detalles de tu cuenta a "+ahora);
        cuentaLbl.setText(cuentaModelo.getNumCuentaConFormato());
        fechaCreacionLbl.setText(formatter.format(cuentaModelo.getFechaCreacion()));
        saldoLbl.setText(cuentaModelo.getSaldo()+" €");
        numMovimLbl.setText(""+cuentaModelo.getNumMovimientos());
        ingresosLbl.setText(cuentaModelo.getIngresosTotal()+" €");
        gastosLbl.setText(cuentaModelo.getGastosTotal()+" €");
    }
    
    public void accionAceptar(ActionEvent event) {
        
        dialogStage.close();
            
    }
    
}
