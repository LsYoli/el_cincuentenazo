package com.example.el_cincuentenazo.modelo;

/**
 * Excepción marcada que indica que se intentó realizar una jugada inválida.
 * Obliga al llamador a manejar explícitamente los errores de lógica de juego.
 */
public class JugadaInvalidaException extends Exception {

    public JugadaInvalidaException(String mensaje) {
        super(mensaje);
    }

    public JugadaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}