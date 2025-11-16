package com.example.el_cincuentenazo.hilos;

import com.example.el_cincuentenazo.modelo.Jugador;
import com.example.el_cincuentenazo.modelo.Partida;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hilo utilitario que simula el tiempo de "pensamiento" de las CPUs antes de que la lógica real
 * de {@link com.example.el_cincuentenazo.modelo.Partida#jugarTurnoCPU()} se ejecute. Este hilo no
 * modifica el estado del juego: únicamente introduce una demora visual y expone qué CPU está
 * pensando actualmente para que la interfaz pueda informarlo.
 */
public class TurnoCPUThread extends Thread {

    private final Partida partida;
    private volatile boolean cpuPensando;
    private final Random random;
    private volatile String cpuActualPensando;

    /**
     * Crea una nueva instancia asociada a la partida en curso. El hilo se marca como daemon para que
     * no impida cerrar la aplicación si la ventana principal se clausura.
     *
     * @param partida referencia a la partida cuyos rivales automáticos se van a simular.
     */
    public TurnoCPUThread(Partida partida) {
        this.partida = partida;
        this.cpuPensando = true;
        this.random = new Random();
        this.cpuActualPensando = null;
        this.setDaemon(true);
    }

    /**
     * Recorre las CPUs activas y simula un tiempo de espera entre dos y cuatro segundos por cada
     * una. Antes de dormir el hilo actualiza {@link #cpuActualPensando} para que otros componentes
     * puedan mostrar qué CPU está siendo procesada.
     */
    @Override
    public void run() {
        try {
            List<Jugador> cpusActivas = new ArrayList<>();
            for (int i = 1; i < partida.getJugadores().size(); i++) {
                Jugador candidato = partida.getJugadores().get(i);
                if (!candidato.estaEliminado()) {
                    cpusActivas.add(candidato);
                }
            }

            for (Jugador cpu : cpusActivas) {
                if (partida.estaTerminada()) {
                    break;
                }
                cpuActualPensando = cpu.getNombre();
                int tiempoPensamiento = 2000 + random.nextInt(2001);
                Thread.sleep(tiempoPensamiento);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("TurnoCPUThread interrumpido: " + e.getMessage());
        } finally {
            cpuPensando = false;
            cpuActualPensando = null;
        }
    }

    /**
     * Indica si el hilo continúa simulando el pensamiento de alguna CPU.
     *
     * @return {@code true} mientras reste al menos una CPU por simular.
     */
    public boolean estaPensando() {
        return cpuPensando;
    }

    /**
     * Devuelve el nombre de la CPU cuya espera se está simulando actualmente. Puede ser {@code null}
     * si todavía no comenzó el ciclo o si el hilo ya finalizó.
     *
     * @return nombre legible de la CPU en proceso, o {@code null} si no corresponde.
     */
    public String getCpuActualPensando() {
        return cpuActualPensando;
    }
}
