import java.io.*;
import java.util.ArrayList;

public class provaECG {

    static final String FILE_NAME = "ecg_signal_patient_1.txt";

    public static void main(String[] args) {

        ArrayList<Double> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String lenLine = br.readLine();
            String bpmLine = br.readLine();
            String sprLine = br.readLine();

            System.out.println("Capcalera:");
            System.out.println(lenLine);
            System.out.println(bpmLine);
            System.out.println(sprLine);
            System.out.println();

            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                values.add(Double.parseDouble(parts[1]));
            }

            System.out.println("Nombre de mostres: " + values.size());
            System.out.println();

            System.out.println("Primeres 10 mostres:");
            for (int i = 0; i < 10 && i < values.size(); i++) {
                System.out.println("mostra[" + i + "] = " + values.get(i));
            }

            System.out.println();
            System.out.println("Primeres 10 diferencies:");

            for (int i = 1; i < 11 && i < values.size(); i++) {
                double diff = values.get(i) - values.get(i - 1);
                System.out.println("diff[" + i + "] = " + diff);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


   