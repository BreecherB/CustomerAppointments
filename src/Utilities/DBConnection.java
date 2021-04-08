package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Brandon Breecher
 */
public class DBConnection {
    
    private static final String protocol = "JDBC";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/U06Zgf";
    
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;
    
    private static final String username = "U06Zgf";
    private static final String password = "53688911317";
    
    
    
    public static Connection getConnection () {
        
        try {
            
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);

        } 
        
        catch (ClassNotFoundException e) {
            
            System.out.println("Error: " + e.getMessage());
            
        }
        
        catch (SQLException e) {
            
            System.out.println("Error: " + e.getMessage());
            
        }
        
        return conn;
        
    }
    
    public static void closeConnection () {
        
        try {
            
            conn.close();
            System.out.println("Connection closed!");
            
        } 
        
        catch(SQLException e) {
            
            System.out.println("Error: " + e.getMessage());
            
        }
    }
    
    
}
