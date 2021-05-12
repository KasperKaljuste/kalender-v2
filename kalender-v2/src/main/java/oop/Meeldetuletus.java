package oop;

import java.util.ArrayList;
import java.util.Date;


public class Meeldetuletus extends Event{

    private Date meeldetuletuseAeg;

    public Meeldetuletus(String nimi, String kuupäev, String aeg, ArrayList<String> detailid, Date meeldetuletuseAeg) {
        super(nimi, kuupäev, aeg, detailid);
        this.meeldetuletuseAeg = meeldetuletuseAeg;
    }

    public void setMeeldetuletuseAeg(Date meeldetuletuseAeg) {
        this.meeldetuletuseAeg = meeldetuletuseAeg;
    }

    public Date getMeeldetuletuseAeg() {
        return meeldetuletuseAeg;
    }

    @Override
    public String toString() {
        return "" + getNimi() + "; " + getKuupäev() + "; " + getAeg() + "; " + getDetailid() +"; "+meeldetuletuseAeg+"\n";
    }


}
