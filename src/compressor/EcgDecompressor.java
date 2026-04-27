package compressor;

import model.EcgHeader;
import model.EcgSignal;
import io.EcgFileWriter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

// Aquesta classe descomprimeix un fitxer ECG binari
// i utilitza EcgFileWriter per generar el fitxer de text.
public class EcgDecompressor {

        public EcgSignal decompress(
                        String inputFile,
                        String outputFile)
                        throws IOException {

                try (DataInputStream dis = new DataInputStream(
                                new FileInputStream(inputFile))) {

                        // ---------- 1. LLEGIR MAGIC NUMBER ----------
                        byte[] magicBytes = new byte[4];
                        dis.readFully(magicBytes);

                        String magic = new String(magicBytes);

                        if (!magic.equals("ECGQ")) {
                                throw new IllegalArgumentException(
                                                "Format de fitxer incorrecte.");
                        }

                        // ---------- 2. LLEGIR CAPÇALERA ----------
                        int len = dis.readInt();
                        int bpm = dis.readInt();
                        int spr = dis.readInt();

                        int numSamples = dis.readInt();

                        double min = dis.readDouble();
                        double max = dis.readDouble();
                        double q = dis.readDouble();

                        // ---------- 3. RECONSTRUIR MOSTRES ----------
                        double[] samples = new double[numSamples];

                        for (int i = 0; i < numSamples; i++) {

                                int compressed = dis.readUnsignedByte();

                                double sample;

                                if (q == 0) {
                                        sample = min;
                                } else {
                                        sample = min +
                                                        compressed * q;
                                }

                                samples[i] = sample;
                        }

                        // ---------- 4. CREAR OBJECTES MODEL ----------
                        EcgHeader header = new EcgHeader(len, bpm, spr);

                        EcgSignal signal = new EcgSignal(header, samples);

                        // ---------- 5. ESCRIURE FITXER AMB FileWriter ----------
                        EcgFileWriter writer = new EcgFileWriter();

                        writer.write(signal, outputFile);

                        // ---------- MISSATGES DE CONTROL ----------
                        System.out.println("Descompressió completada.");
                        System.out.println("Fitxer generat: " + outputFile);
                        System.out.println("LEN: " + len);
                        System.out.println("BPM: " + bpm);
                        System.out.println("SPR: " + spr);
                        System.out.println("Mostres: " + numSamples);
                        System.out.println("Min: " + min);
                        System.out.println("Max: " + max);
                        System.out.println("q: " + q);

                        return signal;
                }
        }
}