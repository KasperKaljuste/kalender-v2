package oop;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class Kalender extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) throws Exception {

        List<Event> evendid = new ArrayList<>();

        try { //Tekitab eventide tekstifaili või loeb evendid listi.
            File evendidFail = new File("evendid.txt");
            if (evendidFail.createNewFile()) {
                System.out.println("File tehtud: " + evendidFail.getName());
            } else {
                System.out.println("Fail on olemas, loen evendid programmi");
                FileInputStream evendidInput = new FileInputStream("evendid.txt");
                Scanner sc = new Scanner(evendidInput);    //file to be scanned
                while (sc.hasNextLine()) {
                    String rida = sc.nextLine();
                    String[] tükid = rida.split("; ");
                    String asenda1 = tükid[3].replace("[", "");
                    String asenda2 = asenda1.replace("]", "");
                    ArrayList<String> detailidFailist = new ArrayList<String>(Arrays.asList(asenda2.split(", ")));
                    if (tükid.length == 4) {
                        Event failistLoetudEvent = new Event(tükid[0], tükid[1], tükid[2], detailidFailist);
                        evendid.add(failistLoetudEvent);
                    } else { //meeldetuletus on ka lisaks muule
                        SimpleDateFormat failistFormaat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                        Date meeldetuletusFailist = failistFormaat.parse(tükid[4]);
                        Meeldetuletus failistLoetudEvent = new Meeldetuletus(tükid[0], tükid[1], tükid[2], detailidFailist, meeldetuletusFailist);
                        evendid.add(failistLoetudEvent);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error faili tegemisel");
            e.printStackTrace();
        }


        Group juur = new Group();
        //Nupud
        Button nupp1 = new Button("Lisa sündmus");
        Button nupp3 = new Button("Väljasta sündmused");
        Button nupp4 = new Button("Muuda sündmust");
        Button nupp5 = new Button("Kustuta sündmus");

        //Kell, mis uuendab jooksvalt aega.

        Label clock = new Label(); //https://stackoverflow.com/questions/38566638/javafx-displaying-time-and-refresh-in-every-second/38567319
        DateFormat clockformat = DateFormat.getInstance();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                clockevent -> {
                    Calendar cal = Calendar.getInstance();
                    clock.setText(clockformat.format(cal.getTime()));
                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        clock.setMinHeight(50);
        clock.setMinWidth(50);
        clock.setFont(Font.font(50));

        BorderPane PeaPane1 = new BorderPane();
        PeaPane1.setMinWidth(1000);
        PeaPane1.setMinHeight(400);

        nupp1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        nupp3.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        nupp4.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        nupp5.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        HBox nupud = new HBox(8);
        nupud.setPadding(new Insets(0, 0, 30, 0));
        nupud.getChildren().addAll(nupp1, nupp3, nupp4, nupp5);
        PeaPane1.setCenter(clock);
        nupud.setAlignment(Pos.CENTER);
        PeaPane1.setBottom(nupud);

        Text escapetekst = new Text("Programmi lõpetamiseks vajutage ESC nuppu.");
        VBox escapetekstVBox = new VBox();
        Text meeldetuletustekstx = new Text("");
        //Meeldetuletuse teksti animatsioon
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), meeldetuletustekstx);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
        meeldetuletustekstx.setVisible(false); //Alguses on nähtamatu, õigel hetkel muutub nähtavaks (allpool).
        escapetekstVBox.getChildren().addAll(escapetekst, meeldetuletustekstx);
        escapetekstVBox.setAlignment(Pos.CENTER);

        PeaPane1.setTop(escapetekstVBox);


        juur.getChildren().addAll(PeaPane1);
        Scene stseen1 = new Scene(juur, 1000, 400, Color.SNOW);
        peaLava.setMinHeight(400); //Minimaalne akna suurus, et kõike normaalselt näha oleks.
        peaLava.setMinWidth(1000);
        stseen1.setOnKeyPressed(event -> { //ESC vajutamine paneb programmi kinni.
            if (event.getCode() == KeyCode.ESCAPE) { //https://stackoverflow.com/questions/33224161/how-do-i-run-a-function-on-a-specific-key-press-in-javafx
                System.exit(0);
            }
        });

        stseen1.widthProperty().addListener((observable, oldvalue, newvalue) -> {
                    PeaPane1.setMinWidth((Double) newvalue);
                }
        );
        stseen1.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                    PeaPane1.setMinHeight((Double) newvalue);
                }
        );


        //Loome 2 Event objekti:
        //Event, mille kuupäev ja kellaaeg on praegune kuupäev ja kellaaeg
        //ning Event, mille kuupäev ja kellaaeg on meeldetuletusest saadud.
        Kuupäev täna = new Kuupäev();
        Kell praegu = new Kell();
        String tänanekuupäev = täna.getTäna();
        String[] tänanekuupäevjupid = tänanekuupäev.split(" ");
        String praegunePäev = tänanekuupäevjupid[1];
        String praeguneAasta = tänanekuupäevjupid[3];
        String praeguneKuu = "";
        if (tänanekuupäevjupid[2].equals("jaanuar"))
            praeguneKuu = "01";
        if (tänanekuupäevjupid[2].equals("veebruar"))
            praeguneKuu = "02";
        if (tänanekuupäevjupid[2].equals("märts"))
            praeguneKuu = "03";
        if (tänanekuupäevjupid[2].equals("aprill"))
            praeguneKuu = "04";
        if (tänanekuupäevjupid[2].equals("mai"))
            praeguneKuu = "05";
        if (tänanekuupäevjupid[2].equals("juuni"))
            praeguneKuu = "06";
        if (tänanekuupäevjupid[2].equals("juuli"))
            praeguneKuu = "07";
        if (tänanekuupäevjupid[2].equals("august"))
            praeguneKuu = "08";
        if (tänanekuupäevjupid[2].equals("september"))
            praeguneKuu = "09";
        if (tänanekuupäevjupid[2].equals("oktoober"))
            praeguneKuu = "10";
        if (tänanekuupäevjupid[2].equals("november"))
            praeguneKuu = "11";
        if (tänanekuupäevjupid[2].equals("detsember"))
            praeguneKuu = "12";
        String praeguneKuupäev = praegunePäev + "." + praeguneKuu + "." + praeguneAasta;
        Event praeguneHetk = new Event("", praeguneKuupäev, praegu.getKell(), new ArrayList<>()); //Praeguse hetke Event
        for (Event event : evendid) {
            if (event instanceof Meeldetuletus) { //Kui event on Meeldetuletus objekt, siis tal on meeldetuletuse aeg
                //Muudame kujult Mon Apr 05 17:28:00 EEST 2021 kujule 05.04.2021 ja 17:28
                //Loome nende põhjal Eventi.
                Date evendiMeeldetuletus = ((Meeldetuletus) event).getMeeldetuletuseAeg();
                String evendiMeeldetuletusStringina = evendiMeeldetuletus.toString();
                String[] evendiMeeldetuletusjupid = evendiMeeldetuletusStringina.split(" ");
                String meeldetuletuseAeg = evendiMeeldetuletusjupid[3].substring(0, 5);
                String aasta = evendiMeeldetuletusjupid[5];
                String päev = evendiMeeldetuletusjupid[2];
                String kuu = "00";
                if (evendiMeeldetuletusjupid[1].equals("Jan"))
                    kuu = "01";
                if (evendiMeeldetuletusjupid[1].equals("Feb"))
                    kuu = "02";
                if (evendiMeeldetuletusjupid[1].equals("Mar"))
                    kuu = "03";
                if (evendiMeeldetuletusjupid[1].equals("Apr"))
                    kuu = "04";
                if (evendiMeeldetuletusjupid[1].equals("May"))
                    kuu = "05";
                if (evendiMeeldetuletusjupid[1].equals("Jun"))
                    kuu = "06";
                if (evendiMeeldetuletusjupid[1].equals("Jul"))
                    kuu = "07";
                if (evendiMeeldetuletusjupid[1].equals("Aug"))
                    kuu = "08";
                if (evendiMeeldetuletusjupid[1].equals("Sep"))
                    kuu = "09";
                if (evendiMeeldetuletusjupid[1].equals("Oct"))
                    kuu = "10";
                if (evendiMeeldetuletusjupid[1].equals("Nov"))
                    kuu = "11";
                if (evendiMeeldetuletusjupid[1].equals("Dec"))
                    kuu = "12";

                String meeldetuletuseKuupäev = päev + "." + kuu + "." + aasta;
                Event evendiMeeldetuletusEvendina = new Event("", meeldetuletuseKuupäev, meeldetuletuseAeg, new ArrayList<>()); //Event, kus kuupäeva ja aja asemel on meeldetuletuse kuupäev ja aeg.
                //Neid kahte Eventi saab võrrelda.
                if (evendiMeeldetuletusEvendina.compareTo(praeguneHetk) == 1) { //Kui meeldetuletuse aeg pole veel möödas, loome Timeline.

                    long ajavahe = (evendiMeeldetuletus.getTime() - System.currentTimeMillis()) / 1000; //Aeg praegusest hetkest meeldetuletuseni.
                    Timeline mtimeline = new Timeline(new KeyFrame(Duration.seconds(ajavahe),
                            meeldetuletuse -> {
                                meeldetuletustekstx.setVisible(true); //Meeldetuletuse teksti muudame õigeks ja teeme nähtavaks.
                                meeldetuletustekstx.setFill(Color.CRIMSON);
                                meeldetuletustekstx.setText("Meeldetuletus! " + event);
                            }));

                    mtimeline.setCycleCount(Animation.INDEFINITE);
                    mtimeline.play();
                }
            }
        }


        nupp1.setOnMouseClicked(event -> { //Sündmuse lisamise nupp
            Group lisamisejuur = new Group();
            Text niminimi = new Text("Sisesta ürituse nimi");
            TextField nimifield = new TextField();
            Text kuupäevfieldnimi = new Text("Sisesta kuupäev kujul päev.kuu.aasta");
            TextField kuupäevfield = new TextField();
            Text aegfieldnimi = new Text("Sisesta aeg kujul tunnid:minutid, kui soovite lasta programmil aja valida, sisestage \"suvaline\"");
            TextField aegfield = new TextField();
            Text detailidfieldnimi = new Text("Sisestage detailid (kui neid pole, jätke lahter tühjaks)");
            TextField detailidfield = new TextField();
            Text meeldetuletusfieldnimi = new Text("Sisestage meeldetuletuse aeg formaadis aasta-kuu-päev at tund:minut:sekund (näiteks 2021-12-01 at 12:00:00) (kui meeldetuletust ei taha, jätke lahter tühjaks)");
            TextField meeldetuletusfield = new TextField();
            Button lisamisenupp = new Button("Lisa sündmus");
            nimifield.setAlignment(Pos.CENTER);
            kuupäevfield.setAlignment(Pos.CENTER);
            aegfield.setAlignment(Pos.CENTER);
            detailidfield.setAlignment(Pos.CENTER);
            meeldetuletusfield.setAlignment(Pos.CENTER);
            lisamisenupp.setOnMouseClicked(e -> { //Sündmuse lisamise stseeni lisamise nupp
                String nimi = nimifield.getText();
                String päev = kuupäevfield.getText();
                try{ //Kuupäeva sisestuse kontroll
                    String[] jupid = päev.split("\\.");
                    if(!(jupid.length==3))
                        throw new Exception("Vigane kuupäeva sisestus.");
                    if(Integer.parseInt(jupid[0])>31 ||Integer.parseInt(jupid[0])<1)
                        throw new Exception("Vigane kuupäeva sisestus.");
                    if(Integer.parseInt(jupid[1])>12 ||Integer.parseInt(jupid[1])<1)
                        throw new Exception("Vigane kuupäeva sisestus.");
                    if(Integer.parseInt(jupid[2])<0)
                        throw new Exception("Vigane kuupäeva sisestus.");
                }
                catch(Exception x){
                    x.printStackTrace();
                    System.exit(0);
                }
                String aeg = aegfield.getText();
                if(!(aeg.equals("suvaline"))) {
                    try { //Aja sisestuse kontroll
                        String[] jupid = aeg.split(":");
                        if (!(jupid.length == 2))
                            throw new Exception("Vigane aja sisestus.");
                        if (Integer.parseInt(jupid[0]) > 24 || Integer.parseInt(jupid[0]) < 0)
                            throw new Exception("Vigane aja sisestus.");
                        if (Integer.parseInt(jupid[1]) > 59 || Integer.parseInt(jupid[1]) < 0)
                            throw new Exception("Vigane aja sisestus.");
                    } catch (Exception x) {
                        x.printStackTrace();
                        System.exit(0);
                    }
                }
                if (aeg.equals("suvaline")) {
                    aeg = suvalineAeg();
                }
                String detail = detailidfield.getText();
                String meeldetuletus = meeldetuletusfield.getText();
                ArrayList<String> detailidelist = new ArrayList<String>();
                if (!detail.equals("")) {
                    detailidelist.add(detail);
                }
                if (meeldetuletus.equals("")) {
                    Event uus = new Event(nimi, päev, aeg, detailidelist);
                    evendid.add(uus);
                } else {
                    Date meeldetuletuseAeg = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                    try {
                        meeldetuletuseAeg = format.parse("0000-01-01 at 00:00:00");
                    } catch (ParseException pe) {
                        System.exit(0);
                    }
                    Meeldetuletus uus2 = new Meeldetuletus(nimi, päev, aeg, detailidelist, meeldetuletuseAeg);
                    Date meeldetuletusaeg = new Date();
                    try { //Meeldetuletuse sisestuse kontroll.
                        meeldetuletusaeg = format.parse(meeldetuletus);
                    } catch (ParseException pe) {
                        System.out.println("Vigane meeldetuletuse sisestus.");
                        pe.printStackTrace();
                        System.exit(0);
                    }
                    uus2.setMeeldetuletuseAeg(meeldetuletusaeg);
                    evendid.add(uus2);

                }
                //Kirjutame kohe pärast sündmuse lisamist faili.
                try {
                    FileWriter evendiKirjutaja = new FileWriter("evendid.txt");
                    for (Event event1 : evendid) {
                        evendiKirjutaja.write(event1.toString());
                    }

                    evendiKirjutaja.close();
                    System.out.println("Kirjutasin evendid faili.");
                } catch (IOException IOE) {
                    System.out.println("Error faili kirjutamisel.");
                    IOE.printStackTrace();
                }
                peaLava.setScene(stseen1);
            });
            VBox lisamiseVBox = new VBox(8);
            Text lisamiseescapetekst = new Text("Tagasi minemiseks vajutage ESC.");

            lisamiseVBox.getChildren().addAll(lisamiseescapetekst, new Text(), niminimi, nimifield, kuupäevfieldnimi, kuupäevfield, aegfieldnimi, aegfield, detailidfieldnimi, detailidfield, meeldetuletusfieldnimi, meeldetuletusfield, lisamisenupp);


            BorderPane lisamisepane = new BorderPane();
            lisamisepane.setMinWidth(1000);
            lisamisepane.setMinHeight(400);
            lisamiseVBox.setAlignment(Pos.CENTER);
            lisamisepane.setCenter(lisamiseVBox);
            lisamisejuur.getChildren().add(lisamisepane);

            Scene lisamisestseen = new Scene(lisamisejuur, 1000, 400, Color.SNOW);
            lisamisestseen.widthProperty().addListener((observable, oldvalue, newvalue) -> {
                        lisamisepane.setMinWidth((Double) newvalue);
                    }
            );

            lisamisestseen.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                        lisamisepane.setMinHeight((Double) newvalue);
                    }
            );
            lisamisestseen.setOnKeyPressed(xy -> { //ESC vajutamine viib tagasi peastseeni.
                if (xy.getCode() == KeyCode.ESCAPE) {
                    peaLava.setScene(stseen1);
                }
            });
            peaLava.setScene(lisamisestseen);
        });

        nupp3.setOnMouseClicked(event -> { //Väljastamise nupp
            Collections.sort(evendid);
            Group vjuur = new Group();
            BorderPane vpane = new BorderPane();
            vpane.setMinWidth(1000);
            vpane.setMinHeight(400);

            VBox vVBox = new VBox(8);
            double fontsuurus = (vpane.getHeight() - 100) / evendid.size(); //Fonti suurus sõltub eventide hulgast.
            for (int i = 0; i < evendid.size(); i++) {
                Text t = new Text("" + evendid.get(i));
                t.setFont(new Font(fontsuurus));
                vVBox.getChildren().add(t);
            }
            Button tagasi = new Button("Tagasi");
            tagasi.setOnMouseClicked(e -> peaLava.setScene(stseen1));
            vVBox.getChildren().add(tagasi);
            vVBox.setAlignment(Pos.CENTER);
            vpane.setCenter(vVBox);

            vjuur.getChildren().add(vpane);

            Scene väljastus = new Scene(vjuur, 1000, 400, Color.SNOW);

            väljastus.widthProperty().addListener((observable, oldvalue, newvalue) -> {
                        vVBox.setMinWidth((Double) newvalue);
                    }
            );
            väljastus.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                        vVBox.setMinHeight((Double) newvalue);
                    }
            );

            peaLava.setScene(väljastus);
        });
        nupp4.setOnMouseClicked(event -> { //Muutmise nupp
            Collections.sort(evendid);
            Group mjuur = new Group();
            BorderPane mpane = new BorderPane();
            mpane.setMinWidth(1000);
            mpane.setMinHeight(400);

            VBox mVBox = new VBox(8);
            mVBox.getChildren().add(new Text("Sündmuse muutmiseks muutke teksti vastavas lahtris."));
            mVBox.getChildren().add(new Text("Formaadi näide meeldetuletuseta: kontsert; 03.05.2020; 0:35; [müts]"));
            mVBox.getChildren().add(new Text("Formaadi näide meeldetuletusega: kontsert; 03.05.2020; 0:35; [müts]; Mon May 10 19:20:00 EEST 2021"));
            double fontsuurus = (mpane.getHeight() - 100) / evendid.size();
            for (int i = 0; i < evendid.size(); i++) {
                TextField t = new TextField("" + evendid.get(i));
                t.setFont(new Font(fontsuurus));
                t.setAlignment(Pos.CENTER);
                mVBox.getChildren().add(t);
            }
            Button salvesta = new Button("Salvesta ja tagasi");
            salvesta.setOnMouseClicked(e -> {
                //Kirjutame faili.
                try (FileWriter evendiKirjutaja = new FileWriter("evendid.txt");) {
                    for (Node child : mVBox.getChildren()) {
                        if (child instanceof TextField) {

                            System.out.println(((TextField) child).getText());
                            String evenditekst = ((TextField) child).getText();
                            evendiKirjutaja.write(evenditekst + System.lineSeparator());
                        }
                    }

                } catch (IOException IOE) {
                    IOE.printStackTrace();
                }
                evendid.clear(); //Teeme listi tühjaks ja loeme uuesti failist sisse.
                try {
                    File evendidFail = new File("evendid.txt");
                    if (evendidFail.createNewFile()) {
                        System.out.println("File tehtud: " + evendidFail.getName());
                    } else {
                        System.out.println("Fail on olemas, loen evendid programmi");
                        FileInputStream evendidInput = new FileInputStream("evendid.txt");
                        Scanner sc = new Scanner(evendidInput);    //file to be scanned
                        while (sc.hasNextLine()) {
                            String rida = sc.nextLine();
                            String[] tükid = rida.split("; ");
                            String asenda1 = tükid[3].replace("[", "");
                            String asenda2 = asenda1.replace("]", "");
                            ArrayList<String> detailidFailist = new ArrayList<String>(Arrays.asList(asenda2.split(", ")));
                            if (tükid.length == 4) {
                                Event failistLoetudEvent = new Event(tükid[0], tükid[1], tükid[2], detailidFailist);
                                evendid.add(failistLoetudEvent);
                            } else { //meeldetuletus on ka lisaks muule
                                SimpleDateFormat failistFormaat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                Date meeldetuletusFailist = failistFormaat.parse(tükid[4]);
                                Meeldetuletus failistLoetudEvent = new Meeldetuletus(tükid[0], tükid[1], tükid[2], detailidFailist, meeldetuletusFailist);
                                evendid.add(failistLoetudEvent);
                            }
                        }
                    }
                } catch (IOException | ParseException a) {
                    System.out.println("Error faili tegemisel");
                    a.printStackTrace();
                }
                peaLava.setScene(stseen1);
            });
            mVBox.getChildren().add(salvesta);
            mVBox.setAlignment(Pos.CENTER);
            mpane.setCenter(mVBox);

            mjuur.getChildren().add(mpane);

            Scene muuda = new Scene(mjuur, 1000, 400, Color.SNOW);

            muuda.widthProperty().addListener((observable, oldvalue, newvalue) -> {
                        mVBox.setMinWidth((Double) newvalue);
                    }
            );
            muuda.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                        mVBox.setMinHeight((Double) newvalue);
                    }
            );
            peaLava.setScene(muuda);
        });

        nupp5.setOnMouseClicked(event -> { //Kustutamise nupp
            Collections.sort(evendid);
            Group kjuur = new Group();
            BorderPane kpane = new BorderPane();
            kpane.setMinWidth(1000);
            kpane.setMinHeight(400);

            VBox kVBox = new VBox(8);
            kVBox.getChildren().add(new Text("Sündmuse kustutamiseks vajutage selle peale."));
            double fontsuurus = (kpane.getHeight() - 100) / evendid.size();
            for (int i = 0; i < evendid.size(); i++) {
                Button t = new Button("" + evendid.get(i));
                Event x = evendid.get(i);
                t.setFont(new Font(fontsuurus));
                t.setOnMouseClicked(event1 -> { //Kustutame listist ja muudame nupu nähtamatuks.
                    evendid.remove(x);

                    t.setVisible(false);
                });
                kVBox.getChildren().add(t);
            }
            Button tagasi = new Button("Tagasi");
            tagasi.setOnMouseClicked(e -> { //Tagasi nupp salvestab evendid faili ja viib peastseeni tagasi.
                try {
                    FileWriter evendiKirjutaja = new FileWriter("evendid.txt");
                    for (Event event1 : evendid) {
                        evendiKirjutaja.write(event1.toString());
                    }

                    evendiKirjutaja.close();
                    System.out.println("Kirjutasin evendid faili.");
                } catch (IOException IOE) {
                    System.out.println("Error faili kirjutamisel.");
                    IOE.printStackTrace();
                }
                peaLava.setScene(stseen1);
            });
            kVBox.getChildren().add(tagasi);
            kVBox.setAlignment(Pos.CENTER);
            kpane.setCenter(kVBox);

            kjuur.getChildren().add(kpane);

            Scene kustuta = new Scene(kjuur, 1000, 400, Color.SNOW);

            kustuta.widthProperty().addListener((observable, oldvalue, newvalue) -> {
                        kVBox.setMinWidth((Double) newvalue);
                    }
            );
            kustuta.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                        kVBox.setMinHeight((Double) newvalue);
                    }
            );

            peaLava.setScene(kustuta);
        });

        peaLava.setTitle("Kalender");
        peaLava.setScene(stseen1);
        peaLava.show();











    }

    public static String suvalineAeg() { //Tagastab suvalise kellaaja.
        int tund = (int) (Math.random() * 25);
        int minut = (int) (Math.random() * 61);
        String tagastatav = tund + ":" + minut;
        return tagastatav;
    }
}

