package com.example.el_cincuentenazo.hilos; // Declara el paquete de hilos

import javafx.application.Platform; // Importa Platform para actualizar la UI desde hilos secundarios
import javafx.scene.control.Label; // Importa Label para manipular el texto animado

// Hilo que anima el texto "Pensando..." mientras las CPUs procesan sus turnos
public class AnimacionPensamientoThread extends Thread { // Extiende Thread para crear el hilo de animación

    private final Label labelEstado; // Referencia al Label donde se mostrará la animación
    private final TurnoCPUThread turnoCPUThread; // Referencia al hilo de las CPUs para saber cuándo detenerse
    private final String[] estados = {"CPU pensando", "CPU pensando.", "CPU pensando..", "CPU pensando..."}; // Ciclo de textos para la animación
    private int indiceEstado; // Índice actual en el array de estados

    public AnimacionPensamientoThread(Label labelEstado, TurnoCPUThread turnoCPUThread) { // Constructor que recibe el label y el hilo CPU
        this.labelEstado = labelEstado; // Guarda la referencia al label
        this.turnoCPUThread = turnoCPUThread; // Guarda la referencia al hilo CPU
        this.indiceEstado = 0; // Inicializa el índice en 0
        this.setDaemon(true); // Marca como daemon para que no bloquee el cierre de la aplicación
    } // Cierra el constructor

    @Override
    public void run() { // Método que se ejecuta cuando el hilo inicia
        try { // Bloque para manejar interrupciones
            while (turnoCPUThread.estaPensando()) { // Mientras las CPUs sigan procesando turnos
                final String textoActual = estados[indiceEstado]; // Obtiene el texto correspondiente al índice actual

                Platform.runLater(() -> { // Ejecuta la actualización de UI en el hilo de JavaFX
                    labelEstado.setText(textoActual); // Cambia el texto del label
                }); // Cierra el runLater

                indiceEstado = (indiceEstado + 1) % estados.length; // Avanza al siguiente estado (con ciclo)

                Thread.sleep(500); // Espera medio segundo antes de cambiar al siguiente texto
            } // Cierra el while

            // Cuando termina, limpia el label
            Platform.runLater(() -> { // Actualiza la UI una última vez
                labelEstado.setText(""); // Limpia el texto del label
            }); // Cierra el runLater

        } catch (InterruptedException e) { // Captura si el hilo es interrumpido
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
            Platform.runLater(() -> labelEstado.setText("")); // Limpia el label incluso si hay error
        } // Cierra el bloque catch
    } // Cierra el método run
} // Cierra la clase AnimacionPensamientoThread