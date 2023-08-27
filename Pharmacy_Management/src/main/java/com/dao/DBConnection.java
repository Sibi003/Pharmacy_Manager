package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {
//    private static final String username="postgres";
//    private static final String password="sibi";
    private static final String url="jdbc:postgresql://medical-server.postgres.database.azure.com:5432/Pharmacy?user=sibi&password=adminPass@3&sslmode=require";
    private static final String loadDriver="org.postgresql.Driver";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(loadDriver);
            con = DriverManager.getConnection(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (con != null) {
            System.out.println("connection established");
            return con;
        }
        else
            System.out.println("connection failed");

        return con;

    }
}
