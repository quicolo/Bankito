/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito.aplicacion;

import com.bankito.Main;
import com.bankito.presentacion.ResourcePath;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.dto.ClienteDto;
import com.bankito.servicio.dto.UsuarioDto;
import com.bankito.servicio.exceptions.ServicioException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kike
 */
public class DatosClienteDialogViewController implements Initializable {

    @FXML
    private Label nombreLbl, apellido1Lbl, apellido2Lbl, nifLbl, direccionLbl,
            fechaAltaLbl, nombreUsuarioLbl;
    private Stage dialogStage;
    private Main mainApp;
    private ServicioBancario sb;
    
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

    public void initializeAfterSettingMain() {
        ClienteDto cli = null;
        try {
            dialogStage.getIcons().add(new Image(Main.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));
            sb = mainApp.getServicioBancario();
            UsuarioDto user = sb.getUsuarioLogado();
            cli = sb.buscaClientePorIdUsuario(user.getIdUsuario());
            nombreLbl.setText(cli.getNombre());
            apellido1Lbl.setText(cli.getApellido1());
            apellido2Lbl.setText(cli.getApellido2());
            nifLbl.setText(cli.getNif());
            direccionLbl.setText(cli.getDireccionCompleta());
            fechaAltaLbl.setText(cli.getFechaCreacion().toString());
            nombreUsuarioLbl.setText(user.getNombre());
        } catch (ServicioException ex) {
            Logger.getLogger(DatosClienteDialogViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
