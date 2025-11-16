package com.example.el_cincuentenazo; // Declara el paquete raíz de la aplicación

import com.example.el_cincuentenazo.controlador.TableroController; // Importa el controlador del tablero para configurarlo
import com.example.el_cincuentenazo.modelo.Partida; // Importa la clase que gestiona la lógica del juego
import javafx.application.Application; // Importa la clase base para aplicaciones JavaFX
import javafx.fxml.FXMLLoader; // Importa el cargador de archivos FXML
import javafx.scene.Parent; // Importa el tipo Parent que representa la raíz de una escena
import javafx.scene.Scene; // Importa la clase Scene para mostrar contenido en pantalla
import javafx.stage.Stage; // Importa la clase Stage que representa la ventana principal

// Clase principal que inicia la aplicación JavaFX.
public class Main extends Application { // Declara la clase Main extendiendo Application

    private static Main instancia; // Guarda una referencia estática para acceder desde los controladores

    private Stage escenarioPrincipal; // Mantiene el Stage para poder cambiar de escena
    private final Partida partida; // Instancia única de la lógica del juego que se comparte en toda la aplicación

    public Main() { // Constructor sin argumentos requerido por JavaFX
        instancia = this; // Asigna la instancia actual para que se pueda consultar más adelante
        this.partida = new Partida(); // Crea la partida principal que se reutilizará entre escenas
    } // Cierra el constructor de Main

    public static Main obtenerInstancia() { // Método estático para recuperar la instancia activa de Main
        return instancia; // Devuelve la instancia almacenada previamente
    } // Cierra el método obtenerInstancia

    @Override
    public void start(Stage stage) throws Exception { // Método de arranque de JavaFX
        this.escenarioPrincipal = stage; // Guarda el stage recibido para reutilizarlo
        stage.setTitle("Cincuentazo"); // Configura el título de la ventana
        mostrarVista("/com/example/el_cincuentenazo/principal.fxml"); // Carga y muestra la vista de inicio
        stage.show(); // Hace visible la ventana una vez cargada la escena inicial
    } // Cierra el método start

    public void mostrarVista(String rutaFXML) { // Método genérico para cambiar la vista mostrada en pantalla
        try { // Inicia un bloque que puede lanzar excepciones de carga
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML)); // Prepara el cargador con la ruta del archivo FXML
            Parent raiz = loader.load(); // Carga el archivo FXML y obtiene la raíz del nodo gráfico
            Scene escena = new Scene(raiz); // Crea una nueva escena utilizando la raíz cargada
            escenarioPrincipal.setScene(escena); // Reemplaza la escena actual con la nueva
        } catch (Exception e) { // Captura cualquier excepción que pudiera ocurrir al cargar la vista
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFXML, e); // Envuelve la excepción en un RuntimeException con mensaje descriptivo
        } // Cierra el bloque catch
    } // Cierra el método mostrarVista

    public void abrirVistaComoJugar() { // Método específico para abrir la escena de instrucciones
        mostrarVista("/com/example/el_cincuentenazo/Instrucciones.fxml"); // Reutiliza el método genérico con la ruta del FXML correspondiente
    } // Cierra el método abrirVistaComoJugar

    public void abrirVistaInicio() { // Método que regresa a la pantalla inicial
        mostrarVista("/com/example/el_cincuentenazo/principal.fxml"); // Carga nuevamente la vista principal
    } // Cierra el método abrirVistaInicio

    public void abrirTablero(int cantidadCPUs) { // Método que prepara la partida y abre la vista del tablero
        partida.iniciar(cantidadCPUs); // Inicializa la lógica del juego con la cantidad solicitada de rivales
        try { // Inicia el bloque que puede lanzar errores de carga FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/el_cincuentenazo/juego.fxml")); // Crea el cargador con la ruta del tablero
            Parent raiz = loader.load(); // Carga el archivo de interfaz y obtiene la raíz del nodo
            TableroController controller = loader.getController(); // Recupera el controlador asociado al tablero
            controller.configurarPartida(partida); // Entrega la instancia de Partida para que el tablero la utilice
            controller.renderizar(); // Solicita al controlador que dibuje el estado inicial
            Scene escena = new Scene(raiz); // Crea una escena con el contenido cargado
            escenarioPrincipal.setScene(escena); // Muestra la nueva escena en la ventana principal
        } catch (Exception e) { // Captura errores durante la carga del tablero
            throw new RuntimeException("No se pudo abrir el tablero", e); // Envuelve la excepción para informar al desarrollador
        } // Cierra el bloque catch
    } // Cierra el método abrirTablero

    public Partida getPartida() { // Método de acceso a la instancia compartida de Partida
        return partida; // Devuelve la partida actual para consultas externas
    } // Cierra el método getPartida

    public static void main(String[] args) { // Punto de entrada tradicional de aplicaciones Java
        launch(args); // Delegado que inicia el ciclo de vida de JavaFX
    } // Cierra el método main
} // Cierra la clase Main
