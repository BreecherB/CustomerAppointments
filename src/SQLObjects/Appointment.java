package SQLObjects;

import Utilities.DBConnection;
import Utilities.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import javafx.collections.ObservableList;

/**
 *
 * @author Brandon Breecher
 */
public class Appointment {
    
    private String type, appointmentId, userId;
    private LocalDate date;
    private LocalTime start, end;
    private Customer customer;
    
    private static ObservableList<Appointment> allAppointments;
    
    public Appointment (String appointmentId, Customer customer, String userId, String type, LocalDate date, LocalTime start, LocalTime end) {
        
        this.appointmentId = appointmentId;
        this.type = type;
        this.customer = customer;
        this.userId = userId;
        this.date = date;
        this.start = start;
        this.end = end;
        
    }
  
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static void setAllAppointments(ObservableList<Appointment> allAppointments) {
        Appointment.allAppointments = allAppointments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public static void addAppointment (String customerId, String userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) throws SQLException {
        
        PreparedStatement psAppointment = DBConnection.getConnection().prepareStatement("insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, ?, ?, ?, ?, ?, ?, ?, \"" + start + "\", \"" + end + "\", \"" + createDate + "\", ?, \"" + lastUpdate + "\", ?)");
        
        psAppointment.setString(1, customerId);
        psAppointment.setString(2, userId);
        psAppointment.setString(3, title);
        psAppointment.setString(4, description);
        psAppointment.setString(5, location);
        psAppointment.setString(6, contact);
        psAppointment.setString(7, type);
        psAppointment.setString(8, url);
        psAppointment.setString(9, createdBy);
        psAppointment.setString(10, lastUpdateBy);

        psAppointment.execute();
        
    }
    
    public static void deleteAppointment(String appointmentId) throws SQLException {
        
        PreparedStatement psDelete = DBConnection.getConnection().prepareStatement("delete from appointment where appointmentId = \"" + appointmentId + "\"");
        psDelete.execute();
        
    }
    
    public static void updateAppointment(String appointmentId, LocalDateTime start, LocalDateTime end, String type, String currentUsername) throws SQLException {
        
        PreparedStatement psUpdate = DBConnection.getConnection().prepareStatement("update appointment set start = \"" + start + "\", end = \"" + end + "\", type = \"" + type + "\", lastUpdate = \"" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")) + "\", lastUpdateBy = \"" + currentUsername + "\" where appointmentId = \"" + appointmentId + "\"");
        psUpdate.execute();
        
    }
    
    public String startToString (LocalTime start) {
        
        String startString = start.toString();
        return startString;
        
    }
    
    public static Appointment selectAppointment(String appointmentId) throws SQLException {
        
        String userId;
        String customerId;
        String type;
        LocalDate date;
        LocalTime start;
        LocalTime end;
        Appointment appointment;
        Customer customer;
        
        PreparedStatement psSelect = DBConnection.getConnection().prepareStatement("select * from appointment where appointmentId = \"" + appointmentId + "\"");
        ResultSet rs = psSelect.executeQuery();
        
        if (rs.next()) {
            
            appointmentId = rs.getString(1);
            customerId = rs.getString(2);
            userId = rs.getString(3);
            type = rs.getString(8);
            start = rs.getTimestamp(10).toLocalDateTime().toLocalTime();
            end = rs.getTimestamp(11).toLocalDateTime().toLocalTime();
            date = rs.getTimestamp(11).toLocalDateTime().toLocalDate();
            start = Time.utcToCurrentTime(start, date);
            end = Time.utcToCurrentTime(end, date);
            
            customer = Customer.getCustomer(customerId);
            appointment = new Appointment(appointmentId, customer, userId, type, date, start, end);
            
        } else {
            
            appointment = null;
            
        }
        
        return appointment;
        
    }
    
    public static String mostRecentAddedAppointment() throws SQLException {
        
        String appointmentsId = null;
        PreparedStatement psAppointment = DBConnection.getConnection().prepareStatement("select max(appointmentId) from appointment");
        ResultSet rs = psAppointment.executeQuery();
        
        if(rs.next()) {
            
            appointmentsId = rs.getString(1);
            
        }
        
        return appointmentsId;
    }

    public static String countType(String typeToCount) throws SQLException {
        
        String countedType = null;
        PreparedStatement typePS = DBConnection.getConnection().prepareStatement("select count(type) from appointment where type = \"" + typeToCount + "\"");
        ResultSet rs = typePS.executeQuery();
        
        if(rs.next()) {
            
            countedType = rs.getString(1);
            
        }
                
        return countedType;
    }
    
}
