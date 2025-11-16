import com.example.el_cincuentenazo.modelo.Carta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Carta.
 * Verifica el comportamiento de los diferentes valores de cartas
 * y sus contribuciones a la suma de la mesa.
 */
@DisplayName("Tests para la clase Carta")
class CartaTest {

    private Carta cartaAs;
    private Carta carta5;
    private Carta carta9;
    private Carta cartaJ;
    private Carta cartaQ;
    private Carta cartaK;

    @BeforeEach
    void setUp() {
        cartaAs = new Carta("A", "corazones");
        carta5 = new Carta("5", "picas");
        carta9 = new Carta("9", "tréboles");
        cartaJ = new Carta("J", "diamantes");
        cartaQ = new Carta("Q", "corazones");
        cartaK = new Carta("K", "picas");
    }

    @Test
    @DisplayName("Una carta debe tener el palo correcto")
    void testGetPalo() {
        assertEquals("corazones", cartaAs.getPalo());
        assertEquals("picas", carta5.getPalo());
        assertEquals("tréboles", carta9.getPalo());
    }

    @Test
    @DisplayName("Una carta debe tener el valor correcto")
    void testGetValor() {
        assertEquals("A", cartaAs.getValor());
        assertEquals("5", carta5.getValor());
        assertEquals("9", carta9.getValor());
    }

    @Test
    @DisplayName("Las cartas numéricas deben sumar su valor")
    void testCartasNumericasSumanSuValor() {
        assertEquals(5, carta5.valorParaSuma(10));
        assertEquals(5, carta5.valorParaSuma(45));

        Carta carta2 = new Carta("2", "picas");
        assertEquals(2, carta2.valorParaSuma(20));

        Carta carta10 = new Carta("10", "diamantes");
        assertEquals(10, carta10.valorParaSuma(15));
    }

    @Test
    @DisplayName("La carta 9 debe sumar cero")
    void testCarta9SumaCero() {
        assertEquals(0, carta9.valorParaSuma(0));
        assertEquals(0, carta9.valorParaSuma(25));
        assertEquals(0, carta9.valorParaSuma(49));
    }

    @Test
    @DisplayName("Las cartas J, Q, K deben restar 10")
    void testCartasFiguraRestanDiez() {
        assertEquals(-10, cartaJ.valorParaSuma(30));
        assertEquals(-10, cartaQ.valorParaSuma(45));
        assertEquals(-10, cartaK.valorParaSuma(20));
    }

    @Test
    @DisplayName("El As debe sumar 10 cuando no exceda 50")
    void testAsSumaDiezSiNoPasa50() {
        assertEquals(10, cartaAs.valorParaSuma(0));
        assertEquals(10, cartaAs.valorParaSuma(30));
        assertEquals(10, cartaAs.valorParaSuma(40));
    }

    @Test
    @DisplayName("El As debe sumar 1 cuando 10 excedería 50")
    void testAsSumaUnoSiPasaria50() {
        assertEquals(1, cartaAs.valorParaSuma(41));
        assertEquals(1, cartaAs.valorParaSuma(45));
        assertEquals(1, cartaAs.valorParaSuma(49));
    }

    @Test
    @DisplayName("El As en el límite exacto (40) debe sumar 10")
    void testAsEnLimiteExacto() {
        assertEquals(10, cartaAs.valorParaSuma(40));
    }

    @Test
    @DisplayName("toString debe mostrar el formato correcto")
    void testToString() {
        assertEquals("A de corazones", cartaAs.toString());
        assertEquals("5 de picas", carta5.toString());
        assertEquals("J de diamantes", cartaJ.toString());
    }

    @Test
    @DisplayName("Las cartas con diferentes palos pero mismo valor deben comportarse igual")
    void testMismoValorDiferentePalo() {
        Carta as1 = new Carta("A", "corazones");
        Carta as2 = new Carta("A", "picas");

        assertEquals(as1.valorParaSuma(30), as2.valorParaSuma(30));
        assertEquals(as1.valorParaSuma(45), as2.valorParaSuma(45));
    }
}