/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.Main;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kike
 */
public class RetirarDialogViewController implements Initializable {

    private Main mainApp;
    private Stage dialogStage;
    private ServicioBancario sb;
    private CuentaModelo cuentaModelo;
    
    
    @FXML
    private Label cuentaLbl;
    @FXML
    private TextField importeTxt, conceptoTxt;
    @FXML
    private Button aceptarBtn, cancelarBtn;

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
    
    public void setMainApp(Main main) {
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
            boolean result = realizaRetirada();
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
        else {
            if(!esImporteValido(importe))
                mensaje.append("- El importe debe ser una cantidad numérica. Ejemplo: 200.50\n");
            else
                if(cuentaModelo.getSaldo() < Float.parseFloat(importe))
                    mensaje.append("- El importe no puede superar el saldo disponible\n");
        }
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
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));

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
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));

        alert.showAndWait();
    }

    private boolean realizaRetirada() {
        float importe = Float.parseFloat(importeTxt.getText());
        String concepto = conceptoTxt.getText();
        
        try {
            CuentaDto cuentaDto = sb.buscaCuentaPorIdCuenta(cuentaModelo.getIdCuenta());
            sb.retiradaDeCuenta(concepto, importe, cuentaDto);
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
