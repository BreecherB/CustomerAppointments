package SQLObjects;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Brandon Breecher
 */
public class TypeReport {
    
    private String type;
    private int count;
    private static ObservableList typeCount = FXCollections.observableArrayList();
    
    public TypeReport(String type, int count) {
        
        this.type = type;
        this.count = count;
        
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    

    
}
