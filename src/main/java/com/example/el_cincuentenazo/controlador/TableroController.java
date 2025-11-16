package com.example.el_cincuentenazo.controlador;

import com.example.el_cincuentenazo.Main;
import com.example.el_cincuentenazo.hilos.AnimacionPensamientoThread;
import com.example.el_cincuentenazo.hilos.TurnoCPUThread;
import com.example.el_cincuentenazo.modelo.Carta;
import com.example.el_cincuentenazo.modelo.ConfiguracionInvalidaException;
import com.example.el_cincuentenazo.modelo.JugadaInvalidaException;
import com.example.el_cincuentenazo.modelo.Jugador;
import com.example.el_cincuentenazo.modelo.JugadorHumano;
import com.example.el_cincuentenazo.modelo.Partida;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controlador encargado de mantener sincronizada la vista del tablero con el
 * estado de la partida.
 */
public class TableroController {

    @FXML
    private Label lblMesaPuntaje;
    @FXML
    private Label lblMesaUltimaCarta;
    @FXML
    private Label lblTurnoActual;
    @FXML
    private Label lblCPU1Estado;
    @FXML
    private Label lblCPU1Ultima;
    @FXML
    private Label lblCPU2Estado;
    @FXML
    private Label lblCPU2Ultima;
    @FXML
    private Label lblCPU3Estado;
    @FXML
    private Label lblCPU3Ultima;
    @FXML
    private Button btnCarta1;
    @FXML
    private Button btnCarta2;
    @FXML
    private Button btnCarta3;
    @FXML
    private Button btnCarta4;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextArea txtHistorial;
    @FXML
    private Label lblEstadoCPU;

    private Partida partida;
    private int cantidadCPUConfiguradas;
    private boolean ganadorAnunciado;

    private final List<Button> botonesCartas = new ArrayList<>();
    private final List<Label> etiquetasEstadoCPU = new ArrayList<>();
    private final List<Label> etiquetasUltimaCPU = new ArrayList<>();

    /**
     * Inicializa referencias a los controles declarados en el FXML.
     */
    @FXML
    public void initialize() {
        botonesCartas.add(btnCarta1);
        botonesCartas.add(btnCarta2);
        botonesCartas.add(btnCarta3);
        botonesCartas.add(btnCarta4);
        etiquetasEstadoCPU.add(lblCPU1Estado);
        etiquetasEstadoCPU.add(lblCPU2Estado);
        etiquetasEstadoCPU.add(lblCPU3Estado);
        etiquetasUltimaCPU.add(lblCPU1Ultima);
        etiquetasUltimaCPU.add(lblCPU2Ultima);
        etiquetasUltimaCPU.add(lblCPU3Ultima);
        txtHistorial.setEditable(false);
        txtHistorial.setWrapText(true);
    }

    /**
     * Recibe la partida configurada desde la clase principal.
     *
     * @param partida instancia lista para usarse en la vista
     */
    public void configurarPartida(Partida partida) {
        this.partida = partida;
        this.cantidadCPUConfiguradas = Math.max(0, partida.getJugadores().size() - 1);
        this.ganadorAnunciado = false;
    }

    /**
     * Refresca todos los elementos visuales para reflejar el estado actual.
     */
    public void renderizar() {
        if (partida == null) {
            return;
        }
        if (!partida.humanoTieneJugada() && !partida.getJugadorHumano().estaEliminado()) {
            partida.eliminarHumanoPorFaltaDeJugadas();
            while (!partida.estaTerminada()) {
                partida.jugarTurnoCPU();
            }
        }
        actualizarMesa();
        actualizarManoHumano();
        actualizarCPUs();
        actualizarHistorial();
        actualizarTurno();
        mostrarGanadorSiCorresponde();
    }

    /**
     * Muestra la suma y la última carta de la mesa.
     */
    private void actualizarMesa() {
        lblMesaPuntaje.setText(String.valueOf(partida.getSumaMesa()));
        Carta ultimaMesa = partida.getUltimaCartaMesa();
        if (ultimaMesa == null) {
            lblMesaUltimaCarta.setText("Sin cartas");
        } else {
            lblMesaUltimaCarta.setText(ultimaMesa.toString());
        }
    }

    /**
     * Actualiza los botones que representan la mano del jugador humano.
     */
    private void actualizarManoHumano() {
        JugadorHumano humano = partida.getJugadorHumano();
        List<Carta> mano = humano.getMano();
        for (int i = 0; i < botonesCartas.size(); i++) {
            Button boton = botonesCartas.get(i);
            if (i < mano.size()) {
                Carta carta = mano.get(i);
                boton.setText(carta.toString());
                boolean jugable = humano.puedeJugar(carta, partida.getSumaMesa());
                boton.setDisable(!jugable || partida.estaTerminada());
            } else {
                boton.setText("Sin carta");
                boton.setDisable(true);
            }
        }
    }

