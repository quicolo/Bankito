/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito;

import com.bankito.aplicacion.ClienteViewController;
import com.bankito.aplicacion.DatosClienteDialogViewController;
import com.bankito.aplicacion.DetallesCuentaDialogViewController;
import com.bankito.aplicacion.IngresarDialogViewController;
import com.bankito.aplicacion.LoginViewController;
import com.bankito.aplicacion.RetirarDialogViewController;
import com.bankito.presentacion.modelos.CuentaModelo;
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
 * <h1>MainFX</h1>
 * Esta clase es el punto de entrada de la aplicación y además extiende la clase
 * javafx.application.Application. Esta herencia obliga a esta clase a realizar
 * ciertas tareas sin poder delegarlas en otras clases:
 * - Esta clase es la que debe crear la ventana (stage) principal de la aplicación
 * - Es la responsable de crear nuevas ventanas o diálogos dependientes
 *   de la ventana principal
 * - Es la responsable de cambiar de escenas (scene) dentro cada una de las ventanas.
 *   Esto obliga a que los distintos controladores del sistema tengan que llamar
 *   a MainFX para realizar el cambio de ventana o de escena. Para ello, se 
 *   implementan métodos del tipo goToXXX() que cargan la ventana o la escena
 *   y devuelve una referencia al controlador correspondiente.
 * <p>
 * Por otro lado, esta clase se encarga de la carga inicial de la configuración 
 * de la aplicación desde el fichero de configuración haciendo uso de la clase
 * AppConfiguration.
 * <p>
 * Por último, dado que esta clase es el punto de comienzo y de retorno de las
 * distintas vistas y controladores, se ha decidido utilizar como punto de 
 * acceso a la referencia del objeto ServicioBancario que utiliza.
 *
 * @author Enrique Royo Sánchez
 */
public class MainFX extends Application {

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

        stage.getIcons().add(new Image(MainFX.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));
        
        String appName = AppConfiguration.getProperty("APP_NAME", "Bankito");
        stage.setTitle(appName);
        goToLogin();
        stage.centerOnScreen();
        stage.show();
        
    }

    public void goToLogin() {
        try {
            LoginViewController login = (LoginViewController) replaceSceneContent(ResourcePath.LOGIN_VIEW);
            login.setMainApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void goToClienteView() {
        try {
            ClienteViewController controller =  (ClienteViewController) replaceSceneContent(ResourcePath.CLIENTE_VIEW);
            controller.setMainApp(this);
            controller.initializeAfterSettingMain(); 
        } catch (Exception ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void goToDialogoIngresar(CuentaModelo cuenta) {
        try {
            Stage dialog = getNewDialogStage("Ingresar en tu cuenta corriente");
            IngresarDialogViewController controller = 
                    (IngresarDialogViewController) openNewDialogScene(ResourcePath.INGRESAR_DIALOG_VIEW, dialog);
            
            controller.setDialogStage(dialog);
            controller.setMainApp(this);
            controller.setModel(cuenta);
            controller.initializeAfterSettingMain();
            // Show the dialog and wait until the user closes it
            dialog.showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDialogoRetirar(CuentaModelo cuenta) {
        try {
            Stage dialog = getNewDialogStage("Retirar dinero de tu cuenta corriente");
            RetirarDialogViewController controller = 
                    (RetirarDialogViewController) openNewDialogScene(ResourcePath.RETIRAR_DIALOG_VIEW, dialog);
            
            controller.setDialogStage(dialog);
            controller.setMainApp(this);
            controller.setModel(cuenta);
            controller.initializeAfterSettingMain();
            // Show the dialog and wait until the user closes it
            dialog.showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
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
        InputStream in = MainFX.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainFX.class.getResource(fxml));

        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.getIcons().add(new Image(MainFX.class.getResourceAsStream(ResourcePath.BANKITO_ICON)));
        dialogStage.centerOnScreen();
        dialogStage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainFX.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainFX.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.hide();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
        return (Initializable) loader.getController();
    }

    public ServicioBancario getServicioBancario() {
        return servicioBancario;
    }

    public void setServicioBancario(ServicioBancario servicio) {
        this.servicioBancario = servicio;
    }

    public void goToDialogoVerDetallesCuenta(CuentaModelo cuenta) {
        try {
            Stage dialog = getNewDialogStage("Detalles de tu cuenta corriente");
            DetallesCuentaDialogViewController controller = 
                    (DetallesCuentaDialogViewController) openNewDialogScene(ResourcePath.DETALLES_CUENTA_DIALOG_VIEW, dialog);
            
            controller.setDialogStage(dialog);
            controller.setMainApp(this);
            controller.setModel(cuenta);
            controller.initializeAfterSettingMain();
            // Show the dialog and wait until the user closes it
            dialog.showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
