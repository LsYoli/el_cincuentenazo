package com.example.el_cincuentenazo.modelo; // Ubica la clase dentro del paquete de modelo

// Clase que representa al jugador controlado por la persona frente al juego.
public class JugadorHumano extends Jugador { // Declara la clase JugadorHumano heredando de Jugador

    public JugadorHumano(String nombre) { // Constructor que recibe el nombre visible del jugador humano
        super(nombre); // Llama al constructor de la clase base para inicializar atributos comunes
    } // Cierra el constructor de JugadorHumano

    @Override
    public Carta jugarCarta(int sumaMesa) { // Implementación obligatoria aunque el humano elige manualmente
        return cartasJugables(sumaMesa).stream().findFirst().orElse(null); // Devuelve la primera carta válida o null si no hay disponible
    } // Cierra el método jugarCarta

    public boolean puedeJugar(Carta carta, int sumaMesa) { // Método que verifica si una carta específica es jugable
        if (carta == null) { // Comprueba si la carta es nula
            return false; // No se puede jugar algo inexistente
        } // Cierra la comprobación de carta nula
        if (!mano.contains(carta)) { // Verifica que la carta pertenezca a la mano del jugador
            return false; // Si la carta no está en la mano, no puede jugarse
        } // Cierra la comprobación de pertenencia
        int valor = carta.valorParaSuma(sumaMesa); // Calcula cuánto sumaría la carta en la mesa
        return sumaMesa + valor <= 50; // Devuelve verdadero solo si jugarla no supera la meta de 50
    } // Cierra el método puedeJugar

    public Carta jugarCartaElegida(Carta carta, int sumaMesa) { // Método que aplica el juego de la carta seleccionada en la interfaz
        if (!puedeJugar(carta, sumaMesa)) { // Reutiliza la verificación para asegurarse de que el movimiento sea legal
            return null; // Si la carta no es válida, se retorna null para que el controlador informe al usuario
        } // Cierra la comprobación de legalidad
        mano.remove(carta); // Elimina la carta de la mano porque se acaba de jugar
        return carta; // Devuelve la carta jugada para que la partida la procese
    } // Cierra el método jugarCartaElegida
} // Cierra la clase JugadorHumano