    /**
     * Refleja en pantalla el estado de cada CPU.
     */
    private void actualizarCPUs() {
        List<Jugador> jugadores = partida.getJugadores();
        for (int i = 0; i < etiquetasEstadoCPU.size(); i++) {
            int indiceJugador = i + 1;
            Label etiquetaEstado = etiquetasEstadoCPU.get(i);
            Label etiquetaUltima = etiquetasUltimaCPU.get(i);
            if (indiceJugador < jugadores.size()) {
                Jugador cpu = jugadores.get(indiceJugador);
                etiquetaEstado.setText(cpu.getNombre() + (cpu.estaEliminado() ? " - Eliminado" : " - Activo"));
                Carta ultima = partida.obtenerUltimaCartaDe(cpu);
                etiquetaUltima.setText(ultima == null ? "Sin jugadas" : ultima.toString());
            } else {
                etiquetaEstado.setText("CPU inactiva");
                etiquetaUltima.setText("-");
            }
        }
    }

    /**
     * Vuelca el historial textual en el área de texto.
     */
    private void actualizarHistorial() {
        StringBuilder constructor = new StringBuilder();
        for (String evento : partida.getHistorial()) {
            constructor.append(evento).append(System.lineSeparator());
        }
        txtHistorial.setText(constructor.toString());
    }

    /**
     * Indica visualmente a quién corresponde el turno actual.
     */
    private void actualizarTurno() {
        if (partida.estaTerminada()) {
            lblTurnoActual.setText("Partida finalizada");
            return;
        }
        if (partida.getTurnoActual() == 0) {
            lblTurnoActual.setText("Tu turno");
        } else {
            lblTurnoActual.setText("Turno de " + partida.getJugadores().get(partida.getTurnoActual()).getNombre());
        }
    }

    /**
     * Muestra un cuadro de diálogo con el ganador cuando la partida termina.
     */
    private void mostrarGanadorSiCorresponde() {
        if (!partida.estaTerminada()) {
            return;
        }
        if (ganadorAnunciado) {
            return;
        }
        ganadorAnunciado = true;
        Jugador ganador = partida.ganador();
        String mensaje = ganador == null ? "No hay ganador." : "Ganador: " + ganador.getNombre();
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Resultado");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void jugarCartaUno(ActionEvent evento) {
        jugarCartaPorIndice(0);
    }

    @FXML
    private void jugarCartaDos(ActionEvent evento) {
        jugarCartaPorIndice(1);
    }

    @FXML
    private void jugarCartaTres(ActionEvent evento) {
        jugarCartaPorIndice(2);
    }

    @FXML
    private void jugarCartaCuatro(ActionEvent evento) {
        jugarCartaPorIndice(3);
    }

    /**
     * Intenta jugar la carta del humano ubicada en el índice indicado.
     *
     * @param indice posición del botón presionado
     */
    private void jugarCartaPorIndice(int indice) {
        if (partida == null || partida.estaTerminada()) {
            return;
        }
        JugadorHumano humano = partida.getJugadorHumano();
        List<Carta> mano = humano.getMano();
        if (indice >= mano.size()) {
            return;
        }
        Carta cartaSeleccionada = mano.get(indice);
        if (!humano.puedeJugar(cartaSeleccionada, partida.getSumaMesa())) {
            mostrarAlerta("Esa carta haría superar 50 puntos. Elige otra.");
            return;
        }
        try {
            partida.jugarTurnoHumano(cartaSeleccionada);
            renderizar();
            ejecutarTurnosCPUConHilos();
        } catch (JugadaInvalidaException e) {
            mostrarAlerta(e.getMessage());
        } catch (ConfiguracionInvalidaException e) {
            mostrarAlerta("Error de configuración: " + e.getMessage());
        }
    }

    @FXML
    private void manejarHome(ActionEvent evento) {
        Main.obtenerInstancia().abrirVistaInicio();
    }

    @FXML
    private void manejarRefresh(ActionEvent evento) {
        if (partida == null) {
            return;
        }
        partida.iniciar(cantidadCPUConfiguradas);
        ganadorAnunciado = false;
        renderizar();
    }

    /**
     * Corre los turnos de las CPU en hilos separados para mostrar la animación de
     * pensamiento.
     */
    private void ejecutarTurnosCPUConHilos() {
        for (Button boton : botonesCartas) {
            boton.setDisable(true);
        }
        btnHome.setDisable(true);
        btnRefresh.setDisable(true);
        TurnoCPUThread turnoCPUThread = new TurnoCPUThread(partida);
        AnimacionPensamientoThread animacionThread = new AnimacionPensamientoThread(lblEstadoCPU, lblTurnoActual, turnoCPUThread);
        turnoCPUThread.start();
        animacionThread.start();
        new Thread(() -> {
            try {
                turnoCPUThread.join();
                animacionThread.join();
                Platform.runLater(() -> {
                    partida.jugarTurnoCPU();
                    renderizar();
                    for (Button boton : botonesCartas) {
                        boton.setDisable(false);
                    }
                    btnHome.setDisable(false);
                    btnRefresh.setDisable(false);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Muestra una alerta informativa con el mensaje recibido.
     *
     * @param mensaje texto a desplegar
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
