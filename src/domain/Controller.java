package domain;
import dataSource.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jonathan
 */
public class Controller {

    ArrayList<OrderDetail> Detail;
    ArrayList<Ressourcer> res;
    ArrayList<OrderDetail> orderdetail;
    ArrayList<Kunde> kunde;
    ArrayList<Team> team;
    ArrayList<Team> TeamSearch;
    ArrayList<TeamOnOrdre> teamOnOrdre;
    ArrayList<String> errorReport;
    ArrayList<String> errorReportTeam;
    ArrayList<Order> Order;
    private DBFacade dbFacade;

    public Controller() {
        dbFacade = DBFacade.getInstance();
        Detail = new ArrayList<OrderDetail>();
        res = new ArrayList<Ressourcer>();
        orderdetail = new ArrayList<OrderDetail>();
        team = new ArrayList<Team>();
        teamOnOrdre = new ArrayList<TeamOnOrdre>();
        TeamSearch = new ArrayList<Team>();
        errorReportTeam = new ArrayList<String>();
        errorReport = new ArrayList<String>();
        Order = new ArrayList<Order>();

    }
    /*-------------------------------ORDER----------------------------------*/
    public void createOrderDetailArray(int id, int ordreFK, int resFK, int antal) {
        OrderDetail od1 = new OrderDetail(id, ordreFK, resFK, antal);
        Detail.add(od1);

    }

    public void dropDetailArray() {
        Detail = new ArrayList<OrderDetail>();
    }
    
    //Ordre
    public boolean createNewOrder(int pris, int kundeNr, String datoStart, String datoslut) {

        //start Transaction    
        dbFacade.startTransactionOrder();
        Order ord;

        //laver ny ordreNr til databasen
        int newOrderNo = dbFacade.getNextOrderNo();

        boolean status;
        if (newOrderNo != 0) {
            status = true;

            ord = new Order(newOrderNo, kundeNr, pris, 0, 1, 1, datoStart, datoslut);
            dbFacade.registerNewOrder(ord);
        } else {
            status = false;
            ord = null;
        }

        return status;
    }

    public int getOrdreNo() {
        return dbFacade.getOrderNo();
    }

    public boolean saveOrder() {

        boolean status = false;

        //commit Transaction
        status = dbFacade.commitBusinessTransactionOrder();
        return status;
    }

    public ArrayList<Order> searchAfterOrders(String startDato, String slutDato) {

        //søger efter ordre i databasen
        Order = dbFacade.searchAfterOrders(startDato, slutDato);

        return Order;

    }

    public ArrayList<Order> getOrderList() {
        return Order;
    }

    public Order SearchAfterOrderID(int ordreid) {
        return dbFacade.getOrder(ordreid);
    }

    //createOrderDetail 
    public void createOrderDetail() {

        //henter(Get) ordreid fra ordren i dbfacade
        int ordreNo = dbFacade.getOrderNo();

        dbFacade.startnewOrderDetailTransaction();

        int orderDetailId = dbFacade.getNextOrderDetailID();

        for (int i = 0; i < Detail.size(); i++) {

            //Tjekker om der er nogel med antallet 0 i vores array 'Detail'  
            if (Detail.get(i).antal != 0) {

                //laver en ny ordre id ved at plus 1 til den nuværende id
                orderDetailId++;

                //laver ET nyt OrderDetail object 
                OrderDetail od = new OrderDetail(orderDetailId, ordreNo, Detail.get(i).getResFK(), Detail.get(i).getAntal());

                //her bliver OrderDetail object sat ind i daatabasen
                dbFacade.registerNewOrderDetail(od);
            }

        }

    }

    public boolean saveOrderDetail() {
        boolean status = dbFacade.commitBusinessTransactionOrderDetail();

        return status;
    }

    //søger på ordreDetail i databasen
    public ArrayList<OrderDetail> searchAfterOrdeDetail(int ordreid) {

        orderdetail = dbFacade.searchAfterOrdeDetail(ordreid);
        return orderdetail;
    }

    public ArrayList<OrderDetail> getOrderdetail() {
        return orderdetail;
    }

    //Kunde
    public boolean createNewKunde(String firmanavn, String fullname, String email, String telefonnr, String adresse, int postnr) {
        Kunde k = new Kunde(0, firmanavn, fullname, email, telefonnr, adresse, postnr, 0);
        return dbFacade.createNyKunde(k);

    }

    public ArrayList<Kunde> SelectKunder() {
        kunde = dbFacade.getKunder();
        return kunde;
    }

    public ArrayList<Kunde> getKunder() {
        return kunde;
    }

