package com.example.mainfx;

public class Arac {
    String seferNo;
    int aracNo;
    String aracPlaka;
    double giseGirisSaati;
    int aracTipi;

    public Arac(String aracPlaka, double giseGirisSaati, int aracTipi) {
        this.aracPlaka = aracPlaka;
        this.giseGirisSaati = giseGirisSaati;
        this.aracTipi = aracTipi;
    }

    @Override
    public String toString() {
        return aracPlaka + " (Tip " + aracTipi + ")";
    }
}