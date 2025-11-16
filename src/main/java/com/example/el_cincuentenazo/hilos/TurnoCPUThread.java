package com.example.el_cincuentenazo.hilos; // Declara el nuevo paquete para hilos

import com.example.el_cincuentenazo.modelo.Carta; // Importa la clase Carta
import com.example.el_cincuentenazo.modelo.Jugador; // Importa la clase Jugador
import com.example.el_cincuentenazo.modelo.Partida; // Importa la clase Partida
import java.util.Random; // Importa Random para generar tiempos aleatorios

// Hilo que simula el procesamiento de turnos de las CPUs con delay realista
public class TurnoCPUThread extends Thread { // Extiende Thread para crear un hilo personalizado

    private final Partida partida; // Referencia a la partida actual para acceder al estado del juego
    private volatile boolean cpuPensando; // Bandera compartida que indica si las CPUs están procesando turnos
    private final Random random; // Generador de números aleatorios para variar el tiempo de "pensamiento"

    public TurnoCPUThread(Partida partida) { // Constructor que recibe la partida en curso
        this.partida = partida; // Guarda la referencia a la partida
        this.cpuPensando = true; // Inicializa la bandera en true porque el hilo va a comenzar a trabajar
        this.random = new Random(); // Crea el generador de números aleatorios
        this.setDaemon(true); // Marca este hilo como daemon para que no impida cerrar la aplicación
    } // Cierra el constructor

    @Override
    public void run() { // Método que se ejecuta cuando el hilo inicia
        try { // Bloque para manejar posibles interrupciones del sleep
            // Cuenta cuántas CPUs activas hay (que no estén eliminadas)
            int cpusActivas = 0; // Contador de CPUs que aún pueden jugar
            for (int i = 1; i < partida.getJugadores().size(); i++) { // Recorre todas las CPUs
                if (!partida.getJugadores().get(i).estaEliminado()) { // Si la CPU no está eliminada
                    cpusActivas++; // Incrementa el contador
                } // Cierra la comprobación
            } // Cierra el bucle de conteo

            // Simula el "pensamiento" total de todas las CPUs
            // Cada CPU toma entre 2 y 4 segundos
            for (int i = 0; i < cpusActivas && !partida.estaTerminada(); i++) { // Por cada CPU activa
                int tiempoPensamiento = 2000 + random.nextInt(2001); // Genera tiempo aleatorio entre 2000-4000 ms
                Thread.sleep(tiempoPensamiento); // Pausa el hilo simulando que la CPU piensa
            } // Cierra el bucle de delays

        } catch (InterruptedException e) { // Captura la excepción si el hilo es interrumpido
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
            System.err.println("TurnoCPUThread interrumpido: " + e.getMessage()); // Imprime mensaje de error
        } finally { // Bloque que siempre se ejecuta
            cpuPensando = false; // Marca que las CPUs terminaron de pensar
        } // Cierra el bloque finally
    } // Cierra el método run

    public boolean estaPensando() { // Método para consultar si las CPUs aún están procesando
        return cpuPensando; // Retorna el estado actual de la bandera
    } // Cierra el método estaPensando
} // Cierra la clase TurnoCPUThread