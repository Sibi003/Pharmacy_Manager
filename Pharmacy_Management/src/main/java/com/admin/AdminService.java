package com.admin;



import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.stockDetails.IncomingStocks;
import com.tablets.Tablet;
import com.bill.Bill;
import com.bill.BillDetails;
import com.dao.DBUtil;
import com.user.User;

import logs.AuditLog;
import logs.LogDetail;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



public class AdminService extends ActionSupport {

    private static final long serialVersionUID = 1L;
    

	HttpServletRequest request=ServletActionContext.getRequest();
    
    private List<Bill> billList=null;
    
    private List<BillDetails> billDetails=null;
    
    private List<String> tabletsName=null;
    
    private List<AuditLog> auditLogs=null;
    
    private List<User> users=null;
    
    private Admin admin=(Admin)ActionContext.getContext().getSession().get("loggedAdmin");
    
    private LogDetail details=null;
    
    private int totalBills=0;
    
    public int getTotalBills() {
		return totalBills;
	}

	public void setTotalBills(int totalBills) {
		this.totalBills = totalBills;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	public LogDetail getDetails() {
		return details;
	}

	public void setDetails(LogDetail details) {
		this.details = details;
	}

	public Admin getAdmin() {
		return admin;
	}

	public List<AuditLog> getAuditLogs() {
		return auditLogs;
	}

	public void setAuditLogs(List<AuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<String> getTabletsName() {
		return tabletsName;
	}

	public void setTabletsName(List<String> tabletsName) {
		this.tabletsName = tabletsName;
	}
	String errorMsg=null;
    
   

    public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<BillDetails> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(List<BillDetails> billDetails) {
		this.billDetails = billDetails;
	}

	
	public List<Bill> getBillList() {
		return billList;
	}

	public void setBillList(List<Bill> billList) {
		this.billList = billList;
	}
/*----------------------------------------- admin service functions------------------------------*/	
	 public String adminLogin()
	    {
		 
		    int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"Admin Logged In");
		    if(operationId>0)
		    {
		    	
		    	String details="name: "+admin.getAdmin_name()+",email: "+admin.getAdmin_email();
		    	if(DBUtil.insertLogDetails(operationId, details))
		    	{
		    		System.out.println("admin login recorded");
		    	}
		    }
            
	    	return SUCCESS;
	    }
	 
	 public String adminLogout()
		{
		    System.out.println("admin logged out");
		     
		    int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"Admin Logged Out");
		    if(operationId>0)
		    {
		    	
		    	String details="name: "+admin.getAdmin_name()+",email: "+admin.getAdmin_email();
		    	
		    	if(DBUtil.insertLogDetails(operationId, details))
		    	{
		    		System.out.println("admin logout recorded");
		    	}
		    }
		    
		    ActionContext.getContext().getSession().put("loggedAdmin", null);
		    return SUCCESS;
	    }
	
	 public String specificBills()
		{
		 int offset=0;
		 
		 if(request.getParameter("offSet")!=null)
		 offset=Integer.parseInt(request.getParameter("offSet"));
		 
		
		 
		
		 
		 String date=getTodayDate();

		    billList=DBUtil.getParticularDayBill(date,offset);
		    System.out.println("all bills function called");
		    if(billList==null)
			  {
				  errorMsg="No bills Found";
					
			  }
		
		 if(errorMsg==null)
		 {
			 return SUCCESS;
		 }
		    
		    
		    
		   
		   return errorMsg;
		}
	 public String allBills() {
		 
		 int offset=0;
		 String date=null;
		 
		 if(request.getParameter("bill_id")!=null && request.getParameter("bill_id")!="") {
			 Integer id=Integer.parseInt(request.getParameter("bill_id"));
			 billList=DBUtil.getSpecificBillDB(id);
			 totalBills=billList.size();
			 
			  if(billList==null)
			  {
				  errorMsg="The Bill you Requested is not found";
				  return "failure";
					
			  }else
			  {
				  return SUCCESS;
			  }
			
		 }
		  if(request.getParameter("offSet")!=null)
		 {
			 offset=Integer.parseInt(request.getParameter("offSet"));
		 }
		  if(request.getParameter("date")!=null && request.getParameter("date").trim().length()==10)
		 {
			 date=request.getParameter("date");
			 billList=DBUtil.getParticularDayBill(dateFormatter(date), offset);
			 totalBills=DBUtil.getParticularDayBillsCount(dateFormatter(date));
			 if(billList!=null)
				 return SUCCESS;
			 else
				 return "failure";
			 
		 }
		 billList=DBUtil.getAllBillsDB(offset);
		 totalBills=DBUtil.allDayBillsCount();
		 if(billList!=null)
			 return SUCCESS;
		 else
			 return "failure";
	 }
	
