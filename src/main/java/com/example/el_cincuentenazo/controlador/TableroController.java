package com.example.el_cincuentenazo.controlador; // Ubica la clase dentro del paquete de controladores

import com.example.el_cincuentenazo.Main; // Importa la clase principal para cambiar de escena
import com.example.el_cincuentenazo.modelo.Carta; // Importa la clase Carta para trabajar con las manos
import com.example.el_cincuentenazo.modelo.Jugador; // Importa la clase base Jugador para recorrer la lista de participantes
import com.example.el_cincuentenazo.modelo.JugadorHumano; // Importa la clase específica del jugador humano
import com.example.el_cincuentenazo.modelo.Partida; // Importa la lógica principal del juego
import java.util.ArrayList; // Importa la lista dinámica para guardar referencias a controles
import java.util.List; // Importa la interfaz List utilizada en varios métodos
import javafx.event.ActionEvent; // Importa el tipo de eventos que disparan los botones
import javafx.fxml.FXML; // Importa la anotación FXML para la inyección de componentes
import javafx.scene.control.Alert; // Importa la clase Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // Importa la clase Button para manipular los botones de la interfaz
import javafx.scene.control.Label; // Importa la clase Label para actualizar textos
import javafx.scene.control.TextArea; // Importa TextArea para desplegar el historial

// Controlador que mantiene sincronizada la interfaz del tablero con la lógica del juego.
public class TableroController { // Declara la clase pública TableroController

    @FXML
    private Label lblMesaPuntaje; // Etiqueta que muestra la suma actual en la mesa

    @FXML
    private Label lblMesaUltimaCarta; // Etiqueta que muestra la última carta jugada

    @FXML
    private Label lblTurnoActual; // Etiqueta que indica de quién es el turno

    @FXML
    private Label lblCPU1Estado; // Etiqueta que indica el estado de la CPU 1

    @FXML
    private Label lblCPU1Ultima; // Etiqueta que muestra la última carta jugada por la CPU 1

    @FXML
    private Label lblCPU2Estado; // Etiqueta que indica el estado de la CPU 2

    @FXML
    private Label lblCPU2Ultima; // Etiqueta que muestra la última carta jugada por la CPU 2

    @FXML
    private Label lblCPU3Estado; // Etiqueta que indica el estado de la CPU 3

    @FXML
    private Label lblCPU3Ultima; // Etiqueta que muestra la última carta jugada por la CPU 3

    @FXML
    private Button btnCarta1; // Botón que representa la primera carta del jugador humano

    @FXML
    private Button btnCarta2; // Botón que representa la segunda carta del jugador humano

    @FXML
    private Button btnCarta3; // Botón que representa la tercera carta del jugador humano

    @FXML
    private Button btnCarta4; // Botón que representa la cuarta carta del jugador humano

    @FXML
    private Button btnHome; // Botón que regresa a la pantalla principal

    @FXML
    private Button btnRefresh; // Botón que reinicia la partida con la misma configuración

    @FXML
    private TextArea txtHistorial; // Área de texto que despliega el registro de jugadas

    private Partida partida; // Referencia a la lógica de juego recibida desde Main
    private int cantidadCPUConfiguradas; // Guarda cuántas CPU se configuraron para poder reiniciar
    private boolean ganadorAnunciado; // Bandera para evitar mostrar el ganador múltiples veces

    private final List<Button> botonesCartas = new ArrayList<>(); // Lista auxiliar con los botones de cartas para iterar fácilmente
    private final List<Label> etiquetasEstadoCPU = new ArrayList<>(); // Lista auxiliar con las etiquetas de estado de cada CPU
    private final List<Label> etiquetasUltimaCPU = new ArrayList<>(); // Lista auxiliar con las etiquetas de última carta de cada CPU

