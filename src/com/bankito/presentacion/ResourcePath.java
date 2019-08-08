package com.bankito.presentacion;

/**
* <h1>ResourcePath</h1>
* Esta clase funciona como un contenedor que centraliza las rutas a los distintos
* recursos necesarios para la interfaz gráfica de usuario que utiliza la aplicación. 
* <p>
* Estos recursos se almacenan como cadenas de texto públicas y estáticas para
* facilitar el acceso a las mismas desde cualquier punto de la aplicación.
* <p>
* Asimismo, se incluye un constructor privado sin parámetros para que dicha clase
* no pueda ser instanciada.
* 
* @author  Enrique Royo Sánchez
*/
public class ResourcePath {
    public static String BANKITO_ICON = "presentacion/images/Bankito Logo app.png";
    public static String USER_BUTTON_ICON = "presentacion/images/user46x46.png";
    public static String POWEROFF_BUTTON_ICON = "presentacion/images/power-off46x46.png";
    
    
    public static String LOGIN_VIEW = "presentacion/LoginView.fxml";
    public static String CLIENTE_VIEW = "presentacion/ClienteView.fxml";
    public static String DATOS_CLIENTE_DIALOG_VIEW = "presentacion/DatosClienteDialogView.fxml";
    public static String INGRESAR_DIALOG_VIEW = "presentacion/IngresarDialogView.fxml";
    public static String RETIRAR_DIALOG_VIEW = "presentacion/RetirarDialogView.fxml";
    public static String DETALLES_CUENTA_DIALOG_VIEW = "presentacion/DetallesCuentaDialogView.fxml";
    
    
    private ResourcePath(){}
    
}
