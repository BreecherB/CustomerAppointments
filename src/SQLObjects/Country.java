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
    
    //Determines if the given country is in the database and returns 0 if it is not
    public static int selectStatement(String country) throws SQLException {

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from country where country = ?");
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
    }
    
    //Inserts a country into the database
    public static void insertStatement(String country, String currentUser) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("insert into country (country, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, \"" + LocalDateTime.now() + "\", ?, \"" + LocalDateTime.now() + "\", ?)");
        ps.setString(1, country);
        ps.setString(2, currentUser);
        ps.setString(3, currentUser);
        ps.execute();
    }
    
    //Gets the countryId for the given country
    public static String selectCountryId(String country) throws SQLException {
        
        String id = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select countryId from country where country = ?");
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getString(1);
        }

        return id;
    }
    
    public static Country getCountry(String country) throws SQLException {
        
        String id = null;
        Country getCountry = new Country(id, country);      
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select countryId, country from country where country = ?");
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            id = rs.getString(1);
            country = rs.getString(2);

            getCountry.setCountryId(id);
            getCountry.setCountry(country);
        }

        return getCountry;
    }
}
