package dataSource;

import java.sql.*;
import java.util.ArrayList;
import domain.*;


//===	keeps track of all changes to domain objects during a business transaction
//	defines system transaction for save  (setAutocommit(false)   >   conn.commit() 
//	2014/hau
public class UnitOfWorkProcessOrder
{   
        private ArrayList<Order> newOrders;
 
    private ArrayList<OrderDetail> newOrderDetail;

    
    public UnitOfWorkProcessOrder()
    {
       newOrders = new ArrayList<Order>();
       newOrderDetail = new ArrayList<OrderDetail>();
       
    }
    
    
    public void registerNewOrder(Order ord){
        
        if(!newOrders.contains(ord)){
            newOrders.add(ord);
        }
      
    }
    
 
    
    public void registerNewOrderDetail(OrderDetail od){
        
        if(!newOrderDetail.contains(od))
        {
            newOrderDetail.add(od);
        }
    }
    
    public boolean commit(Connection con) throws SQLException
    {
        boolean status = true;
        
        try{
            
            con.setAutoCommit(false);
            OrderMapper om = new OrderMapper();
            
            status = status && om.insertOrder(newOrders, con);
            status = status && om.insertOrderDetail(newOrderDetail, con);
            
            if (!status){
                throw new Exception("business transaction aborted");
            }
            con.commit();
            
        }catch(Exception err){
            System.out.println("Error UnitOfWorkProcessOrder Commit");
            err.printStackTrace();
            System.out.println(err);
            
            con.rollback();
            status = false;
        }
    
        return status;
    }
    
    
}
