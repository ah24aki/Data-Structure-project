package com.example.mainfx;

public class Kuyruk {
    private AracDugum bas;
    private AracDugum son;

    public Kuyruk() {
        bas = null;
        son = null;
    }

    public void ekle(Arac arac) {
        AracDugum yeniDugum = new AracDugum(arac);
        if (son == null) {
            bas = son = yeniDugum;
            return;
        }
        son.sonraki = yeniDugum;
        son = yeniDugum;
    }

    public Arac cikar() {
        if (bas == null) return null;
        Arac cikanArac = bas.arac;
        bas = bas.sonraki;
        if (bas == null) son = null;
        return cikanArac;
    }

    public boolean bosMu() {
        return bas == null;
    }
}