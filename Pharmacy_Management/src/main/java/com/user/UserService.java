package com.user;



import org.json.JSONArray;

import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.stockDetails.InventoryStorageDetails;
import com.tablets.Tablet;
import com.tablets.TabletInfo;
import com.bill.Bill;
import com.bill.BillDetails;
import com.bill.NewBill;
import com.dao.DBUtil;




import org.apache.struts2.ServletActionContext;


import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UserService extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private List<Bill> dataList = null;
    private List<BillDetails> billDetails=null;
    private List<String> tabletsName=null;

    private List<NewBill> rowdata=null;
    
    private String quantityWarning=null;
    
    private Tablet tabletData=null;
    
    private List<TabletInfo> tabletDetails=null;
    
    private List<String> storageIds=null;
    
    public List<String> getStorageIds() {
		return storageIds;
	}

	public void setStorageIds(List<String> storageIds) {
		this.storageIds = storageIds;
	}

	private User user=(User)ActionContext.getContext().getSession().get("loggedUser");
    


   

	public List<TabletInfo> getTabletDetails() {
		return tabletDetails;
	}

	public void setTabletDetails(List<TabletInfo> tabletDetails) {
		this.tabletDetails = tabletDetails;
	}

	public Tablet getTabletData() {
		return tabletData;
	}

	public void setTabletData(Tablet tabletData) {
		this.tabletData = tabletData;
	}

	/*public InventoryStorageDetails getStockData() {
		return stockData;
	}

	public void setStockData(InventoryStorageDetails stockData) {
		this.stockData = stockData;
	}*/

	public String getQuantityWarning() {
		return quantityWarning;
	}

	public void setQuantityWarning(String quantityWarning) {
		this.quantityWarning = quantityWarning;
	}

	//private HttpServletResponse response=ServletActionContext.getResponse();
    private HttpServletRequest request=ServletActionContext.getRequest();

    public List<NewBill> getRowdata() {
        return rowdata;
    }

    public void setRowdata(List<NewBill> rowdata) {
        this.rowdata = rowdata;
    }

    public List<String> getTabletsName() {
        return tabletsName;
    }

    public void setTabletsName(List<String> tabletsName) {
        this.tabletsName = tabletsName;
    }

    public List<Bill> getDataList() {
        return dataList;
    }

    public void setDataList(List<Bill> dataList) {
        this.dataList = dataList;
    }
    public void setBillDetails(List<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }
    public List<BillDetails> getBillDetails() {
        return billDetails;
    }
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


