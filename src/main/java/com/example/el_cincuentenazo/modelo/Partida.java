package com.example.el_cincuentenazo.modelo; // Ubica la clase Partida dentro del paquete de modelo

import java.util.ArrayList; // Importa la lista dinámica para manejar colecciones
import java.util.Collections; // Importa utilidades para listas inmutables
import java.util.HashMap; // Importa la implementación HashMap para asociaciones clave-valor
import java.util.List; // Importa la interfaz List
import java.util.Map; // Importa la interfaz Map para los registros de cartas

// Clase que coordina todo el flujo del juego Cincuentazo.
public class Partida { // Declara la clase pública Partida

    public static final String RUTA_BARAJA = "cartas.txt"; // Ruta del archivo de cartas dentro de los recursos

    private final JugadorHumano jugadorHumano; // Referencia al jugador humano principal
    private final List<Jugador> jugadores; // Lista ordenada con el humano seguido de las CPU
    private final Baraja barajaComer; // Baraja principal desde la que se roban cartas
    private final Baraja barajaMesa; // Baraja secundaria que actúa como pila de descarte
    private final List<String> historial; // Registro de los eventos importantes de la partida
    private final Map<Jugador, Carta> ultimaCartaPorJugador; // Mapa que almacena la última carta jugada por cada participante

    private int sumaMesa; // Acumulado de puntos que hay actualmente en la mesa
    private int turnoActual; // Índice del jugador cuyo turno se está procesando
    private Carta ultimaCartaMesa; // Carta más reciente que se colocó en la mesa

    public Partida() { // Constructor que prepara las estructuras necesarias
        this.jugadorHumano = new JugadorHumano("Jugador"); // Crea al jugador humano con un nombre genérico
        this.jugadores = new ArrayList<>(); // Crea la lista vacía de jugadores
        this.barajaComer = new Baraja(RUTA_BARAJA); // Carga la baraja principal desde archivo o por defecto
        this.barajaMesa = new Baraja(RUTA_BARAJA); // Crea otra baraja que se usará como pila de descarte
        this.barajaMesa.vaciar(); // Vacía la baraja de la mesa porque se reutiliza como contenedor
        this.historial = new ArrayList<>(); // Prepara la lista del historial de jugadas
        this.ultimaCartaPorJugador = new HashMap<>(); // Inicializa el mapa de cartas jugadas por jugador
        this.sumaMesa = 0; // Inicializa la suma de la mesa en cero
        this.turnoActual = 0; // Fija el turno actual en el primer jugador
        this.ultimaCartaMesa = null; // Indica que todavía no se ha jugado ninguna carta
    } // Cierra el constructor de Partida

    public void iniciar(int cantidadCPUs) { // Método que reinicia la partida con una cantidad específica de rivales automáticos
        jugadores.clear(); // Limpia cualquier jugador de partidas anteriores
        historial.clear(); // Borra el historial para empezar desde cero
        ultimaCartaPorJugador.clear(); // Resetea el registro de cartas jugadas
        barajaComer.cargarDesdeArchivo(RUTA_BARAJA); // Recarga las cartas desde el archivo configurado
        barajaComer.barajar(); // Baraja las cartas para obtener un orden aleatorio
        barajaMesa.vaciar(); // Deja vacía la pila de descarte para la nueva partida
        jugadorHumano.eliminado = false; // Marca al humano como activo por si se había eliminado antes
        jugadores.add(jugadorHumano); // Agrega al humano como primer participante en la lista
        for (int i = 1; i <= cantidadCPUs; i++) { // Repite según la cantidad de CPUs solicitadas
            CPU cpu = new CPU("CPU " + i); // Crea una nueva CPU numerada para identificarla en la interfaz
            jugadores.add(cpu); // Agrega la CPU recién creada a la lista de jugadores
        } // Cierra el ciclo de creación de CPU
        for (Jugador jugador : jugadores) { // Reparte cartas iniciales a cada jugador activo
            jugador.eliminado = false; // Garantiza que cada jugador esté marcado como activo
            jugador.obtenerCartasIniciales(barajaComer); // Entrega cuatro cartas a cada jugador desde la baraja principal
            ultimaCartaPorJugador.put(jugador, null); // Inicializa el registro de última carta para cada participante
        } // Cierra el ciclo de reparto inicial
        Carta cartaInicial = barajaComer.robar(); // Obtiene la carta que quedará boca arriba al iniciar la mesa
        if (cartaInicial == null) { // Comprueba que realmente se haya obtenido una carta
            throw new IllegalStateException("La baraja no tiene cartas para iniciar la mesa"); // Lanza un error si la baraja estaba vacía, lo cual no debería ocurrir
        } // Cierra la comprobación de carta inicial nula
        barajaMesa.poner(cartaInicial); // Coloca la carta inicial en la pila de descarte
        ultimaCartaMesa = cartaInicial; // Actualiza la referencia de la última carta jugada
        sumaMesa = cartaInicial.valorParaSuma(0); // Calcula la suma inicial tomando la carta como primer valor
        historial.add("Comienza la partida con " + cartaInicial + ". Suma: " + sumaMesa + "."); // Registra el inicio en el historial
        turnoActual = 0; // Asegura que el turno empiece en el jugador humano
    } // Cierra el método iniciar

