package com.example.el_cincuentenazo;

import javafx.application.Application;

/**
 * Clase auxiliar utilizada por algunos entornos para lanzar la aplicación.
 */
public class Launcher {
    /**
     * Delegado que inicia {@link Main} desde un método {@code main} tradicional.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
