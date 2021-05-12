package oop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Kuupäev{
    Date kuupäev = Calendar.getInstance().getTime();
    DateFormat kuupäevaVorming = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    String täna = kuupäevaVorming.format(kuupäev);

    public String getTäna() {
        return täna;
    }
}

