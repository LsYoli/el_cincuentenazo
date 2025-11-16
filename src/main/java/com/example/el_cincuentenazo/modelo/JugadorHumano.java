package com.example.el_cincuentenazo.modelo;

/**
 * Representa al jugador controlado por una persona a través de la interfaz de
 * usuario.
 */
public class JugadorHumano extends Jugador {

    /**
     * Construye al jugador humano con el nombre que se verá en pantalla.
     *
     * @param nombre alias elegido por la persona
     */
    public JugadorHumano(String nombre) {
        super(nombre);
    }

    @Override
    public Carta jugarCarta(int sumaMesa) {
        return cartasJugables(sumaMesa).stream().findFirst().orElse(null);
    }

    /**
     * Comprueba si una carta concreta puede jugarse manteniendo la suma en el
     * rango permitido.
     *
     * @param carta    carta seleccionada en la interfaz
     * @param sumaMesa valor actual acumulado
     * @return {@code true} si la carta pertenece a la mano y es segura
     */
    public boolean puedeJugar(Carta carta, int sumaMesa) {
        if (carta == null) {
            return false;
        }
        if (!mano.contains(carta)) {
            return false;
        }
        int valor = carta.valorParaSuma(sumaMesa);
        return sumaMesa + valor <= 50;
    }

    /**
     * Ejecuta la jugada elegida por la persona, retirando la carta de su mano si
     * resulta válida.
     *
     * @param carta    carta que quiere jugar el usuario
     * @param sumaMesa valor actual de la mesa
     * @return carta jugada o {@code null} si no es válida
     */
    public Carta jugarCartaElegida(Carta carta, int sumaMesa) {
        if (!puedeJugar(carta, sumaMesa)) {
            return null;
        }
        mano.remove(carta);
        return carta;
    }
}