    //søger på en kunde på en bestemte ordre 
    public Kunde searchAfterKundeOnOrder(int ordreid) {
        return dbFacade.searchAfterKundeOnOrder(ordreid);
    }

    public Kunde searchKunder(int kundeid) {
        return dbFacade.searchKunder(kundeid);
    }

/*-------------------------Ressourcer----------------------------------------*/    
    
    //Ressource
    public boolean createNewRessource(String navn, int antal) {
        Ressourcer or = new Ressourcer(0, navn, antal, 0);
        return dbFacade.createNewRessource(or);
    }

    public ArrayList<Ressourcer> SelectRessourcer() {
        res = dbFacade.getRessourcer();
        return res;
    }

    public ArrayList<Ressourcer> getRes() {
        return res;
    }

    public boolean nedSkrivRes(String delnavn, int antal) {
        return dbFacade.nedSkrivRes(delnavn, antal);
    }

    

    //booking res
    public ArrayList<String> checkIfResIsAvailable(String datoStart, String datoslut) {
        errorReport = dbFacade.checkIfResIsAvailable(Detail, datoStart, datoslut);
        return errorReport;

    }

    public ArrayList<String> getErrorReport() {
        return errorReport;
    }

    public void dropErrorReport() {
        errorReport = new ArrayList<String>();
    }
    /*--------------------------TEAMS---------------------------------------*/
    //Teams
    public ArrayList<Team> selectTeams() {
        team = dbFacade.selectTeams();
        return team;
    }

    public ArrayList<Team> getTeams() {
        return team;
    }

    //TeamOnOrder
    public void createTeamOnOrder(int id, String datostart, String datoslut, int fk_order, int fk_team) {
        TeamOnOrdre too = new TeamOnOrdre(id, datostart, datoslut, fk_order, fk_team);
        teamOnOrdre.add(too);
    }

    public boolean saveTeamOnOrder() {
        //Gemmer hvilke Team(s) der er sat på en ordre 
        return dbFacade.saveTeamOnOrder(teamOnOrdre);
    }

    public ArrayList<TeamOnOrdre> getTeamOnOrder() {
        return teamOnOrdre;
    }

    public void dropTeamOnOrdre() {
        teamOnOrdre = new ArrayList<TeamOnOrdre>();
    }

    public boolean updateOrder(int ordreId, int pris, int kundeConfirm, String datoStart, String datoSlut) {
        Order o = new Order(ordreId, pris, kundeConfirm, datoStart, datoSlut);
        return dbFacade.updateOrder(o);
    }

    public boolean DeleteTeamOnOrder(int ordreId, int teamId) {
        return dbFacade.DeleteTeamOnOrder(ordreId, teamId);
    }

    //booking af team 
    public ArrayList<String> checkIfTeamIsAvailable() {
        errorReportTeam = dbFacade.checkIfTeamIsAvailable(teamOnOrdre);
        return errorReportTeam;

    }

    public ArrayList<String> geterrorReportTeam() {
        return errorReportTeam;
    }

    public void droperrorReportTeam() {
        errorReportTeam = new ArrayList<String>();
    }

    //søg efter team
    public ArrayList<Team> SearchafterTeamsOnOrder(int ordreid) {
        TeamSearch = dbFacade.SearchafterTeamsOnOrder(ordreid);
        return TeamSearch;
    }

    public ArrayList<Team> getTeamsearch() {
        return TeamSearch;
    }

    public void dropTeamSearch() {
        TeamSearch = new ArrayList<Team>();
    }

/*---------------------------------DATO--------------------------------------*/    
    //dato format
    public String omformaterDato(String str) {
        String dag = str.substring(0, 2);
        String mdr = str.substring(3, 5);
        String year = str.substring(6, 8);
        return year + "-" + mdr + "-" + dag;
    }

    public String ForkortDato(String str) {
        String dag = str.substring(8, 10);
        String mdr = str.substring(5, 7);
        String year = str.substring(0, 4);
        return year + "-" + mdr + "-" + dag;
    }

    //Tjekke om de 2 datoer er ens, eller om slutdato'en er mindre end stardato'en 
    public boolean dateValidate(String datoStart, String datoSlut) {
        boolean date = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

            Date dato1 = sdf.parse(datoStart);
            Date dato2 = sdf.parse(datoSlut);

            if (dato2.compareTo(dato1) < 0) {
                date = true;
            } else if (dato1.compareTo(dato2) == 0) {
                date = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public boolean confirmDepositum(int ordreId) {
        return dbFacade.confirmDepositum(ordreId);
    }

}
