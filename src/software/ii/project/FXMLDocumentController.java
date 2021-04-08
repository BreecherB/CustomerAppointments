package software.ii.project;

import SQLObjects.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Brandon Breecher
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label errorLabel;
    ResourceBundle rb1 = ResourceBundle.getBundle("Utilities/login", Locale.getDefault());
    
    String userName;
    static User currentUser;

    

    //controls log in with error notifications for incorrect or missing username/passwords
    @FXML private void loginButtonPushed(ActionEvent event) throws IOException, SQLException {
        
        String username = usernameField.getText();
        String password = passwordField.getText();

        currentUser = User.getUser(username, password);
        
        if (currentUser.getUsername() == null)
            
            errorLabel.setText(rb1.getString("error"));
        
        else {

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
            
            String filename = "UserLog.text";
            FileWriter userLogWriter = new FileWriter(filename, true);
            PrintWriter userLog = new PrintWriter(userLogWriter);
            userLog.print(username + " logged in on " + LocalDateTime.now() + '\n');
            userLog.close();
            
            currentUser.setUser(currentUser);
            
        } 

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loginButton.setText(rb1.getString("login"));
        usernameLabel.setText(rb1.getString("username"));
        passwordLabel.setText(rb1.getString("password"));

        
        
    }    
   
    
    
}
