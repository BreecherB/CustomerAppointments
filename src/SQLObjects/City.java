package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author Brandon Breecher
 */
public class City {
    
    private static String cityId, countryId;
    private String city, createDate, createdBy, lastUpdate, lastUpdateBy;
    private Country country;
    
    public City(String cityId, String city, Country country) {
        
        this.cityId = cityId;
        this.countryId = countryId;
        this.city = city;
        this.country = country;
        
    }

    public static String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public static String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    
    //Determines if a city is already in the database and returns 0 if it is not
    public static int selectStatement(String city) throws SQLException {

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from city where city = ?");
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
    }
    
    //Inserts city into the database
    public static void insertStatement(String city, String country, String currentUser) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("insert into city " + "(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values " + "(?, (select countryId from country where country = \"" + country + "\"), \""  + LocalDateTime.now() + "\", ?, \"" + LocalDateTime.now() + "\", ?)");
        ps.setString(1, city);
        ps.setString(2, currentUser);
        ps.setString(3, currentUser);
        ps.execute();
    }
    
    //Gets the cityId for the given city
    public static String selectCityId(String city) throws SQLException {
        
        String id = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select cityId from city where city = ?");
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            id = rs.getString(1);
        }

        return id;
    }
    
    //Returns a full city object with the given city information
    public static City getCity(String city, Country country) throws SQLException {
        
        String id = null;
        City getCity = new City(id, city, country);
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select cityId, city from city where city = ?");
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            id = rs.getString(1);
            city = rs.getString(2);
            getCity.setCityId(id);
            getCity.setCity(city);
            getCity.setCountry(country);
        }
        
        return getCity;
    }
}
