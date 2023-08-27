package com.dao;

//import com.dao.SQLQueryConstants;

import com.admin.Admin;
import com.bill.Bill;
import com.bill.BillDetails;

import com.stockDetails.IncomingStocks;
import com.stockDetails.InventoryStorageDetails;
import com.tablets.Tablet;
import com.tablets.TabletInfo;
import com.user.User;

import logs.AuditLog;
import logs.LogDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

public class DBUtil {

	
	/*----------------------------------------users section(user and admins operations)--------------------*/
    public static boolean addUserDB(User user)
    {
        boolean status=false;
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(SQLQueryConstants.ADD_USER);
            ps.setString(1,user.getUser_email());
            ps.setString(2,user.getUser_name());
            ps.setString(3,user.getUser_password());
            int result=ps.executeUpdate();
            if(result>0)
            	status=true;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }
    public static boolean addAdminDB(Admin admin)
    {
        boolean status=false;
        try
        {
            Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(SQLQueryConstants.ADD_ADMIN);
            ps.setString(1,admin.getAdmin_email());
            ps.setString(2,admin.getAdmin_name());
            ps.setString(3,admin.getAdmin_password());
            int result=ps.executeUpdate();
            if(result>0)
            	status=true;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }
    public static User getUserDB(String username,String password)
    {
        
        User user=null;
        try
        {
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_USER);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                user=new User();
                user.setUser_id(rs.getInt(1));
                user.setUser_name(rs.getString(2));
                user.setUser_email(rs.getString(3));
                user.setUser_password(rs.getString(4));
                return user;

            }



        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public static Admin getAdminDB(String username,String password)
    { 
        Admin admin=null;
        try
        {
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_ADMIN);
            ps.setString(1,username);
            ps.setString(2,password);
           ResultSet rs=ps.executeQuery();
            while(rs.next()) {
            	admin=new Admin();
            	 admin.setAdmin_id(rs.getInt(1));
            	 admin.setAdmin_name(rs.getString(2));
            	 admin.setAdmin_password(rs.getString(3));
            	 admin.setAdmin_email(rs.getString(4));
            	  
            }
           

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return admin;
    }
    
    public static  boolean removeUser(int userId)
    {
    	
    	try {
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.REMOVE_USER);
    		ps.setInt(1, userId);
    		int status=ps.executeUpdate();
    		if(status>0)
    			return true;
    		else 
    			return false;
    		
    	}catch(Exception e)
    	{
    		System.out.println("failed to remove user");
    		e.printStackTrace();
    	}
		return false;
    }
    
