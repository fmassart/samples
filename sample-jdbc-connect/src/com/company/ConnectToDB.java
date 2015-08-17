package com.company;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Inspired from http://www.tutorialspoint.com/jdbc/jdbc-sample-code.htm.
 */
public class ConnectToDB {

    private enum DBType {
        MYSQL, SQLSERVER
    }

    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String JDBC_DRIVER_KEY = "driver_key";
    public static final String JDBC_URL_KEY = "url_key";
    public static final String JDBC_USERNAME_KEY = "user_key";
    public static final String JDBC_PASSWORD_KEY = "pass_key";

    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "username";
    static final String PASS = "password";

    public static void main(String[] args) {
        ConnectToDB connectToDB = new ConnectToDB();
        Map<String, String> dbArgs = connectToDB.getArgs(args);
        connectToDB.connect(dbArgs);
    }

    /**
     * @param args ARG 0 : db type : "mysql" or "sqlserver"
     *             ARG 1 : JDBC URL
     *             ARG 2 : DB USERNAME
     *             ARG 3 : DB PASSWORD
     * @return args in a map
     */
    public Map<String, String> getArgs(String[] args) {
        Map<String, String> dbArgs = new HashMap<>();
        if (args.length == 4) {
            DBType dbType = getDBType(args[0]);
            if (dbType.equals(DBType.MYSQL)) {
                dbArgs.put(JDBC_DRIVER_KEY, MYSQL_JDBC_DRIVER);
            } else if (dbType.equals(DBType.SQLSERVER)) {
                dbArgs.put(JDBC_DRIVER_KEY, SQLSERVER_JDBC_DRIVER);
            }
            dbArgs.put(JDBC_URL_KEY, args[1]);
            dbArgs.put(JDBC_USERNAME_KEY, args[2]);
            dbArgs.put(JDBC_PASSWORD_KEY, args[3]);
        }
        return dbArgs;
    }

    private DBType getDBType(String db) {
        DBType dbType = null;
        if (db != null) {
            switch (db.trim()) {
                case "mysql":
                    dbType = DBType.MYSQL;
                    break;
                case "sqlserver":
                    dbType = DBType.SQLSERVER;
            }
        }
        return dbType;
    }

    public void connect(Map<String, String> dbArgs) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(dbArgs.get(JDBC_DRIVER_KEY));

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(
                    dbArgs.get(JDBC_URL_KEY), dbArgs.get(JDBC_USERNAME_KEY), dbArgs.get(JDBC_PASSWORD_KEY));

            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT 1 + 1 AS solution";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int solution = rs.getInt("solution");
                System.out.println("Solution: " + solution);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
