package oop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Kell{
    Date kuupäev = Calendar.getInstance().getTime();
    DateFormat kellaVorming = new SimpleDateFormat("HH:mm");
    String kell = kellaVorming.format(kuupäev);

    public String getKell() {
        return kell;
    }

}
