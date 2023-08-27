package com.dao;

public class SQLQueryConstants {

    public static final String GET_USER="SELECT * FROM USERS WHERE USER_EMAIL=? AND USER_PASSWORD=?";
    public static final String GET_TABLETS_NAME="SELECT TABLET_NAME FROM TABLETS";
    public static final String INSERT_NEW_BILL="INSERT INTO BILLS(BILL_DATE,BILL_AMOUNT) VALUES(?,?) RETURNING BILL_ID";
    public static final String INSERT_NEW_BILL_DETAILS="INSERT INTO BILL_DETAILS (BILL_ID,TABLET_ID,QUANTITY,TOTAL_TABLET_PRICE) VALUES(?,?,?,?)";
    public static final String TABLET_DETAILS="SELECT * FROM TABLETS WHERE TABLET_NAME=?";
    public static final String TABLET_QUANTITY="SELECT REMAINING_QUANTITY FROM INVENTORY_STORAGE_DETAILS WHERE TABLET_ID=? AND LOCATION_ID=?";
    public static final String UPDATE_QUANTITY="UPDATE INVENTORY_STORAGE_DETAILS SET REMAINING_QUANTITY=? WHERE LOCATION_ID=? AND TABLET_ID=?";
    public static final String ADD_STORAGE_DETAILS="INSERT INTO INVENTORY_STORAGE_DETAILS(LOCATION_ID,TABLET_ID,REMAINING_QUANTITY) VALUES(?,?,?)";
    public static final String UPDATE_STOCK_POSITION="UPDATE INVENTORY_STORAGE_DETAILS  SET REMAINING_QUANTITY=? WHERE LOCATION_ID=? AND TABLET_ID=?";
    public static final String TABLET_INFO="select tablet_id,tablet_name,location_id,remaining_quantity from inventory_storage_details \n"
    		+ "inner join tablets using(tablet_id) WHERE TABLET_NAME=?";
    public static final String GET_STORAGE_IDS="SELECT LOCATION_ID FROM INVENTORY_STORAGE_DETAILS WHERE TABLET_ID=? AND REMAINING_QUANTITY>0";
    
    
    
    
    /*------------------queries common to both user and admin---------------------------------*/
    
    public static final String BILL_DETAILS="SELECT * FROM BILL_DETAILS INNER JOIN TABLETS USING(TABLET_ID) WHERE BILL_ID=?";
    public static final String GET_ALL_BILLS="SELECT * FROM BILLS OFFSET ? LIMIT 2";
    public static final String GET_SPECIFIC_BILL="SELECT * FROM BILLS WHERE BILL_ID=?";
    public static final String GET_PARTICUALR_DAY_BLL="SELECT * FROM BILLS WHERE BILL_DATE=? OFFSET ?";
    public static final String PRESENT_DAY_BILLS="SELECT * FROM BILLS WHERE BILL_DATE=?";
    public static final String GET_PARTICUALR_DAY_BLLS_COUNT="SELECT COUNT(*) FROM BILLS WHERE BILL_DATE=?";
    public static final String GET_ALL_BILLS_COUNT="SELECT COUNT(*) FROM BILLS";
    
    
    /* ----------------------Admin Section related queries-------------------------------------*/
    
    public static final String ADD_USER="INSERT INTO USERS(USER_EMAIL,USER_NAME,USER_PASSWORD) VALUES(?,?,?)";
    public static final String ADD_ADMIN="INSERT INTO ADMINS(ADMIN_EMAIL,ADMIN_NAME,ADMIN_PASSWORD) VALUES(?,?,?)";
    public static final String GET_ADMIN="SELECT * FROM ADMINS WHERE ADMIN_EMAIL=? AND ADMIN_PASSWORD=?";
    public static final String ADD_INCOMING_STOCKS="INSERT INTO INCOMING_STOCKS(TABLET_ID,QUANTITY,INCOMING_DATE,TOTAL_STOCK_PRICE)"
    		+ "VALUES(?,?,?,?)";
    public static final String REMOVE_USER="DELETE FROM USERS WHERE USER_ID=?";
    public static final String INSERT_LOG="INSERT INTO AUDITLOGS(USER_TYPE,ID,DATE_AND_TIME,OPERATION_PERFORMED) VALUES(?,?,?,?) RETURNING OPERATION_ID";
    public static final String INSERT_LOG_DETAILS="INSERT INTO LOGOPERATIONSDETAILS(OPERATIONID,DETAILS) VALUES(?,?)";
    public static final String GET_OPERATION_ID="SELECT * FROM AUDITLOGS ORDER BY OPERATION_ID DESC LIMIT 1";
    public static final String GET_USER_BY_ID="SELECT * FROM USERS WHERE USER_ID=?";
    public static final String GET_AUDIT_LOGS="SELECT * FROM AUDITLOGS";
    public static final String GET_LOG_DETAILS="SELECT * FROM LOGOPERATIONSDETAILS WHERE OPERATIONID=?";
    public static final String GET_ALL_USERS="SELECT * FROM USERS";
   // public static final String GET_PARTICULAR_DATE_LOGS="SELECT * FROM AUDITLOGS WHERE DATE_AND_TIME LIKE '?%' ";
  //  public static final String GET__PARTICULAR_USER_TYPE_LOGS="SELECT * FROM AUDITLOGS WHERE USER_TYPE=? ";
    //public static final String GET_PARTICULAR_ID_LOGS="SELECT * FROM AUDITLOGS WHERE ID=?";
   // public static final String LOG_FILTERED_BY_DATE_AND_ID_AND_USER_TYPE="SELECT * FROM AUDITLOGS WHERE (USER_TYPE IS ? OR ? IS NULL OR ? IS '') ";
    public static  String GET_PARTICULAR_LOGS="SELECT * FROM AUDITLOGS";
    public static final String GET_TOTAL_BILLS_COUNT="SELECT COUNT(*) FROM BILLS";
    
    
    
    
}
   
