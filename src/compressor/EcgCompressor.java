package compressor;

import model.EcgHeader;
import model.EcgSignal;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EcgCompressor {

    private static final int LEVELS = 255;

    public void compress(EcgSignal signal, String outputFile)
            throws IOException {

        EcgHeader header = signal.getHeader();
        double[] samples = signal.getSamples();

        if (samples.length == 0) {
            throw new IllegalArgumentException("No hi ha mostres.");
        }

        // Trobar min i max del senyal
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (double s : samples) {
            if (s < min)
                min = s;
            if (s > max)
                max = s;
        }

        // Calcular factor de quantificació q
        double q;

        if (max == min) {
            q = 0.0;
        } else {
            q = (max - min) / LEVELS;
        }

        // Escriure fitxer comprimit
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(outputFile))) {

            dos.writeBytes("ECGQ");

            dos.writeInt(header.getLen());
            dos.writeInt(header.getBpm());
            dos.writeInt(header.getSpr());

            dos.writeInt(samples.length);
            dos.writeDouble(min);
            dos.writeDouble(max);
            dos.writeDouble(q);

            for (double sample : samples) {

                int compressed;

                if (q == 0) {
                    compressed = 0;
                } else {
                    compressed = (int) Math.round(
                            (sample - min) / q);
                }

                if (compressed < 0)
                    compressed = 0;
                if (compressed > LEVELS)
                    compressed = LEVELS;

                dos.writeByte(compressed);
            }

            System.out.println("Compressió completada.");
            System.out.println("Min: " + min);
            System.out.println("Max: " + max);
            System.out.println("q: " + q);
            System.out.println("Mostres: " + samples.length);
        }
    }

}