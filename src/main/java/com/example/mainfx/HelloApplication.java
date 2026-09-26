package com.example.mainfx;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class HelloApplication extends Application {


    Kuyruk yol1 = new Kuyruk();
    Kuyruk yol2 = new Kuyruk();
    HashTablosu plakaKayitlari = new HashTablosu();
    Feribot[] feribotlar = new Feribot[10];
    int feribotSayisi = 0;
    int siradakiFeribotIndex = 0;
    int globalAracNo = 1;
    boolean siraYol1de = true;
    boolean siraYol1deYukle = true;
    double simulasyonSaati = 9.00;

    Arac[] tumAraclar = new Arac[200];
    int toplamAracSayisi = 0;

    private Image imgCar, imgTruck, imgFerry, imgRihtim, imgOkyanus;
    private boolean assetsLoaded = false;


    TextArea txtSistemLog = new TextArea();
    Label lblDurum = new Label("⚓ Sistem Hazır. Bekleniyor...");
    Label lblFeribotBilgi = new Label("🚢 Sıradaki Feribot: Bekleniyor...");

    ProgressBar barAltKat = new ProgressBar(0);
    ProgressBar barUstKat = new ProgressBar(0);
    Label lblAltOran = new Label("%0");
    Label lblUstOran = new Label("%0");


    Pane oyunAlani = new Pane();
    Pane gemiKonteyner = new Pane();
    Pane animasyonPaneli = new Pane();

    HBox uiYol1 = new HBox(12);
    HBox uiYol2 = new HBox(12);
    HBox paneUstKatGaraj = new HBox(5);
    HBox paneAltKatGaraj = new HBox(5);

    DropShadow dsArac = new DropShadow(6, 2, 2, Color.color(0,0,0,0.6));

    @Override
    public void start(Stage stage) {

        assetsYukle();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef2f5;");

        DropShadow golge = new DropShadow();
        golge.setRadius(10);
        golge.setOffsetX(0);
        golge.setOffsetY(5);
        golge.setColor(Color.color(0, 0, 0, 0.15));


        VBox ustPanel = new VBox(15);
        ustPanel.setPadding(new Insets(20));
        ustPanel.setStyle("-fx-background-color: white; -fx-background-radius: 0 0 20 20;");
        ustPanel.setEffect(golge);
        ustPanel.setAlignment(Pos.CENTER);

        Label baslik = new Label("⛴ LİMAN OTOMASYON SİSTEMİ ⛴");
        baslik.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 26));
        baslik.setTextFill(Color.web("#2c3e50"));

        HBox kontrolKutusu = new HBox(15);
        kontrolKutusu.setAlignment(Pos.CENTER);

        TextField tPlaka = new TextField();
        tPlaka.setPromptText("Plaka (Örn: 34XYZ12)");
        tPlaka.setStyle("-fx-pref-width: 180; -fx-font-size: 14px; -fx-background-radius: 8;");

        ComboBox<String> cmbTip = new ComboBox<>();
        cmbTip.getItems().addAll("1 - Ağır Vasıta", "2 - Otomobil");
        cmbTip.setValue("2 - Otomobil");
        cmbTip.setStyle("-fx-font-size: 14px; -fx-background-radius: 8;");

        Button btnEkle = new Button("➕ Gişeden Araç Ekle");
        btnEkle.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");

        Button btnIlerle = new Button("▶ Feribota Yükle");
        btnIlerle.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");

        kontrolKutusu.getChildren().addAll(tPlaka, cmbTip, btnEkle, btnIlerle);
        ustPanel.getChildren().addAll(baslik, kontrolKutusu);


        oyunAlani.setPrefSize(1250, 650);
        oyunAlani.setStyle("-fx-background-color: #1e90ff;");


        if (imgOkyanus != null) {
            ImageView viewOkyanus = new ImageView(imgOkyanus);
            viewOkyanus.setFitWidth(1350); viewOkyanus.setFitHeight(750);
            oyunAlani.getChildren().add(viewOkyanus);
        }


        if (imgRihtim != null) {
            ImageView viewRihtim = new ImageView(imgRihtim);
            viewRihtim.setFitWidth(900);
            viewRihtim.setPreserveRatio(true);
            viewRihtim.setLayoutX(20);
            viewRihtim.setLayoutY(0);
            oyunAlani.getChildren().add(viewRihtim);
        }

        Rotate sabitAci = new Rotate(27, 0, 0);

        uiYol1.setLayoutX(350);
        uiYol1.setLayoutY(230);
        uiYol1.getTransforms().add(sabitAci);

        uiYol2.setLayoutX(300);
        uiYol2.setLayoutY(300);
        uiYol2.getTransforms().add(sabitAci);


        gemiKonteyner.setLayoutX(570);
        gemiKonteyner.setLayoutY(150);
        if (imgFerry != null) {
            ImageView viewFeribot = new ImageView(imgFerry);
            viewFeribot.setFitWidth(450); viewFeribot.setPreserveRatio(true);
            gemiKonteyner.getChildren().add(viewFeribot);
        }


        paneUstKatGaraj.setOpacity(0); paneAltKatGaraj.setOpacity(0);
        Rotate gemiAcisi = new Rotate(-16, 0, 0);
        paneUstKatGaraj.setLayoutX(90); paneUstKatGaraj.setLayoutY(175);
        paneUstKatGaraj.getTransforms().add(gemiAcisi);
        paneAltKatGaraj.setLayoutX(70); paneAltKatGaraj.setLayoutY(235);
        paneAltKatGaraj.getTransforms().add(gemiAcisi);

        VBox infoPanel = new VBox(5);
        infoPanel.setLayoutX(170); infoPanel.setLayoutY(-30);
        infoPanel.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 8; -fx-padding: 8;");
        lblFeribotBilgi.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        barUstKat.setPrefWidth(90); barAltKat.setPrefWidth(90);
        infoPanel.getChildren().addAll(lblFeribotBilgi,
                new HBox(5, new Label("Üst:"), barUstKat, lblUstOran),
                new HBox(5, new Label("Alt:"), barAltKat, lblAltOran));

        gemiKonteyner.getChildren().addAll(paneAltKatGaraj, paneUstKatGaraj, infoPanel);
        animasyonPaneli.setMouseTransparent(true);
        oyunAlani.getChildren().addAll(uiYol1, uiYol2, gemiKonteyner, animasyonPaneli);


        VBox altPanel = new VBox(8);
        altPanel.setPadding(new Insets(15));
        txtSistemLog.setPrefHeight(180);
        txtSistemLog.setStyle("-fx-font-family: 'Consolas'; -fx-control-inner-background: #1e272e; -fx-text-fill: #0be881;");
        altPanel.getChildren().addAll(lblDurum, txtSistemLog);

        root.setTop(ustPanel);
        root.setCenter(oyunAlani);
        root.setBottom(altPanel);


        btnEkle.setOnAction(e -> {
            String plaka = tPlaka.getText().trim().toUpperCase();
            if (plaka.isEmpty()) {
                txtSistemLog.appendText("❌ Plaka boş bırakılamaz!\n");
                return;
            }

            if (plakaKayitlari.plakaVarMi(plaka)) {
                txtSistemLog.appendText("❌ Bu plaka zaten kayıtlı!\n");
                return;
            }

            int tip = cmbTip.getValue().startsWith("1") ? 1 : 2;
            simulasyonSaati = saatIlerlet(simulasyonSaati, 10);

            Arac a = new Arac(plaka, simulasyonSaati, tip);
            a.aracNo = globalAracNo++;
            plakaKayitlari.plakaEkle(plaka);
            araciDosyayaKaydet(a);
            tumAraclar[toplamAracSayisi++] = a;

            Node gorsel = aracGorseliOlustur(a);


            if (siraYol1de) { yol1.ekle(a); uiYol1.getChildren().add(0, gorsel); }
            else { yol2.ekle(a); uiYol2.getChildren().add(0, gorsel); }
            siraYol1de = !siraYol1de;

            yollariGorselGuncelle();

            logYaz("✅ GİŞE: " + plaka + " limana giriş yaptı. (Saat: " + String.format(Locale.US, "%.2f", simulasyonSaati) + ")");
            tPlaka.clear();
        });

        btnIlerle.setOnAction(e -> simulasyonuCalistir());

        verileriYukle();
        gemiBilgisiniGuncelle();

        stage.setScene(new Scene(root, 1250, 900));
        stage.setTitle("Liman & Feribot ");
        stage.show();
    }


    private void yollariGorselGuncelle() {
        guncelleYolPaneli(uiYol1);
        guncelleYolPaneli(uiYol2);
    }

    private void guncelleYolPaneli(HBox yolUi) {
        int size = yolUi.getChildren().size();
        for (int i = 0; i < size; i++) {
            Node n = yolUi.getChildren().get(i);

            if (i >= size - 4) {
                n.setVisible(true);
                n.setManaged(true);
            } else {
                n.setVisible(false);
                n.setManaged(false);
            }
        }
    }

    private void assetsYukle() {
        try {
            imgCar = new Image(new FileInputStream("araba.png"));
            imgTruck = new Image(new FileInputStream("tır.png"));
            imgFerry = new Image(new FileInputStream("feribot.png"));
            imgRihtim = new Image(new FileInputStream("rıhtım.png"));
            imgOkyanus = new Image(new FileInputStream("okyanus.png"));
            assetsLoaded = true;
        } catch (Exception e) { assetsLoaded = false; }
    }

    private Node aracGorseliOlustur(Arac a) {
        if (assetsLoaded) {
            ImageView iv = new ImageView(a.aracTipi == 1 ? imgTruck : imgCar);
            if (a.aracTipi == 1) { iv.setFitWidth(65); iv.setFitHeight(30); }
            else { iv.setFitWidth(45); iv.setFitHeight(28); }
            iv.setPreserveRatio(true);
            iv.setEffect(dsArac);
            Label lbl = new Label(a.aracPlaka);
            lbl.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-font-size: 8px; -fx-font-weight: bold; -fx-text-fill: black;");
            VBox vb = new VBox(1, iv, lbl); vb.setAlignment(Pos.CENTER);
            return vb;
        } else {
            String ikon = (a.aracTipi == 1) ? "🚛" : "🚗";
            Label lbl = new Label(ikon + "\n" + a.aracPlaka);
            lbl.setAlignment(Pos.CENTER);
            lbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
            lbl.setPrefSize(70, 40);
            String bgColor = (a.aracTipi == 1) ? "#e67e22" : "#f1c40f";
            lbl.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 5; -fx-text-fill: black;");
            return lbl;
        }
    }

    private void simulasyonuCalistir() {
        if (siradakiFeribotIndex >= feribotSayisi) {
            logYaz("⛔ SİMÜLASYON BİTTİ: Tüm feribotlar seferini tamamladı.");
            lblFeribotBilgi.setText("TÜM SEFERLER TAMAMLANDI");

            logYaz("\n=== TÜM ARAÇLARIN SEFER VE BİNİŞ DURUMU ===");
            logYaz("Sefer No\tAraç No\t\tPlaka\t\tAraç Tipi");
            for (int i = 0; i < toplamAracSayisi; i++) {
                Arac arac = tumAraclar[i];
                String sefer = (arac.seferNo != null && !arac.seferNo.equals("*")) ? arac.seferNo : "BİNEMEDİ";
                logYaz(sefer + "\t\t" + arac.aracNo + "\t\t" + arac.aracPlaka + "\t\t" + arac.aracTipi);
            }
            logYaz("===========================================\n");
            return;
        }

        Feribot f = feribotlar[siradakiFeribotIndex];

        if (yol1.bosMu() && yol2.bosMu()) {
            logYaz("ℹ️ Rıhtımda bekleyen araç bulunmuyor.");
            return;
        }

        Arac a = null; Node uiNode = null; Point2D startLocal = null;


        if (siraYol1deYukle) {
            if (!yol1.bosMu()) {
                a = yol1.cikar();
                int lastIdx = uiYol1.getChildren().size() - 1;
                uiNode = uiYol1.getChildren().get(lastIdx);
                startLocal = oyunAlani.sceneToLocal(uiNode.localToScene(0, 0));
                uiYol1.getChildren().remove(lastIdx);
            }
            else if (!yol2.bosMu()) {
                a = yol2.cikar();
                int lastIdx = uiYol2.getChildren().size() - 1;
                uiNode = uiYol2.getChildren().get(lastIdx);
                startLocal = oyunAlani.sceneToLocal(uiNode.localToScene(0, 0));
                uiYol2.getChildren().remove(lastIdx);
            }
        } else {
            if (!yol2.bosMu()) {
                a = yol2.cikar();
                int lastIdx = uiYol2.getChildren().size() - 1;
                uiNode = uiYol2.getChildren().get(lastIdx);
                startLocal = oyunAlani.sceneToLocal(uiNode.localToScene(0, 0));
                uiYol2.getChildren().remove(lastIdx);
            }
            else if (!yol1.bosMu()) {
                a = yol1.cikar();
                int lastIdx = uiYol1.getChildren().size() - 1;
                uiNode = uiYol1.getChildren().get(lastIdx);
                startLocal = oyunAlani.sceneToLocal(uiNode.localToScene(0, 0));
                uiYol1.getChildren().remove(lastIdx);
            }
        }

        siraYol1deYukle = !siraYol1deYukle;

        yollariGorselGuncelle();

        if (a != null && startLocal != null) {
            gorselAnimasyonOynat(a, f, startLocal);
        }
    }

    private void gorselAnimasyonOynat(Arac a, Feribot f, Point2D startLocal) {
        Node animNode = aracGorseliOlustur(a);
        animasyonPaneli.getChildren().add(animNode);
        animNode.setLayoutX(startLocal.getX());
        animNode.setLayoutY(startLocal.getY());

        HBox hedefGaraj = (a.aracTipi == 1) ? paneAltKatGaraj : paneUstKatGaraj;
        Point2D endLocal = oyunAlani.sceneToLocal(hedefGaraj.localToScene(0, 0));

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.7), animNode);

        tt.setToX(endLocal.getX() - startLocal.getX() + 60);
        tt.setToY(endLocal.getY() - startLocal.getY() + 10);

        tt.setOnFinished(e -> {
            animasyonPaneli.getChildren().remove(animNode);
            araciFeribotaYerlestir(a, f);
        });
        tt.play();
    }

    private void araciFeribotaYerlestir(Arac a, Feribot f) {
        boolean yuklendi = false;
        if (a.aracTipi == 1 && !f.getAltKat().doluMu()) {
            f.getAltKat().ekle(a);
            paneAltKatGaraj.getChildren().add(aracGorseliOlustur(a));
            yuklendi = true;
        } else if (a.aracTipi == 2 && !f.getUstKat().doluMu()) {
            f.getUstKat().ekle(a);
            paneUstKatGaraj.getChildren().add(aracGorseliOlustur(a));
            yuklendi = true;
        }

        if (yuklendi) {
            a.seferNo = f.getSeferNo();
            logYaz(">> " + a.aracPlaka + " gemiye yüklendi. (Giriş Saati: "
                    + String.format(Locale.US, "%.2f", a.giseGirisSaati) + ")");
            f.kalkisDurumuGuncelle(a.giseGirisSaati);
            kapasiteBarlariniGuncelle(f);
        } else {
            logYaz(">> ⚠️ " + a.aracPlaka + " bindiği kat dolu olduğu için kuyruğa geri döndü.");

            a.giseGirisSaati = saatIlerlet(simulasyonSaati, 5);

            Node geriGidenArac = aracGorseliOlustur(a);
            if (siraYol1deYukle) { yol1.ekle(a); uiYol1.getChildren().add(geriGidenArac); }
            else { yol2.ekle(a); uiYol2.getChildren().add(geriGidenArac); }
            yollariGorselGuncelle();
        }

        if (f.isFeribotKalkisaHazirMi()) {
            feribotKalkisAnimasyonuBaslat(f);
        }
    }

    private void feribotKalkisAnimasyonuBaslat(Feribot f) {
        logYaz("\n🚢 === FERİBOT KALKIŞ YAPIYOR === 🚢");
        TranslateTransition kalkis = new TranslateTransition(Duration.seconds(4), gemiKonteyner);
        kalkis.setByX(900);
        kalkis.setOnFinished(e -> {
            feribotRaporYaz(f);
            siradakiFeribotIndex++;
            gemiBilgisiniGuncelle();
            paneAltKatGaraj.getChildren().clear();
            paneUstKatGaraj.getChildren().clear();
            gemiKonteyner.setTranslateX(0);
            barAltKat.setProgress(0); barUstKat.setProgress(0);
            lblAltOran.setText("%0"); lblUstOran.setText("%0");
        });
        kalkis.play();
    }

    private void kapasiteBarlariniGuncelle(Feribot f) {
        double altOran = f.getAltKat().getBoyut() / 5.0;
        double ustOran = f.getUstKat().getBoyut() / 5.0;

        barAltKat.setProgress(altOran);
        barUstKat.setProgress(ustOran);

        lblAltOran.setText("%" + (int)(altOran * 100));
        lblUstOran.setText("%" + (int)(ustOran * 100));
    }

    private void gemiBilgisiniGuncelle() {
        if (siradakiFeribotIndex < feribotSayisi) {
            Feribot f = feribotlar[siradakiFeribotIndex];
            lblFeribotBilgi.setText(" Feribot: " + f.getSeferNo() + " (Planlanan Kalkış: " + f.getFeribotRihtimKalkisSaati() + ")");
        }
    }

    private void feribotRaporYaz(Feribot f) {
        logYaz("\nSefer No = " + f.getSeferNo());
        logYaz("Feribot No = " + f.getFeribotNo());
        logYaz(String.format(Locale.US, "Kalkış Saati = %.2f", f.getFeribotRihtimKalkisSaati()));
        logYaz(String.format(Locale.US, "Gerçekleşen Kalkış Saati = %.2f", f.getGercekKalkisSaati()));
        logYaz(String.format(Locale.US, "Gecikme Süresi = %.2f", f.getGecikmeSuresi()));

        logYaz("\nGiriş Kat Araç Bilgileri");
        katIcerigiYazdir(f.getAltKat());

        logYaz("\nÜst Kat Araç Bilgileri");
        katIcerigiYazdir(f.getUstKat());
        logYaz("---------------------------------\n");
    }

    private void katIcerigiYazdir(Yigin y) {
        if (y.bosMu()) {
            logYaz("Bu kat boş.");
            return;
        }
        Yigin gecici = new Yigin(5);
        while (!y.bosMu()) { gecici.ekle(y.cikar()); }

        int alanNo = 1;
        while (!gecici.bosMu()) {
            Arac a = gecici.cikar();
            logYaz(alanNo + " Nolu Alan = " + a.aracPlaka);
            y.ekle(a);
            alanNo++;
        }
    }

    private double saatIlerlet(double suAnkiSaat, int dakikaEkle) {
        int saat = (int) suAnkiSaat;
        int dakika = (int) Math.round((suAnkiSaat - saat) * 100);

        dakika += dakikaEkle;

        if (dakika >= 60) {
            saat += (dakika / 60);
            dakika = dakika % 60;
        }

        return saat + (dakika / 100.0);
    }

    private void logYaz(String m) {
        txtSistemLog.appendText(m + "\n");
    }

    private void araciDosyayaKaydet(Arac a) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Arac.txt", true)))) {
            pw.println("*," + a.aracNo + "," + a.aracPlaka + "," + a.giseGirisSaati + "," + a.aracTipi);
        } catch (IOException e) {}
    }

    private void verileriYukle() {
        try {
            Scanner fs = new Scanner(new File("Feribot.txt"));
            while (fs.hasNextLine()) {
                String satir = fs.nextLine().trim();
                if(satir.isEmpty()) continue;
                String[] p = satir.split(",");
                feribotlar[feribotSayisi++] = new Feribot(
                        p[0].replace("\"", "").trim(),
                        Integer.parseInt(p[1].replace("\"", "").trim()),
                        Double.parseDouble(p[2].replace("\"", "").trim()),
                        Double.parseDouble(p[3].replace("\"", "").trim())
                );
            }
            fs.close();

            Scanner as = new Scanner(new File("Arac.txt"));
            Arac[] geciciDizi = new Arac[200];
            int okunanAracSayisi = 0;

            while (as.hasNextLine()) {
                String satir = as.nextLine().trim();
                if(satir.isEmpty()) continue;
                String[] p = satir.split(",");

                if (p.length < 5 || p[0].contains("seferNo") || p[2].contains("*")) continue;

                Arac a = new Arac(
                        p[2].replace("\"", "").trim(),
                        Double.parseDouble(p[3].replace("\"", "").trim()),
                        Integer.parseInt(p[4].replace("\"", "").trim())
                );
                geciciDizi[okunanAracSayisi++] = a;
            }
            as.close();

            for (int i = 0; i < okunanAracSayisi - 1; i++) {
                for (int j = i + 1; j < okunanAracSayisi; j++) {
                    if (geciciDizi[j].giseGirisSaati < geciciDizi[i].giseGirisSaati) {
                        Arac temp = geciciDizi[i];
                        geciciDizi[i] = geciciDizi[j];
                        geciciDizi[j] = temp;
                    }
                }
            }

            if (okunanAracSayisi > 0) {
                simulasyonSaati = geciciDizi[okunanAracSayisi - 1].giseGirisSaati;
            }

            for (int i = 0; i < okunanAracSayisi; i++) {
                Arac a = geciciDizi[i];
                a.aracNo = globalAracNo++;
                plakaKayitlari.plakaEkle(a.aracPlaka);

                tumAraclar[toplamAracSayisi++] = a;

                Node gorselArac = aracGorseliOlustur(a);

                if (siraYol1de) { yol1.ekle(a); uiYol1.getChildren().add(0, gorselArac); }
                else { yol2.ekle(a); uiYol2.getChildren().add(0, gorselArac); }

                siraYol1de = !siraYol1de;
            }

            yollariGorselGuncelle();

            lblDurum.setText("🟢 Veriler başarıyla yüklendi ve saatlerine göre sıralandı.");
        } catch (Exception e) {
            lblDurum.setText("🔴 Uyarı: Txt dosyaları okunamadı, boş sistem başlatıldı.");
            lblDurum.setTextFill(Color.RED);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}