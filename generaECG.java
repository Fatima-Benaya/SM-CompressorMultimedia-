import java.io.FileWriter;
import java.io.IOException;

public class generaECG {

    static final int LEN = 8;   // segons
    static final int BPM = 64;   // beats per minute
    static final int SPR = 25;  // samples per second

    public static void main(String[] args) {

        double beatPeriod = 60.0 / BPM;
        int totalSamples = LEN * SPR;

        try (FileWriter writer = new FileWriter("ecg_signal.txt")) {

            // Capçalera
            writer.write("LEN " + LEN + "\n");
            writer.write("BPM " + BPM + "\n");
            writer.write("SPR " + SPR + "\n");

            for (int i = 0; i < totalSamples; i++) {

                double t = i / (double) SPR;
                double phase = (t % beatPeriod) / beatPeriod;

                double value = ecgWaveform(phase);

                writer.write(String.format(java.util.Locale.US, "%.4f %.5f\n", t, value));            
            }

            System.out.println("ECG generat correctament.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double ecgWaveform(double x) {

        double p = gaussian(x, 0.2, 0.025, 0.1);
        double q = gaussian(x, 0.45, 0.01, -0.15);
        double r = gaussian(x, 0.5, 0.012, 1.0);
        double s = gaussian(x, 0.55, 0.01, -0.25);
        double t = gaussian(x, 0.75, 0.04, 0.3);

        return p + q + r + s + t;
    }

    static double gaussian(double x, double mu, double sigma, double amp) {
        return amp * Math.exp(-Math.pow(x - mu, 2) / (2 * sigma * sigma));
    }
}