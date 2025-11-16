


import com.example.el_cincuentenazo.modelo.Baraja;
import com.example.el_cincuentenazo.modelo.Carta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para la clase Baraja")
class BarajaTest {

    private Baraja baraja;

    @BeforeEach
    void setUp() {
        baraja = new Baraja(null); // Carga baraja por defecto
    }

    @Test
    @DisplayName("Baraja por defecto debe contener 52 cartas")
    void testBarajaPorDefectoTiene52Cartas() {
        assertEquals(52, baraja.tamano());
    }

    @Test
    @DisplayName("Baraja debe tener 4 palos con 13 cartas cada uno")
    void testBarajaTieneCuatroPalos() {
        baraja.cargarPorDefecto();

        int contadorTreboles = 0;
        int contadorPicas = 0;
        int contadorCorazones = 0;
        int contadorDiamantes = 0;

        while (!baraja.estaVacia()) {
            Carta carta = baraja.robar();
            switch (carta.getPalo()) {
                case "tréboles": contadorTreboles++; break;
                case "picas": contadorPicas++; break;
                case "corazones": contadorCorazones++; break;
                case "diamantes": contadorDiamantes++; break;
            }
        }

        assertEquals(13, contadorTreboles);
        assertEquals(13, contadorPicas);
        assertEquals(13, contadorCorazones);
        assertEquals(13, contadorDiamantes);
    }

    @Test
    @DisplayName("Robar debe extraer la primera carta y reducir el tamaño")
    void testRobar() {
        int tamanoInicial = baraja.tamano();
        Carta carta = baraja.robar();

        assertNotNull(carta);
        assertEquals(tamanoInicial - 1, baraja.tamano());
    }

    @Test
    @DisplayName("Robar de baraja vacía debe retornar null")
    void testRobarDeBarajaVacia() {
        baraja.vaciar();

        assertNull(baraja.robar());
        assertTrue(baraja.estaVacia());
    }

    @Test
    @DisplayName("Poner debe agregar carta al final de la baraja")
    void testPoner() {
        Carta nuevaCarta = new Carta("A", "corazones");
        int tamanoInicial = baraja.tamano();

        baraja.poner(nuevaCarta);

        assertEquals(tamanoInicial + 1, baraja.tamano());
    }

    @Test
    @DisplayName("Poner carta null debe lanzar NullPointerException")
    void testPonerCartaNull() {
        assertThrows(NullPointerException.class, () -> {
            baraja.poner(null);
        });
    }

    @Test
    @DisplayName("Barajar debe cambiar el orden de las cartas")
    void testBarajar() {
        // Guardar el orden inicial
        Baraja baraja1 = new Baraja(null);
        Baraja baraja2 = new Baraja(null);

        baraja1.barajar();

        // Verificar que al menos algunas cartas están en diferente orden
        boolean algunaDiferencia = false;
        for (int i = 0; i < 10; i++) {
            Carta c1 = baraja1.robar();
            Carta c2 = baraja2.robar();
            if (!c1.toString().equals(c2.toString())) {
                algunaDiferencia = true;
                break;
            }
        }

        assertTrue(algunaDiferencia, "El barajado debería cambiar el orden");
    }

    @Test
    @DisplayName("VerUltimaCarta no debe modificar la baraja")
    void testVerUltimaCarta() {
        int tamanoInicial = baraja.tamano();
        Carta ultima = baraja.verUltimaCarta();

        assertNotNull(ultima);
        assertEquals(tamanoInicial, baraja.tamano());
    }

    @Test
    @DisplayName("SacarUltimaCarta debe extraer la última carta")
    void testSacarUltimaCarta() {
        int tamanoInicial = baraja.tamano();
        Carta ultima = baraja.sacarUltimaCarta();

        assertNotNull(ultima);
        assertEquals(tamanoInicial - 1, baraja.tamano());
    }

    @Test
    @DisplayName("Vaciar debe eliminar todas las cartas")
    void testVaciar() {
        baraja.vaciar();

        assertTrue(baraja.estaVacia());
        assertEquals(0, baraja.tamano());
    }

    @Test
    @DisplayName("Cargar desde archivo inválido debe usar baraja por defecto")
    void testCargarArchivoInvalidoUsaDefecto() {
        Baraja barajaTest = new Baraja("archivo_inexistente.txt");

        assertEquals(52, barajaTest.tamano());
    }
}