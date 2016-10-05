package dataSource;

import domain.Kunde;
import domain.Order;
import domain.OrderDetail;
import domain.Ressourcer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class OrderMapper {


    public boolean confirmDepositum(Connection con, int id) throws SQLException {
        
        int rowInserted = 0;

        Statement statement = null;

        String SqlString = "UPDATE p_ordre\n"
                + " SET kundeconfirm=1\n"
                + " WHERE ordreid=" + id + " ";

        try {

            statement = con.createStatement();

            rowInserted = statement.executeUpdate(SqlString);
            con.commit();

        } catch (Exception e) {
            System.out.println("Fejl ordremapper - confirmDepositum");
        }

        //return true hvis rowInserted == ellers return den false    
        return rowInserted == 1;
    }

    public ArrayList<String> checkIfResIsAvailable(ArrayList<OrderDetail> detailList, String datoStart, String datoslut, Connection con) {
       
        // størrelse på array'et detailList   
        int countArrayDetailList = detailList.size();
        String fejlBesked = "";

        ArrayList<String> errorReport = new ArrayList<String>();

        String SQLString = null;
        Statement statement = null;

        //køre alt efter hvor mange produkter brugeren har valgt    
        ResultSet rs;
        for (int i = 0; i < countArrayDetailList; i++) {

            //finder difference på en res i db'en
            SQLString = "SELECT (p_res.ANTAL - SUM(p_ordre_detaljer.antal)), p_res.navn"
                    + " FROM p_ordre"
                    + " INNER JOIN p_ordre_detaljer ON p_ordre_detaljer.ordre_fk = p_ordre.ordreid"
                    + " INNER JOIN p_res ON p_res.rid = p_ordre_detaljer.res_fk"
                    + " WHERE p_ordre.datoslut > '" + datoStart + "' "
                    + " AND p_ordre.datostart < '" + datoslut + "'"
                    + " AND p_res.rid =" + detailList.get(i).getResFK() + " "
                    + " GROUP BY p_res.ANTAL, p_res.navn";
            statement = null;
        
            try {

                statement = con.createStatement();
                rs = statement.executeQuery(SQLString);

                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        //så længe at brugens antal er større eller lige med difference fra db-udtræk
                        if (rs.getInt(1) < detailList.get(i).getAntal()) {
                            errorReport.add("Fejl - " + rs.getString(2) + " har kun " + rs.getInt(1) + " på lager i denne periode");
                        }
                    }
                } else {
                    String maxRes = null;
                    maxRes = getMaxRes(detailList.get(i).getResFK(), detailList.get(i).getAntal(), con);

                    if (maxRes != null) {
                        errorReport.add(maxRes);
                    }
                    maxRes = null;
                }

                rs.close();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Fejl i OrderMapper - checkIfResIsAvailable");
            }
        }

        return errorReport;
    }

    
    //henter det maksimal antal fra p_res
    public String getMaxRes(int id, int antal, Connection con) {

        String name = null;
        int maxRes = 0;
        String endString = null;

        String SQLString = "SELECT navn, antal FROM p_res WHERE rid = " + id;

        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQLString);
            if (rs.next()) {
                name = rs.getString(1);
                maxRes = rs.getInt(2);
            }
        } catch (Exception e) {
            System.out.println("Fail in orderMapper - getNextOrderNo");
            e.printStackTrace();
        }

        if (maxRes < antal) {
            endString = "Fejl - " + name + " har kun " + maxRes + " på lager i denne periode";
        }

        return endString;

    }

    public int getNextOrderNo(Connection con) {
        int nextOno = 0;
        String SQLString = "SELECT MAX(ordreid) FROM p_ordre";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextOno += rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Fail in orderMapper - getNextOrderNo");
            e.printStackTrace();
        }
        if (nextOno == 0) {
            nextOno = 1;
        } else {
            nextOno++;
        }
        return nextOno;
    }

    

    public boolean insertOrderDetail(ArrayList<OrderDetail> od, Connection con) throws SQLException {

        String SqlString = "insert into p_ordre_detaljer values(?,?,?,?)";
        PreparedStatement statement = null;

        int rowInserted = 0;
        if (0 < od.size()) {
            statement = con.prepareStatement(SqlString);

            for (int i = 0; i < od.size(); i++) {

                statement.setInt(1, od.get(i).getId());
                statement.setInt(2, od.get(i).getOrdreFK());
                statement.setInt(3, od.get(i).getResFK());
                statement.setInt(4, od.get(i).getAntal());

                rowInserted += statement.executeUpdate();
            }
        }
        return rowInserted == od.size();
    }



    public int getNextOrderDetailNo(Connection con) {
        int nextOno = 0;
        String SQLString = "SELECT MAX(ordre_detaljerId) FROM p_ordre_detaljer";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nextOno = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Fail in orderMapper - getNextOrderDetailNo");
            e.printStackTrace();
        }
        if (nextOno == 0) {
            nextOno = 1;
        } else {
            nextOno++;
        }
        return nextOno;

    }

    public ArrayList<OrderDetail> searchAfterOrdeDetail(int ordreid, Connection con) {
        ArrayList<OrderDetail> odArr = new ArrayList<OrderDetail>();

        String SQLString = "select p_ordre_detaljer.ORDRE_DETALJERID,p_ordre_detaljer.ORDRE_FK,p_ordre_detaljer.RES_FK,p_ordre_detaljer.antal,p_res.NAVN from p_ordre_detaljer \n"
                + "inner join p_res on p_ordre_detaljer.res_fk=p_res.rid\n"
                + "where ordre_fk = ?";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, ordreid);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) { 
               OrderDetail od = new OrderDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
                odArr.add(od);
            }
        } catch (Exception e) {
            System.out.println("Fail in OrderMapper - searchAfterResOnOrder");
            e.printStackTrace();
        }

        return odArr;
    }
    
        
    public ArrayList<Order> searchAfterOrders(String datoStart, String datoSlut, Connection con) throws SQLException {
        ArrayList<Order> Order = new ArrayList<Order>();
        int ordreId;
        int pris;
        int kundeconfirm;
        String datostart;
        String datoslut;

        String sql = "SELECT ordreid, pris, kundeconfirm, datostart, datoslut FROM p_ordre WHERE '" + datoStart + "' <= datostart AND datoslut <= '" + datoSlut + "' order by datostart asc";

     
        Statement statement = null;
        ResultSet rs;

        try {
        
            statement = con.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt(1) != 0) {

                    ordreId = rs.getInt(1);
                    pris = rs.getInt(2);
                    kundeconfirm = rs.getInt(3);
                    datostart = rs.getString(4);
                    datoslut = rs.getString(5);
                    Order o = new Order(ordreId, pris, kundeconfirm, datostart, datoslut);
                    Order.add(o);
                }
            }

        } catch (Exception e) {
            System.out.println("Fejl i Ordermapper searchAfterOrders");
            e.printStackTrace();
        }
        return Order;
    }
    
    
        public boolean insertOrder(ArrayList<Order> ol, Connection con) throws SQLException {
        int rowsInserted = 0;
        String SqlString = "insert into p_ordre values(?,?,?,?,?,?,?,?)";
        PreparedStatement statement = null;

        statement = con.prepareStatement(SqlString);

        for (int i = 0; i < ol.size(); i++) {
            Order o = ol.get(i);
            statement.setInt(1, o.getOrdreID());
            statement.setInt(2, o.getKundeFK());
            statement.setInt(3, o.getPris());
            statement.setInt(4, o.getKunde_confirm());
            statement.setInt(5, o.getVersionNr());
            statement.setInt(6, o.getStatus());
            statement.setString(7, o.getDatoStart());
            statement.setString(8, o.getDatoSlut());

            rowsInserted += statement.executeUpdate();

        }
      
        return (rowsInserted == ol.size());
    }
    
    
      public Order getOrder(int ordreid, Connection con) {
        int OrderId = 0;
        int kunde_fk;
        int pris;
        int confirm;
        int versionsnr;
        int status;
        String datostart;
        String datoslut;
        String SQLString = "SELECT * FROM p_ordre where ordreid = ?";
        PreparedStatement statement = null;
        Order o = null;
        try {
            statement = con.prepareStatement(SQLString);
            statement.setInt(1, ordreid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                OrderId = rs.getInt(1);
                kunde_fk = rs.getInt(2);
                pris = rs.getInt(3);
                confirm = rs.getInt(4);
                versionsnr = rs.getInt(5);
                status = rs.getInt(6);
                datostart = rs.getString(7);
                datoslut = rs.getString(8);
                o = new Order(OrderId, kunde_fk, pris, confirm, versionsnr, status, datostart, datoslut);
            }
        } catch (Exception e) {
            System.out.println("Fail in orderMapper - getNextOrderDetailNo");
            e.printStackTrace();
        }
        return o;
    }

    public boolean UpdateOrder(Order o, Connection con) throws SQLException {
        int rowupdated = 0;
        String sqlString = "UPDATE p_ordre set PRIS= ?, kundeconfirm = ?, DATOSTART = ?, DATOSLUT = ? "
                + "WHERE ORDREID = ?";

        PreparedStatement statement = null;
        statement = con.prepareStatement(sqlString);

        statement.setInt(1, o.getPris());
        statement.setInt(2, o.getKunde_confirm());
        statement.setString(3, o.getDatoStart());
        statement.setString(4, o.getDatoSlut());
        statement.setInt(5, o.getOrdreID());
        rowupdated = statement.executeUpdate();

        return rowupdated == 1;
    }
}
