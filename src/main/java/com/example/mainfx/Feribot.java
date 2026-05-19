package com.example.mainfx;

public class Feribot {

    private String seferNo;
    private int feribotNo;

    private double feribotRihtimGirisSaati;
    private double feribotRihtimKalkisSaati;
    private double gercekKalkisSaati;

    private double gecikmeSuresi;
    private String kalkisNedeni = "";

    private boolean feribotKalkisaHazirMi;

    private Yigin altKat;
    private Yigin ustKat;

    public Feribot(String seferNo, int feribotNo, double girisSaati, double kalkisSaati) {
        this.seferNo = seferNo;
        this.feribotNo = feribotNo;
        this.feribotRihtimGirisSaati = girisSaati;
        this.feribotRihtimKalkisSaati = kalkisSaati;
        this.feribotKalkisaHazirMi = false;


        this.altKat = new Yigin(5);
        this.ustKat = new Yigin(5);
    }

    public void kalkisDurumuGuncelle(double guncelSaat) {

        double alt = (altKat.getBoyut() / 5.0) * 100;
        double ust = (ustKat.getBoyut() / 5.0) * 100;


        boolean sart1 = (guncelSaat >= feribotRihtimKalkisSaati) && (alt >= 50 && ust >= 50);
        boolean sart2 = (guncelSaat >= feribotRihtimKalkisSaati) && (alt == 100 || ust == 100);
        boolean sart3 = (alt == 100 && ust == 100);

        if (sart1 || sart2 || sart3) {
            feribotKalkisaHazirMi = true;


            gercekKalkisSaati = guncelSaat;


            gecikmeSuresi = Math.max(0, gercekKalkisSaati - feribotRihtimKalkisSaati);

        }
    }



    public String getSeferNo() { return seferNo; }
    public void setSeferNo(String seferNo) { this.seferNo = seferNo; }

    public int getFeribotNo() { return feribotNo; }
    public void setFeribotNo(int feribotNo) { this.feribotNo = feribotNo; }

    public double getFeribotRihtimGirisSaati() { return feribotRihtimGirisSaati; }
    public void setFeribotRihtimGirisSaati(double feribotRihtimGirisSaati) { this.feribotRihtimGirisSaati = feribotRihtimGirisSaati; }

    public double getFeribotRihtimKalkisSaati() { return feribotRihtimKalkisSaati; }
    public void setFeribotRihtimKalkisSaati(double feribotRihtimKalkisSaati) { this.feribotRihtimKalkisSaati = feribotRihtimKalkisSaati; }

    public double getGercekKalkisSaati() { return gercekKalkisSaati; }
    public void setGercekKalkisSaati(double gercekKalkisSaati) { this.gercekKalkisSaati = gercekKalkisSaati; }

    public double getGecikmeSuresi() { return gecikmeSuresi; }
    public void setGecikmeSuresi(double gecikmeSuresi) { this.gecikmeSuresi = gecikmeSuresi; }

    public String getKalkisNedeni() { return kalkisNedeni; }
    public void setKalkisNedeni(String kalkisNedeni) { this.kalkisNedeni = kalkisNedeni; }

    public boolean isFeribotKalkisaHazirMi() { return feribotKalkisaHazirMi; }
    public void setFeribotKalkisaHazirMi(boolean feribotKalkisaHazirMi) { this.feribotKalkisaHazirMi = feribotKalkisaHazirMi; }

    public Yigin getAltKat() { return altKat; }
    public void setAltKat(Yigin altKat) { this.altKat = altKat; }

    public Yigin getUstKat() { return ustKat; }
    public void setUstKat(Yigin ustKat) { this.ustKat = ustKat; }
}