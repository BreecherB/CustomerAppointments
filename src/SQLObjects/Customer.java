package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Brandon Breecher
 */
public class Customer {
    
    private static String customerId, addressId;
    private String customerName, createdBy, lastUpdateBy;
    private boolean active;
    private Address address;
    private LocalDateTime createDate, lastUpdate;
    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
    
    public Customer(String customerId, boolean active, String customerName, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, Address address) {
       
        this.customerId = customerId;
        this.address = address;
        this.active = active;
        this.customerName = customerName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        
    }

    public static String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) throws SQLException {
        this.customerId = customerId;
    }

    public static String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static void setAllCustomers(ObservableList<Customer> allCustomers) {
        Customer.allCustomers = allCustomers;
    }
    
    //Determines if the customer is already in the database and returns 0 if it is not
    public static int selectStatement(String customerName) throws SQLException {

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from customer where customerName = ?");
        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();
        
        int count = 0; 
        while (rs.next())
            ++count;
        
        return count;
    }
    
    //Inserts a customer into the database
    public static void insertStatement(String address, String customerName, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, (select addressId from address where address = ?), 1, ?, ?, ?, ?)");
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(4, createdBy);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, lastUpdateBy);
        ps.execute();
    }
    
    //Updates customer in the database to the new values given
    public static void updateCustomer(String customerId, String name, String addressId, String username) throws SQLException {
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("update customer set customerName = ?, addressId = ?, lastUpdate = ?, lastUpdateBy = ? where customerId = ?");
        ps.setString(1, name);
        ps.setString(2, addressId);
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(4, username);
        ps.setString(5, customerId);
        ps.executeUpdate();
    }
    
    //Deletes customer with the given customerId
    public static void deleteCustomer(String customerId) throws SQLException {
                
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("delete from customer where customerId = ?");
        ps.setString(1, customerId);
        ps.execute();
    }
    
    //Gets the customerId for the given customer
    public static String selectCustomerId(String customerName) throws SQLException {
       
        String id = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select customerId from customer where customerName = ?");
        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            id = rs.getString(1);
        }  
        
        return id;
    }
    
    //Returns a full customer object with the given customer information
    public static Customer getCustomer (boolean active, String customerName, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, Address address) throws SQLException {
        
        String id = null;
        Customer getCustomer = new Customer(id, active, customerName, createDate, createdBy, lastUpdate, lastUpdateBy, address);
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select customerId, customerName from customer where customerName = ?");
        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            id = rs.getString(1);
            customerName = rs.getString(2);
            
            getCustomer.setCustomerId(id);
            getCustomer.setCustomerName(customerName);
            getCustomer.setAddress(address);
        }
        
        return getCustomer;
    }
    
    //Returns customer object for a given customerId
    public static Customer getCustomer(String customerId) throws SQLException {
        
        String name;
        LocalDateTime customerCreateDate;
        String customerCreatedBy;
        LocalDateTime customerLastUpdate;
        String customerLastUpdateBy;
        String customerAddress;
        String customerAddress2;
        String postalCode;       
        String city;
        String country;
        String phone;
        
        Country countryObject;
        City cityObject;
        Address addressObject;
        Customer customerObject = null;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select customer.customerName, customer.createDate, customer.createdBy, customer.lastUpdate, customer.lastUpdateBy, address.address, city.city, country.country, address.phone, address.address2, address.postalCode from customer join address on customer.addressId = address.addressId join city on city.cityId = address.cityId join country on country.countryId = city.countryId where customer.customerId = \"" + customerId + "\"");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            name = rs.getString(1);
            customerCreateDate = rs.getTimestamp(2).toLocalDateTime();
            customerCreatedBy = rs.getString(3);            
            customerLastUpdate = rs.getTimestamp(4).toLocalDateTime();
            customerLastUpdateBy = rs.getString(5);
            customerAddress = rs.getString(6);
            city = rs.getString(7);
            country = rs.getString(8);
            phone = rs.getString(9);
            customerAddress2 = rs.getString(10);
            postalCode = rs.getString(11);
            
            countryObject = Country.getCountry(country);
            cityObject = City.getCity(city, countryObject);
            addressObject = Address.getAddress(customerAddress, customerAddress2, postalCode, phone, cityObject);
            customerObject = getCustomer(true, name, customerCreateDate, customerCreatedBy, customerLastUpdate, customerLastUpdateBy, addressObject);
        }
        
        return customerObject;
    }
    
    //Retrieves all the current customers from the database
    public static ObservableList<Customer> getCurrentCustomers(ObservableList<Customer> list) throws SQLException {
        
        String name;
        LocalDateTime createDate;
        String createdBy;
        LocalDateTime lastUpdate;
        String lastUpdateBy;
        String customerAddress;
        String customerAddress2;
        String postalCode;       
        String city;
        String country;
        String phone;
        
        Country countryObject;
        City cityObject;
        Address addressObject;
        Customer customerObject;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select customer.customerName, customer.createDate, customer.createdBy, customer.lastUpdate, customer.lastUpdateBy, address.address, city.city, country.country, address.phone, address.address2, address.postalCode from customer join address on customer.addressId = address.addressId join city on city.cityId = address.cityId join country on country.countryId = city.countryId");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            name = rs.getString(1);
            createDate = rs.getTimestamp(2).toLocalDateTime();
            createdBy = rs.getString(3);            
            lastUpdate = rs.getTimestamp(4).toLocalDateTime();
            lastUpdateBy = rs.getString(5);
            customerAddress = rs.getString(6);
            city = rs.getString(7);
            country = rs.getString(8);
            phone = rs.getString(9);
            customerAddress2 = rs.getString(10);
            postalCode = rs.getString(11);
            
            countryObject = Country.getCountry(country);
            cityObject = City.getCity(city, countryObject);
            addressObject = Address.getAddress(customerAddress, customerAddress2, postalCode, phone, cityObject);
            customerObject = getCustomer(true, name, createDate, createdBy, lastUpdate, lastUpdateBy, addressObject);
            list.add(customerObject);
        }
        
        return list;
    }
}
