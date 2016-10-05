package dataSource;
import java.sql.*;
import domain.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBFacade
{
    private UnitOfWorkProcessOrder uowo;//UnitofworkOrder
    private Connection con;
    //=====	Singleton
    private static DBFacade instance;

    //DBfacade constructor
    private DBFacade()
    {
        con = DBConnector.getInstance().getConnection();
    }
    
    public static DBFacade getInstance()
    {
        if (instance == null)
        {
            instance = new DBFacade();
        }   
        
        return instance;
    }     
    /**-----------------------TRANSACTION-----------------------------------**/
    //commit
    public boolean commitBusinessTransactionOrder(){
        boolean status = false;
        if(uowo != null){
            try{
                status = uowo.commit(con);
            }
            catch(Exception err){
                System.out.println("error in DBFacade - commitBusinessTransactionOrder");
                err.printStackTrace();
            }
            uowo = null;
        }
            return status;
    }
    //Commit business transactionorderdetail
    public boolean commitBusinessTransactionOrderDetail(){
        boolean status = false;
        if(uowo != null){
            try {
                status = uowo.commit(con);
            }
            catch (Exception e){
                System.out.println("fail! in dbFacade commitBusinessTransactionOrderDetail()");
                System.err.println(e);
            }
            uowo = null;
        }
        return status;
    }

    //ny ordrer detalje transaction
    public void startnewOrderDetailTransaction(){
        uowo = new UnitOfWorkProcessOrder();
    }

    //start transaction ordrer
    public void startTransactionOrder()
    {
            uowo = new UnitOfWorkProcessOrder();
    }
    /**---------------------------KUNDER----------------------------------**/

    //Opret ny kunde
    public boolean createNyKunde(Kunde k){

        return new KundeMapper().createNyKunde(k, con);
        
    }
    //get kunder
    public ArrayList<Kunde> getKunder(){
    ArrayList<Kunde> k = null;
    k = new KundeMapper().GetKunder(con);
    return k;
    }
    //søg efter kunde på ordrer
    public Kunde searchAfterKundeOnOrder(int ordreid)
    {
        Kunde k = null;
        k = new KundeMapper().searchAfterKundeOnOrder(ordreid, con);
        return k;
    }
    //søg på kunder
    public Kunde searchKunder(int kundeid){
        
        Kunde ks = null;
        ks = new KundeMapper().searchKunder(kundeid, con);
        return ks;
    }

    //tjek om res er tilgængelig
    public ArrayList<String> checkIfResIsAvailable(ArrayList<OrderDetail> detailList,String datoStart,String datoslut ){
        ArrayList<String> list = null;
        list = new OrderMapper().checkIfResIsAvailable(detailList,datoStart,datoslut,con);
        return list;
    }
    /**---------------------------RESSOURCER----------------------------------**/

    //få ressourcer
    public ArrayList<Ressourcer> getRessourcer()
    {
        ArrayList<Ressourcer> R = null;
        R = new RessourcerMapper().getRessourcer(con);
        return R;
    }
    //nedskrivning af ressourcer
    public boolean nedSkrivRes(String delnavn, int antal)
    {
        return new RessourcerMapper().nedSkrivRes(delnavn, antal, con);
    }
   
    
    int nextNo = 0;
    
    /**---------------------------ORDRER----------------------------------**/
    //Få næste ordre nummer fraDB
    public int getNextOrderNo(){    
        nextNo = new OrderMapper().getNextOrderNo(con);
        return nextNo; 
    }
    //getOrder
    public Order getOrder(int ordreid)
    {
        return new OrderMapper().getOrder(ordreid,con);
    }
    
    public int getOrderNo(){
        return nextNo;
    }
    //søg på ordrer
    public ArrayList<Order> searchAfterOrders(String datoStart, String datoSlut){
       
           ArrayList<Order>  o = null;
        
        try {
            o = new OrderMapper().searchAfterOrders(datoStart, datoSlut, con);
        } catch (SQLException ex) {
            
        }
        return o;
    }    
    //Registrer ny ordrer
    public void registerNewOrder(Order ord){

        if(uowo != null){
            uowo.registerNewOrder(ord);
        }
        
    }
  //opdater ordrer
    public boolean updateOrder(Order o)
    {
      try {
      return new OrderMapper().UpdateOrder(o, con);
      } catch (Exception e) {
         
      }
       return false;
    }
    //registrer ny ressource
    public boolean createNewRessource(Ressourcer or){
        
        return new RessourcerMapper().createNewRessource(or, con);
    }
    //Søg på ordrer detail
    public ArrayList<OrderDetail> searchAfterOrdeDetail(int ordreid){
        
        return new OrderMapper().searchAfterOrdeDetail(ordreid, con);
        
    }
    //Få ny ordrer detalje ID
    public int getNextOrderDetailID(){
        
        int nextOdId = new OrderMapper().getNextOrderDetailNo(con);
        
        return nextOdId;
    }
    //Registrer ny ordre detalje
    public void registerNewOrderDetail(OrderDetail od){
        if (uowo != null){
            uowo.registerNewOrderDetail(od);
        }

    }
/**----------------------------TEAMS----------------------------------------**/    
    //vælg teams  
    public ArrayList<Team> selectTeams()
    {
      return new TeamMapper().selectTeams(con);
    }      
  
    //gem teams på ordrer
    public boolean saveTeamOnOrder(ArrayList<TeamOnOrdre> t )
    {
      return new TeamMapper().saveTeamOnOrder(t,con);
    }
    //tjek om teams er tilgængelige 
    public ArrayList<String> checkIfTeamIsAvailable(ArrayList<TeamOnOrdre> t)
    {
      return new TeamMapper().checkIfTeamIsAvailable(t,con);
    }
  
    //søg efter teams på ordrer
    public ArrayList<Team> SearchafterTeamsOnOrder(int ordreid)
    {
      return new TeamMapper().SearchafterTeamsOnOrder(ordreid, con);
    }

    //slet teams på ordrer
    public boolean DeleteTeamOnOrder(int ordreId, int teamId)
    {
      try {
          
     
      return new TeamMapper().DeleteTeamOnOrder(ordreId, teamId,con);
           } catch (Exception e) {
      } 
      return false;
    }
  
    //confirm depositum 
    public boolean confirmDepositum(int id){
         try {
                 return new OrderMapper().confirmDepositum(con, id);
     
         } catch (Exception e) {
             return false;
         }
       
     }
 
  
    }
