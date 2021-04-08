package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Brandon Breecher
 */
public class User {
    
    private String username;
    private String password;
    private User user;

    public User (String username, String password) {
        
        this.username = username;
        this.password = password;
        
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

    
    public static User getUser(String username, String password) throws SQLException {
        
        User getUser = new User(username, password);      
        PreparedStatement psGetUser = DBConnection.getConnection().prepareStatement("Select userName, password from user where userName = \"" + username + "\"" + " and password = \"" + password + "\"");
        ResultSet rs = psGetUser.executeQuery();

        if (rs.next()) {
            username = rs.getString(1);
            password = rs.getString(2);
            
            getUser.setUsername(username);
            getUser.setPassword(password);
            
        } else {
            
            getUser.setUsername(null);
            getUser.setPassword(null);
            
        }

        return getUser;
        
    }
    
    public String getUserId(String username) throws SQLException {
        
        String userId;
        PreparedStatement psGetUserId = DBConnection.getConnection().prepareStatement("Select userId from user where userName = \"" + username + "\"");
        ResultSet rs = psGetUserId.executeQuery();
        
        if(rs.next()) {
            
            userId = rs.getString(1);
            
        } else
            
            userId = null;
        
        return userId;
        
    }
    
}