	 public String billInfo()
	 {
		 if(request.getParameter("Bill_id")!=null)
		 {
			 int billId=Integer.parseInt(request.getParameter("Bill_id"));
			 billDetails=DBUtil.getBillDetailsDB(billId);
		 }else
		 {
			 System.out.println("bill id is null");
			 return NONE;
		 }
		 return "success";
	 }
	 
	 
	 public String addIncomingStocks()
	 {
		IncomingStocks newStocks=new IncomingStocks();
		    
		 
		 String stockData=request.getParameter("stockData");
		 JSONObject jsonObject=null;
			try {
				 jsonObject = new JSONObject(stockData);
			}catch(Exception e)
		    {
				System.out.println("json Object creation for incoming stocks failed");
				e.printStackTrace();
		    }
	    	
	    	String tabletName=jsonObject.getString("tabletId");
	    	int quantity=jsonObject.getInt("Quantity");
	    	float price=jsonObject.getFloat("totalPrice");
	        String date=getTodayDate();
	        
	        Tablet tablet=DBUtil.getTabletDetailsDB(tabletName);
	    	int id=tablet.getTablet_id();
	    	
	    	newStocks.setTablet_id(id);
	    	newStocks.setQuantity(quantity);
	    	newStocks.setTotal_stock_price(price);
	    	newStocks.setIncoming_date(date);
	    	
	    	if(DBUtil.addIncomingStockDB(newStocks))
	    	{
	    		System.out.println("stock addition success");
	    		 int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"Admin added new Incoming Stocks");
	    		 if(operationId>0)
	 		    {
	 		    	
	 		    	String details="name: "+tabletName+",quantity: "+quantity+",totalprice: "+price;
	 		    	if(DBUtil.insertLogDetails(operationId, details))
	 		    	{
	 		    		System.out.println("admin login recorded");
	 		    	}
	 		    }
	    		return SUCCESS;
	    	}else
	    	{
	    		return "failure";
	    	}
	 }
	
	 
	 public String addUser() {
		 User user=new User() ;
		 
		 String userData=request.getParameter("userData");
		 JSONObject jsonObject=null;
		try {
			jsonObject =new JSONObject(userData);
		}catch(Exception e)
		{
			System.out.println("json Object creation failed");
			e.printStackTrace();
		}
		 String name=jsonObject.getString("name");
		 String  email=jsonObject.getString("email");
		 String password=jsonObject.getString("password");
		 user.setUser_name(name);
		 user.setUser_email(email);
		 user.setUser_password(password);
		 if(DBUtil.addUserDB(user))
		 {
			 int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"New user is added By Admin");
			 if(operationId>0)
			 {
				 String details="name: "+name+",email: "+email+",password: "+password;
				 if(DBUtil.insertLogDetails(operationId, details)) {
					 System.out.println("new user added by admin is recorded");
				 }
			 }
			 return SUCCESS;
		 }else
		 {
			 return "failure";
		 }
		 
		 
			 
		 
	 }
	 
	 public String addAdmin()
	 {
		 Admin admin =new Admin();
		 
		 String adminData=request.getParameter("adminData");
		 JSONObject jsonObject=null;
		 try {
			 jsonObject=new JSONObject(adminData);
		 }catch(Exception e)
		 {
			 System.out.println("failed to create json object");
			 e.printStackTrace();
		 }
		 
		 String name=jsonObject.getString("name");
		 String  email=jsonObject.getString("email");
		 String password=jsonObject.getString("password");
		 admin.setAdmin_name(name);
		 admin.setAdmin_email(email);
		 admin.setAdmin_password(password);
		 
		 if(DBUtil.addAdminDB(admin))
		 {
			 int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"New admin is added By Admin");
			 if(operationId>0)
			 {
				 String details="name: "+name+",email: "+email+",password: "+password;
				 if(DBUtil.insertLogDetails(operationId, details)) {
					 System.out.println("new user added by admin is recorded");
				 }
			 }

			 return SUCCESS;
			 
		 }else
		 {
			 return "failure";
		 }

	 }
	 
	 public String removeUser()
	 {
		 
		 if(request.getParameter("userId")!=null)
		 {
			int id=Integer.parseInt(request.getParameter("userId"));
			User user=DBUtil.getUserByIdDB(id);
			if(DBUtil.removeUser(id))
			{
				 int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"Existing user is removed By Admin");
				 if(operationId>0)
				 {
					 String details="Id: "+user.getUser_id()+",name: "+user.getUser_name()+",email: "+user.getUser_email();
					 if(DBUtil.insertLogDetails(operationId, details)) {
						 System.out.println("Existing user removed by admin is recorded");
					 }
				 }
				return SUCCESS;	
			}
		 }
		 return "failure";
	 }
	 
	 public String fetchAllUsers()
	 {
		 users=DBUtil.getAllUsersDB();
		 if(users!=null)
			 return SUCCESS;
		 else
			 return "failure";
	 }

	 /*------------------------------------logs related functions-----------------------------------------*/
	 
	 public String fetchAuditLogs()
	 {
		 String logFilters=request.getParameter("logFilters");
		  JSONObject jsonObject=null;
		  try
		  {
			  jsonObject=new JSONObject(logFilters);
			 
		  String date=jsonObject.getString("logDate");
		  date=dateFormatter(date);
		 
		  String userType=jsonObject.getString("userType");
		  String id=jsonObject.getString("userId");
		  int userId=0;
		  if(id!="")
		    userId=Integer.parseInt(id);
		 
		 auditLogs=DBUtil.getAuditLogsDB(date,userId,userType);
	   }catch(Exception e)
	    {
		  System.out.println("creating jsonObject for log filters failed");
		  e.printStackTrace();
	     }
		 if(auditLogs!=null)
			 return SUCCESS;
		 else
			 return "failure";
	 }
	 
	 public String  fetchLogDetails()
	 {
		 Integer operationId=null;
		 if(request.getParameter("operationId")!=null)
		 {
			 operationId=Integer.parseInt(request.getParameter("operationId"));
		 }
		 details=DBUtil.getLogDetails(operationId);
		 if(details!=null)
			 return SUCCESS;
		 else
			 return "failure";
	 }
	 
	 
	 /*----------------------------general utility functions----------------------------------*/
	 
	 public String populateTabletsName()
	    {
	       tabletsName= DBUtil.getTabletsNameDB();
	       return NONE;
	    }
	 public String dateFormatter(String date)
	 {
		 String properDate="";
	 
		 
		 if(date!="" && date!=null) {
		 String[] dateValues=date.split("-");
		
		 properDate=dateValues[2]+"/"+dateValues[1]+"/"+dateValues[0];
		 }else
		 {
			 System.out.println("date to be formatted is null");
		 }
		 return properDate;
	 }
	 public String getTodayDate() {
	    	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	        Date date =new Date();
	        String todayDate;
	        todayDate = sdf.format(date);
	        
	        return todayDate;
	    }
	 public static String getTodayDateAndTime()
	    {
	    	Date date=new Date();
		    SimpleDateFormat formatDate=new SimpleDateFormat("dd/MM/yyyy h:mm a");
		    formatDate.setTimeZone(TimeZone.getTimeZone("IST"));
		    
		    String today=formatDate.format(date);
		    return today;
		    
	    	
	    }
	 


}