    @FXML
    public void initialize() { // Método que se ejecuta automáticamente al cargar el FXML del tablero
        botonesCartas.add(btnCarta1); // Agrega el botón de la primera carta a la lista auxiliar
        botonesCartas.add(btnCarta2); // Agrega el botón de la segunda carta a la lista auxiliar
        botonesCartas.add(btnCarta3); // Agrega el botón de la tercera carta a la lista auxiliar
        botonesCartas.add(btnCarta4); // Agrega el botón de la cuarta carta a la lista auxiliar
        etiquetasEstadoCPU.add(lblCPU1Estado); // Inserta la etiqueta de estado de la CPU 1 en la lista auxiliar
        etiquetasEstadoCPU.add(lblCPU2Estado); // Inserta la etiqueta de estado de la CPU 2 en la lista auxiliar
        etiquetasEstadoCPU.add(lblCPU3Estado); // Inserta la etiqueta de estado de la CPU 3 en la lista auxiliar
        etiquetasUltimaCPU.add(lblCPU1Ultima); // Inserta la etiqueta de última carta de la CPU 1 en la lista auxiliar
        etiquetasUltimaCPU.add(lblCPU2Ultima); // Inserta la etiqueta de última carta de la CPU 2 en la lista auxiliar
        etiquetasUltimaCPU.add(lblCPU3Ultima); // Inserta la etiqueta de última carta de la CPU 3 en la lista auxiliar
        txtHistorial.setEditable(false); // Evita que el usuario modifique manualmente el historial mostrado
        txtHistorial.setWrapText(true); // Activa el ajuste de texto para que el historial se vea completo
    } // Cierra el método initialize

    public void configurarPartida(Partida partida) { // Método invocado desde Main para entregar la lógica lista para usar
        this.partida = partida; // Guarda la referencia recibida para utilizarla en el controlador
        this.cantidadCPUConfiguradas = Math.max(0, partida.getJugadores().size() - 1); // Calcula cuántas CPU participan actualmente
        this.ganadorAnunciado = false; // Restablece la bandera de anuncios de ganador al preparar la vista
    } // Cierra el método configurarPartida

    public void renderizar() { // Método central que refresca todos los elementos visuales
        if (partida == null) { // Verifica que exista una partida configurada
            return; // Si no hay partida, no se actualiza la interfaz
        } // Cierra la comprobación de partida nula
        if (!partida.humanoTieneJugada() && !partida.getJugadorHumano().estaEliminado()) { // Revisa si el humano se quedó sin movimientos antes de actualizar
            partida.eliminarHumanoPorFaltaDeJugadas(); // Marca al humano como eliminado dejando constancia en el historial
            while (!partida.estaTerminada()) { // Mientras la partida siga en curso después de la eliminación
                partida.jugarTurnoCPU(); // Permite que las CPU sigan jugando automáticamente hasta que quede un ganador
            } // Cierra el ciclo de jugadas automáticas
        } // Cierra la comprobación de eliminación por falta de jugadas
        actualizarMesa(); // Actualiza la información relacionada con la mesa central
        actualizarManoHumano(); // Refresca los botones que representan la mano del jugador humano
        actualizarCPUs(); // Muestra el estado de cada CPU en la pantalla
        actualizarHistorial(); // Vuelca el historial de jugadas en el área de texto
        actualizarTurno(); // Indica de quién es el turno actual
        mostrarGanadorSiCorresponde(); // Comprueba si la partida terminó para mostrar un mensaje final
    } // Cierra el método renderizar

    private void actualizarMesa() { // Método auxiliar que refleja el estado de la mesa
        lblMesaPuntaje.setText(String.valueOf(partida.getSumaMesa())); // Muestra la suma acumulada en la mesa
        Carta ultimaMesa = partida.getUltimaCartaMesa(); // Obtiene la última carta jugada en la mesa
        if (ultimaMesa == null) { // Revisa si todavía no hay cartas visibles
            lblMesaUltimaCarta.setText("Sin cartas"); // Indica que no hay cartas en la mesa
        } else { // Si existe una carta jugada
            lblMesaUltimaCarta.setText(ultimaMesa.toString()); // Muestra la carta en formato legible
        } // Cierra la bifurcación de existencia de carta
    } // Cierra el método actualizarMesa

    private void actualizarManoHumano() { // Método que actualiza los botones de la mano del jugador humano
        JugadorHumano humano = partida.getJugadorHumano(); // Obtiene la referencia directa al jugador humano
        List<Carta> mano = humano.getMano(); // Recupera la lista de cartas actuales del humano
        for (int i = 0; i < botonesCartas.size(); i++) { // Recorre cada botón disponible
            Button boton = botonesCartas.get(i); // Obtiene el botón actual
            if (i < mano.size()) { // Verifica si existe una carta para ese índice
                Carta carta = mano.get(i); // Obtiene la carta correspondiente
                boton.setText(carta.toString()); // Coloca el nombre de la carta como texto del botón
                boolean jugable = humano.puedeJugar(carta, partida.getSumaMesa()); // Determina si la carta puede jugarse sin pasar de 50
                boton.setDisable(!jugable || partida.estaTerminada()); // Deshabilita el botón si la carta no es válida o la partida terminó
            } else { // Si no existe carta para ese botón
                boton.setText("Sin carta"); // Muestra un texto neutro para indicar la ausencia de carta
                boton.setDisable(true); // Deshabilita el botón para evitar interacciones
            } // Cierra la bifurcación por existencia de carta
        } // Cierra el recorrido de botones
    } // Cierra el método actualizarManoHumano

