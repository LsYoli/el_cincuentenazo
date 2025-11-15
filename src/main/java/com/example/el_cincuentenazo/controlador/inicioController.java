package com.example.el_cincuentenazo.controlador; // Coloca la clase dentro del paquete de controladores

import com.example.el_cincuentenazo.Main; // Importa la clase principal para solicitar cambios de escena
import javafx.collections.FXCollections; // Importa utilidades para crear listas observables
import javafx.event.ActionEvent; // Importa el tipo de evento de acción de JavaFX
import javafx.fxml.FXML; // Importa la anotación FXML para vincular componentes
import javafx.scene.control.Alert; // Importa la clase Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // Importa la clase Button para referenciar botones del FXML
import javafx.scene.control.ChoiceBox; // Importa ChoiceBox para elegir la cantidad de CPU
import javafx.scene.control.Label; // Importa Label para mostrar textos dinámicos

// Controlador de la pantalla inicial donde se configura la partida.
public class inicioController { // Declara la clase pública InicioController

    @FXML
    private Button botonJugar; // Referencia al botón que inicia la partida

    @FXML
    private Button botonAyuda; // Referencia al botón que abre la vista de instrucciones

    @FXML
    private ChoiceBox<Integer> selectorCPUs; // Referencia al componente que permite elegir la cantidad de CPU

    @FXML
    private Label etiquetaDetalleCPUs; // Etiqueta que muestra información sobre la selección actual

    @FXML
    public void initialize() { // Método que JavaFX ejecuta automáticamente después de cargar el FXML
        selectorCPUs.setItems(FXCollections.observableArrayList(1, 2, 3)); // Carga las opciones válidas en el selector
        selectorCPUs.setValue(1); // Selecciona por defecto un rival automático para que siempre haya un valor válido
        actualizarEtiquetaCPUs(); // Actualiza la etiqueta de detalle en función del valor inicial
        selectorCPUs.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevoValor) -> actualizarEtiquetaCPUs()); // Añade un oyente para refrescar el texto cuando se cambie la selección
    } // Cierra el método initialize

    @FXML
    private void manejarBotonJugar(ActionEvent evento) { // Método que se ejecuta al presionar el botón Jugar
        Integer seleccion = selectorCPUs.getValue(); // Obtiene la cantidad elegida de rivales CPU
        if (seleccion == null) { // Verifica que exista una selección válida
            mostrarAlerta("Debes escoger cuántas CPU enfrentar."); // Muestra una alerta informativa si no se eligió ninguna opción
            return; // Sale del método porque no se puede continuar sin selección
        } // Cierra la comprobación de selección nula
        Main.obtenerInstancia().abrirTablero(seleccion); // Solicita a la clase principal que abra el tablero con la cantidad elegida
    } // Cierra el método manejarBotonJugar

    @FXML
    private void manejarBotonAyuda(ActionEvent evento) { // Método que responde al botón de cómo jugar
        Main.obtenerInstancia().abrirVistaComoJugar(); // Indica a la clase principal que muestre la vista de instrucciones
    } // Cierra el método manejarBotonAyuda

    private void actualizarEtiquetaCPUs() { // Método que refresca el texto informativo sobre las CPU seleccionadas
        int cantidad = selectorCPUs.getValue() == null ? 0 : selectorCPUs.getValue(); // Obtiene la selección actual o cero si no hay valor
        String texto = cantidad == 1 ? "Te enfrentarás a 1 CPU." : "Te enfrentarás a " + cantidad + " CPU."; // Construye un mensaje amigable en singular o plural
        etiquetaDetalleCPUs.setText(texto); // Actualiza la etiqueta del FXML con el texto generado
    } // Cierra el método actualizarEtiquetaCPUs

    private void mostrarAlerta(String mensaje) { // Método auxiliar para mostrar un cuadro de diálogo sencillo
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // Crea una nueva alerta de tipo informativo
        alerta.setTitle("Información"); // Asigna un título descriptivo
        alerta.setHeaderText(null); // Elimina el encabezado para que el mensaje sea más limpio
        alerta.setContentText(mensaje); // Establece el mensaje que se mostrará al usuario
        alerta.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    } // Cierra el método mostrarAlerta
} // Cierra la clase InicioController
