package com.example.el_cincuentenazo.modelo; // Declara el paquete de la baraja

import java.io.BufferedReader; // Importa la clase para leer texto de forma eficiente
import java.io.IOException; // Importa la excepción de entrada y salida
import java.io.InputStream; // Importa la clase para manejar flujos de entrada
import java.io.InputStreamReader; // Importa la clase que convierte bytes en caracteres
import java.nio.charset.StandardCharsets; // Importa la codificación estándar UTF-8
import java.util.ArrayDeque; // Importa una cola doble para almacenar cartas
import java.util.ArrayList; // Importa la lista dinámica
import java.util.Collections; // Importa utilidades para desordenar elementos
import java.util.Deque; // Importa la interfaz de cola doble
import java.util.List; // Importa la interfaz de lista
import java.util.Objects; // Importa utilidades para validación de objetos

// Clase que representa un mazo de cartas con operaciones básicas.
public class Baraja { // Declara la clase pública Baraja

    private final Deque<Carta> cartas; // Cola doble que guarda las cartas en orden de extracción

    public Baraja(String rutaArchivo) { // Constructor que intenta cargar las cartas desde un archivo
        this.cartas = new ArrayDeque<>(); // Inicializa la estructura de datos vacía
        cargarDesdeArchivo(rutaArchivo); // Intenta llenar la baraja leyendo el archivo indicado
    } // Cierra el constructor de la baraja

    public void cargarDesdeArchivo(String rutaArchivo) { // Método que llena la baraja desde un archivo de texto
        if (rutaArchivo == null || rutaArchivo.isBlank()) { // Comprueba si no se proporcionó una ruta válida
            cargarPorDefecto(); // Si no hay ruta, se carga la baraja estándar
            return; // Sale del método porque no se puede leer un archivo inexistente
        } // Cierra la comprobación de ruta vacía
        try { // Inicia el bloque que puede generar excepciones de lectura
            cartas.clear(); // Limpia cualquier carta existente antes de cargar nuevas
            InputStream entrada = obtenerStreamDesdeRuta(rutaArchivo); // Obtiene un flujo de entrada para el archivo solicitado
            if (entrada == null) { // Verifica si no se pudo abrir el archivo
                cargarPorDefecto(); // Si falla, se carga la baraja estándar
                return; // Termina el método porque no hay nada más que leer
            } // Cierra la verificación del flujo nulo
            try (BufferedReader lector = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) { // Crea un lector de texto en UTF-8
                String linea; // Variable para guardar cada línea del archivo
                while ((linea = lector.readLine()) != null) { // Recorre el archivo línea por línea
                    procesarLinea(linea); // Procesa cada línea para convertirla en cartas
                } // Cierra el ciclo de lectura de líneas
            } // Cierra el bloque try-with-resources que asegura el cierre del lector
        } catch (IOException | RuntimeException e) { // Captura errores de lectura o formato inesperado
            cargarPorDefecto(); // Si ocurre cualquier problema, se recurre a la baraja estándar
        } // Cierra el bloque catch que maneja excepciones
    } // Cierra el método cargarDesdeArchivo

