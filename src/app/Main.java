package app;

import io.EcgFileReader;
import model.EcgSignal;

// Main de prova per comprovar si EcgFileReader funciona.
public class Main {

    public static void main(String[] args) {

        String filePath = "ecg_signal_patient_1.txt";
        if (args.length > 0) {
            filePath = args[0];
        }

        EcgFileReader reader = new EcgFileReader();

        try {
            EcgSignal signal = reader.read(filePath);

            System.out.println("Fitxer llegit correctament: " + filePath);
            System.out.println("LEN = " + signal.getHeader().getLen());
            System.out.println("BPM = " + signal.getHeader().getBpm());
            System.out.println("SPR = " + signal.getHeader().getSpr());
            System.out.println("Nombre de mostres = " + signal.getSamples().length);

            System.out.println("Mostres llegides:");
            for (double sample : signal.getSamples()) {
                System.out.println(sample);
            }
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error en llegir el fitxer.");
            System.out.println(e.getMessage());
        }
    }
}
