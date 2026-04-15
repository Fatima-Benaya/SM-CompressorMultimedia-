package model;

import java.util.Arrays;

// Guarda la cabecera y todas las muestras del ECG.
public class EcgSignal {

    private EcgHeader header;
    private double[] samples;

    // Constructor para crear la señal completa.
    public EcgSignal(EcgHeader header, double[] samples) {
        this.header = header;
        this.samples = samples;
    }

    public EcgHeader getHeader() {
        return header;
    }

    public double[] getSamples() {
        return samples;
    }

    @Override
    public String toString() {
        return "EcgSignal{header=" + header + ", samples=" + Arrays.toString(samples) + "}";
    }
}