    public static User getUserByIdDB(int id)
    {
    	User user=null;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_USER_BY_ID);
    		ps.setInt(1,id);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			user=new User();
    			user.setUser_id(rs.getInt(1));
    			user.setUser_name(rs.getString(2));
    			user.setUser_email(rs.getString(3));
    		}
    		
    		
    	}catch(Exception e)
    	{
    		System.out.println("getting user by id failed");
    		e.printStackTrace();
    	}
    	return user;
    }
    
    public static List<User> getAllUsersDB()
    {
    	List<User> users=new ArrayList<>();
    	User user=null;
    	
    	
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_ALL_USERS);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			user=new User();
    			user.setUser_id(rs.getInt(1));
    			user.setUser_name(rs.getString(2));
    			user.setUser_email(rs.getString(3));
    			users.add(user);
    		}
    		
    		
    	}catch(Exception e)
    	{
    		System.out.println("getting user by id failed");
    		e.printStackTrace();
    	}
    	return users;
    	
    }
    
    /*-------------------------bills and their operations and details--------------------------------------------------------------*/
    
    public static List<Bill> getTodayBillsDB(String date)
    {
        List<Bill> presentDayBills=new ArrayList<>();
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(SQLQueryConstants.PRESENT_DAY_BILLS);
            ps.setString(1,date);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                Bill bill=new Bill();
                bill.setBill_id(rs.getInt(1));
                bill.setBill_date(rs.getString(2));
                bill.setBill_amount(rs.getFloat(3));
                presentDayBills.add(bill);
            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return presentDayBills;

    }
    public static List<BillDetails> getBillDetailsDB(int bill_id)
    {
         List<BillDetails> billDetails =new ArrayList<>();


        try
        {
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(SQLQueryConstants.BILL_DETAILS);
            ps.setInt(1,bill_id);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                BillDetails billEntry=new BillDetails();

                billEntry.setBill_id(rs.getInt(1));
                billEntry.setTablet_id(rs.getInt(2));
                billEntry.setQuantity(rs.getInt(3));
                billEntry.setTotal_tablet_price(rs.getFloat(4));
                billEntry.setTablet_name(rs.getString(5));
                billEntry.setTablet_price(rs.getFloat(6));
                billDetails.add(billEntry);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return billDetails;
    }
    
    public  static boolean insertBillDetailsDB(BillDetails billDetails)
    {
    	boolean status=false;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.INSERT_NEW_BILL_DETAILS);
    		ps.setInt(1,billDetails.getBill_id());
    		ps.setInt(2,billDetails.getTablet_id());
    		ps.setInt(3,billDetails.getQuantity());
    		ps.setFloat(4,billDetails.getTotal_tablet_price());
    		int result=ps.executeUpdate();
    		if(result!=0)
    			status=true;
    		
    		
    		
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return status;
    }
    
    public static int insertBillDB(String date, float billAmount) {
        int id = 0;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQLQueryConstants.INSERT_NEW_BILL);
            ps.setString(1, date);
            ps.setFloat(2, billAmount);

           ResultSet rs=ps.executeQuery();
           
                
              while(rs.next())
              {
            	  id=rs.getInt(1);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    
    public static List<Bill> getAllBillsDB(int offset)
    {
    	List<Bill> allBills=new ArrayList<>();
    	Bill bill;
       try {
    	   Connection con=DBConnection.getConnection();
    	   PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_ALL_BILLS);
    	   ps.setInt(1, offset);
    	   ResultSet rs=ps.executeQuery();
    	   while(rs.next())
    	   {
    		   bill=new Bill();
    		   bill.setBill_id(rs.getInt(1));
    		   bill.setBill_date(rs.getString(2));
    		   bill.setBill_amount(rs.getFloat(3));
    		   allBills.add(bill);
    	   }
       }catch(Exception e)
       {
    	   e.printStackTrace();
       }
       return allBills;
       
    }
    
    public static List<Bill> getSpecificBillDB(int id)
    {
    	List<Bill> allBills=new ArrayList<>();
    	Bill bill;
       try {
    	   Connection con=DBConnection.getConnection();
    	   PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_SPECIFIC_BILL);
    	   ps.setInt(1, id);
    	   ResultSet rs=ps.executeQuery();
    	   while(rs.next())
    	   {
    		   bill=new Bill();
    		   bill.setBill_id(rs.getInt(1));
    		   bill.setBill_date(rs.getString(2));
    		   bill.setBill_amount(rs.getFloat(3));
    		   allBills.add(bill);
    	   }
       }catch(Exception e)
       {
    	   e.printStackTrace();
       }
       return allBills;
       
    }
    public static List<Bill> getParticularDayBill(String date,int offset)
    {
    	List<Bill> allBills=new ArrayList<>();
    	Bill bill;
       try {
    	   Connection con=DBConnection.getConnection();
    	   PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_PARTICUALR_DAY_BLL);
    	   ps.setString(1, date);
    	   ps.setInt(2, offset);
    	   ResultSet rs=ps.executeQuery();
    	   while(rs.next())
    	   {
    		   bill=new Bill();
    		   bill.setBill_id(rs.getInt(1));
    		   bill.setBill_date(rs.getString(2));
    		   bill.setBill_amount(rs.getFloat(3));
    		   allBills.add(bill);
    	   }
       }catch(Exception e)
       {
    	   e.printStackTrace();
       }
       return allBills;
       
    }
    
    public static int getParticularDayBillsCount(String date)
    {
    	int billsCount=0;
    	try {
     	   Connection con=DBConnection.getConnection();
     	   PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_PARTICUALR_DAY_BLLS_COUNT);
     	   ps.setString(1, date);
     	   ResultSet rs=ps.executeQuery();
     	   while(rs.next())
     	   {
     		   billsCount=rs.getInt(1);
     	   }
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return billsCount;
    }
    public static int allDayBillsCount()
    {
    	int allBillsCount=0;
    	try {
    		Connection con=DBConnection.getConnection();
      	    PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_ALL_BILLS_COUNT);
      	    
      	   
      	    ResultSet rs=ps.executeQuery();
      	   while(rs.next())
      	   {
      		   allBillsCount=rs.getInt(1);
      	   }
    	}catch(Exception e)
      	   {
      		   e.printStackTrace();
      	   }
    	return allBillsCount;
    }
    
    
    /*----------------------------------------tablets section and their storage details------------------------------------------------*/
    
    public static List<String> getTabletsNameDB()
    {
        List<String> tabletsName=new ArrayList<>();
        try
        {
            Connection con=DBConnection.getConnection();
            PreparedStatement ps= con.prepareStatement(SQLQueryConstants.GET_TABLETS_NAME);
            ResultSet rs= ps.executeQuery();
            while(rs.next())
            {
                tabletsName.add(rs.getString(1));
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return tabletsName;
    }
    

    public static Tablet getTabletDetailsDB(String tabletName) {
    	Tablet tablet=null;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.TABLET_DETAILS);
    		ps.setString(1,tabletName);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			tablet=new Tablet();
    			tablet.setTablet_id(rs.getInt(1));
    			tablet.setTablet_name(rs.getString(2));
    			tablet.setTablet_price(rs.getFloat(3));
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return tablet;
    }
    public static int getTabletQuantityDB(int tabletId,String locationId)
    {
    	int quantity=0;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.TABLET_QUANTITY);
    		ps.setInt(1,tabletId);
    		ps.setString(2, locationId);
    		
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			quantity=rs.getInt(1);
    		}
    		
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return quantity;
    }
    public static boolean updateTabletQuantity(String loaction_id,int tablet_id,int remaining_quantity)
    {
    	boolean status=false;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.UPDATE_QUANTITY);
    		ps.setString(2, loaction_id);
    		ps.setInt(3,tablet_id);
    		ps.setInt(1, remaining_quantity);
    		int result=ps.executeUpdate();
    		if(result>0)
    			status=true;
    		
    		
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return status;
    	
    }
    
    public static boolean addStorageDetails(InventoryStorageDetails stockDetails)
    {

    	boolean status=false;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.ADD_STORAGE_DETAILS);
    		ps.setString(1,stockDetails.getLocation_id());
    		ps.setInt(2,stockDetails.getTablet_id());
    		ps.setInt(3,stockDetails.getRemaining_quantity());
    
             
           int  result=ps.executeUpdate();
    		if(result>0)
    			status=true;
    		
    		System.out.println("try:"+status);
    		
    		
    	}catch(PSQLException e)
    	{  int currentQuantity=DBUtil.getTabletQuantityDB(stockDetails.getTablet_id(),stockDetails.getLocation_id());
    		int updateQuantity=currentQuantity+stockDetails.getRemaining_quantity();
    		if(DBUtil.updateTabletQuantity(stockDetails.getLocation_id(), stockDetails.getTablet_id(), updateQuantity))
    		{
    			status=true;
    			System.out.println("already  present stock is updated");
    		}
    		System.out.println("catch:"+status);
    		
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return status;
    
    }
    public static List<TabletInfo> getTabletInfoDB(String tabletName)
    { 
    	List<TabletInfo> tabletsInfo=new ArrayList<>();
    	TabletInfo tablet;

    
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.TABLET_INFO);
    		ps.setString(1,tabletName);
    		ResultSet rs=ps.executeQuery();
    		ps.setString(1, tabletName);
    		while(rs.next()) {
    			tablet=new TabletInfo();
    			 tablet.setTablet_id(rs.getInt(1));
    			 tablet.setTablet_name(rs.getString(2));
    			 tablet.setLocation_id(rs.getString(3));
    			 tablet.setQuantity(rs.getInt(4));
    			 tabletsInfo.add(tablet);
    		}
    	}catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	return tabletsInfo;
    }
    public static boolean addIncomingStockDB(IncomingStocks stocks)
    { 
    	boolean status=false;
    	int result=0;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.ADD_INCOMING_STOCKS);
    		ps.setInt(1, stocks.getTablet_id());
    		ps.setInt(2,stocks.getQuantity());
    		ps.setString(3,stocks.getIncoming_date());
    		ps.setFloat(4,stocks.getTotal_stock_price());
    		
           result=ps.executeUpdate();
           if(result>0)
           {
        	   status=true;
           }
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return status;
    	
    }
    public static List<String> getStorageIdsDB(int tabletId)
    {
    	List<String> locations=null;
    	try {
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_STORAGE_IDS);
    		ps.setInt(1,tabletId);
    		ResultSet rs=ps.executeQuery();
    		locations=new ArrayList<>();
    		while(rs.next())
    		{
    			locations.add(rs.getString(1));
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("fetching storage id failed");
    	}
    	return locations;
    }
    
   
   /*---------------------------------------------------audit logs and  operations-------------------------------------*/
    
    public static int insertLog(String user,int id,String time,String operation)
    { 
      int operationId=0;
    	try
    	{
    		
    		System.out.println("insertLog function is called");
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.INSERT_LOG);
    		ps.setString(1,user);
    		ps.setInt(2, id);
    		ps.setString(3, time);
    		ps.setString(4,operation);
    		ResultSet rs=ps.executeQuery();
    		if(rs.next())
    		{
    			operationId=rs.getInt(1);
    		}
    		
    			
    			
    	}catch(Exception e)
    	{
    		System.out.println("log addition failed");
    		e.printStackTrace();
    	}
    	return operationId;
    }
    public static boolean insertLogDetails(int operationId,String details)
    {
    	boolean result=false;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.INSERT_LOG_DETAILS);
    		ps.setInt(1, operationId);
    		ps.setString(2, details);
    		int status=ps.executeUpdate();
    		if(status>0)
    			result=true;
    		
    	}catch(Exception e)
    	{
    		System.out.println("logDetailsAddition Failed");
    		e.printStackTrace();
    	}
    	return result;
    }
    public static int getOperationIdDB() {
    	int id=0;
    	try {
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_OPERATION_ID);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			id=rs.getInt(1);
    			return id;
    		}
    	}catch(Exception e)
    	{
    		System.out.println("getting last operation id failed");
    		e.printStackTrace();
    	}
    	return id;
    }
    
    public static List<AuditLog> getAuditLogsDB(String date,int id,String userType)
    {
    	List<AuditLog> auditLogs=new ArrayList<>();
    	AuditLog auditLog=null;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=null;
    		
    		if(id!=0 && date!="" && userType!="")
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where id=? and date_and_time like ? and user_type=?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setInt(1,id);
    			ps.setString(2,date+"%");
    			ps.setString(3, userType);
    			
    					
    		}else if(id!=0 && date!="")
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where id=? and date_and_time like ?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setInt(1,id);
    			ps.setString(2, date+"%");
    			
    		}
    		else if(date!="" && userType!="")
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where user_type=? and date_and_time like ?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setString(1, userType);
    			ps.setString(2,date+"%");
    		}
    		else if(id!=0 && userType!="")
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where id=? and user_type=?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setInt(1, id);
    			ps.setString(2, userType);
    		}
    		else if(id!=0)
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where id=?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setInt(1, id);
    		}
    		else if(userType!="")
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where user_type=?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setString(1, userType);
    		}
    		else
    		{
    			SQLQueryConstants.GET_PARTICULAR_LOGS+=" where date_and_time like ?";
    			ps=con.prepareStatement(SQLQueryConstants.GET_PARTICULAR_LOGS);
    			ps.setString(1, date+"%");
    		}
    		ResultSet rs=ps.executeQuery();
    		SQLQueryConstants.GET_PARTICULAR_LOGS="SELECT * FROM AUDITLOGS";
    		
    		while(rs.next())
    		{
    			auditLog=new AuditLog();
    			auditLog.setOperationId(rs.getInt(1));
    			auditLog.setUserType(rs.getString(2));
    			auditLog.setId(rs.getInt(3));
    			auditLog.setDateAndTime(rs.getString(4));
    			auditLog.setOperationPerformed(rs.getString(5));
    			auditLogs.add(auditLog);
    		}
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("getting audit logs failed");
    	}
    	return auditLogs;
    }
    public static LogDetail getLogDetails(int id)
    {
    	LogDetail details=null;
    	try
    	{
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_LOG_DETAILS);
    		ps.setInt(1, id);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			details=new LogDetail();
    			details.setOperationId(rs.getInt(1));
    			details.setDetails(rs.getString(2));
    		}
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("getting audit logs failed");
    	}
    	return details;
    }
    
    public static int getTotalBillCountDB()
    {
    	int billCount=0;
    	try {
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(SQLQueryConstants.GET_TOTAL_BILLS_COUNT);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			billCount=rs.getInt(1);
    		}
    		
    	}catch(Exception e)
    	{
    		System.out.println("fetching bill count failed");
    	}
    	return billCount; 
    }
    
   
    
}