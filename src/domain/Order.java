package domain;
public class Order
{
   private int ordreID;
   private int kundeFK;
   private int pris;
   private int kunde_confirm;
   private int versionNr;
   private int status;
   private String datoStart;
   private String datoSlut;
   
   
   public Order(){}
   public Order(int ordreID, int kunde_confirm, int versionNr){
       this.ordreID = ordreID;
       this.kunde_confirm = kunde_confirm;
       this.versionNr = versionNr; 
       
   }
   
   public Order(int ordreID, int pris ,int kunde_confirm, String datoStart, String datoSlut )
   {
        this.ordreID = ordreID;
        this.pris = pris;
        this.kunde_confirm = kunde_confirm;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
   }        
   
    public Order(int ordreID, int kundeFK, int pris, int kunde_confirm, int versionNr, int status, String datoStart, String datoSlut) {
        this.ordreID = ordreID;
        this.kundeFK = kundeFK;
        this.pris = pris;
        this.kunde_confirm = kunde_confirm;
        this.versionNr = versionNr;
        this.status = status;
        this.datoStart = datoStart;
        this.datoSlut = datoSlut;
    }

    @Override
    public String toString() {
        return "Order{" + "ordreID=" + ordreID + ", kundeFK=" + kundeFK + ", pris=" + pris + ", kunde_confirm=" + kunde_confirm + ", versionNr=" + versionNr + ", status=" + status + '}';
    }

    public int getOrdreID() {
        return ordreID;
    }

    public int getKundeFK() {
        return kundeFK;
    }

    public String getDatoStart() {
        return datoStart;
    }

    public String getDatoSlut() {
        return datoSlut;
    }

    public int getPris() {
        return pris;
    }

    public int getKunde_confirm() {
        return kunde_confirm;
    }

    public int getVersionNr() {
        return versionNr;
    }

    public int getStatus() {
        return status;
    }
   
    

}