/*--------------------------------------------user service functions-------------------------------*/
    
    public String userLogin()
    {
        System.out.println("userAllowed");
        
       int operationId= DBUtil.insertLog("user",user.getUser_id(),UserService.getTodayDateAndTime(),"User Logged In");
        if(operationId>0)
        {
        	
        	String logDetails="name: "+user.getUser_name()+",email: "+user.getUser_email();
        	if(DBUtil.insertLogDetails(operationId, logDetails))
        		System.out.println("user login details logged");
        	else
        		System.out.println("user login details addition failed");
        }
        return "success";
    }

    public String userLogout()
    {
        System.out.println("user logged out");
      
       
        int operationId=DBUtil.insertLog("user",getUser().getUser_id(),UserService.getTodayDateAndTime(),"User Logged Out");
        if(operationId>0)
        {
        	
        	String logDetails="name: "+user.getUser_name()+",email: "+user.getUser_email();
        	if(DBUtil.insertLogDetails(operationId, logDetails))
        		System.out.println("user logout details logged");
        	else
        		System.out.println("user logout details addition failed");
        }
        
        ActionContext.getContext().getSession().put("loggedUser",null);
        return "success";
    }

   

	public String getTodayBills() {
		

        System.out.println("ajax call for fetching today bills for user dashboard is called");
        String filterDate=request.getParameter("date");
        if(request.getParameter("id")!=null && request.getParameter("id")!="")
        {
        	int billId=Integer.parseInt(request.getParameter("id"));
        	dataList=DBUtil.getSpecificBillDB(billId);
        	return NONE;
        }
        else if(filterDate!=null && filterDate!="")
        {
          dataList=DBUtil.getTodayBillsDB(dateFormatter(filterDate));
        }else
        {
        	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            Date date =new Date();
            String todayDate;
            todayDate = sdf.format(date);
            List<Bill> todayBills= DBUtil.getTodayBillsDB(todayDate);
            if(todayBills!=null)
                System.out.println("bills are present");
            this.dataList=todayBills;
            System.out.println(dataList);
        }

       /* GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        String jsonBills= gson.toJson(todayBills);
        try{
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(dataList);
        }catch(Exception e)
        {
            e.printStackTrace();
        }



        /*
           HttpServletResponse response = ServletActionContext.getResponse();
           response.setContentType("application/json");*/



         return NONE;
    }



    public String BillInfo()
    {
         HttpServletRequest request=ServletActionContext.getRequest();
        try {
            Integer bill_id = null;
            if(request.getParameter("Bill_id")!=null){
                bill_id = Integer.parseInt(request.getParameter("Bill_id"));
            }
            System.out.println("bill_id:"+bill_id);
            billDetails = DBUtil.getBillDetailsDB(bill_id);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(billDetails!=null)
            System.out.println("billdetails is fetched from billinfo method");
        else {
           System.out.println("error fetching bill details");
        }
        return NONE;
    }


    public String populateTabletsName()
    {
       tabletsName= DBUtil.getTabletsNameDB();
       return NONE;
    }
    
    public String fetchStorageIds()
    {
    	String tabletName=null;
    	if(request.getParameter("tabletName")!=null)
    	{
    		 tabletName=request.getParameter("tabletName");
    	}
    
    	if(tabletName!=null)
    	{
    		
    		int id=DBUtil.getTabletDetailsDB(tabletName).getTablet_id();
    		
    		storageIds=DBUtil.getStorageIdsDB(id);
    		
    		
    	}
    	return NONE;
    }


    public String generateNewBill() {
        System.out.println("generate new bill is called");
        BillDetails billRow=null;
        List<BillDetails> billDetails=new ArrayList<>();
        try {
            String jsonString = request.getParameter("rowdata");
            System.out.println(jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);

            rowdata = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleData = jsonArray.getJSONObject(i);
                NewBill newBill = new NewBill();
                newBill.setTabletName(singleData.getString("tabletName"));
                newBill.setQuantity(singleData.getInt("quantity"));
                newBill.setLocationId(singleData.getString("locationId"));
                rowdata.add(newBill);
                
            } 
            int totalBillSum=0;
            
            boolean billFlag=false;
        	int properBillCount=0;
        	
            for(int j=0;j<rowdata.size();j++) {
            	Tablet tablet=DBUtil.getTabletDetailsDB(rowdata.get(j).getTabletName());
        		if(rowdata.get(j).getQuantity()<=DBUtil.getTabletQuantityDB(tablet.getTablet_id(),rowdata.get(j).getLocationId()))
        		{
        			properBillCount++;
        			
        		}else {
        			quantityWarning=tablet.getTablet_name()+" remaining:"+DBUtil.getTabletQuantityDB(tablet.getTablet_id(),rowdata.get(j).getLocationId());
        			break;
      
        		}
        			
        	}
            
            if(properBillCount==rowdata.size())
        	{
        		billFlag=true;
        	}
        	
            
            for(int i=0;i<rowdata.size();i++) {
            	billRow=new BillDetails();
            	
            	Tablet tablet=DBUtil.getTabletDetailsDB(rowdata.get(i).getTabletName());
            	
            	
            	

            	if(billFlag)
            	{
            		float totalPrice=(tablet.getTablet_price())*(rowdata.get(i).getQuantity());
            		billRow.setTablet_id(tablet.getTablet_id());
            		billRow.setQuantity(rowdata.get(i).getQuantity());
            		billRow.setTotal_tablet_price(totalPrice);
            		
            		totalBillSum+=totalPrice;
            		billDetails.add(billRow);
            		int remaininig_quantity=DBUtil.getTabletQuantityDB(tablet.getTablet_id(),rowdata.get(i).getLocationId())-rowdata.get(i).getQuantity();
            		if(DBUtil.updateTabletQuantity(rowdata.get(i).getLocationId(),tablet.getTablet_id(),remaininig_quantity))
            		{
            			System.out.println("tablet quantity updated succesfully");
            		}
            		else
            		{
            			System.out.println("tablet quantity updation failed");
            		}
            	}
            }
            if(quantityWarning==null)
            {
             int billId=DBUtil.insertBillDB(getTodayDate(),totalBillSum);
                if(billId!=0)
                {
                	
                	for(int i=0;i<billDetails.size();i++)
                	{
                		billDetails.get(i).setBill_id(billId);
                		boolean insertDetail=DBUtil.insertBillDetailsDB(billDetails.get(i));
                		if(insertDetail)
                		{
                			System.out.print("bill details added succefully");
                			
                			Bill bill=DBUtil.getSpecificBillDB(billId).get(0);
                			if(bill!=null)
                			{
                				int operationId=DBUtil.insertLog("user",user.getUser_id(),UserService.getTodayDateAndTime(),"Added New Bill");
                				if(operationId>0)
                		        {
                		        	
                		        	String details="bill_id: "+bill.getBill_id()+",bill_date: "+bill.getBill_date()+",bill_amount: "+bill.getBill_amount();
                		        	if(DBUtil.insertLogDetails(operationId, details))
                		        	{
                		        		System.out.println("new bill addition logged successfully");
                		        	}
                    				
                		        }
                				
                			}
                		}
                		
                	}
                }
            	
                else
                {
                	System.out.println("new bill addition failed");
                	return "failure";
                }
            	
            
            }
            
            
            
            
      
        } catch (Exception e) {
            e.printStackTrace();
        }

        return NONE;
    }
  
    
    public String addStocks()
    {
    	InventoryStorageDetails stockDetails=new InventoryStorageDetails();
    	String stockData=request.getParameter("stockData");
    	JSONObject jsonObject = new JSONObject(stockData);
    	String location=jsonObject.getString("location_Id");
    	String name=jsonObject.getString("tablet_Id");
    	int quantity=jsonObject.getInt("quantity");
    	
    	Tablet tablet=DBUtil.getTabletDetailsDB(name);
    	int id=tablet.getTablet_id();
         
    	stockDetails.setLocation_id(location);
    	stockDetails.setTablet_id(id);
    	stockDetails.setRemaining_quantity(quantity);
    	
    	
    	boolean result=DBUtil.addStorageDetails(stockDetails);
    	if(result)
    	{
    		System.out.println("stock data added successfully");
    		int operationId=DBUtil.insertLog("user",user.getUser_id(),UserService.getTodayDateAndTime(),"Added new Stocks");
    		if(operationId>0)
    		{
    		
    			String details="Location: "+location+",tablet: "+name+",quantity: "+quantity;
    			if(DBUtil.insertLogDetails(operationId, details));
    			{
    				System.out.println("new stock addition is logged");
    			}
    		}
    		return NONE;
    	}else
    	{
    		System.out.println("stock addtiton failed");
    	}
    	return "failure";
    }
    
    
    public String updateStockPositions()
    {
    	String stockData=request.getParameter("stockData");
    	JSONObject jsonObject = new JSONObject(stockData);
    	String from=jsonObject.getString("fromPosition");
    	String to=jsonObject.getString("toPosition");
    	int quantity=jsonObject.getInt("quantity");
    	String name=jsonObject.getString("tablet_Id");
    	Tablet tablet=DBUtil.getTabletDetailsDB(name);
    	int id=tablet.getTablet_id();
    	
    	int removedQuantity=DBUtil.getTabletQuantityDB(id, from)-quantity;
    	int addedQuantity=DBUtil.getTabletQuantityDB(id, to)+quantity;
    	 
        boolean addUpdate=DBUtil.updateTabletQuantity(to, id, addedQuantity);
        boolean removeUpdate=DBUtil.updateTabletQuantity(from, id, removedQuantity);
        
        if(addUpdate && removeUpdate)
        {
        	System.out.print("the stock exchange details updated");
        	
        	int operationId=DBUtil.insertLog("user",user.getUser_id(),UserService.getTodayDateAndTime(),"Stock position Updated");
        	if(operationId>0)
    		{
    			
    			String details="tablet: "+name+",Moved from: "+from+",to: "+to+",quantity: "+quantity;
    			if(DBUtil.insertLogDetails(operationId, details));
    			{
    				System.out.println("new stock addition is logged");
    			}
    		}
        	
        	return SUCCESS;
        	
        }
        return NONE;
    	
    	
    }
   /*------------------------------------generic functions--------------------------*/
    public String tabletDetails()
    {
    	
    	String name=request.getParameter("tabletName");
    	tabletDetails=DBUtil.getTabletInfoDB(name);
    	if(tabletDetails!=null)
    	{
    		System.out.println("tablets info fetched");
    	}
    	return NONE;
    	
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



}
