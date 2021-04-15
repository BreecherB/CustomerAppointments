package SQLObjects;

import Utilities.DBConnection;
import Utilities.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    //Inserts an appointment into the database
    public static void addAppointment (String customerId, String userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, customerId);
        ps.setString(2, userId);
        ps.setString(3, title);
        ps.setString(4, description);
        ps.setString(5, location);
        ps.setString(6, contact);
        ps.setString(7, type);
        ps.setString(8, url);
        ps.setTimestamp(9, Timestamp.valueOf(start));
        ps.setTimestamp(10, Timestamp.valueOf(end));
        ps.setTimestamp(11, Timestamp.valueOf(createDate));
        ps.setString(12, createdBy);
        ps.setTimestamp(13, Timestamp.valueOf(lastUpdate));
        ps.setString(14, lastUpdateBy);
        ps.execute();
    }
    
    //Deletes appointment with the given appointmentId from the database
    public static void deleteAppointment(String appointmentId) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("delete from appointment where appointmentId = ?");
        ps.setString(1, appointmentId);
        ps.execute();
    }
    
    //Updates an appointment in the database
    public static void updateAppointment(String appointmentId, LocalDateTime start, LocalDateTime end, String type, String currentUsername) throws SQLException {
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("update appointment set start = ?, end = ?, type = ?, lastUpdate = ?, lastUpdateBy = ? where appointmentId = ?");
        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));
        ps.setString(3, type);
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(5, currentUsername);
        ps.setString(6, appointmentId);
        ps.execute();
    }
    
    //Finds appointment information given an appointmentId
    public static Appointment selectAppointment(String appointmentId) throws SQLException {
        
        String userId;
        String customerId;
        String type;
        LocalDate date;
        LocalTime start;
        LocalTime end;
        Appointment appointment;
        Customer customer;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from appointment where appointmentId = ?");
        ps.setString(1, appointmentId);
        ResultSet rs = ps.executeQuery();
        
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
    
    //Gets the appointmentId from the most recently added appointment
    public static String mostRecentAddedAppointment() throws SQLException {
        
        String appointmentId = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select max(appointmentId) from appointment");
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()) {
            appointmentId = rs.getString(1);
        }
        
        return appointmentId;
    }

    //Gathers the amount of different types of appointments
    public static String countType(String typeToCount) throws SQLException {
        
        String countedType = null;
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select count(type) from appointment where type = ?");
        ps.setString(1, typeToCount);
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()) {
            countedType = rs.getString(1);
        }

        return countedType;
    }
    
    //Retrieves the list of appointments for the current day
    public static ObservableList<Appointment> todaysAppointments(LocalDate datePicker, ObservableList<Appointment> list, ObservableList<Appointment> list2) throws SQLException {
        
        String appointmentId;
        String userId;
        String customerId;
        String type;
        LocalDate date;
        LocalDateTime start, utcStart;
        LocalDateTime end, utcEnd;
        Appointment appointment;
        Customer customer;
        PreparedStatement psAppointments = DBConnection.getConnection().prepareStatement("select * from appointment");
        ResultSet rs = psAppointments.executeQuery();

        while (rs.next()) {
            
            appointmentId = rs.getString(1);
            customerId = rs.getString(2);
            userId = rs.getString(3);
            type = rs.getString(8);
            utcStart = rs.getTimestamp(10).toLocalDateTime();
            utcEnd = rs.getTimestamp(11).toLocalDateTime();
            date = rs.getTimestamp(11).toLocalDateTime().toLocalDate();
            start = Time.utcToCurrentTime(utcStart);
            end = Time.utcToCurrentTime(utcEnd);
            
            if(utcStart.getDayOfYear() > start.getDayOfYear()) {
                start.minusDays(1);
                end.minusDays(1);
            }
            
            customer = Customer.getCustomer(customerId);
            appointment = new Appointment(appointmentId, customer, userId, type, date, start.toLocalTime(), end.toLocalTime());
            
            if (date.isEqual(datePicker)) {
                list.add(appointment);
                list2.add(appointment);
            } else
                list2.add(appointment);
        }
        
        return list;
    }
    
    //Retrieves the list of appointments for the current month
    public static ObservableList<Appointment> selectCurrentMonthAppointments(LocalDate datePicker, ObservableList<Appointment> list) throws SQLException {
        
        Appointment appointment;
        int month = datePicker.getMonthValue();

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where month(start) = ?");
        ps.setInt(1, month);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            
            appointment = Appointment.selectAppointment(rs.getString(1));
            list.add(appointment);
        }
        
        return list;
    }
    
    //Retrieves the list of appointments for a 31 day period
    public static ObservableList<Appointment> selectMonthAppointments(LocalDate datePicker, ObservableList<Appointment> list) throws SQLException {
                
        Appointment appointment;

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where date(start) between ? and ?");
        ps.setString(1, datePicker.toString());
        ps.setString(2, datePicker.plusDays(30).toString());
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            appointment = Appointment.selectAppointment(rs.getString(1));
            list.add(appointment);
        }

        return list;
    }
    
    //Retrieves the list of appointments for a 7 day period
    public static ObservableList<Appointment> selectWeekAppointments(LocalDate datePicker, ObservableList<Appointment> list) throws SQLException {
        
        Appointment appointment;

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where date(start) between ? and ?");
        ps.setString(1, datePicker.toString());
        ps.setString(2, datePicker.plusDays(6).toString());
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            appointment = Appointment.selectAppointment(rs.getString(1));
            list.add(appointment);
        }
        
        return list;
    }
    
    //Retrieves the list of appointments for a given customer
    public static ObservableList<Appointment> customerAppointments(String currentCustomer, ObservableList<Appointment> list) throws SQLException {
        
        String appointmentId;
        String userId;
        String customerId;
        String type;
        LocalDate date;
        LocalTime start;
        LocalTime end;
        Appointment appointment;
        Customer customer;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select * from appointment where customerId = \"" + currentCustomer + "\"");
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            appointmentId = rs.getString(1);
            userId = rs.getString(3);
            customerId = rs.getString(2);
            type = rs.getString(8);
            start = rs.getTimestamp(10).toLocalDateTime().toLocalTime();
            end = rs.getTimestamp(11).toLocalDateTime().toLocalTime();
            date = rs.getTimestamp(11).toLocalDateTime().toLocalDate();
            start = Time.utcToCurrentTime(start, date);
            end = Time.utcToCurrentTime(end, date);
            
            customer = Customer.getCustomer(customerId);
            appointment = new Appointment(appointmentId, customer, userId, type, date, start, end);
            
            list.add(appointment);
        }
        
        return list;
    }
    
    //Retrieves a list of the different types of appointments along with a count of how many appointments with that type
    public static ObservableList typeCount (int month, ObservableList list) throws SQLException {
        
        String type1;
        int count1;
        TypeReport typeR;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select type, count(type) from appointment where month(start) = \"" + month + "\" group by type");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()){
            type1 = rs.getString(1);
            count1 = rs.getInt(2);
            typeR = new TypeReport (type1, count1);
            list.add(typeR);
        }
        
        return list;
    }
}
