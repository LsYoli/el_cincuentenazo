

import com.example.el_cincuentenazo.modelo.Carta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para la clase Carta")
class CartaTest {

    @Test
    @DisplayName("Constructor debe crear carta con valor y palo correctos")
    void testConstructor() {
        Carta carta = new Carta("A", "corazones");

        assertEquals("A", carta.getValor());
        assertEquals("corazones", carta.getPalo());
    }

    @Test
    @DisplayName("As debe valer 10 cuando no excede 50")
    void testAsValeDiez() {
        Carta as = new Carta("A", "picas");

        assertEquals(10, as.valorParaSuma(30));
        assertEquals(10, as.valorParaSuma(40));
    }

    @Test
    @DisplayName("As debe valer 1 cuando 10 excedería 50")
    void testAsValeUno() {
        Carta as = new Carta("A", "tréboles");

        assertEquals(1, as.valorParaSuma(41));
        assertEquals(1, as.valorParaSuma(45));
        assertEquals(1, as.valorParaSuma(50));
    }

    @ParameterizedTest
    @CsvSource({
            "J, 20, -10",
            "Q, 35, -10",
            "K, 40, -10"
    })
    @DisplayName("Figuras (J, Q, K) deben restar 10 puntos")
    void testFigurasRestanDiez(String valor, int sumaActual, int esperado) {
        Carta figura = new Carta(valor, "diamantes");

        assertEquals(esperado, figura.valorParaSuma(sumaActual));
    }

    @Test
    @DisplayName("El 9 debe valer 0 puntos")
    void testNueveValeCero() {
        Carta nueve = new Carta("9", "corazones");

        assertEquals(0, nueve.valorParaSuma(25));
        assertEquals(0, nueve.valorParaSuma(50));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 10, 2",
            "3, 20, 3",
            "5, 15, 5",
            "7, 30, 7",
            "10, 25, 10"
    })
    @DisplayName("Cartas numéricas deben sumar su valor nominal")
    void testCartasNumericas(String valor, int sumaActual, int esperado) {
        Carta carta = new Carta(valor, "picas");

        assertEquals(esperado, carta.valorParaSuma(sumaActual));
    }

    @Test
    @DisplayName("toString debe retornar formato 'valor de palo'")
    void testToString() {
        Carta carta = new Carta("K", "diamantes");

        assertEquals("K de diamantes", carta.toString());
    }

    @Test
    @DisplayName("As en el límite exacto debe preferir valor 10")
    void testAsEnLimite() {
        Carta as = new Carta("A", "tréboles");

        assertEquals(10, as.valorParaSuma(40)); // 40 + 10 = 50 (permitido)
        assertEquals(1, as.valorParaSuma(41));  // 41 + 10 = 51 (excede)
    }
}