package dataSource;
import domain.Ressourcer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Jonathan
 */
public class RessourcerMapper {
    //Få ressourcer
    //SQL SELECT fra p_res
     public ArrayList<Ressourcer> getRessourcer(Connection con)
    {
        ArrayList<Ressourcer> r = new ArrayList<Ressourcer>();
        
        String SQLString = "select * from p_res";
        
        PreparedStatement statement = null;
        Ressourcer Ressourcererne;
        try
        {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {                 
                Ressourcererne = new Ressourcer(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4));
                r.add(Ressourcererne);             
            }
        } catch (Exception e)
        {
            System.out.println("Fail in Ressourcemapper - getRessourcer");
            e.printStackTrace();
        }

        return r;
    }
     //Lav ny ressource
     //SQL INSERT
     public boolean createNewRessource(Ressourcer or, Connection con){
         
         int rowsInsert = 0;
         
         String sql = "INSERT INTO p_res values (?, ?, ?, ?)";
         PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, getNewRessoureId(con));
            statement.setString(2, or.getNavn());
            statement.setInt(3, or.getAntal());
            statement.setInt(4, 0);

           rowsInsert += statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in createNyKunde");
            e.printStackTrace();
        }
        
       return   rowsInsert == 1;
     }
     //Få nyt ressource id
     //SQL SELECT MAX
     public int getNewRessoureId(Connection con){
        
        int newRessourceId = 0;
        String sql = "SELECT MAX(rid)FROM p_res";
        PreparedStatement statement = null;
        
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                newRessourceId = result.getInt(1);
                
            }
        } catch (Exception e) {
            System.out.println("Fail in RessourceMapper - getNewRessourceId");
            e.printStackTrace();
        }
        if(newRessourceId == 0){ newRessourceId =1; }
        else{ newRessourceId++; }
        return newRessourceId;
    }
     //Nedskrivning
     //SQl Update
     public boolean nedSkrivRes(String delnavn, int antal, Connection con)
     {;
        
        int rowsupdated = 0;
         
         String sql = "UPDATE p_res SET antal = ? WHERE navn = ?";
         PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(sql);
             statement.setInt(1,antal);
            statement.setString(2, delnavn);
           

           rowsupdated += statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in nedSkrivRes");
            e.printStackTrace();
        }
        
       return   rowsupdated == 1;

     }
    
}
