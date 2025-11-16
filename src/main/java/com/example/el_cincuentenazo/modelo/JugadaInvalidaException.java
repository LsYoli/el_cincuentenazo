package com.example.el_cincuentenazo.modelo;

/**
 * Excepción marcada que indica que se intentó realizar una jugada inválida en
 * función de las reglas del juego.
 */
public class JugadaInvalidaException extends Exception {

    /**
     * Crea la excepción con una descripción del motivo.
     *
     * @param mensaje detalle de la infracción
     */
    public JugadaInvalidaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción incluyendo la causa original.
     *
     * @param mensaje mensaje a mostrar
     * @param causa   error subyacente
     */
    public JugadaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
