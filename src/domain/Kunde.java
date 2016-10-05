package domain;

/**
 *
 * @author nikolai
 */
public class Kunde {

    String firmanavn,fullName,email,adresse,telefonnr;
    int id,postnr,versionsnr;

    public Kunde(int id, String firmanavn,String fullName, String email, String telefonnr, String adresse, int postnr, int versionsnr) {
        this.firmanavn = firmanavn;
        this.fullName = fullName;
        this.email = email;
        this.adresse = adresse;
        this.id = id;
        this.telefonnr = telefonnr;
        this.postnr = postnr;
        this.versionsnr = versionsnr;
    }

    @Override
    public String toString() {
        return "Kunde{" + "firmanavn=" + firmanavn + ", fullName=" + fullName + ", email=" + email + ", adresse=" + adresse + ", id=" + id + ", telefonnr=" + telefonnr + ", postnr=" + postnr + ", versionsnr=" + versionsnr + '}';
    }

    public String getFirmanavn() {
        return firmanavn;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public int getId() {
        return id;
    }

    public int getPostnr() {
        return postnr;
    }

    public int getVersionsnr() {
        return versionsnr;
    }
    
  
    
    
    
}
