package Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 *
 * @author Brandon Breecher
 */
public class Time {

    public static LocalDateTime toUTCTime(LocalDateTime ldt, ZoneId zoneId) {
       
        ZonedDateTime currentZDT = ZonedDateTime.of(ldt, zoneId);
        ZoneId utcId = ZoneId.of("UTC");
        LocalDateTime utcTime = currentZDT.withZoneSameInstant(utcId).toLocalDateTime();
        
        return utcTime;
        
    }
    
    public static ZonedDateTime toCurrentTime(LocalDateTime ldt, ZoneId zoneId) {
        
        ZoneId utcId = ZoneId.of("UTC");
        ZonedDateTime utcTime = ZonedDateTime.of(ldt, utcId);
        ZonedDateTime currentTime = utcTime.withZoneSameInstant(zoneId);
        
        return currentTime;
        
    }
    
    public static LocalTime chicagoToUTCTime(LocalTime chicago, LocalDate datePicker) {
        
        LocalTime utcTime;
        ZonedDateTime chicagoZDT = ZonedDateTime.of(datePicker, chicago, ZoneId.of("America/Chicago"));
        ZonedDateTime utcZDT = chicagoZDT.withZoneSameInstant(ZoneId.of("UTC"));
        utcTime = utcZDT.toLocalTime();
        
        return utcTime;
        
    }
    
    public static LocalTime utcToCurrentTime(LocalTime utc, LocalDate datePicker) {
        
        LocalTime currentTime;
        ZonedDateTime utcZDT = ZonedDateTime.of(datePicker, utc, ZoneId.of("UTC"));
        ZonedDateTime currentZDT = utcZDT.withZoneSameInstant(ZoneId.systemDefault());
        currentTime = currentZDT.toLocalTime();
        
        return currentTime;
        
    }
    
    public static LocalDateTime utcToCurrentTime(LocalDateTime utc) {
        
        LocalDateTime currentTime;
        ZonedDateTime utcZDT = ZonedDateTime.of(utc, ZoneId.of("UTC"));
        ZonedDateTime currentZDT = utcZDT.withZoneSameInstant(ZoneId.systemDefault());
        currentTime = currentZDT.toLocalDateTime();
        
        return currentTime;
        
    }
    
    //Checks for conflicting appointment times outside of the current appointment
    public static Boolean timeCheck (String appointmentId, LocalDateTime startTime, LocalDateTime endTime, LocalDate datePicker) throws SQLException {
        
        Boolean isTrue = true;
        LocalDateTime checkTimeStart;
        LocalDateTime checkTimeEnd;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select start, end from appointment where date(start) = ? and appointmentId != ?");
        ps.setString(1, datePicker.toString());
        ps.setString(2, appointmentId);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            checkTimeStart = rs.getTimestamp(1).toLocalDateTime();
            checkTimeEnd = rs.getTimestamp(2).toLocalDateTime();
            if (startTime.isAfter(checkTimeEnd) && endTime.isAfter(checkTimeStart)) {
                isTrue = true;
            } else if (startTime.isBefore(checkTimeEnd) && endTime.isBefore(checkTimeStart)) {
                isTrue = true;
            } else {
                isTrue = false;
                break;
            }
        }
        
        return isTrue;
    }
    
    //Checks for conflicting appointment times when creating an appointment
    public static Boolean timeCheck (LocalDateTime startTime, LocalDateTime endTime, LocalDate datePicker) throws SQLException {
        
        Boolean isTrue = true;
        LocalDateTime checkTimeStart;
        LocalDateTime checkTimeEnd;
        
        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select start, end from appointment where date(start) = ?");
        ps.setString(1, datePicker.toString());
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            checkTimeStart = rs.getTimestamp(1).toLocalDateTime();
            checkTimeEnd = rs.getTimestamp(2).toLocalDateTime();
            if (startTime.isAfter(checkTimeEnd) && endTime.isAfter(checkTimeStart)) {
                isTrue = true;
            } else if (startTime.isBefore(checkTimeEnd) && endTime.isBefore(checkTimeStart)) {
                isTrue = true;
            } else {
                isTrue = false;
                break;
            }
        }
        
        return isTrue;
    }
    
    //Checks if there is an appointment within 15 minutes of a user logging in
    public static Boolean appointmentWithin15Minutes (LocalDateTime currentTime) throws SQLException {
        
        Boolean isWithin15Minutes = false;
        LocalDateTime time = Time.toUTCTime(currentTime, ZoneId.systemDefault());

        PreparedStatement ps = DBConnection.getConnection().prepareStatement("select start from appointment where start between ? and ?");
        ps.setString(1, time.toString());
        ps.setString(2, time.plusMinutes(15).toString());
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            isWithin15Minutes = true;
        }

        return isWithin15Minutes;
    }
}
