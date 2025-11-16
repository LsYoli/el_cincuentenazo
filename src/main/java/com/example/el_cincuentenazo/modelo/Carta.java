package com.example.el_cincuentenazo.modelo;

/**
 * Representa una carta individual de la baraja española reducida utilizada por
 * el juego.
 */
public class Carta {

    /** Palo o familia a la que pertenece la carta. */
    private final String palo;
    /** Valor nominal mostrado en la carta. */
    private final String valor;

    /**
     * Crea una carta con su valor y palo correspondientes.
     *
     * @param valor número o figura representada
     * @param palo  palo al que pertenece la carta
     */
    public Carta(String valor, String palo) {
        this.valor = valor;
        this.palo = palo;
    }

    /**
     * Devuelve el palo de la carta.
     *
     * @return palo asociado
     */
    public String getPalo() {
        return palo;
    }

    /**
     * Devuelve el valor nominal de la carta.
     *
     * @return valor textual configurado
     */
    public String getValor() {
        return valor;
    }

    /**
     * Calcula cuánto aporta la carta a la suma de la mesa considerando reglas
     * especiales para figuras y ases.
     *
     * @param sumaActual valor acumulado antes de jugar la carta
     * @return cantidad que debe sumarse al total
     */
    public int valorParaSuma(int sumaActual) {
        switch (valor) {
            case "J":
            case "Q":
            case "K":
                return -10;
            case "9":
                return 0;
            case "A":
                if (sumaActual + 10 <= 50) {
                    return 10;
                }
                return 1;
            default:
                return Integer.parseInt(valor);
        }
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}
