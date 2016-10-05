package domain;

/**
 *
 * @author nikolai
 */
public class TeamOnOrdre {
   
    int id,fk_ordre,fk_team;
    String datoStart,datoSlut;

    public TeamOnOrdre(int id, String datoStart, String datoSlut,int fk_ordre, int fk_team) {
        this.id = id;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
        this.fk_ordre = fk_ordre;
        this.fk_team = fk_team;
    }

    public int getId() {
        return id;
    }

    public int getFk_ordre() {
        return fk_ordre;
    }

    public int getFk_team() {
        return fk_team;
    }

    public String getDatoStart() {
        return datoStart;
    }

    public String getDatoSlut() {
        return datoSlut;
    }

    @Override
    public String toString() {
        return "TeamOnOrder{" + "id=" + id + ", fk_ordre=" + fk_ordre + ", fk_team=" + fk_team + ", datoStart=" + datoStart + ", datoSlut=" + datoSlut + '}';
    }
    
    
    
}