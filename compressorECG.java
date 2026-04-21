import java.io.*;
import java.util.ArrayList;

public class compressorECG {

    static final String INPUT_FILE  = "ecg_signal_patient_1.txt";
    static final String OUTPUT_FILE = "ecg_signal_patient_1.cmp";

    public static void main(String[] args) {
        
        ArrayList<Double> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {

            // llegir capçalera
            String lenLine = br.readLine();
            String bpmLine = br.readLine();
            String sprLine = br.readLine();

            int len = Integer.parseInt(lenLine.split("\\s+")[1]);
            int bpm = Integer.parseInt(bpmLine.split("\\s+")[1]);
            int spr = Integer.parseInt(sprLine.split("\\s+")[1]);

            String line;

            // Llegir les amplituds i guardar-les en una llista
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                double amplitude = Double.parseDouble(parts[1]);
                values.add(amplitude);
            }

            // Comprovar que hi ha mostres
            if (values.size() == 0) {
                System.out.println("No hi ha mostres.");
                return;
            }
            // Guardar primera mostra
            double firstSample = values.get(0);

            // Calcular minDiff i maxDiff
            double minDiff = Double.MAX_VALUE;
            double maxDiff = -Double.MAX_VALUE;

            for (int i = 1; i < values.size(); i++) {
                double diff = values.get(i) - values.get(i - 1);

                if (diff < minDiff) {
                    minDiff = diff;
                }
                if (diff > maxDiff) {
                    maxDiff = diff;
                }
            }

            // Escriure capçalera del fitxer comprimit
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE));

            dos.writeBytes("ECG2");
            dos.writeInt(len);
            dos.writeInt(bpm);
            dos.writeInt(spr);
            dos.writeInt(values.size());
            dos.writeDouble(firstSample);
            dos.writeDouble(minDiff);
            dos.writeDouble(maxDiff);

            for (int i = 1; i < values.size(); i++) {
                double diff = values.get(i) - values.get(i - 1);

                int q;
                if (maxDiff == minDiff) {
                    q = 0;
                } else {
                    q = (int) Math.round(((diff - minDiff) / (maxDiff - minDiff)) * 255.0);
                }

                if (q < 0) q = 0;
                if (q > 255) q = 255;

                dos.writeByte(q);
            }

            dos.close();

            //Missatges de control
            System.out.println("ECG comprimit correctement.");
            System.out.println ("LEN: " + len);
            System.out.println ("BPM: " + bpm);
            System.out.println ("SPR: " + spr);
            System.out.println ("Nombre de mostres: " + values.size());
            System.out.println ("Primera mostra: " + firstSample);
            System.out.println ("MinDiff: " + minDiff); 
            System.out.println ("MaxDiff: " + maxDiff);
        


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}