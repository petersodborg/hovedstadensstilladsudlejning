package dataSource;
import domain.Kunde;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author nikolai
 */
public class KundeMapper {

    //get kunder
    public ArrayList<Kunde> GetKunder(Connection con)
    {
        ArrayList<Kunde> k = new ArrayList<Kunde>();
        int id,telefonnr,postnr,versionsnr;
        String firmanavn,fullName,email,adresse;
        
        String SQLString = "select * from p_kunde";
        
        PreparedStatement statement = null;
        Kunde kunderne;
        try
        {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                kunderne = new Kunde(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(8));
                k.add(kunderne);             
            }
        } catch (Exception e)
        {
            System.out.println("Fail in Kundemapper - getKunder");
            e.printStackTrace();
        }

        return k;
    }
    //Søg efter kunde på ordren
    //SQL SELECT
    public Kunde searchAfterKundeOnOrder(int ordreid, Connection con)//eneste sted i program med subselect
    {
        String SQLString = "select * from p_kunde where kid =(Select kunde_fk from p_ordre where ordreid = ?)";
        PreparedStatement statement = null;
        Kunde k = null;
        try
        {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, ordreid);//Spørgsmåltegnet til at være ligmed ordre id vi sætter ind
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                
                k = new Kunde(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getInt(8));
            }
        } catch (Exception e)
        {
            System.out.println("Fail in orderMapper - getNextOrderDetailNo");
            e.printStackTrace();
        }
        return k;
    }
    //Søg kunder
    //SQL SELECT
    public Kunde searchKunder(int kundeid, Connection con){
        
        String sql = "SELECT * FROM p_kunde WHERE kid = ?";
        PreparedStatement statement = null;
        Kunde ks = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, kundeid);
            ResultSet res = statement.executeQuery();
            if (res.next()){
                ks = new Kunde(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getInt(7),res.getInt(8));
            }
        } catch (Exception e) {
            System.out.println("fejl i KundeMapper - searchKunder");
            e.printStackTrace();
        }
        return ks;
    }
    
    //Lav ny kunde
    //SQL INSERT TUPLE
    public boolean createNyKunde(Kunde k, Connection con){
        int rowsInsert = 0;
        String sql = "INSERT INTO p_kunde values(?,?,?,?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, getNewKundeID(con));
            statement.setString(2, k.getFirmanavn());
            statement.setString(3, k.getFullName());
            statement.setString(4, k.getEmail());
            statement.setString(5, k.getTelefonnr());
            statement.setString(6, k.getAdresse());
            statement.setInt(7, k.getPostnr());
            statement.setInt(8, 0);
           rowsInsert += statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in createNyKunde");
            e.printStackTrace();
        }
        
       return   rowsInsert == 1;
   
    }
    //get ny kunde ID
    //SQL SELECT
    public int getNewKundeID(Connection con){
        
        int newKundeID = 0;
        String sql = "SELECT MAX(kId)FROM p_kunde";
        PreparedStatement statement = null;
        
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                newKundeID = result.getInt(1);
                
            }
        } catch (Exception e) {
            System.out.println("Fail in Kundemapper - getNewKundeID");
            e.printStackTrace();
        }
        if(newKundeID == 0){ newKundeID =1; }
        else{ newKundeID++; }
        return newKundeID;
    }
    
    
    
}