    public JugadorHumano getJugadorHumano() { // Método que expone al jugador humano para la interfaz
        return jugadorHumano; // Devuelve la referencia al jugador humano
    } // Cierra el método getJugadorHumano

    public List<Jugador> getJugadores() { // Método que permite consultar la lista de jugadores
        return Collections.unmodifiableList(jugadores); // Devuelve una vista inmutable para evitar modificaciones externas
    } // Cierra el método getJugadores

    public int getSumaMesa() { // Método que informa el puntaje acumulado en la mesa
        return sumaMesa; // Devuelve el valor actual de la suma de la mesa
    } // Cierra el método getSumaMesa

    public Carta getUltimaCartaMesa() { // Método que devuelve la carta jugada más recientemente
        return ultimaCartaMesa; // Retorna la referencia a la carta que está a la vista
    } // Cierra el método getUltimaCartaMesa

    public List<String> getHistorial() { // Método que expone el historial de jugadas
        return Collections.unmodifiableList(historial); // Entrega una vista inmutable para preservarlo
    } // Cierra el método getHistorial

    public Carta obtenerUltimaCartaDe(Jugador jugador) { // Método que informa cuál fue la última carta jugada por un participante
        return ultimaCartaPorJugador.get(jugador); // Devuelve la carta asociada en el mapa o null si no ha jugado
    } // Cierra el método obtenerUltimaCartaDe

    public boolean humanoTieneJugada() { // Método que evalúa si el humano conserva alguna jugada válida
        return !jugadorHumano.cartasJugables(sumaMesa).isEmpty(); // Devuelve verdadero si al menos una carta puede jugarse sin superar 50
    } // Cierra el método humanoTieneJugada

    public void eliminarHumanoPorFaltaDeJugadas() { // Método que elimina al humano cuando no puede jugar más
        if (!jugadorHumano.estaEliminado()) { // Comprueba que aún siga activo
            jugadorHumano.eliminar(); // Marca al jugador humano como eliminado
            historial.add(jugadorHumano.getNombre() + " no tiene jugadas y queda eliminado."); // Registra el motivo en el historial
        } // Cierra la comprobación de estado activo
    } // Cierra el método eliminarHumanoPorFaltaDeJugadas

    public void jugarTurnoHumano(Carta cartaElegida) { // Método que procesa la carta elegida por la persona
        if (estaTerminada()) { // Verifica si la partida ya terminó
            return; // Si está terminada, no se hace nada
        } // Cierra la comprobación de partida terminada
        if (cartaElegida == null) { // Comprueba que se haya recibido una carta válida
            throw new IllegalArgumentException("Debe seleccionarse una carta válida"); // Lanza un error para informar al controlador
        } // Cierra la comprobación de carta nula
        Carta cartaJugada = jugadorHumano.jugarCartaElegida(cartaElegida, sumaMesa); // Intenta jugar la carta en nombre del humano
        if (cartaJugada == null) { // Si el método devuelve null significa que no se pudo jugar
            throw new IllegalArgumentException("La carta seleccionada no es válida para la suma actual"); // Informa que el movimiento es inválido
        } // Cierra la comprobación de carta inválida
        int valor = cartaJugada.valorParaSuma(sumaMesa); // Calcula cuánto aporta la carta jugada al total
        sumaMesa += valor; // Actualiza el acumulado con el valor recién jugado
        barajaMesa.poner(cartaJugada); // Coloca la carta en la pila de descarte
        ultimaCartaMesa = cartaJugada; // Actualiza la referencia a la última carta visible
        ultimaCartaPorJugador.put(jugadorHumano, cartaJugada); // Registra la carta como la última jugada por el humano
        historial.add(jugadorHumano.getNombre() + " juega " + cartaJugada + ". Suma: " + sumaMesa + "."); // Registra la jugada en el historial
        reciclarSiHaceFalta(); // Comprueba si se necesita reciclar la mesa hacia la baraja
        jugadorHumano.robarSiHaceFalta(barajaComer); // Permite que el humano reponga su mano si hay cartas
        turnoActual = 1; // Avanza el turno para que comiencen a jugar las CPU
    } // Cierra el método jugarTurnoHumano

