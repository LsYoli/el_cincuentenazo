package com.example.el_cincuentenazo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase base para cualquier jugador del cincuentenazo. Mantiene el nombre, la
 * mano de cartas y el estado de eliminación, y proporciona operaciones comunes
 * para manipular esa información.
 */
public abstract class Jugador {

    /** Nombre que se mostrará para identificar al jugador. */
    protected final String nombre;
    /** Cartas que posee el jugador en un momento dado. */
    protected final List<Carta> mano;
    /** Bandera que indica si el jugador salió de la partida. */
    protected boolean eliminado;

    /**
     * Crea un nuevo jugador con su nombre y una mano vacía.
     *
     * @param nombre etiqueta que identifica al jugador dentro de la interfaz
     */
    protected Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.eliminado = false;
    }

    /**
     * Obtiene el nombre visible del jugador.
     *
     * @return nombre definido al crear la instancia
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Informa si el jugador sigue participando de la partida.
     *
     * @return {@code true} si fue eliminado anteriormente
     */
    public boolean estaEliminado() {
        return eliminado;
    }

    /**
     * Devuelve una vista inmutable de las cartas que posee el jugador.
     *
     * @return lista inmodificable con la mano actual
     */
    public List<Carta> getMano() {
        return Collections.unmodifiableList(mano);
    }

    /**
     * Entrega cuatro cartas iniciales tomadas de la baraja, siempre que haya
     * disponibilidad.
     *
     * @param baraja mazo desde el cual se reparten las cartas
     */
    public void obtenerCartasIniciales(Baraja baraja) {
        mano.clear();
        for (int i = 0; i < 4; i++) {
            Carta carta = baraja.robar();
            if (carta != null) {
                mano.add(carta);
            }
        }
    }

    /**
     * Calcula qué cartas pueden jugarse sin que la suma supere el límite de 50.
     *
     * @param sumaMesa valor actual acumulado en la mesa
     * @return lista con las cartas seguras para jugar
     */
    public List<Carta> cartasJugables(int sumaMesa) {
        List<Carta> jugables = new ArrayList<>();
        for (Carta carta : mano) {
            int valor = carta.valorParaSuma(sumaMesa);
            if (sumaMesa + valor <= 50) {
                jugables.add(carta);
            }
        }
        return jugables;
    }

    /**
     * Define la estrategia de cada tipo de jugador para decidir qué carta jugar.
     *
     * @param sumaMesa valor actual en la mesa
     * @return carta escogida para la jugada
     */
    public abstract Carta jugarCarta(int sumaMesa);

    /**
     * Repone cartas hasta completar cuatro, siempre que la baraja aún disponga
     * de unidades.
     *
     * @param baraja mazo desde el cual se roba
     */
    public void robarSiHaceFalta(Baraja baraja) {
        while (mano.size() < 4 && !baraja.estaVacia()) {
            Carta carta = baraja.robar();
            if (carta != null) {
                mano.add(carta);
            } else {
                break;
            }
        }
    }

    /**
     * Marca al jugador como eliminado y libera sus cartas actuales.
     */
    public void eliminar() {
        eliminado = true;
        mano.clear();
    }
}
