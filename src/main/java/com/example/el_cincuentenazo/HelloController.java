package com.example.el_cincuentenazo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de ejemplo generado por la plantilla de JavaFX.
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    /**
     * Cambia el texto de bienvenida cuando se pulsa el botón de ejemplo.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
