package com.example.el_cincuentenazo.modelo; // Define el paquete para las clases del modelo

import java.util.ArrayList; // Importa la lista dinámica para almacenar cartas
import java.util.Collections; // Importa utilidades para trabajar con listas inmutables
import java.util.List; // Importa la interfaz de lista

// Clase abstracta que encapsula la lógica común de cualquier jugador.
public abstract class Jugador { // Declara la clase abstracta Jugador

    protected final String nombre; // Guarda el nombre que se mostrará en pantalla
    protected final List<Carta> mano; // Lista que contiene las cartas actuales del jugador
    protected boolean eliminado; // Bandera que indica si el jugador sigue en la partida

    protected Jugador(String nombre) { // Constructor protegido para evitar instanciación directa fuera de la jerarquía
        this.nombre = nombre; // Asigna el nombre recibido al atributo correspondiente
        this.mano = new ArrayList<>(); // Inicializa la lista de cartas vacía
        this.eliminado = false; // Marca al jugador como activo al inicio
    } // Cierra el constructor de Jugador

    public String getNombre() { // Método que expone el nombre del jugador
        return nombre; // Devuelve el nombre almacenado
    } // Cierra el método getNombre

    public boolean estaEliminado() { // Método que indica si el jugador está eliminado
        return eliminado; // Retorna el estado de eliminación actual
    } // Cierra el método estaEliminado

    public List<Carta> getMano() { // Método que devuelve una vista inmutable de la mano
        return Collections.unmodifiableList(mano); // Devuelve una lista que no puede modificarse desde afuera
    } // Cierra el método getMano

    public void obtenerCartasIniciales(Baraja baraja) { // Método que reparte cuatro cartas al comienzo de la partida
        mano.clear(); // Asegura que la mano esté vacía antes de repartir
        for (int i = 0; i < 4; i++) { // Itera cuatro veces para entregar cada carta inicial
            Carta carta = baraja.robar(); // Extrae una carta del mazo principal
            if (carta != null) { // Verifica que la baraja aún tenga cartas
                mano.add(carta); // Agrega la carta obtenida a la mano del jugador
            } // Cierra la comprobación de carta nula
        } // Cierra el ciclo de reparto
    } // Cierra el método obtenerCartasIniciales

    public List<Carta> cartasJugables(int sumaMesa) { // Método que identifica las cartas seguras para jugar
        List<Carta> jugables = new ArrayList<>(); // Crea una lista temporal para guardar las cartas válidas
        for (Carta carta : mano) { // Recorre todas las cartas disponibles en la mano
            int valor = carta.valorParaSuma(sumaMesa); // Calcula el impacto de la carta sobre la mesa actual
            if (sumaMesa + valor <= 50) { // Verifica si jugarla mantiene la suma en 50 o menos
                jugables.add(carta); // Si la carta es segura, se agrega a la lista
            } // Cierra la comprobación de seguridad
        } // Cierra el recorrido de la mano
        return jugables; // Devuelve la lista con las cartas que se pueden jugar
    } // Cierra el método cartasJugables

    public abstract Carta jugarCarta(int sumaMesa); // Declaración abstracta que obliga a definir cómo juega cada tipo de jugador

    public void robarSiHaceFalta(Baraja baraja) { // Método que garantiza que el jugador conserve cuatro cartas si es posible
        while (mano.size() < 4 && !baraja.estaVacia()) { // Repite mientras falten cartas y el mazo tenga disponibles
            Carta carta = baraja.robar(); // Toma una nueva carta del mazo principal
            if (carta != null) { // Confirma que realmente se obtuvo una carta
                mano.add(carta); // Agrega la carta a la mano
            } else { // Si no se obtuvo carta
                break; // Sale del ciclo porque no hay más cartas que tomar
            } // Cierra el bloque alternativo
        } // Cierra el ciclo de robo
    } // Cierra el método robarSiHaceFalta

    public void eliminar() { // Método que marca al jugador como fuera del juego
        eliminado = true; // Cambia la bandera para indicar que ya no participa
        mano.clear(); // Vacía la mano para liberar cartas
    } // Cierra el método eliminar
} // Cierra la clase abstracta Jugador
