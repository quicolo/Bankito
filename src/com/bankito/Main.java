/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito;

import com.bankito.aplicacion.ClienteViewController;
import com.bankito.aplicacion.DatosClienteDialogViewController;
import com.bankito.aplicacion.LoginViewController;
import com.bankito.presentacion.ResourcePath;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.util.AppConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Kike
 */
public class Main extends Application {

    private Stage stage;
    private ServicioBancario servicioBancario;

    public static void main(String[] args) {
        // Lanza la aplicación JavaFX llamando al método estático heredado launch.
        // launch crea una instancia de Application, llama al método init(), crea el
        // escenario principal (primaryStage) y llama al método start entre otras cosas
        Application.launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AppConfiguration.loadProperties();
        this.stage = primaryStage;
        servicioBancario = ServicioBancarioFactory.create();

        stage.getIcons().add(new Image(Main.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));
        
        String appName = AppConfiguration.getProperty("APP_NAME", "Bankito");
        stage.setTitle(appName);
        goToLogin();
        stage.show();
        stage.centerOnScreen();
    }

    public void goToLogin() {
        try {
            LoginViewController login = (LoginViewController) replaceSceneContent(ResourcePath.LOGIN_VIEW);
            login.setMainApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void goToClienteView() {
        try {
            ClienteViewController login = (ClienteViewController) replaceSceneContent(ResourcePath.CLIENTE_VIEW);
            login.setMainApp(this);
            login.initializeAfterSettingMain();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goToDialogoDatosCliente() {

        try {
            Stage dialog = getNewDialogStage("Datos personales");
            DatosClienteDialogViewController controller = 
                    (DatosClienteDialogViewController) openNewDialogScene(ResourcePath.DATOS_CLIENTE_DIALOG_VIEW, dialog);
            
            controller.setDialogStage(dialog);
            controller.setMainApp(this);
            controller.initializeAfterSettingMain();
            // Show the dialog and wait until the user closes it
            dialog.showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Stage getNewDialogStage(String tituloDialogo) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(tituloDialogo);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        return dialogStage;
    }

    private Initializable openNewDialogScene(String fxml, Stage dialogStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));

        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        dialogStage.centerOnScreen();
        dialogStage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public ServicioBancario getServicioBancario() {
        return servicioBancario;
    }

    public void setServicioBancario(ServicioBancario servicio) {
        this.servicioBancario = servicio;
    }

}
