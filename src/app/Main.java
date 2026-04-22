package app;

import io.EcgFileReader;
import compressor.EcgCompressor;
import compressor.EcgDecompressor;
import model.EcgSignal;

public class Main {

    public static void main(String[] args) {

        String inputFile = "ecg_signal_patient_1.txt";
        String compressedFile = "ecg_signal_patient_1.ecgq";
        String decompressedFile = "ecg_signal_patient_1_out.txt";

        if (args.length > 0) {
            inputFile = args[0];
        }

        EcgFileReader reader = new EcgFileReader();
        EcgCompressor compressor = new EcgCompressor();
        EcgDecompressor decompressor = new EcgDecompressor();

        try {

            // ---------- 1. LLEGIR ----------
            EcgSignal signal = reader.read(inputFile);

            System.out.println("Fitxer llegit correctament.");
            System.out.println("LEN = " + signal.getHeader().getLen());
            System.out.println("BPM = " + signal.getHeader().getBpm());
            System.out.println("SPR = " + signal.getHeader().getSpr());
            System.out.println("Mostres = " + signal.getSamples().length);

            // ---------- 2. COMPRIMIR ----------
            compressor.compress(
                    signal,
                    compressedFile
            );

            System.out.println("Fitxer comprimit: " + compressedFile);

            // ---------- 3. DESCOMPRIMIR ----------
            decompressor.decompress(
                    compressedFile,
                    decompressedFile
            );

            System.out.println("Fitxer descomprimit: " + decompressedFile);

        } catch (Exception e) {

            System.out.println(
                    "Error en el procés:"
            );

            e.printStackTrace();
        }
    }
}