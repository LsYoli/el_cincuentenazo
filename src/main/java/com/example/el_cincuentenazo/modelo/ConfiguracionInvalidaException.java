package com.example.el_cincuentenazo.modelo;


/**
 * Excepción no marcada que indica un problema de configuración o estado inconsistente.
 * Generalmente representa errores de programación que no deberían ocurrir en runtime normal.
 */
public class ConfiguracionInvalidaException extends RuntimeException {

    public ConfiguracionInvalidaException(String mensaje) {
        super(mensaje);
    }

    public ConfiguracionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}