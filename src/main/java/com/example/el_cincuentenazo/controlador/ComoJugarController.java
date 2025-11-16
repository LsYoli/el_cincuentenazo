package com.example.el_cincuentenazo.controlador; // Declara el paquete del controlador de instrucciones

import com.example.el_cincuentenazo.Main; // Importa la clase principal para cambiar de escena
import javafx.event.ActionEvent; // Importa el tipo de evento asociado a los botones
import javafx.fxml.FXML; // Importa la anotación FXML para vincular componentes del diseño
import javafx.scene.control.Button; // Importa la clase Button para manipular el botón regresar

// Controlador responsable de la vista "Cómo jugar".
public class ComoJugarController { // Declara la clase pública ComoJugarController

    @FXML
    private Button botonRegresar; // Referencia al botón que regresa a la vista de inicio

    @FXML
    private void manejarBotonRegresar(ActionEvent evento) { // Método que responde cuando se presiona el botón regresar
        Main.obtenerInstancia().abrirVistaInicio(); // Solicita a la clase principal que muestre nuevamente la pantalla inicial
    } // Cierra el método manejarBotonRegresar
} // Cierra la clase ComoJugarController
