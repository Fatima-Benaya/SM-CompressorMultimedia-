package comparador;

import model.EcgSignal;

public class EcgComparator {

    public double calculateMSE(EcgSignal original, EcgSignal reconstructed) {
        double[] originalSamples = original.getSamples();
        double[] reconstructedSamples = reconstructed.getSamples();

        if (originalSamples.length != reconstructedSamples.length) {
            throw new IllegalArgumentException("Els ECGs han de tenir el mateix nombre de mostres.");
        }

        if (originalSamples.length == 0) {
            throw new IllegalArgumentException("No es poden comparar senyals buits.");
        }

        double mse = 0.0;
        int n = originalSamples.length;

        for (int i = 0; i < n; i++) {
            double diff = originalSamples[i] - reconstructedSamples[i];
            mse += diff * diff;
        }

        return mse / n;
    }
}
