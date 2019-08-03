/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bankito;

import com.bankito.aplicacion.ClienteViewController;
import com.bankito.aplicacion.LoginViewController;
import com.bankito.aplicacion.MainCont;
import com.bankito.servicio.ServicioBancario;
import com.bankito.servicio.ServicioBancarioFactory;
import com.bankito.util.AppConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
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
        
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("presentacion/images/Bankito Logo app.png")));
        primaryStage.setTitle("Bankito");
        goToLogin();
        primaryStage.show();
    }

    
    
    public void goToLogin() {
        try {
            LoginViewController login = (LoginViewController) replaceSceneContent("presentacion/LoginView.fxml");
            login.setMainApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void goToClienteView(){
        try {
            ClienteViewController login = (ClienteViewController) replaceSceneContent("presentacion/ClienteView.fxml");
            login.setMainApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
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
