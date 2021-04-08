package Utilities;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Brandon Breecher
 */
public class DBQueries {
    
    private static PreparedStatement statement;
    
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        
        statement = conn.prepareStatement(sqlStatement);
        
    }

    public static PreparedStatement getPreparedStatement() {
        
        return statement;
        
    }
    
    
    
    
    
    
    
}
