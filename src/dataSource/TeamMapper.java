package dataSource;
import domain.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.Connection;

/**
 *
 * @author nikolai
 */
public class TeamMapper {
    //Vælg teams
    //SQL SELECT
    public ArrayList<Team> selectTeams(Connection con){
        
        ArrayList<Team> Teams = new ArrayList<Team>();
        
         String SQLString = null;
         Statement statement = null;
         ResultSet rs;
          SQLString = "SELECT * FROM p_team";
        try
        {
            
            statement = con.createStatement();
            rs = statement.executeQuery(SQLString);
            
               while(rs.next())
               {
                   Team t = new Team(rs.getInt(1), rs.getString(2), rs.getInt(3));
                   Teams.add(t);
               }
               rs.close();
        
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Fejl i TeamMapper!");
        }    
          
        return Teams;
        
    }  
    
    //Gem team på ordrer
    //SQL INSERT
    public boolean saveTeamOnOrder(ArrayList<TeamOnOrdre> t, Connection con) 
    {
        String SqlString = "insert into p_teamonorder values(?,?,?,?,?)";
    PreparedStatement statement = null;
    
    int rowInserted = 0;
        try {
            
        
    if(0 < t.size()){
        statement = con.prepareStatement(SqlString);
        
        for(int i = 0; i < t.size(); i++){
            
            
           statement.setInt(1,GetnextsaveTeamOnOrderNo(con));
           statement.setString(2,t.get(i).getDatoStart());
           statement.setString(3,t.get(i).getDatoStart());
           statement.setInt(4,t.get(i).getFk_ordre());
           statement.setInt(5, t.get(i).getFk_team());
        
            rowInserted += statement.executeUpdate();
        } 
    }
    } 
    catch (Exception e) {
        System.out.println("Fejl i TeamMapper - saveTeamOnOrder");
        }
    return rowInserted == t.size();
    }
    //SQL SELECT teamonorder id fra p_teamonorder
    public int GetnextsaveTeamOnOrderNo(Connection con){
        int nextOno = 0;
        String SQLString = "SELECT MAX(teamonorder_id) FROM p_teamonorder";
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                nextOno += rs.getInt(1);
            }
        } catch (Exception e)
        {
            System.out.println("Fail in TeamMapper - GetnextsaveTeamOnOrderNo");
            e.printStackTrace();
        }
        if(nextOno == 0 ){ nextOno =1;}
        else{ nextOno++; }
        return nextOno;
    }
    //Tjekker om team er tilgengælig
    //SQL SELECT
    public ArrayList<String> checkIfTeamIsAvailable(ArrayList<TeamOnOrdre> t, Connection con)
    {
        ArrayList<String> errorReportTeam = new ArrayList<String>();
        
        String sqlString = null;
        Statement statement = null;
        
        ResultSet rs;
        for (int i = 0; i <t.size(); i++) {
            sqlString = "select count(P_TEAMONORDER.TEAMONORDER_ID), p_team.NAVN   \n" +
            "from P_TEAMONORDER \n" +
            "inner join p_team on p_team.TEAM_ID = P_TEAMONORDER.FK_TEAM \n"+        
            "where\n" +
            "fk_team ="+t.get(i).getFk_team()+"\n" +
            "and datostart = '"+t.get(i).getDatoStart()+"'\n" +
            "or datoslut = '"+t.get(i).getDatoSlut()+"' and\n" +
            "fk_team = "+t.get(i).getFk_team()+" group by TEAMONORDER_ID,fk_team,p_team.NAVN";
           
            statement = null;
            try {
                statement = con.createStatement();
                rs = statement.executeQuery(sqlString);
                
                if(rs.isBeforeFirst())
                {
                    while(rs.next())
                    {
                        if(rs.getInt(1) != 0)
                        {
                            errorReportTeam.add("Fejl i hold "+rs.getString(2));
                        }
                    }
                }
                
            } catch (Exception e) {
                System.out.println("FEJL I TEAMMAPPER - checkIfTeamIsAvailable");
                System.out.println(e);
            }
        }
        
        return errorReportTeam;
    }
    //Søg efter teams på ordrer
    //SQL SELECT
    public ArrayList<Team> SearchafterTeamsOnOrder(int ordreid, Connection con)
    {
        ArrayList<Team> t = new ArrayList<Team>();
        
        String sqlString = null;
        Statement statement = null;
        
        ResultSet rs;
        sqlString = "SELECT p_team.team_id,p_team.navn,p_team.fk_lastbil FROM p_teamonorder \n" +
        "INNER JOIN p_team ON p_team.TEAM_ID = p_teamonorder.FK_TEAM \n" +
        "WHERE p_teamonorder.FK_ORDRE = "+ordreid+"";
         try {
                statement = con.createStatement();
                rs = statement.executeQuery(sqlString);

                    while(rs.next())
                    {
                        if(rs.getInt(1) != 0)
                        {
                            Team team = new Team(rs.getInt(1),rs.getString(2),rs.getInt(3));
                            t.add(team);
                        }
                    }
                
                
            } catch (Exception e) {
                System.out.println("FEJL I TEAMMAPPER - SearchafterTeamsOnOrder");
                System.out.println(e);
            }
        
        
        return t;
    }
    
    //Slet teams på ordrer
    //SQL DELETE
    public boolean DeleteTeamOnOrder(int ordreId, int teamId, Connection con) throws SQLException
     {
        
        int rowsdeleted = 0;
        String sqlString = "DELETE FROM P_TEAMONORDER WHERE FK_ORDRE = ? and fk_team = ?";
                
        PreparedStatement statement = null;
        statement = con.prepareStatement(sqlString);
  
         statement.setInt(1, ordreId);
        statement.setInt(2, teamId);
        //System.out.println(sqlString);
         //System.out.println(o.getPris() +" " + o.getKunde_confirm() +"  " + o.getDatoStart() +" " + o.getDatoSlut());
        rowsdeleted = statement.executeUpdate();
         
       
        return rowsdeleted == 1;
     }
}
