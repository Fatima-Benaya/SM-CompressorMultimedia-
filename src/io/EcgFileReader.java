package io;

import model.EcgHeader;
import model.EcgSignal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Esta clase lee un fichero ECG de texto y construye un objeto EcgSignal.
public class EcgFileReader {

    // Lee el fichero completo.
    // Espera encontrar las cabeceras LEN, BPM y SPR, y despues las muestras.
    public EcgSignal read(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));

        int len = -1;
        int bpm = -1;
        int spr = -1;

        List<Double> samples = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();

            // Si la linea esta vacia, no hacemos nada.
            if (line.isEmpty()) {
                continue;
            }

            // Leemos cada dato de la cabecera.
            if (line.startsWith("LEN")) {
                len = extractInt(line);
                continue;
            }

            if (line.startsWith("BPM")) {
                bpm = extractInt(line);
                continue;
            }

            if (line.startsWith("SPR")) {
                spr = extractInt(line);
                continue;
            }

            // Si no es cabecera, intentamos sacar una muestra numerica.
            Double sample = extractLastDouble(line);
            if (sample != null) {
                samples.add(sample);
            }
        }

        // Si falta algun dato importante, el fichero no es valido.
        if (len == -1 || bpm == -1 || spr == -1) {
            throw new IllegalArgumentException("Faltan datos de cabecera: LEN, BPM o SPR.");
        }

        if (samples.isEmpty()) {
            throw new IllegalArgumentException("El fitxer ECG no conté mostres.");
        }

        double[] sampleArray = new double[samples.size()];
        for (int i = 0; i < samples.size(); i++) {
            sampleArray[i] = samples.get(i);
        }

        EcgHeader header = new EcgHeader(len, bpm, spr);
        return new EcgSignal(header, sampleArray);
    }

    // Busca el primer numero entero de una linea.
    // Ejemplo: "LEN=10" devuelve 10.
    private int extractInt(String line) {
        String[] parts = line.split("[^0-9-]+");

        for (String part : parts) {
            if (!part.isEmpty()) {
                return Integer.parseInt(part);
            }
        }

        throw new IllegalArgumentException("No se pudo leer un entero de la linea: " + line);
    }

    // Busca el ultimo numero decimal de una linea.
    // Esto permite leer lineas como:
    // "0.123"
    // "12 0.456"
    private Double extractLastDouble(String line) {
        String[] parts = line.split("\\s+");

        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Double.parseDouble(parts[i].replace(',', '.'));
            } catch (NumberFormatException e) {
                // Si esta parte no es un numero, seguimos buscando.
            }
        }

        return null;
    }
}