    public void jugarTurnoCPU() { // Método que ejecuta la secuencia de turnos de todos los rivales automáticos
        for (int i = 1; i < jugadores.size(); i++) { // Recorre la lista de jugadores comenzando por la primera CPU
            Jugador jugador = jugadores.get(i); // Obtiene la CPU actual
            turnoActual = i; // Actualiza el turno actual con el índice correspondiente
            if (jugador.estaEliminado()) { // Revisa si la CPU ya está fuera del juego
                continue; // Si está eliminada se pasa directamente al siguiente participante
            } // Cierra la comprobación de eliminación
            if (estaTerminada()) { // Si la partida ya terminó, no se siguen procesando turnos
                break; // Abandona el ciclo porque ya hay un ganador
            } // Cierra la comprobación de finalización
            Carta carta = jugador.jugarCarta(sumaMesa); // Solicita a la CPU que elija una carta válida si la tiene
            if (carta == null) { // Si no devuelve carta significa que no puede jugar
                jugador.eliminar(); // Marca a la CPU como eliminada
                historial.add(jugador.getNombre() + " no puede jugar y queda eliminado."); // Registra la eliminación en el historial
                continue; // Continúa con la siguiente CPU
            } // Cierra la comprobación de jugada nula
            int valor = carta.valorParaSuma(sumaMesa); // Calcula el valor de la carta jugada
            sumaMesa += valor; // Actualiza la suma de la mesa con la jugada de la CPU
            barajaMesa.poner(carta); // Envía la carta a la pila de descarte
            ultimaCartaMesa = carta; // Actualiza cuál es la carta visible
            ultimaCartaPorJugador.put(jugador, carta); // Actualiza la carta más reciente asociada a la CPU
            historial.add(jugador.getNombre() + " juega " + carta + ". Suma: " + sumaMesa + "."); // Registra la jugada en el historial
            reciclarSiHaceFalta(); // Verifica si se necesita reciclar la mesa
            jugador.robarSiHaceFalta(barajaComer); // Permite que la CPU reponga su mano con nuevas cartas
            if (estaTerminada()) { // Comprueba si después de la jugada la partida terminó
                break; // Sale del ciclo si ya hay un resultado definitivo
            } // Cierra la comprobación de fin de partida
        } // Cierra el recorrido de las CPU
        turnoActual = 0; // Devuelve el turno al jugador humano para la siguiente ronda
    } // Cierra el método jugarTurnoCPU

    public void reciclarSiHaceFalta() { // Método que mueve cartas de la mesa a la baraja cuando esta se agota
        if (!barajaComer.estaVacia()) { // Si aún quedan cartas para robar no es necesario reciclar
            return; // Sale del método sin hacer nada
        } // Cierra la comprobación de baraja con cartas
        if (barajaMesa.tamano() <= 1) { // Verifica que haya al menos dos cartas en la mesa para poder reciclar
            return; // Si hay cero o una carta no se puede reciclar nada
        } // Cierra la comprobación de cantidad suficiente
        Carta ultima = barajaMesa.sacarUltimaCarta(); // Guarda temporalmente la carta visible para conservarla en la mesa
        List<Carta> recicladas = new ArrayList<>(); // Crea una lista para almacenar las cartas que regresarán a la baraja
        Carta carta; // Variable temporal para iterar sobre las cartas del descarte
        while ((carta = barajaMesa.robar()) != null) { // Extrae cada carta de la mesa hasta vaciarla
            recicladas.add(carta); // Agrega la carta retirada a la lista temporal
        } // Cierra el ciclo de extracción de cartas de la mesa
        for (Carta reciclada : recicladas) { // Recorre la lista de cartas para devolverlas a la baraja principal
            barajaComer.poner(reciclada); // Coloca cada carta en la baraja principal
        } // Cierra el ciclo de reingreso a la baraja
        barajaComer.barajar(); // Baraja nuevamente para mezclar las cartas recicladas
        barajaMesa.vaciar(); // Vacía la pila de descarte para colocar la carta visible nuevamente
        barajaMesa.poner(ultima); // Devuelve la carta visible a la mesa
        historial.add("Se recicla la mesa. La carta visible es " + ultima + "."); // Registra el reciclaje en el historial
    } // Cierra el método reciclarSiHaceFalta

    public boolean estaTerminada() { // Método que indica si la partida llegó a su fin
        int jugadoresActivos = 0; // Contador de jugadores aún en competencia
        for (Jugador jugador : jugadores) { // Recorre todos los participantes
            if (!jugador.estaEliminado()) { // Verifica si el jugador sigue activo
                jugadoresActivos++; // Incrementa el conteo de jugadores activos
            } // Cierra la comprobación de estado activo
        } // Cierra el recorrido de jugadores
        if (jugadoresActivos <= 1) { // Si queda uno o ninguno, la partida termina
            return true; // Devuelve verdadero porque ya hay ganador definido
        } // Cierra la comprobación de jugadores activos
        return jugadorHumano.estaEliminado(); // Si el humano salió de la partida también se considera finalizada
    } // Cierra el método estaTerminada

    public Jugador ganador() { // Método que devuelve al ganador una vez que la partida termina
        for (Jugador jugador : jugadores) { // Recorre todos los participantes
            if (!jugador.estaEliminado()) { // Busca al primero que siga activo
                return jugador; // Devuelve ese jugador como el ganador
            } // Cierra la comprobación de estado activo
        } // Cierra el recorrido completo
        return null; // Si todos están eliminados (caso teórico) devuelve null
    } // Cierra el método ganador

    public int getTurnoActual() { // Método que informa el índice del turno actual
        return turnoActual; // Devuelve el índice que se está usando en la ronda
    } // Cierra el método getTurnoActual
} // Cierra la clase Partida
