package domain;

/**
 *
 * @author Jonathan
 */
public class Team {
    int id,fk_lastbil;
    String navn;

    public Team(int id,String navn ,int fk_lastbil) {
        this.id = id;
        this.fk_lastbil = fk_lastbil;
        this.navn = navn;
     
    }

    public int getId() {
        return id;
    }

    public int getFk_lastbil() {
        return fk_lastbil;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", fk_lastbil=" + fk_lastbil + ", navn=" + navn + '}';
    }


    
    
    
}
