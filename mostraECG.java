
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class mostraECG extends JPanel {

    static final String FILE_NAME = "ecg_signal_patient_1.txt";

    ArrayList<Double> values = new ArrayList<>();

    public mostraECG() {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            // llegir capçalera
            br.readLine();
            br.readLine();
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                values.add(Double.parseDouble(parts[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (values.size() < 2) return;

        int w = getWidth();
        int h = getHeight();

        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (double v : values) {
            if (v < min) min = v;
            if (v > max) max = v;
        }

        int prevX = 0;
        int prevY = scale(values.get(0), min, max, h);

        for (int i = 1; i < values.size(); i++) {

            int x = (int)((i / (double)values.size()) * w);
            int y = scale(values.get(i), min, max, h);

            g.drawLine(prevX, prevY, x, y);

            prevX = x;
            prevY = y;
        }
    }

    int scale(double v, double min, double max, int h) {
        return (int)(h - ((v - min) / (max - min)) * h);
    }

    public static void main(String[] args) {

        File f = new File(FILE_NAME);

        if (!f.exists()) {
            System.out.println("Error: el fitxer " + FILE_NAME + " no existeix.");
            System.out.println("Finalitzant programa.");
            return;
        }

        JFrame frame = new JFrame("ECG Viewer");
        mostraECG panel = new mostraECG();

        frame.setSize(1000, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.setVisible(true);
    }
}