    private void actualizarCPUs() { // Método que refresca la información de los rivales automáticos
        List<Jugador> jugadores = partida.getJugadores(); // Obtiene la lista completa de jugadores
        for (int i = 0; i < etiquetasEstadoCPU.size(); i++) { // Recorre cada posición de CPU disponible
            int indiceJugador = i + 1; // Calcula el índice real en la lista de jugadores (saltando al humano)
            Label etiquetaEstado = etiquetasEstadoCPU.get(i); // Obtiene la etiqueta del estado correspondiente
            Label etiquetaUltima = etiquetasUltimaCPU.get(i); // Obtiene la etiqueta de la última carta correspondiente
            if (indiceJugador < jugadores.size()) { // Verifica si existe una CPU en esa posición
                Jugador cpu = jugadores.get(indiceJugador); // Recupera a la CPU correspondiente
                etiquetaEstado.setText(cpu.getNombre() + (cpu.estaEliminado() ? " - Eliminado" : " - Activo")); // Actualiza el estado textual según siga en juego
                Carta ultima = partida.obtenerUltimaCartaDe(cpu); // Busca la última carta jugada por esa CPU
                etiquetaUltima.setText(ultima == null ? "Sin jugadas" : ultima.toString()); // Muestra la carta o indica que aún no ha jugado
            } else { // Si no hay CPU configurada en esa posición
                etiquetaEstado.setText("CPU inactiva"); // Indica que la casilla no se usa
                etiquetaUltima.setText("-"); // Coloca un separador simple para la última carta
            } // Cierra la bifurcación por existencia de CPU
        } // Cierra el recorrido de etiquetas
    } // Cierra el método actualizarCPUs

    private void actualizarHistorial() { // Método que vuelca el historial en el área de texto
        StringBuilder constructor = new StringBuilder(); // Crea un acumulador de texto eficiente
        for (String evento : partida.getHistorial()) { // Recorre cada evento guardado
            constructor.append(evento).append(System.lineSeparator()); // Añade el evento seguido de un salto de línea
        } // Cierra el recorrido del historial
        txtHistorial.setText(constructor.toString()); // Coloca el texto completo en el área visible
    } // Cierra el método actualizarHistorial

    private void actualizarTurno() { // Método que informa al usuario de quién es el turno actual
        if (partida.estaTerminada()) { // Si la partida ya terminó
            lblTurnoActual.setText("Partida finalizada"); // Indica claramente que ya no hay turnos activos
            return; // Sale del método porque no hay más información que mostrar
        } // Cierra la comprobación de partida terminada
        if (partida.getTurnoActual() == 0) { // Si el índice del turno corresponde al jugador humano
            lblTurnoActual.setText("Tu turno"); // Muestra un mensaje amigable para el jugador humano
        } else { // En caso contrario el turno pertenece a una CPU
            lblTurnoActual.setText("Turno de " + partida.getJugadores().get(partida.getTurnoActual()).getNombre()); // Muestra cuál CPU está actuando
        } // Cierra la bifurcación por turno
    } // Cierra el método actualizarTurno

    private void mostrarGanadorSiCorresponde() { // Método que anuncia al ganador cuando la partida termina
        if (!partida.estaTerminada()) { // Verifica si la partida continúa en juego
            return; // Si aún no termina, no se hace nada
        } // Cierra la comprobación de partida en curso
        if (ganadorAnunciado) { // Revisa si ya se mostró el mensaje anteriormente
            return; // Evita mostrar la misma alerta varias veces
        } // Cierra la comprobación de anuncio previo
        ganadorAnunciado = true; // Marca que el ganador ya fue anunciado
        Jugador ganador = partida.ganador(); // Obtiene al jugador que ganó la partida
        String mensaje = ganador == null ? "No hay ganador." : "Ganador: " + ganador.getNombre(); // Construye el mensaje a mostrar
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // Crea una alerta informativa
        alerta.setTitle("Resultado"); // Asigna el título de la ventana emergente
        alerta.setHeaderText(null); // Oculta el encabezado para simplificar el mensaje
        alerta.setContentText(mensaje); // Coloca el mensaje con el nombre del ganador
        alerta.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    } // Cierra el método mostrarGanadorSiCorresponde

