package io;

import model.EcgHeader;
import model.EcgSignal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class EcgFileWriter {

    public void write(EcgSignal signal, String filePath)
            throws IOException {

        EcgHeader header = signal.getHeader();
        double[] samples = signal.getSamples();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            
            bw.write("LEN " + header.getLen());
            bw.newLine();

            bw.write("BPM " + header.getBpm());
            bw.newLine();

            bw.write("SPR " + header.getSpr());
            bw.newLine();

            bw.newLine();

            
            // Reconstruim la primera columna amb el temps: temps = index / SPR
            for (int i = 0; i < samples.length; i++) {

                double time = i / (double) header.getSpr();

                bw.write(String.format(
                        Locale.US,
                        "%.4f %.5f",
                        time,
                        samples[i]));

                bw.newLine();
            }

            System.out.println("Fitxer escrit correctament: " + filePath);
        }
    }
}
