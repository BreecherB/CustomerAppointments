package Utilities;

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

}
