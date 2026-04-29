package io;

import model.EcgHeader;
import model.EcgSignal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Aquesta classe llegeix un fitxer de text amb format ECG i el converteix en un objecte EcgSignal.
 */
public class EcgFileReader {

    public EcgSignal read(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));

        int len = -1;
        int bpm = -1;
        int spr = -1;

        List<Double> samples = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.trim(); 

            if (line.isEmpty()) {
                continue;
            }

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

            
            Double sample = extractLastDouble(line);
            if (sample != null) {
                samples.add(sample);
            }
        }

        
        if (len == -1 || bpm == -1 || spr == -1) {
            throw new IllegalArgumentException("El fitxer ECG no conté una capçalera vàlida.");
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

    private int extractInt(String line) {
        String[] parts = line.split("[^0-9-]+"); 

        for (String part : parts) {
            if (!part.isEmpty()) {
                return Integer.parseInt(part);
            }
        }

        throw new IllegalArgumentException("No s'ha pogut llegir un enter de la línia: " + line);
    }


    private Double extractLastDouble(String line) {
        String[] parts = line.split("\\s+");

        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Double.parseDouble(parts[i].replace(',', '.'));
            } catch (NumberFormatException e) {
                
            }
        }

        return null;
    }
}