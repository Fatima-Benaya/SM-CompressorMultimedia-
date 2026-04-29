package app;

import io.EcgFileReader;
import compressor.EcgCompressor;
import compressor.EcgDecompressor;
import model.EcgSignal;
import comparador.EcgComparator;

public class Main {

    public static void main(String[] args) {

        String inputFile = "t_forn_1.txt";
        String compressedFile = "t_forn_1.cmp";
        String decompressedFile = "t_forn_1.txt.out";

        if (args.length > 0) {
            inputFile = args[0];
        }

        EcgFileReader reader = new EcgFileReader();
        EcgCompressor compressor = new EcgCompressor();
        EcgDecompressor decompressor = new EcgDecompressor();
        EcgComparator comparator = new EcgComparator();

        try {

            // LLEGIR
            EcgSignal signal = reader.read(inputFile);

            System.out.println("Fitxer llegit correctament.");
            System.out.println("LEN = " + signal.getHeader().getLen());
            System.out.println("BPM = " + signal.getHeader().getBpm());
            System.out.println("SPR = " + signal.getHeader().getSpr());
            System.out.println("Mostres = " + signal.getSamples().length);

            // COMPRIMIR
            compressor.compress(
                    signal,
                    compressedFile);

            System.out.println("Fitxer comprimit: " + compressedFile);

            // DESCOMPRIMIR
            decompressor.decompress(
                    compressedFile,
                    decompressedFile);

            System.out.println("Fitxer descomprimit: " + decompressedFile);

            // COMPARAR
            double mse = comparator.calculateMSE(
                    signal,
                    reader.read(decompressedFile));

            System.out.println("MSE=" + mse);

        } catch (Exception e) {

            System.out.println(
                    "Error en el procés:");

            e.printStackTrace();
        }
    }
}