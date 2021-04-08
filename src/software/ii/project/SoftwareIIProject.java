package software.ii.project;

import Utilities.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Brandon Breecher
 */
public class SoftwareIIProject extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) throws SQLException {

        Connection conn = DBConnection.getConnection();

        launch(args);
        
        DBConnection.closeConnection();
        
    }
    
}
