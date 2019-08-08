package com.bankito.aplicacion;

import com.bankito.MainFX;
import com.bankito.presentacion.modelos.CuentaModelo;
import com.bankito.presentacion.ResourcePath;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.CuentaDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
* <h1>IngresarDialogViewController</h1>
* Esta clase representa al controlador de la vista que se encarga de mostrar
* una nueva ventana (diálogo) solicita los datos para poder realizar un ingreso
* en la cuenta escogida. 
* <p>
* Esta clase presenta propiedades de distintos tipos. Según su funcionalidad las
* podemos clasificar en: 
* - Propiedades asociadas a la interfaz gráfica: están anotadas con @FXML y se 
*   asocian a objetos definidos en el archivo de interfaz gráfica .fxml. También
*   la propiedad 'dialogStage' que es la referencia a la nueva ventana abierta, esta
*   propiedad se necesita para cerrarla cuando se pulse el botón 'Aceptar' o 
*   'Cancelar'. También se guarda una referencia al modelo de datos que necesita la vista
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
* - setDialogStage: sirve para inyectar la referencia a la nueva ventana stage
*   para que pueda ser cerrada cuando corresponda.<br>
* - initializeAfterSettingMain: este método permite inicializar el modelo de datos
*   que se utilizará en la vista una vez se tenga cargada la referencia a MainFX 
*   y al ServicioBancario<br>
* - Los del tipo accionXXX(): se encargan de procesar un evento producido por 
*   la interfaz gráfica que pueden llaman a otro método de MainFX para cambiar
*   la vista si corresponde (método goToXXX()).<br>
* - validaDatosUsuario: valida que los datos introducidos por el usuario sean
*   válidos para realizar la operación de ingreso.<br>
* - realizaIngreso: realiza la operación de ingreso en cuenta usando la referencia
*   a ServicioBancario obtenida.<br>
* - actualizaModelo: actualiza el modelo con la nueva información fruto de haber
*   realizado la operación del ingreso. Este método llama al método recalcula()
*   de la CuentaModelo que a su vez propagará los cambios a todo el objeto 
*   SituacionGlobalModelo
*
* @author  Enrique Royo Sánchez
*/
public class IngresarDialogViewController implements Initializable {

    private MainFX mainApp;
    private Stage dialogStage;
    private ServicioBancario sb;
    private CuentaModelo cuentaModelo;
        
    @FXML
    private Label cuentaLbl;
    @FXML
    private TextField importeTxt, conceptoTxt;

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
        sb = mainApp.getServicioBancario();
        cuentaLbl.setText(cuentaModelo.getNumCuentaConFormato());
    }
    
    public void accionAceptar(ActionEvent event) {
        
        String mensajeError = "";
        mensajeError = validaDatosUsuario(importeTxt.getText(), conceptoTxt.getText());
        
        if(mensajeError.equals("")) {  // Todo OK
            boolean result = realizaIngreso();
            if(result==false)
                alertaErrorNoEsperado();
            dialogStage.close();
        }
        else {
            alertaDatosErroneos(mensajeError);
        }
            
    }
    
    public void accionCancelar(ActionEvent event) {
        dialogStage.close();
    }

    private String validaDatosUsuario(String importe, String concepto) {
        StringBuilder mensaje = new StringBuilder();
        
        if(concepto.equals(""))
            mensaje.append("- El concepto no puede ser vacío\n");
        
        if(importe.equals(""))
            mensaje.append("- El importe no puede ser vacío\n");
        else
            if(!esImporteValido(importe))
                mensaje.append("- El importe debe ser una cantidad numérica. Ejemplo: 200.50\n");
        
        return mensaje.toString();
    }
    
    private boolean esImporteValido(String importe) {
        try {
            Float.parseFloat(importe);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    
    @FXML
    public void alertaDatosErroneos(String mensajeError) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Datos erróneos");
        alert.setHeaderText(null);
        alert.setContentText("Tienes que corregir los siguientes datos para realizar la operación: \n"+mensajeError);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(MainFX.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));

        alert.showAndWait();
    }
    
    @FXML
    public void alertaErrorNoEsperado() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ErrorInesperado");
        alert.setHeaderText(null);
        alert.setContentText("Se ha producido un error inesperado. La operación no se ha realizado correctamente");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(MainFX.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));

        alert.showAndWait();
    }

    private boolean realizaIngreso() {
        float importe = Float.parseFloat(importeTxt.getText());
        String concepto = conceptoTxt.getText();
        
        try {
            CuentaDto cuentaDto = sb.buscaCuentaPorIdCuenta(cuentaModelo.getIdCuenta());
            sb.ingresoEnCuenta(concepto, importe, cuentaDto);
            actualizaModelo();
            return true;
        } catch (ServicioException e) {
            return false;
        }
    }

    private void actualizaModelo() throws ServicioException {
        CuentaDto cuentaDto = sb.buscaCuentaPorIdCuenta(cuentaModelo.getIdCuenta());
        cuentaModelo.setSaldo(cuentaDto.getSaldo());
        cuentaModelo.setListaMov(cuentaDto.getListaMov());
        cuentaModelo.recalcula();
    }
    
}
