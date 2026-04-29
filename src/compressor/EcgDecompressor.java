package compressor;

import model.EcgHeader;
import model.EcgSignal;
import io.EcgFileWriter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class EcgDecompressor {

        public EcgSignal decompress(
                        String inputFile,
                        String outputFile)
                        throws IOException {

                try (DataInputStream dis = new DataInputStream(
                                new FileInputStream(inputFile))) {

                        
                        byte[] magicBytes = new byte[4];   
                        dis.readFully(magicBytes);

                        String magic = new String(magicBytes);

                        if (!magic.equals("ECGQ")) {
                                throw new IllegalArgumentException(
                                                "Format de fitxer incorrecte.");
                        }

                        // LLEGIR LA CAPÇALERA
                        int len = dis.readInt();
                        int bpm = dis.readInt();
                        int spr = dis.readInt();

                        int numSamples = dis.readInt();

                        double min = dis.readDouble();
                        double max = dis.readDouble();
                        double q = dis.readDouble();

                        // RECONSTRUIR LES MOSTRES 
                        double[] samples = new double[numSamples];

                        for (int i = 0; i < numSamples; i++) {

                                int compressed = dis.readUnsignedByte();

                                double sample;

                                if (q == 0) {
                                        sample = min;
                                } else {
                                        sample = min + compressed * q;
                                }

                                samples[i] = sample;
                        }

                        // CREAR OBJECTE DESCOMPRIMIT        
                        EcgHeader header = new EcgHeader(len, bpm, spr);

                        EcgSignal signal = new EcgSignal(header, samples);

                        
                        EcgFileWriter writer = new EcgFileWriter();

                        writer.write(signal, outputFile);

                        
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