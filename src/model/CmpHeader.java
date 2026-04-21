package model;

// Guarda la capçalera del fitxer comprimit .cmp
public class CmpHeader {

    private String signatura;
    private int len;
    private int bpm;
    private int spr;
    private int numSamples;
    private double firstSample;
    private double minDiff;
    private double maxDiff;

    public CmpHeader(String signatura, int len, int bpm, int spr, int numSamples,
                     double firstSample, double minDiff, double maxDiff) {
        this.signatura = signatura;
        this.len = len;
        this.bpm = bpm;
        this.spr = spr;
        this.numSamples = numSamples;
        this.firstSample = firstSample;
        this.minDiff = minDiff;
        this.maxDiff = maxDiff;
    }

    public String getSignatura() {
        return signatura;
    }

    public int getLen() {
        return len;
    }

    public int getBpm() {
        return bpm;
    }

    public int getSpr() {
        return spr;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public double getFirstSample() {
        return firstSample;
    }

    public double getMinDiff() {
        return minDiff;
    }

    public double getMaxDiff() {
        return maxDiff;
    }

    @Override
    public String toString() {
        return "CmpHeader{" +
                "signatura='" + signatura + '\'' +
                ", len=" + len +
                ", bpm=" + bpm +
                ", spr=" + spr +
                ", numSamples=" + numSamples +
                ", firstSample=" + firstSample +
                ", minDiff=" + minDiff +
                ", maxDiff=" + maxDiff +
                '}';
    }
}

// Per guardar info de la capcalera
