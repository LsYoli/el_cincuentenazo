package com.example.el_cincuentenazo.modelo; // Declara el paquete donde vive la clase Carta

// Clase que modela una carta individual del juego.
public class Carta { // Define la clase pública Carta

    private final String palo; // Atributo que guarda el palo de la carta
    private final String valor; // Atributo que guarda el valor nominal de la carta

    public Carta(String valor, String palo) { // Constructor que recibe el valor y el palo
        this.valor = valor; // Asigna el valor recibido al atributo valor
        this.palo = palo; // Asigna el palo recibido al atributo palo
    } // Cierra el constructor de la carta

    public String getPalo() { // Método que devuelve el palo de la carta
        return palo; // Retorna el palo almacenado
    } // Cierra el método getPalo

    public String getValor() { // Método que devuelve el valor de la carta
        return valor; // Retorna el valor almacenado
    } // Cierra el método getValor

    public int valorParaSuma(int sumaActual) { // Método que calcula cuánto suma la carta según la situación actual
        switch (valor) { // Evalúa el valor de la carta con una estructura switch
            case "J": // Si la carta es una J
            case "Q": // O si la carta es una Q
            case "K": // O si la carta es una K
                return -10; // Estas cartas restan 10 puntos
            case "9": // Si la carta es un 9
                return 0; // El nueve no modifica la suma
            case "A": // Si la carta es un As
                if (sumaActual + 10 <= 50) { // Verifica si sumar 10 no hace superar el límite
                    return 10; // Si es seguro, usa el valor 10
                } // Cierra el if que evalúa el uso del 10
                return 1; // Si sumar 10 excede, usa el valor 1
            default: // Para cualquier otro valor numérico
                return Integer.parseInt(valor); // Convierte el texto del número a entero
        } // Cierra el switch de evaluación de la carta
    } // Cierra el método valorParaSuma

    @Override
    public String toString() { // Método que representa la carta como texto
        return valor + " de " + palo; // Concatena el valor, la palabra "de" y el palo
    } // Cierra el método toString
} // Cierra la clase Carta
