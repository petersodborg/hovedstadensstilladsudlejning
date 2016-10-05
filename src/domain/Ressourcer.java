package domain;

/**
 *
 * @author Jonathan
 */
public class Ressourcer {
    int id,antal,version;
    String navn;

    public Ressourcer(int id, String navn, int antal, int version ) {
        this.id = id;
        this.antal = antal;
        this.version = version;
        this.navn = navn;
    }
    
    public Ressourcer(String navn, int antal) {
        this.navn = navn;
        this.antal = antal;
    }

    public int getId() {
        return id;
    }

    public int getAntal() {
        return antal;
    }

    public int getVersion() {
        return version;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return "Del " +navn + " antal " +antal; 
    }
    
    
    
    
}
