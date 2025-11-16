package com.example.el_cincuentenazo.controlador;

import com.example.el_cincuentenazo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controlador de la vista de instrucciones "Cómo jugar".
 */
public class ComoJugarController {

    @FXML
    private Button botonRegresar;

    /**
     * Vuelve a la pantalla principal cuando se presiona el botón.
     *
     * @param evento evento generado por el botón
     */
    @FXML
    private void manejarBotonRegresar(ActionEvent evento) {
        Main.obtenerInstancia().abrirVistaInicio();
    }
}
