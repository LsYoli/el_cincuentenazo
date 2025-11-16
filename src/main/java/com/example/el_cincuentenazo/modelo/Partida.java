package com.example.el_cincuentenazo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordina el flujo completo de una partida del cincuentenazo, incluyendo la
 * administración de barajas, jugadores, turnos e historial de jugadas.
 */
public class Partida {

    /** Ruta del archivo que contiene la definición textual de las cartas. */
    public static final String RUTA_BARAJA = "cartas.txt";

    private final JugadorHumano jugadorHumano;
    private final List<Jugador> jugadores;
    private final Baraja barajaComer;
    private final Baraja barajaMesa;
    private final List<String> historial;
    private final Map<Jugador, Carta> ultimaCartaPorJugador;

    private int sumaMesa;
    private int turnoActual;
    private Carta ultimaCartaMesa;

    /**
     * Construye una partida dejando listas las estructuras necesarias.
     */
    public Partida() {
        this.jugadorHumano = new JugadorHumano("Jugador");
        this.jugadores = new ArrayList<>();
        this.barajaComer = new Baraja(RUTA_BARAJA);
        this.barajaMesa = new Baraja(RUTA_BARAJA);
        this.barajaMesa.vaciar();
        this.historial = new ArrayList<>();
        this.ultimaCartaPorJugador = new HashMap<>();
        this.sumaMesa = 0;
        this.turnoActual = 0;
        this.ultimaCartaMesa = null;
    }

    /**
     * Reinicia la partida con la cantidad de CPUs solicitada.
     *
     * @param cantidadCPUs número de rivales automáticos (1 a 3)
     */
    public void iniciar(int cantidadCPUs) {
        if (cantidadCPUs < 1 || cantidadCPUs > 3) {
            throw new ConfiguracionInvalidaException("La cantidad de CPUs debe estar entre 1 y 3");
        }
        jugadores.clear();
        historial.clear();
        ultimaCartaPorJugador.clear();
        barajaComer.cargarDesdeArchivo(RUTA_BARAJA);
        barajaComer.barajar();
        barajaMesa.vaciar();
        jugadorHumano.eliminado = false;
        jugadores.add(jugadorHumano);
        for (int i = 1; i <= cantidadCPUs; i++) {
            jugadores.add(new CPU("CPU " + i));
        }
        for (Jugador jugador : jugadores) {
            jugador.eliminado = false;
            jugador.obtenerCartasIniciales(barajaComer);
            ultimaCartaPorJugador.put(jugador, null);
        }
        Carta cartaInicial = barajaComer.robar();
        if (cartaInicial == null) {
            throw new IllegalStateException("La baraja no tiene cartas para iniciar la mesa");
        }
        barajaMesa.poner(cartaInicial);
        ultimaCartaMesa = cartaInicial;
        sumaMesa = cartaInicial.valorParaSuma(0);
        historial.add("Comienza la partida con " + cartaInicial + ". Suma: " + sumaMesa + ".");
        turnoActual = 0;
    }

    /**
     * Devuelve al jugador humano principal.
     *
     * @return referencia al jugador controlado por la persona
     */
    public JugadorHumano getJugadorHumano() {
        return jugadorHumano;
    }

    /**
     * Obtiene la lista completa de jugadores en orden de turno.
     *
     * @return vista inmutable de los jugadores
     */
    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /**
     * Expone la suma actual de la mesa.
     *
     * @return valor acumulado
     */
    public int getSumaMesa() {
        return sumaMesa;
    }

    /**
     * Devuelve la carta que se encuentra visible sobre la mesa.
     *
     * @return última carta jugada
     */
    public Carta getUltimaCartaMesa() {
        return ultimaCartaMesa;
    }

    /**
     * Proporciona una vista del historial de eventos.
     *
     * @return lista inmutable con las entradas registradas
     */
    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    /**
     * Obtiene la última carta jugada por un participante.
     *
     * @param jugador jugador consultado
     * @return carta jugada o {@code null} si aún no registró jugadas
     */
    public Carta obtenerUltimaCartaDe(Jugador jugador) {
        return ultimaCartaPorJugador.get(jugador);
    }

    /**
     * Indica si el jugador humano conserva jugadas disponibles.
     *
     * @return {@code true} si al menos una carta es segura
     */
    public boolean humanoTieneJugada() {
        return !jugadorHumano.cartasJugables(sumaMesa).isEmpty();
    }

