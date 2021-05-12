package oop;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import java.time.LocalTime;
import java.util.*;

import static javafx.application.Application.launch;


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

        Button nupp1 = new Button("Lisa sündmus");
        Button nupp3 = new Button("Väljasta sündmused");
        Button nupp4 = new Button("Muuda sündmust");
        Button nupp5 = new Button("Kustuta sündmus");


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
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), meeldetuletustekstx);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
        meeldetuletustekstx.setVisible(false);
        escapetekstVBox.getChildren().addAll(escapetekst, meeldetuletustekstx);
        escapetekstVBox.setAlignment(Pos.CENTER);

        PeaPane1.setTop(escapetekstVBox);


        juur.getChildren().addAll(PeaPane1);
        Scene stseen1 = new Scene(juur, 1000, 400, Color.SNOW);
        peaLava.setMinHeight(400);
        peaLava.setMinWidth(1000);
        stseen1.setOnKeyPressed(event -> {
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
                if (evendiMeeldetuletusEvendina.compareTo(praeguneHetk) == 1) { //Kui meeldetuletuse aeg pole veel möödas, loome timeri.

                    long ajavahe = (evendiMeeldetuletus.getTime() - System.currentTimeMillis()) / 1000;
                    Timeline mtimeline = new Timeline(new KeyFrame(Duration.seconds(ajavahe),
                            meeldetuletuse -> {
                                meeldetuletustekstx.setVisible(true);
                                meeldetuletustekstx.setFill(Color.CRIMSON);
                                meeldetuletustekstx.setText("Meeldetuletus! " + event);
                            }));

                    mtimeline.setCycleCount(Animation.INDEFINITE);
                    mtimeline.play();
                }
            }
        }


        nupp1.setOnMouseClicked(event -> {
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
            lisamisenupp.setOnMouseClicked(e -> {
                String nimi = nimifield.getText();
                String päev = kuupäevfield.getText();
                try{
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
                    try {
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
                    try {
                        meeldetuletusaeg = format.parse(meeldetuletus);
                    } catch (ParseException pe) {
                        System.out.println("Vigane meeldetuletuse sisestus.");
                        pe.printStackTrace();
                        System.exit(0);
                    }
                    uus2.setMeeldetuletuseAeg(meeldetuletusaeg);
                    evendid.add(uus2);

                }
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
                        //lisamisepane.setMinWidth((Double) newvalue);
                    }
            );

            lisamisestseen.heightProperty().addListener((observable, oldvalue, newvalue) -> {
                        lisamisepane.setMinHeight((Double) newvalue);
                        /*Double muutus = (Double)newvalue/(Double)oldvalue;
                        nimifield.setMinHeight(nimifield.getHeight() * muutus);
                        kuupäevfield.setMinHeight(kuupäevfield.getHeight() * muutus);
                        aegfield.setMinHeight(aegfield.getHeight() * muutus);
                        detailidfield.setMinHeight(detailidfield.getHeight() * muutus);
                        meeldetuletusfield.setMinHeight(meeldetuletusfield.getHeight() * muutus);
                        lisamisenupp.setMinHeight(lisamisenupp.getHeight() * muutus);
                        //Buggib
                        */
                    }
            );
            lisamisestseen.setOnKeyPressed(xy -> {
                if (xy.getCode() == KeyCode.ESCAPE) {
                    peaLava.setScene(stseen1);
                }
            });
            peaLava.setScene(lisamisestseen);
        });

        nupp3.setOnMouseClicked(event -> {
            Collections.sort(evendid);
            Group vjuur = new Group();
            BorderPane vpane = new BorderPane();
            vpane.setMinWidth(1000);
            vpane.setMinHeight(400);

            VBox vVBox = new VBox(8);
            double fontsuurus = (vpane.getHeight() - 100) / evendid.size();
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
        nupp4.setOnMouseClicked(event -> {
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
                int i = 0;
                try (FileWriter evendiKirjutaja = new FileWriter("evendid.txt");) {
                    for (Node child : mVBox.getChildren()) {
                        if (child instanceof TextField) {

                            System.out.println(((TextField) child).getText());
                            String evenditekst = ((TextField) child).getText();
                            evendiKirjutaja.write(evenditekst + System.lineSeparator());
                        }
                    }
                    i++;
                } catch (IOException IOE) {
                    IOE.printStackTrace();
                }
                evendid.clear();
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

        nupp5.setOnMouseClicked(event -> {
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
                t.setOnMouseClicked(event1 -> {
                    evendid.remove(x);

                    t.setVisible(false);
                });
                kVBox.getChildren().add(t);
            }
            Button tagasi = new Button("Tagasi");
            tagasi.setOnMouseClicked(e -> {
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

    public static void meetod() throws Exception {
        Kell kell = new Kell();
        Kuupäev kuupäev = new Kuupäev();
        String state = new String();
        state = ""; //Algne state on tühi ehk näitab kuupäeva
        Scanner scanner = new Scanner(System.in);
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


        System.out.println("KALENDER");
        System.out.println("Autorid: Kasper Kaljuste ja Karl-Magnus Laikoja");
        System.out.println("Juhised:");
        System.out.println("Praeguse kuupäeva väljastamiseks sisestage \"kuupäev\"");
        System.out.println("Praeguse kellaaja väljastamiseks sisestage \"kell\"");
        System.out.println("Sündmuse lisamise alustamiseks sisestage \"lisa\"");
        System.out.println("Sündmuste salvestamiseks sisestage \"salvesta\"");
        System.out.println("Sündmuste väljastamiseks sisestage \"väljasta\"");
        System.out.println("Sündmuse muutmise alustamiseks sisestage \"muuda\"");
        System.out.println("Sündmuse kustutamiseks sisestage \"kustuta\"");


        while (true) {

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
                    if (evendiMeeldetuletusEvendina.compareTo(praeguneHetk) == 1) { //Kui meeldetuletuse aeg pole veel möödas, loome timeri.
                        Timer timer = new Timer();
                        Date aeg = ((Meeldetuletus) event).getMeeldetuletuseAeg();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                System.out.println("MEELDETULETUS! " + event.toString());
                                cancel();
                            }
                        }, aeg);
                    }
                }
            }


            if (state.equals("") || state.equals("kuupäev")) {
                System.out.print("\r" + kuupäev.getTäna());
                System.out.println();
                state = scanner.nextLine(); //Mis state järgmises tsüklis läheb
                System.out.println("Valisite: " + state);
            } else if (state.equals("kell")) {
                System.out.print("\r" + kell.getKell());
                System.out.println();
                state = scanner.nextLine(); //Mis state järgmises tsüklis läheb
                System.out.println("Valisite: " + state);
            } else if (state.equals("salvesta")) {
                try {
                    FileWriter evendiKirjutaja = new FileWriter("evendid.txt");
                    for (Event event : evendid) {
                        evendiKirjutaja.write(event.toString());
                    }

                    evendiKirjutaja.close();
                    System.out.println("Kirjutasin evendid faili.");
                } catch (IOException e) {
                    System.out.println("Error faili kirjutamisel.");
                    e.printStackTrace();
                }
                state = "";
            } else if (state.equals("lisa")) {

                System.out.println();
                System.out.println("Sisesta ürituse nimi: ");
                String nimi = scanner.nextLine();
                System.out.println("Sisesta kuupäev kujul päev.kuu.aasta : ");
                String päev = scanner.nextLine();
                System.out.println("Sisesta aeg kujul tunnid:minutid, kui soovite lasta programmil aja valida, sisestage \"suvaline\": ");
                String aegvastus = scanner.nextLine();
                String aeg = aegvastus;
                if (aegvastus.equals("suvaline")) {
                    aeg = suvalineAeg();
                    System.out.println("Arvuti valis ajaks: " + aeg);
                }
                System.out.println("Kas soovite lisada detaile (jah/ei)?: ");
                String vastus = scanner.nextLine();
                ArrayList<String> detailidelist = new ArrayList<String>();
                Event uus = new Event(nimi, päev, aeg, detailidelist);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                Date meeldetuletuseAeg = format.parse("0000-01-01 at 00:00:00");
                Meeldetuletus uus2 = new Meeldetuletus(nimi, päev, aeg, detailidelist, meeldetuletuseAeg);
                if (vastus.equals("jah")) {
                    boolean tingimus = true;
                    System.out.println("Sisestage ükshaaval detaile. Kui rohkem pole vaja lisada, kirjutage \"lõpeta\"");
                    while (tingimus) {
                        String detailvastus = scanner.nextLine();
                        if (detailvastus.equals("lõpeta")) {
                            tingimus = false;
                            break;
                        } else {
                            uus2.lisaDetail(detailvastus); //uus2-le lisamine lisab ka uus-le
                        }
                    }
                }
                System.out.println("Kas soovite lisada meeldetuletuse (jah/ei) ?: ");
                String meeldetuletusjahei = scanner.nextLine();
                if (meeldetuletusjahei.equals("ei")) {
                    evendid.add(uus);
                } else if (meeldetuletusjahei.equals("jah")) {
                    System.out.println("Sisestage meeldetuletuse aeg formaadis aasta-kuu-päev at tund:minut:sekund (näiteks 2021-12-01 at 12:00:00): ");
                    String meeldetuletusaegvastus = scanner.nextLine();
                    Date meeldetuletusaeg = format.parse(meeldetuletusaegvastus);
                    uus2.setMeeldetuletuseAeg(meeldetuletusaeg);
                    evendid.add(uus2);
                } else {
                    System.out.println("Vigane sisestus.");
                }
                System.out.println(evendid);
                state = ""; //Esialgsesse state tagasi
            } else if (state.equals("väljasta")) {
                Collections.sort(evendid);
                System.out.println(evendid);
                state = "";
            } else if (state.equals("kustuta")) {
                System.out.println("Millist sündmust soovite kustutada(sisestage nimi või indeks): ");
                String kustutaVastus = scanner.nextLine();
                try {
                    evendid.remove(Integer.parseInt(kustutaVastus)); //indeksi järgi kustutamine
                } catch (Exception d) {  //Kui ei suuda sisestatud teksti arvuks muuta.
                    for (Event event : evendid) { //nime järgi kustutamine
                        if (event.getNimi().equals(kustutaVastus)) {
                            evendid.remove(event);
                            break;
                        }
                    }
                }
                state = "";
            } else if (state.equals("muuda")) {
                System.out.println("Sisestage sündmuse nimi, mida soovite muuta: ");
                String muudetavaNimi = scanner.nextLine();
                boolean lõpetamiseTingimus = false;
                while (lõpetamiseTingimus == false) {
                    System.out.println("Sündmuse muutmise lõpetamiseks sisestage: \"lõpeta\"");
                    System.out.println("Mida soovite sündmuse juures muuta (sisestage \"nimi\", \"kuupäev\", \"aeg\", \"detail\" või \"meeldetuletus\")");
                    String muutmisevastus = scanner.nextLine();
                    if (muutmisevastus.equals("lõpeta")) {
                        lõpetamiseTingimus = true;
                        break;
                    } else if (muutmisevastus.equals("nimi")) {
                        System.out.println("Sisestage uus nimi: ");
                        String uusnimi = scanner.nextLine();
                        for (Event event : evendid) {
                            if (event.getNimi().equals(muudetavaNimi)) {
                                event.setNimi(uusnimi);
                            }
                        }
                    } else if (muutmisevastus.equals("kuupäev")) {
                        System.out.println("Sisestage uus kuupäev kujul päev.kuu.aasta: ");
                        String uuskuupäev = scanner.nextLine();
                        for (Event event : evendid) {
                            if (event.getNimi().equals(muudetavaNimi)) {
                                event.setKuupäev(uuskuupäev);
                            }
                        }
                    } else if (muutmisevastus.equals("aeg")) {
                        System.out.println("Sisestage uus aeg kujul tunnid:minutid: ");
                        String uusaeg = scanner.nextLine();
                        for (Event event : evendid) {
                            if (event.getNimi().equals(muudetavaNimi)) {
                                event.setAeg(uusaeg);
                            }
                        }
                    } else if (muutmisevastus.equals("detail")) {
                        System.out.println("Kas soovite detaili lisada või kustutada (\"lisa\"/\"kustuta\"): ");
                        String detailimuutmisevastus = scanner.nextLine();
                        if (detailimuutmisevastus.equals("lisa")) {
                            System.out.println("Sisestage lisatav detail: ");
                            String uusdetail = scanner.nextLine();
                            for (Event event : evendid) {
                                if (event.getNimi().equals(muudetavaNimi)) {
                                    event.lisaDetail(uusdetail);
                                }
                            }
                        } else if (detailimuutmisevastus.equals("kustuta")) {
                            System.out.println("Sisestage kustutatav detail kas nime või indeksi järgi: ");
                            String kustutatavdetail = scanner.nextLine();
                            try {
                                int kustutatavaindeks = Integer.parseInt(kustutatavdetail);
                                for (Event event : evendid) {
                                    if (event.getNimi().equals(muudetavaNimi)) {
                                        event.kustutaDetail(kustutatavaindeks);
                                    }
                                }
                            } catch (Exception e) {
                                for (Event event : evendid) {
                                    if (event.getNimi().equals(muudetavaNimi)) {
                                        event.kustutaDetail(kustutatavdetail);
                                    }
                                }
                            }
                        } else {
                            System.out.println("Vigane sisestus.");
                        }
                    } else if (muutmisevastus.equals("meeldetuletus")) {
                        //Loo uus Meeldetuletus, kustuta vana event ja pane asemele Meeldetuletus
                        System.out.println("Sisestage meeldetuletuse aeg kujul aasta-kuu-päev at tund:minut:sekund");
                        String Mvastus = scanner.nextLine();
                        SimpleDateFormat formaat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                        Date Mmeeldetuletus = formaat.parse(Mvastus);
                        String Mnimi = "";
                        String Mkuupäev = "";
                        String Maeg = "";
                        ArrayList<String> Mdetailid = new ArrayList<>();

                        for (Event event : evendid) {
                            if (event.getNimi().equals(muudetavaNimi)) {
                                Mnimi = event.getNimi();
                                Mkuupäev = event.getKuupäev();
                                Maeg = event.getAeg();
                                Mdetailid = event.getDetailid();
                                evendid.remove(event); //eemaldame evendi
                                break;
                            }
                        }
                        Meeldetuletus uusM = new Meeldetuletus(Mnimi, Mkuupäev, Maeg, Mdetailid, Mmeeldetuletus);
                        evendid.add(uusM); //lisame asemele Meeldetuletuse
                    } else {
                        System.out.println("Vigane sisestus.");
                    }
                }
                state = "";
            } else {
                state = ""; //Kui kirjutatakse mingi suvaline käsklus siis läheb ka esialgsesse state tagasi
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String suvalineAeg() { //Tagastab suvalise kellaaja.
        int tund = (int) (Math.random() * 25);
        int minut = (int) (Math.random() * 61);
        String tagastatav = tund + ":" + minut;
        return tagastatav;
    }
}

