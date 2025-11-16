package com.example.el_cincuentenazo;

import com.example.el_cincuentenazo.controlador.TableroController;
import com.example.el_cincuentenazo.modelo.ConfiguracionInvalidaException;
import com.example.el_cincuentenazo.modelo.Partida;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación JavaFX del cincuentenazo.
 */
public class Main extends Application {

    private static Main instancia;

    private Stage escenarioPrincipal;
    private final Partida partida;

    /**
     * Crea la aplicación e inicializa la partida compartida.
     */
    public Main() {
        instancia = this;
        this.partida = new Partida();
    }

    /**
     * Obtiene la instancia de la aplicación en ejecución.
     *
     * @return instancia activa de {@link Main}
     */
    public static Main obtenerInstancia() {
        return instancia;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        stage.setTitle("Cincuentazo");
        mostrarVista("/com/example/el_cincuentenazo/principal.fxml");
        stage.show();
    }

    /**
     * Cambia la escena principal cargando el FXML especificado.
     *
     * @param rutaFXML recurso a cargar
     */
    public void mostrarVista(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            escenarioPrincipal.setScene(escena);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFXML, e);
        }
    }

    /**
     * Abre la vista de instrucciones.
     */
    public void abrirVistaComoJugar() {
        mostrarVista("/com/example/el_cincuentenazo/Instrucciones.fxml");
    }

    /**
     * Vuelve a la pantalla inicial.
     */
    public void abrirVistaInicio() {
        mostrarVista("/com/example/el_cincuentenazo/principal.fxml");
    }

    /**
     * Prepara la partida y muestra el tablero con la cantidad de CPUs indicada.
     *
     * @param cantidadCPUs número de rivales automáticos
     */
    public void abrirTablero(int cantidadCPUs) {
        try {
            partida.iniciar(cantidadCPUs);
        } catch (ConfiguracionInvalidaException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Configuración inválida");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/el_cincuentenazo/juego.fxml"));
            Parent raiz = loader.load();
            TableroController controller = loader.getController();
            controller.configurarPartida(partida);
            controller.renderizar();
            Scene escena = new Scene(raiz);
            escenarioPrincipal.setScene(escena);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo abrir el tablero", e);
        }
    }

    /**
     * Expone la partida compartida.
     *
     * @return instancia única reutilizada por la aplicación
     */
    public Partida getPartida() {
        return partida;
    }

    /**
     * Punto de entrada tradicional de Java.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
