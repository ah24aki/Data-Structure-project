package com.example.mainfx;

public class HashTablosu {
    private PlakaDugum[] tablo;
    private final int KAPASITE = 50;

    public HashTablosu() {

        tablo = new PlakaDugum[KAPASITE];
    }

    private int hashKoduUret(String plaka) {
        int hash = 0;
        for (int i = 0; i < plaka.length(); i++) {
            hash = (hash * 31 + plaka.charAt(i)) % KAPASITE;
        }
        return Math.abs(hash);
    }

    public boolean plakaVarMi(String plaka) {
        int index = hashKoduUret(plaka);
        PlakaDugum mevcut = tablo[index];

        while (mevcut != null) {
            if (mevcut.plaka.equals(plaka)) return true;
            mevcut = mevcut.sonraki;
        }

        return false;
    }

    public void plakaEkle(String plaka) {
        if (plakaVarMi(plaka)) return;

        int index = hashKoduUret(plaka);

        PlakaDugum yeni = new PlakaDugum(plaka);
        yeni.sonraki = tablo[index];
        tablo[index] = yeni;
    }
}