package com.example.el_cincuentenazo.modelo;

/**
 * Implementación de un jugador controlado por la computadora con una estrategia
 * simple: juega la primera carta segura disponible.
 */
public class CPU extends Jugador {

    /**
     * Construye una CPU con el nombre que se mostrará en pantalla.
     *
     * @param nombre etiqueta usada en la interfaz
     */
    public CPU(String nombre) {
        super(nombre);
    }

    @Override
    public Carta jugarCarta(int sumaMesa) {
        for (Carta carta : mano) {
            int valor = carta.valorParaSuma(sumaMesa);
            if (sumaMesa + valor <= 50) {
                mano.remove(carta);
                return carta;
            }
        }
        return null;
    }
}
