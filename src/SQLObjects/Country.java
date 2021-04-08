package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 *
 * @author Brandon Breecher
 */
public class Country {
    
    private static String countryId;
    private String country, createdBy, lastUpdateBy;
    private Calendar createDate, lastUpdate;

    
    public Country(String countryId, String country) {
        
        this.countryId = countryId;
        this.country = country;
        
    }

    public static String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    
    
    public static int selectStatement(String country) throws SQLException {

        PreparedStatement checkCountry = DBConnection.getConnection().prepareStatement("select * from country where country = \"" + country + "\"");
        checkCountry.execute();
        ResultSet rs = checkCountry.getResultSet();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
        
    }
    
    public static String insertStatement(String country, String currentUser) throws SQLException {
        
        PreparedStatement psCountry = DBConnection.getConnection().prepareStatement("insert into country (country, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, \"" + LocalDateTime.now() + "\", ?, \"" + LocalDateTime.now() + "\", ?)");
        psCountry.setString(1, country);
        psCountry.setString(2, currentUser);
        psCountry.setString(3, currentUser);

        psCountry.execute();

        return country;
        
    }
    
    public static String selectCountryId(String country) throws SQLException {
        
        PreparedStatement psCountryId = DBConnection.getConnection().prepareStatement("select countryId from country where country = \"" + country + "\"");
        ResultSet rs = psCountryId.executeQuery();
        if (rs.next()) {
            countryId = rs.getString(1);
        }

        return countryId;
        
    }
    
    public static Country getCountry(String country) throws SQLException {
        
        Country getCountry = new Country(countryId, country);      
        PreparedStatement psGetCountry = DBConnection.getConnection().prepareStatement("select countryId, country from country where country = \"" + country + "\"");
        ResultSet rs = psGetCountry.executeQuery();

        
        if (rs.next()) {
            countryId = rs.getString(1);
            country = rs.getString(2);

            getCountry.setCountryId(countryId);
            getCountry.setCountry(country);
        }

        return getCountry;
        
    }
    
}
