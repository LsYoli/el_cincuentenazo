package com.example.el_cincuentenazo.hilos;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Hilo auxiliar que alterna el texto mostrado en la etiqueta de estado de las CPUs para simular
 * actividad mientras {@link TurnoCPUThread} introduce una espera artificial.
 */
public class AnimacionPensamientoThread extends Thread {

    private final Label labelEstado;
    private final Label labelTurno;
    private final TurnoCPUThread turnoCPUThread;
    private final String[] sufijos = {"", ".", "..", "..."};
    private int indiceEstado;

    /**
     * Construye el hilo de animación y lo marca como daemon para no impedir que la aplicación se
     * cierre si la ventana principal desaparece.
     *
     * @param labelEstado etiqueta que se actualizará.
     * @param labelTurno etiqueta utilizada para indicar en pantalla de quién es el turno.
     * @param turnoCPUThread hilo que indica cuánto tiempo debe continuar la animación.
     */
    public AnimacionPensamientoThread(Label labelEstado, Label labelTurno, TurnoCPUThread turnoCPUThread) {
        this.labelEstado = labelEstado;
        this.labelTurno = labelTurno;
        this.turnoCPUThread = turnoCPUThread;
        this.indiceEstado = 0;
        this.setDaemon(true);
    }

    /**
     * Mientras {@link TurnoCPUThread} informe que las CPUs siguen "pensando", alterna el texto
     * mostrado con el nombre de la CPU actualmente simulada.
     */
    @Override
    public void run() {
        try {
            while (turnoCPUThread.estaPensando()) {
                final String nombreCPU = turnoCPUThread.getCpuActualPensando();
                final String cpuMostrada = nombreCPU == null ? "CPU" : nombreCPU;
                final String textoActual = cpuMostrada + " pensando" + sufijos[indiceEstado];
                final String textoTurno = "Turno de " + cpuMostrada;

                Platform.runLater(() -> {
                    labelEstado.setText(textoActual);
                    labelTurno.setText(textoTurno);
                });

                indiceEstado = (indiceEstado + 1) % sufijos.length;
                Thread.sleep(500);
            }

            Platform.runLater(() -> labelEstado.setText(""));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Platform.runLater(() -> labelEstado.setText(""));
        }
    }
}
