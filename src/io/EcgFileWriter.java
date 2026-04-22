package io;

import model.EcgHeader;
import model.EcgSignal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Aquesta classe escriu un objecte EcgSignal en un fitxer de text.
public class EcgFileWriter {

    // Escriu el senyal complet en un fitxer.
    public void write(EcgSignal signal, String filePath)
            throws IOException {

        EcgHeader header = signal.getHeader();
        double[] samples = signal.getSamples();

        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(filePath))) {

            // ---------- CAPÇALERA ----------
            bw.write("LEN " + header.getLen());
            bw.newLine();

            bw.write("BPM " + header.getBpm());
            bw.newLine();

            bw.write("SPR " + header.getSpr());
            bw.newLine();

            // línia en blanc (opcional)
            bw.newLine();

            // ---------- MOSTRES ----------
            for (int i = 0; i < samples.length; i++) {

                double sample = samples[i];

                bw.write(i + " " + sample);
                bw.newLine();
            }

            System.out.println(
                    "Fitxer escrit correctament: "
                    + filePath
            );
        }
    }
}
