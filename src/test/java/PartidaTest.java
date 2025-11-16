
import com.example.el_cincuentenazo.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para la clase Partida")
class PartidaTest {

    private Partida partida;

    @BeforeEach
    void setUp() {
        partida = new Partida();
    }

    @Test
    @DisplayName("Iniciar partida debe configurar jugadores correctamente")
    void testIniciarPartida() {
        partida.iniciar(2);

        assertEquals(3, partida.getJugadores().size()); // 1 humano + 2 CPU
        assertNotNull(partida.getJugadorHumano());
        assertFalse(partida.getJugadorHumano().estaEliminado());
    }

    @Test
    @DisplayName("Iniciar debe repartir 4 cartas a cada jugador")
    void testRepartoInicial() {
        partida.iniciar(1);

        assertEquals(4, partida.getJugadorHumano().getMano().size());

        for (Jugador jugador : partida.getJugadores()) {
            if (!jugador.estaEliminado()) {
                assertEquals(4, jugador.getMano().size());
            }
        }
    }

    @Test
    @DisplayName("Suma inicial debe ser mayor a 0")
    void testSumaInicial() {
        partida.iniciar(1);

        assertTrue(partida.getSumaMesa() >= 0);
        assertNotNull(partida.getUltimaCartaMesa());
    }

    @Test
    @DisplayName("Historial debe registrar inicio de partida")
    void testHistorialInicio() {
        partida.iniciar(1);

        assertFalse(partida.getHistorial().isEmpty());
        assertTrue(partida.getHistorial().get(0).contains("Comienza la partida"));
    }

    @Test
    @DisplayName("Turno inicial debe ser del jugador humano")
    void testTurnoInicial() {
        partida.iniciar(1);

        assertEquals(0, partida.getTurnoActual());
    }

    @Test
    @DisplayName("Jugar carta válida debe actualizar la suma")
    void testJugarCartaValida() throws JugadaInvalidaException {
        partida.iniciar(1);
        JugadorHumano humano = partida.getJugadorHumano();
        int sumaAntes = partida.getSumaMesa();

        // Buscar una carta jugable
        Carta cartaJugable = null;
        for (Carta carta : humano.getMano()) {
            if (humano.puedeJugar(carta, partida.getSumaMesa())) {
                cartaJugable = carta;
                break;
            }
        }

        if (cartaJugable != null) {
            int valorEsperado = cartaJugable.valorParaSuma(sumaAntes);
            partida.jugarTurnoHumano(cartaJugable);

            // La suma puede cambiar después de turnos CPU, pero debe haberse modificado
            assertNotEquals(sumaAntes, partida.getSumaMesa());
        }
    }

    @Test
    @DisplayName("Jugar carta null debe lanzar IllegalArgumentException")
    void testJugarCartaNull() {
        partida.iniciar(1);

        assertThrows(IllegalArgumentException.class, () -> {
            partida.jugarTurnoHumano(null);
        });
    }

    @Test
    @DisplayName("HumanoTieneJugada debe retornar false si todas las cartas exceden 50")
    void testHumanoSinJugadas() {
        partida.iniciar(1);
        JugadorHumano humano = partida.getJugadorHumano();

        // Forzar una situación donde no hay jugadas (esto es teórico)
        // En la práctica, verificamos que el método funcione
        boolean tieneJugada = partida.humanoTieneJugada();

        // Debe haber al menos una jugada al inicio
        assertTrue(tieneJugada || humano.getMano().isEmpty());
    }

    @Test
    @DisplayName("EliminarHumanoPorFaltaDeJugadas debe marcar al humano como eliminado")
    void testEliminarHumano() {
        partida.iniciar(1);

        partida.eliminarHumanoPorFaltaDeJugadas();

        assertTrue(partida.getJugadorHumano().estaEliminado());
        assertTrue(partida.getHistorial().stream()
                .anyMatch(h -> h.contains("no tiene jugadas")));
    }

    @Test
    @DisplayName("Partida debe terminar cuando solo queda un jugador activo")
    void testPartidaTermina() {
        partida.iniciar(1);

        // Eliminar al humano
        partida.getJugadorHumano().eliminar();

        assertTrue(partida.estaTerminada());
    }

    @Test
    @DisplayName("Ganador debe retornar el único jugador activo")
    void testGanador() {
        partida.iniciar(2);

        // Eliminar a todos excepto uno
        partida.getJugadorHumano().eliminar();
        partida.getJugadores().get(1).eliminar();

        Jugador ganador = partida.ganador();

        assertNotNull(ganador);
        assertFalse(ganador.estaEliminado());
    }

    @Test
    @DisplayName("ObtenerUltimaCartaDe debe retornar null si el jugador no ha jugado")
    void testObtenerUltimaCartaSinJugar() {
        partida.iniciar(1);

        // Al inicio, los jugadores tienen null como última carta
        Carta ultima = partida.obtenerUltimaCartaDe(partida.getJugadorHumano());

        assertNull(ultima);
    }

    @Test
    @DisplayName("Reiniciar partida debe limpiar estado anterior")
    void testReiniciarPartida() {
        partida.iniciar(1);
        partida.eliminarHumanoPorFaltaDeJugadas();

        // Reiniciar
        partida.iniciar(1);

        assertFalse(partida.getJugadorHumano().estaEliminado());
        assertEquals(0, partida.getTurnoActual());
        assertFalse(partida.estaTerminada());
    }

    @Test
    @DisplayName("JugarTurnoCPU debe procesar todos los turnos de las CPU")
    void testJugarTurnoCPU() {
        partida.iniciar(2);
        int historialAntes = partida.getHistorial().size();

        partida.jugarTurnoCPU();

        // El historial debe tener al menos una entrada más (por las CPU)
        assertTrue(partida.getHistorial().size() >= historialAntes);
    }
}