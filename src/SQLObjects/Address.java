package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Brandon Breecher
 */
public class Address {
    
    private static String addressId, cityId;
    private String address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy;
    private City city;
    
    public Address(String addressId, String address, String address2, String postalCode, String phone, City city) {
    
        this.addressId = addressId;
        this.city = city;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        
    }

    public static String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public static String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    //Determines if the given address is already in the database and returns 0 if it is not
    public static int selectStatement(String address) throws SQLException {

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from address where address = ?");
        ps.setString(1, address);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
    }
    
    //Inserts an address into the database
    public static void insertStatement(String address, String city, String address2, String zip, String number, String currentUser) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, ?, (select cityId from city where city = ?), ?, ?, ?, ?, ?, ?)");
        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setString(3, city);
        ps.setString(4, zip);
        ps.setString(5, number);
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, currentUser);
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, currentUser);
        ps.execute();
    }
    
    //Returns the addressId for the given address
    public static String selectAddressId(String address) throws SQLException {
        
        String id = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select addressId from address where address = ?");
        ps.setString(1, address);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            id = rs.getString(1);
        }

        return id;
    }

    //Returns a full address object with the given address information
    public static Address getAddress(String address, String address2, String postalCode, String phone, City city) throws SQLException {
        
        String id = null;
        Address getAddress = new Address(id, address, address2, postalCode, phone, city);
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select addressId, address from address where address = ?");
        ps.setString(1, address);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            
            id = rs.getString(1);
            address = rs.getString(2);
            getAddress.setAddressId(id);
            getAddress.setAddress(address);
        }
        
        return getAddress;
    }
    
    //Updates an address in the database
    public static Address updateAddress(String addressId, String address, String address2, String postalCode, String phone, City city, String currentUser) throws SQLException {
        
        Address updateAddress = new Address(addressId, address, address2, postalCode, phone, city);
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("update address set address = ?, address2 = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? where addressId = ?");
        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, currentUser);
        ps.setString(7, addressId);
        
        ps.executeUpdate();
        
        return updateAddress;
    }
 
}