    /**
     * Marca al humano como eliminado cuando no puede jugar más cartas.
     */
    public void eliminarHumanoPorFaltaDeJugadas() {
        if (!jugadorHumano.estaEliminado()) {
            jugadorHumano.eliminar();
            historial.add(jugadorHumano.getNombre() + " no tiene jugadas y queda eliminado.");
        }
    }

    /**
     * Procesa la jugada del humano, validando la carta seleccionada.
     *
     * @param cartaElegida carta tomada desde la interfaz
     * @throws JugadaInvalidaException si la jugada no es posible o la partida ya
     *                                 terminó
     */
    public void jugarTurnoHumano(Carta cartaElegida) throws JugadaInvalidaException {
        if (estaTerminada()) {
            throw new JugadaInvalidaException("No se puede jugar, la partida ya terminó");
        }
        if (cartaElegida == null) {
            throw new JugadaInvalidaException("Debe seleccionarse una carta válida");
        }
        Carta cartaJugada = jugadorHumano.jugarCartaElegida(cartaElegida, sumaMesa);
        if (cartaJugada == null) {
            throw new JugadaInvalidaException("La carta seleccionada haría superar 50 puntos");
        }
        int valor = cartaJugada.valorParaSuma(sumaMesa);
        sumaMesa += valor;
        barajaMesa.poner(cartaJugada);
        ultimaCartaMesa = cartaJugada;
        ultimaCartaPorJugador.put(jugadorHumano, cartaJugada);
        historial.add(jugadorHumano.getNombre() + " juega " + cartaJugada + ". Suma: " + sumaMesa + ".");
        reciclarSiHaceFalta();
        jugadorHumano.robarSiHaceFalta(barajaComer);
        turnoActual = 1;
    }

    /**
     * Ejecuta los turnos consecutivos de todas las CPU activas.
     */
    public void jugarTurnoCPU() {
        for (int i = 1; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            turnoActual = i;
            if (jugador.estaEliminado()) {
                continue;
            }
            if (estaTerminada()) {
                break;
            }
            Carta carta = jugador.jugarCarta(sumaMesa);
            if (carta == null) {
                jugador.eliminar();
                historial.add(jugador.getNombre() + " no puede jugar y queda eliminado.");
                continue;
            }
            int valor = carta.valorParaSuma(sumaMesa);
            sumaMesa += valor;
            barajaMesa.poner(carta);
            ultimaCartaMesa = carta;
            ultimaCartaPorJugador.put(jugador, carta);
            historial.add(jugador.getNombre() + " juega " + carta + ". Suma: " + sumaMesa + ".");
            reciclarSiHaceFalta();
            jugador.robarSiHaceFalta(barajaComer);
            if (estaTerminada()) {
                break;
            }
        }
        turnoActual = 0;
    }

    /**
     * Transfiere cartas de la mesa a la baraja cuando esta última se queda sin
     * cartas para robar.
     */
    public void reciclarSiHaceFalta() {
        if (!barajaComer.estaVacia()) {
            return;
        }
        if (barajaMesa.tamano() <= 1) {
            return;
        }
        Carta ultima = barajaMesa.sacarUltimaCarta();
        List<Carta> recicladas = new ArrayList<>();
        Carta carta;
        while ((carta = barajaMesa.robar()) != null) {
            recicladas.add(carta);
        }
        for (Carta reciclada : recicladas) {
            barajaComer.poner(reciclada);
        }
        barajaComer.barajar();
        barajaMesa.vaciar();
        barajaMesa.poner(ultima);
        historial.add("Se recicla la mesa. La carta visible es " + ultima + ".");
    }

    /**
     * Indica si la partida ya terminó.
     *
     * @return {@code true} cuando solo queda un jugador o el humano fue
     *         eliminado
     */
    public boolean estaTerminada() {
        int jugadoresActivos = 0;
        for (Jugador jugador : jugadores) {
            if (!jugador.estaEliminado()) {
                jugadoresActivos++;
            }
        }
        if (jugadoresActivos <= 1) {
            return true;
        }
        return jugadorHumano.estaEliminado();
    }

    /**
     * Devuelve al ganador una vez concluida la partida.
     *
     * @return jugador aún activo o {@code null} si ninguno quedó en pie
     */
    public Jugador ganador() {
        for (Jugador jugador : jugadores) {
            if (!jugador.estaEliminado()) {
                return jugador;
            }
        }
        return null;
    }

    /**
     * Indica el índice del turno actualmente en ejecución.
     *
     * @return índice dentro de la lista de jugadores
     */
    public int getTurnoActual() {
        return turnoActual;
    }
}
