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

    public static int selectStatement(String address) throws SQLException {

        PreparedStatement checkAddress = DBConnection.getConnection().prepareStatement("select * from address where address = \"" + address + "\"");
        checkAddress.execute();
        ResultSet rs = checkAddress.getResultSet();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
    }
    
    public static String insertStatement(String address, String city, String address2, String zip, String number, String currentUser) throws SQLException {
        
        PreparedStatement psAddress = DBConnection.getConnection().prepareStatement("insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, ?, (select cityId from city where city = \"" + city + "\"), ?, ?, \"" + LocalDateTime.now() + "\", ?, \"" + LocalDateTime.now() + "\", ?)");
        psAddress.setString(1, address);
        psAddress.setString(2, address2);
        psAddress.setString(3, zip);
        psAddress.setString(4, number);
        psAddress.setString(5, currentUser);
        psAddress.setString(6, currentUser);

        psAddress.execute();

        return city;
        
    }
    
    public static String selectAddressId(String address) throws SQLException {
        
        PreparedStatement psAddressId = DBConnection.getConnection().prepareStatement("select addressId from address where address = \"" + address + "\"");
        ResultSet rs = psAddressId.executeQuery();
        
        if (rs.next()) {
            
            addressId = rs.getString(1);
            
        }

        return addressId;
        
    }

    public static Address getAddress(String address, String address2, String postalCode, String phone, City city) throws SQLException {
        
        Address getAddress = new Address(addressId, address, address2, postalCode, phone, city);
        PreparedStatement psGetAddress = DBConnection.getConnection().prepareStatement("select addressId, address from address where address = \"" + address + "\"");
        ResultSet rs = psGetAddress.executeQuery();

        if (rs.next()) {
            
            addressId = rs.getString(1);
            address = rs.getString(2);
            getAddress.setAddressId(addressId);
            getAddress.setAddress(address);
            getAddress.setCity(city);
            
        }
        
        return getAddress;
        
    }
    
    public static Address updateAddress(String addressId, String address, String address2, String postalCode, String phone, City city, String currentUser) throws SQLException {
        
        Address updateAddress = new Address(addressId, address, address2, postalCode, phone, city);
        PreparedStatement psUpdateAddress = DBConnection.getConnection().prepareStatement("update address set address = \"" + address + "\", address2 = \"" + address2 + "\", postalCode = \"" + postalCode + "\", phone = \"" + phone + "\", lastUpdate = \"" + LocalDateTime.now() + "\", lastUpdateBy = \"" + currentUser + "\" where addressId = \"" + addressId + "\"");
        psUpdateAddress.executeUpdate();
        
        return updateAddress;
        
    }
 
}
