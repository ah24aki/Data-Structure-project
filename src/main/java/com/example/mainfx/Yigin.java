package com.example.mainfx;

public class Yigin {
    private AracDugum ust;
    private int boyut;
    private int kapasite;

    public Yigin(int kapasite) {
        this.ust = null;
        this.boyut = 0;
        this.kapasite = kapasite;
    }

    public boolean ekle(Arac arac) {
        if (doluMu()) return false;
        AracDugum yeniDugum = new AracDugum(arac);
        yeniDugum.sonraki = ust;
        ust = yeniDugum;
        boyut++;
        return true;
    }

    public Arac cikar() {
        if (bosMu()) return null;
        Arac cikanArac = ust.arac;
        ust = ust.sonraki;
        boyut--;
        return cikanArac;
    }

    public boolean bosMu() { return boyut == 0; }
    public boolean doluMu() { return boyut >= kapasite; }
    public int getBoyut() { return boyut; }
}