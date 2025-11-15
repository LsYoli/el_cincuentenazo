package com.example.el_cincuentenazo.modelo; // Define la ubicación de la clase CPU

// Clase que modela a un rival controlado por la computadora.
public class CPU extends Jugador { // Declara la clase CPU como una subclase de Jugador

    public CPU(String nombre) { // Constructor que recibe el nombre que aparecerá en pantalla
        super(nombre); // Llama al constructor de Jugador para configurar atributos básicos
    } // Cierra el constructor de CPU

    @Override
    public Carta jugarCarta(int sumaMesa) { // Implementa la selección automática de cartas para la CPU
        for (Carta carta : mano) { // Recorre las cartas en el orden en que las recibió
            int valor = carta.valorParaSuma(sumaMesa); // Calcula cuánto aportaría la carta a la suma de la mesa
            if (sumaMesa + valor <= 50) { // Comprueba si la carta mantiene la suma dentro del límite
                mano.remove(carta); // Extrae la carta de la mano porque se va a jugar
                return carta; // Devuelve la carta válida encontrada
            } // Cierra la comprobación de carta segura
        } // Cierra el recorrido de la mano
        return null; // Si no encontró ninguna carta válida, devuelve null para indicar eliminación
    } // Cierra el método jugarCarta
} // Cierra la clase CPU
