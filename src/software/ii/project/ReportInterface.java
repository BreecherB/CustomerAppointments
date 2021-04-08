package software.ii.project;

import SQLObjects.Appointment;
import javafx.collections.ObservableList;

/**
 *
 * @author Brandon Breecher
 */
public interface ReportInterface {
    
    //int value returning abstract method
    int addAppointments(ObservableList<Appointment> a);
    
}
