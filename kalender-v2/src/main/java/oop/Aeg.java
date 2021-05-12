package oop;

import java.util.Date;
//Veel ei läinud vaja seda klassi ega meetodit.
public class Aeg extends Date {
    private Date aeg;
    public int võrdleKellaaegu(Aeg AegteineAeg) {
        String seeAeg = this.aeg.toString();
        String teineAeg = AegteineAeg.toString();
        String[] seeAegJupid = seeAeg.split(" ");
        String[] teineAegJupid = teineAeg.split(" ");
        String[] seeKellJupid = seeAegJupid[3].split(":");
        String[] teineKellJupid = teineAegJupid[3].split(":");
        if (Integer.parseInt(seeKellJupid[0]) > Integer.parseInt(teineKellJupid[0])){
            return 1;
        }
        else if(Integer.parseInt(seeKellJupid[0]) < Integer.parseInt(teineKellJupid[0])){
            return -1;
        }
        else{
            if (Integer.parseInt(seeKellJupid[1]) > Integer.parseInt(teineKellJupid[1])){
                return 1;
            }
            else if(Integer.parseInt(seeKellJupid[1]) < Integer.parseInt(teineKellJupid[1])){
                return -1;
            }
            else{
                if (Integer.parseInt(seeKellJupid[2]) > Integer.parseInt(teineKellJupid[2])){
                    return 1;
                }
                else if(Integer.parseInt(seeKellJupid[2]) < Integer.parseInt(teineKellJupid[2])){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }
    }
}