    @FXML
    private void jugarCartaUno(ActionEvent evento) { // Maneja el clic sobre el primer botón de carta
        jugarCartaPorIndice(0); // Intenta jugar la carta situada en el índice 0
    } // Cierra el método jugarCartaUno

    @FXML
    private void jugarCartaDos(ActionEvent evento) { // Maneja el clic sobre el segundo botón de carta
        jugarCartaPorIndice(1); // Intenta jugar la carta situada en el índice 1
    } // Cierra el método jugarCartaDos

    @FXML
    private void jugarCartaTres(ActionEvent evento) { // Maneja el clic sobre el tercer botón de carta
        jugarCartaPorIndice(2); // Intenta jugar la carta situada en el índice 2
    } // Cierra el método jugarCartaTres

    @FXML
    private void jugarCartaCuatro(ActionEvent evento) { // Maneja el clic sobre el cuarto botón de carta
        jugarCartaPorIndice(3); // Intenta jugar la carta situada en el índice 3
    } // Cierra el método jugarCartaCuatro

    private void jugarCartaPorIndice(int indice) { // Método auxiliar que intenta jugar la carta indicada
        if (partida == null || partida.estaTerminada()) { // Comprueba que exista partida activa y no haya terminado
            return; // Si no se puede jugar, simplemente finaliza el método
        } // Cierra la comprobación de partida válida
        JugadorHumano humano = partida.getJugadorHumano(); // Obtiene al jugador humano actual
        List<Carta> mano = humano.getMano(); // Recupera su mano completa
        if (indice >= mano.size()) { // Verifica que el índice solicitado exista
            return; // Si no existe la carta, sale sin hacer nada
        } // Cierra la comprobación del índice
        Carta cartaSeleccionada = mano.get(indice); // Obtiene la carta elegida según el índice
        if (!humano.puedeJugar(cartaSeleccionada, partida.getSumaMesa())) { // Verifica si la carta respeta la regla de no pasar de 50
            mostrarAlerta("Esa carta haría superar 50 puntos. Elige otra."); // Advierte al usuario que no puede jugar esa carta
            return; // Sale del método para evitar avanzar en el turno
        } // Cierra la comprobación de jugada válida
        try { // Inicia un bloque controlado para capturar errores de la lógica
            partida.jugarTurnoHumano(cartaSeleccionada); // Aplica la jugada del humano en la lógica del juego
            partida.jugarTurnoCPU(); // De inmediato procesa los turnos de las CPU según el flujo normal
        } catch (IllegalArgumentException excepcion) { // Captura errores que puedan surgir en la lógica
            mostrarAlerta(excepcion.getMessage()); // Muestra el mensaje de error al usuario para que entienda la causa
        } // Cierra el bloque catch
        renderizar(); // Refresca la interfaz para mostrar el nuevo estado
    } // Cierra el método jugarCartaPorIndice

    @FXML
    private void manejarHome(ActionEvent evento) { // Método que responde al botón de volver al inicio
        Main.obtenerInstancia().abrirVistaInicio(); // Solicita a la clase principal que muestre la pantalla inicial
    } // Cierra el método manejarHome

    @FXML
    private void manejarRefresh(ActionEvent evento) { // Método que responde al botón de reiniciar
        if (partida == null) { // Verifica que exista partida configurada
            return; // Si no hay partida, no se realiza ninguna acción
        } // Cierra la comprobación de partida nula
        partida.iniciar(cantidadCPUConfiguradas); // Reinicia la lógica con la misma cantidad de CPU
        ganadorAnunciado = false; // Restablece la bandera para permitir un nuevo anuncio al finalizar
        renderizar(); // Refresca la vista con la partida recién iniciada
    } // Cierra el método manejarRefresh

    private void mostrarAlerta(String mensaje) { // Método auxiliar para mostrar un cuadro de diálogo simple
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // Crea una alerta informativa
        alerta.setTitle("Aviso"); // Define el título de la ventana emergente
        alerta.setHeaderText(null); // Oculta el encabezado para mantener el mensaje limpio
        alerta.setContentText(mensaje); // Coloca el texto del mensaje
        alerta.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    } // Cierra el método mostrarAlerta
} // Cierra la clase TableroController