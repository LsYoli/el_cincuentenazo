package com.example.el_cincuentenazo.controlador;

import com.example.el_cincuentenazo.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

/**
 * Controlador de la pantalla inicial donde el usuario elige la cantidad de CPUs
 * y navega hacia otras vistas.
 */
public class InicioController {

    @FXML
    private Button botonJugar;
    @FXML
    private Button botonAyuda;
    @FXML
    private ChoiceBox<Integer> selectorCPUs;
    @FXML
    private Label etiquetaDetalleCPUs;

    /**
     * Configura los elementos del formulario al cargarse el FXML.
     */
    @FXML
    public void initialize() {
        selectorCPUs.setItems(FXCollections.observableArrayList(1, 2, 3));
        selectorCPUs.setValue(1);
        actualizarEtiquetaCPUs();
        selectorCPUs.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevoValor) -> actualizarEtiquetaCPUs());
    }

    /**
     * Inicia una nueva partida con la cantidad de CPUs elegida.
     *
     * @param evento evento generado por el botón
     */
    @FXML
    private void manejarBotonJugar(ActionEvent evento) {
        Integer seleccion = selectorCPUs.getValue();
        if (seleccion == null) {
            mostrarAlerta("Debes escoger cuántas CPU enfrentar.");
            return;
        }
        Main.obtenerInstancia().abrirTablero(seleccion);
    }

    /**
     * Abre la vista con las instrucciones del juego.
     *
     * @param evento evento generado por el botón
     */
    @FXML
    private void manejarBotonAyuda(ActionEvent evento) {
        Main.obtenerInstancia().abrirVistaComoJugar();
    }

    /**
     * Actualiza la etiqueta que describe la selección actual de CPUs.
     */
    private void actualizarEtiquetaCPUs() {
        int cantidad = selectorCPUs.getValue() == null ? 0 : selectorCPUs.getValue();
        String texto = cantidad == 1 ? "Te enfrentarás a 1 CPU." : "Te enfrentarás a " + cantidad + " CPU.";
        etiquetaDetalleCPUs.setText(texto);
    }

    /**
     * Muestra un cuadro de diálogo informativo.
     *
     * @param mensaje texto a mostrar
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
