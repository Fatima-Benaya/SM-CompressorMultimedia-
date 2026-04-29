package model;

// Guarda les dades principals de la capçalera del fitxer ECG.  
public class EcgHeader {

    private int len;
    private int bpm;
    private int spr;

    
    public EcgHeader(int len, int bpm, int spr) {
        this.len = len;
        this.bpm = bpm;
        this.spr = spr;
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

    @Override
    public String toString() {
        return "EcgHeader{len=" + len + ", bpm=" + bpm + ", spr=" + spr + "}";
    }
}