    private InputStream obtenerStreamDesdeRuta(String rutaArchivo) { // Método auxiliar para abrir el recurso tanto en archivos como en recursos empaquetados
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); // Obtiene el cargador de clases actual
        InputStream stream = classLoader.getResourceAsStream(rutaArchivo); // Intenta abrir el archivo como recurso dentro del paquete
        if (stream != null) { // Comprueba si se encontró el recurso
            return stream; // Devuelve el flujo encontrado en los recursos
        } // Cierra la comprobación del recurso embebido
        try { // Intenta abrir el archivo directamente desde el sistema de archivos
            return java.nio.file.Files.newInputStream(java.nio.file.Path.of(rutaArchivo)); // Abre un flujo de bytes hacia la ruta física
        } catch (IOException e) { // Si ocurre un error al abrir el archivo físico
            return null; // Devuelve nulo para que el llamador sepa que no se pudo abrir
        } // Cierra el bloque catch
    } // Cierra el método obtenerStreamDesdeRuta

    private void procesarLinea(String linea) { // Método auxiliar que transforma una línea de texto en cartas
        String[] partes = linea.split("[\\t,]"); // Divide la línea tomando comas o tabulaciones como separadores
        for (String parte : partes) { // Recorre cada fragmento obtenido
            String textoCarta = parte.trim(); // Elimina espacios iniciales y finales de la descripción de la carta
            if (textoCarta.isEmpty()) { // Omite fragmentos vacíos que puedan aparecer
                continue; // Pasa al siguiente fragmento
            } // Cierra la comprobación de texto vacío
            String[] secciones = textoCarta.split(" de "); // Separa valor y palo usando la palabra "de" como guía
            if (secciones.length != 2) { // Valida que el formato sea el esperado
                throw new IllegalArgumentException("Formato de carta inválido: " + textoCarta); // Si no es válido, lanza una excepción para activar la carga por defecto
            } // Cierra la validación del formato
            String valor = normalizarValor(secciones[0]); // Normaliza el valor a una forma estándar (por ejemplo, As -> A)
            String palo = secciones[1].trim(); // Toma el palo quitando espacios sobrantes
            cartas.addLast(new Carta(valor, palo)); // Crea la carta y la agrega al final de la cola
        } // Cierra el recorrido de fragmentos
    } // Cierra el método procesarLinea

    private String normalizarValor(String valorOriginal) { // Método auxiliar que homogeniza la forma escrita del valor
        String limpio = valorOriginal.trim(); // Quita espacios innecesarios del valor original
        if (limpio.equalsIgnoreCase("As")) { // Si el texto corresponde a un As
            return "A"; // Devuelve la letra A como representación estándar
        } // Cierra la comprobación del As
        if (limpio.equalsIgnoreCase("Sota")) { // Si el texto usa el término "Sota"
            return "J"; // Se interpreta como una J
        } // Cierra la comprobación de la sota
        if (limpio.equalsIgnoreCase("Reina")) { // Si el texto usa el término "Reina"
            return "Q"; // Se interpreta como una Q
        } // Cierra la comprobación de la reina
        if (limpio.equalsIgnoreCase("Rey")) { // Si el texto usa el término "Rey"
            return "K"; // Se interpreta como una K
        } // Cierra la comprobación del rey
        return limpio.toUpperCase(); // Para el resto, devuelve el texto en mayúsculas tal cual
    } // Cierra el método normalizarValor

    public void cargarPorDefecto() { // Método que construye una baraja estándar si no se pudo leer el archivo
        cartas.clear(); // Limpia cualquier carta existente para evitar duplicados
        List<String> palos = List.of("tréboles", "picas", "corazones", "diamantes"); // Define los palos tradicionales con tildes
        List<String> valores = new ArrayList<>(); // Crea la lista que contendrá todos los valores posibles
        valores.add("A"); // Agrega el As al inicio de la lista
        for (int i = 2; i <= 10; i++) { // Recorre los valores numéricos del 2 al 10
            valores.add(String.valueOf(i)); // Convierte cada número a texto y lo agrega
        } // Cierra el bucle de números
        valores.add("J"); // Agrega la J a la lista de valores
        valores.add("Q"); // Agrega la Q a la lista de valores
        valores.add("K"); // Agrega la K a la lista de valores
        for (String palo : palos) { // Recorre cada palo disponible
            for (String valor : valores) { // Recorre cada valor para crear la combinación completa
                cartas.addLast(new Carta(valor, palo)); // Crea cada carta y la coloca al final de la cola
            } // Cierra el bucle de valores
        } // Cierra el bucle de palos
    } // Cierra el método cargarPorDefecto

    public void barajar() { // Método que desordena las cartas
        List<Carta> temporal = new ArrayList<>(cartas); // Copia el contenido actual a una lista mutable
        Collections.shuffle(temporal); // Desordena la lista de forma aleatoria
        cartas.clear(); // Limpia la cola para prepararla
        for (Carta carta : temporal) { // Recorre la lista desordenada
            cartas.addLast(carta); // Vuelve a colocar cada carta en la cola en el nuevo orden
        } // Cierra el bucle de reposición
    } // Cierra el método barajar

    public Carta robar() { // Método que extrae la carta situada al inicio de la baraja
        return cartas.pollFirst(); // Quita y devuelve la primera carta o null si está vacía
    } // Cierra el método robar

    public void poner(Carta carta) { // Método que coloca una carta al final de la baraja
        cartas.addLast(Objects.requireNonNull(carta)); // Añade la carta asegurando que no sea nula
    } // Cierra el método poner

    public boolean estaVacia() { // Método que informa si la baraja no tiene cartas
        return cartas.isEmpty(); // Devuelve verdadero si la cola está vacía
    } // Cierra el método estaVacia

    public int tamano() { // Método que devuelve cuántas cartas contiene la baraja
        return cartas.size(); // Retorna el tamaño actual de la cola
    } // Cierra el método tamano

    public void vaciar() { // Método adicional que deja la baraja sin cartas
        cartas.clear(); // Elimina todos los elementos almacenados
    } // Cierra el método vaciar

    public Carta verUltimaCarta() { // Método que permite ver la carta situada al final sin retirarla
        return cartas.peekLast(); // Devuelve la carta del fondo o null si la baraja está vacía
    } // Cierra el método verUltimaCarta

    public Carta sacarUltimaCarta() { // Método que retira la carta final de la baraja
        return cartas.pollLast(); // Quita y devuelve la última carta o null si no hay cartas
    } // Cierra el método sacarUltimaCarta
} // Cierra la clase Baraja
