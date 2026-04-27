package compressor;

import model.EcgHeader;
import model.EcgSignal;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EcgCompressor {

    // Compressor que utilitza min, max i factor q

    // Nombre de nivells de quantificació
    private static final int LEVELS = 255;

    public void compress(EcgSignal signal, String outputFile)
            throws IOException {

        EcgHeader header = signal.getHeader();
        double[] samples = signal.getSamples();

        if (samples.length == 0) {
            throw new IllegalArgumentException("No hi ha mostres.");
        }

        // 1. Trobar min i max del senyal
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (double s : samples) {
            if (s < min)
                min = s;
            if (s > max)
                max = s;
        }

        // 2. Calcular factor de quantificació q
        double q;

        if (max == min) {
            q = 0.0;
        } else {
            q = (max - min) / LEVELS;
        }

        // 3. Escriure fitxer comprimit
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(outputFile))) {

            // ---------- MAGIC NUMBER ----------
            dos.writeBytes("ECGQ");

            // ---------- CAPÇALERA COMPRIMIDA ----------
            // (ja és compressió perquè és binari)

            dos.writeInt(header.getLen());
            dos.writeInt(header.getBpm());
            dos.writeInt(header.getSpr());

            // ---------- INFORMACIÓ NECESSÀRIA ----------
            dos.writeInt(samples.length);
            dos.writeDouble(min);
            dos.writeDouble(max);
            dos.writeDouble(q);

            // ---------- MOSTRES COMPRIMIDES ----------
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