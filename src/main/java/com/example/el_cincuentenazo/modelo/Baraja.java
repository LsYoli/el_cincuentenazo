package com.example.el_cincuentenazo.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Representa el mazo de cartas desde el cual se reparten y se roban nuevas
 * cartas durante la partida. Permite cargar la configuración desde un archivo o
 * crear una baraja por defecto.
 */
public class Baraja {

    /** Cartas disponibles para robar en el orden actual. */
    private final Deque<Carta> cartas;

    /**
     * Crea una baraja intentando cargar su contenido desde un archivo externo.
     *
     * @param rutaArchivo ubicación del archivo de configuración o recurso
     */
    public Baraja(String rutaArchivo) {
        this.cartas = new ArrayDeque<>();
        cargarDesdeArchivo(rutaArchivo);
    }

    /**
     * Lee las cartas desde el archivo indicado. Si la ruta es inválida o ocurre
     * algún error, se recurre a la baraja por defecto.
     *
     * @param rutaArchivo archivo o recurso a leer
     */
    public void cargarDesdeArchivo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            cargarPorDefecto();
            return;
        }
        try {
            cartas.clear();
            InputStream entrada = obtenerStreamDesdeRuta(rutaArchivo);
            if (entrada == null) {
                cargarPorDefecto();
                return;
            }
            try (BufferedReader lector = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    procesarLinea(linea);
                }
            }
        } catch (IOException | RuntimeException e) {
            cargarPorDefecto();
        }
    }

    /**
     * Intenta abrir la ruta dada como recurso del classpath o archivo físico.
     *
     * @param rutaArchivo cadena con la ruta del archivo
     * @return flujo de entrada listo para leerse o {@code null} si no existe
     */
    private InputStream obtenerStreamDesdeRuta(String rutaArchivo) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(rutaArchivo);
        if (stream != null) {
            return stream;
        }
        try {
            return java.nio.file.Files.newInputStream(java.nio.file.Path.of(rutaArchivo));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Convierte una línea de texto en una o más cartas y las agrega al mazo.
     *
     * @param linea cadena con cartas separadas por coma o tabulación
     */
    private void procesarLinea(String linea) {
        String[] partes = linea.split("[\\t,]");
        for (String parte : partes) {
            String textoCarta = parte.trim();
            if (textoCarta.isEmpty()) {
                continue;
            }
            String[] secciones = textoCarta.split(" de ");
            if (secciones.length != 2) {
                throw new IllegalArgumentException("Formato de carta inválido: " + textoCarta);
            }
            String valor = normalizarValor(secciones[0]);
            String palo = secciones[1].trim();
            cartas.addLast(new Carta(valor, palo));
        }
    }

    /**
     * Homogeneiza la forma escrita de los valores leídos.
     *
     * @param valorOriginal texto original encontrado en el archivo
     * @return representación interna del valor
     */
    private String normalizarValor(String valorOriginal) {
        String limpio = valorOriginal.trim();
        if (limpio.equalsIgnoreCase("As")) {
            return "A";
        }
        if (limpio.equalsIgnoreCase("Sota")) {
            return "J";
        }
        if (limpio.equalsIgnoreCase("Reina")) {
            return "Q";
        }
        if (limpio.equalsIgnoreCase("Rey")) {
            return "K";
        }
        return limpio.toUpperCase();
    }

    /**
     * Rellena la baraja con una versión estándar si no se pudo cargar desde
     * archivo.
     */
    public void cargarPorDefecto() {
        cartas.clear();
        List<String> palos = List.of("tréboles", "picas", "corazones", "diamantes");
        List<String> valores = new ArrayList<>();
        valores.add("A");
        for (int i = 2; i <= 10; i++) {
            valores.add(String.valueOf(i));
        }
        valores.add("J");
        valores.add("Q");
        valores.add("K");
        for (String palo : palos) {
            for (String valor : valores) {
                cartas.addLast(new Carta(valor, palo));
            }
        }
    }

    /**
     * Desordena aleatoriamente las cartas restantes.
     */
    public void barajar() {
        List<Carta> temporal = new ArrayList<>(cartas);
        Collections.shuffle(temporal);
        cartas.clear();
        for (Carta carta : temporal) {
            cartas.addLast(carta);
        }
    }

    /**
     * Extrae la carta situada al inicio del mazo.
     *
     * @return carta obtenida o {@code null} si no quedan cartas
     */
    public Carta robar() {
        return cartas.pollFirst();
    }

    /**
     * Coloca una carta al final del mazo.
     *
     * @param carta carta a insertar
     */
    public void poner(Carta carta) {
        cartas.addLast(Objects.requireNonNull(carta));
    }

    /**
     * Indica si ya no quedan cartas para robar.
     *
     * @return {@code true} cuando la baraja está vacía
     */
    public boolean estaVacia() {
        return cartas.isEmpty();
    }

    /**
     * Devuelve cuántas cartas hay disponibles.
     *
     * @return tamaño actual del mazo
     */
    public int tamano() {
        return cartas.size();
    }

    /**
     * Elimina todas las cartas restantes.
     */
    public void vaciar() {
        cartas.clear();
    }

    /**
     * Permite consultar la carta que está al final del mazo sin retirarla.
     *
     * @return carta al fondo o {@code null} si la baraja está vacía
     */
    public Carta verUltimaCarta() {
        return cartas.peekLast();
    }

    /**
     * Retira la carta final del mazo.
     *
     * @return carta extraída o {@code null} si ya no hay
     */
    public Carta sacarUltimaCarta() {
        return cartas.pollLast();
    }
}
