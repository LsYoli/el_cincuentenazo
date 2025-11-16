package com.example.el_cincuentenazo.modelo;

/**
 * Excepción no verificada que indica parámetros o estados inválidos al iniciar
 * una partida.
 */
public class ConfiguracionInvalidaException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje detalle del problema detectado
     */
    public ConfiguracionInvalidaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción incluyendo la causa original.
     *
     * @param mensaje mensaje a mostrar
     * @param causa   excepción subyacente
     */
    public ConfiguracionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
