/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * FXML Controller class
 *
 * @author Kike
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
