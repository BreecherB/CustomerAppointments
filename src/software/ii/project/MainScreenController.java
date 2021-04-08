package software.ii.project;

import SQLObjects.Address;
import SQLObjects.Appointment;
import SQLObjects.City;
import Utilities.DBConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import SQLObjects.Country;
import static SQLObjects.Customer.getCustomer;
import javafx.beans.property.SimpleStringProperty;
import SQLObjects.Customer;
import SQLObjects.TypeReport;
import Utilities.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import static software.ii.project.FXMLDocumentController.currentUser;

/**
 * FXML Controller class
 *
 * @author Brandon Breecher
 */
public class MainScreenController implements Initializable {
 
    @FXML private TextField nameField;
    @FXML private TextField numberField;
    @FXML private TextField addressField;
    @FXML private TextField address2Field;    
    @FXML private TextField cityField;
    @FXML private TextField countryField;
    @FXML private TextField zipField;
    @FXML private TableView<Customer> customerTV;
    @FXML private TableView<Appointment> scheduleTV;
    @FXML private TableColumn<Appointment, LocalTime> startColumn;
    @FXML private TableColumn<Appointment, LocalTime> endColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, String> customerNameColumn;
    @FXML private TableView<Appointment> monthScheduleTV;
    @FXML private TableColumn<Appointment, LocalDate> monthDateColumn;
    @FXML private TableColumn<Appointment, LocalTime> monthStartColumn;
    @FXML private TableColumn<Appointment, LocalTime> monthEndColumn;
    @FXML private TableColumn<Appointment, String> monthTypeColumn;
    @FXML private TableColumn<Appointment, String> monthCustomerNameColumn;
    @FXML private TableView<Appointment> weekScheduleTV;
    @FXML private TableColumn<Appointment, LocalDate> weekDateColumn;
    @FXML private TableColumn<Appointment, LocalTime> weekStartColumn;
    @FXML private TableColumn<Appointment, LocalTime> weekEndColumn;
    @FXML private TableColumn<Appointment, String> weekTypeColumn;
    @FXML private TableColumn<Appointment, String> weekCustomerNameColumn;
    @FXML private DatePicker monthDatePicker;
    @FXML private DatePicker weekDatePicker;
    @FXML private TableView<Appointment> reportTV;
    @FXML private TableColumn<Appointment, LocalDate> reportDateColumn;
    @FXML private TableColumn<Appointment, LocalTime> reportStartColumn;
    @FXML private TableColumn<Appointment, LocalTime> reportEndColumn;
    @FXML private TableColumn<Appointment, String> reportTypeColumn;
    @FXML private TableColumn<Appointment, String> reportCustomerNameColumn;
    @FXML private DatePicker reportDatePicker;
    @FXML private TextField monthField;
    @FXML private TextField halfAppointmentField;
    @FXML private TextField hourAppointmentField;
    @FXML private TextField overAnHourAppointmentField;
    @FXML private Label monthLabel;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> cityColumn;
    @FXML private TableColumn<Customer, String> countryColumn;
    @FXML private TableColumn<Customer, String> numberColumn;
    @FXML private TextField nameFieldUpdate;
    @FXML private TextField numberFieldUpdate;
    @FXML private TextField addressFieldUpdate;
    @FXML private TextField address2FieldUpdate;    
    @FXML private TextField cityFieldUpdate;
    @FXML private TextField countryFieldUpdate;
    @FXML private TextField zipFieldUpdate;
    @FXML private DatePicker scheduleDatePicker;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox startTime;
    @FXML private ChoiceBox endTime;
    @FXML private TextField typeField;
    @FXML private DatePicker datePickerUpdate;
    @FXML private ChoiceBox startTimeUpdate;
    @FXML private ChoiceBox endTimeUpdate;
    @FXML private TextField typeFieldUpdate;
    @FXML private TextField customerNameUpdate;
    @FXML private TableView<Customer> customerAppointmentTV;
    @FXML private TableColumn<Customer, String> customerAppointmentNameColumn;
    @FXML private TableView<Customer> customerReportTV;
    @FXML private TableColumn<Customer, String> customerReportNameColumn;
    @FXML private TableView<TypeReport> typeTV;
    @FXML private TableColumn<TypeReport, String> typeToCount;
    @FXML private TableColumn<TypeReport, Integer> typeCount;
    @FXML private ChoiceBox type;
    //@FXML private TextField typeCount;

    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> times = FXCollections.observableArrayList();
    private static ObservableList<Appointment> todaysAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> empty = FXCollections.observableArrayList();
    private static ObservableList<Appointment> numberAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    //private static ObservableList<String> types = FXCollections.observableArrayList();
    private static ObservableList<String> filteredTypes = FXCollections.observableArrayList();
    private static ObservableList<String> typeListCount = FXCollections.observableArrayList();
    private static ObservableList<TypeReport> types = FXCollections.observableArrayList();
    
    private Customer customerToDelete;
    private String selectedCustomerId;

    public void customerAddButtonPushed (ActionEvent event) throws SQLException {
        
        String name = nameField.getText();
        String number = numberField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String country = countryField.getText();
        String zip = zipField.getText();
        String address2 = address2Field.getText();
  
        Country countryObject;
        City cityObject;
        Address addressObject;
        Customer customerObject;
        
        if (name.isEmpty() || number.isEmpty() || address.isEmpty() || city.isEmpty() || country.isEmpty() || zip.isEmpty() || address2.isEmpty()) {
            
            System.out.println("Please enter a value for each field!");
            
        } else {
        
            if (Country.selectStatement(country) == 0) {

                Country.insertStatement(country, currentUser.getUsername());

                countryObject = new Country(Country.selectCountryId(country), country);
                countryObject.setCountryId(Country.selectCountryId(country));
                countryObject.setCountry(country);

            } else {

                countryObject = Country.getCountry(country);

            }

            if (City.selectStatement(city) == 0) {

                City.insertStatement(city, country, currentUser.getUsername());
                cityObject = new City(City.selectCityId(city), city, countryObject);

            } else {

                cityObject = City.getCity(city, countryObject);

            }

            if (Address.selectStatement(address) == 0) {

                Address.insertStatement(address, city, address2, zip, number, currentUser.getUsername()); 
                addressObject = new Address(Address.selectAddressId(address), address, address2, zip, number, cityObject);

            } else {

                addressObject = Address.getAddress(address, address2, zip, number, cityObject);

            }

            if (Customer.selectStatement(name) == 0) {

                Customer.insertStatement(address, name, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername());
                customerObject = new Customer(Customer.selectCustomerId(name), true, name, LocalDateTime.now(), currentUser.getUsername(), LocalDateTime.now(), currentUser.getUsername(), addressObject);

            } else {

                customerObject = Customer.getCustomer(true, name, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), addressObject);

            }

            allCustomers.add(customerObject);
        }
    }
    
    public void selectCustomer (MouseEvent event) throws SQLException {
        
        Customer selectedCustomer = customerTV.getSelectionModel().getSelectedItem();
        Address selectedCustomerAddress = selectedCustomer.getAddress();
        City selectedCustomerCity = selectedCustomerAddress.getCity();
        Country selectedCustomerCountry = selectedCustomerCity.getCountry();
        
        nameFieldUpdate.setText(selectedCustomer.getCustomerName());
        numberFieldUpdate.setText(selectedCustomerAddress.getPhone());
        addressFieldUpdate.setText(selectedCustomerAddress.getAddress());
        cityFieldUpdate.setText(selectedCustomerCity.getCity());
        countryFieldUpdate.setText(selectedCustomerCountry.getCountry());
        zipFieldUpdate.setText(selectedCustomerAddress.getPostalCode());
        address2FieldUpdate.setText(selectedCustomerAddress.getAddress2());
        
        selectedCustomerId = Customer.selectCustomerId(selectedCustomer.getCustomerName());

    }
        
    public void customerSaveButtonPushed (ActionEvent event) throws SQLException {

        Customer selectedCustomer = customerTV.getSelectionModel().getSelectedItem();
        String id = selectedCustomerId;
        String name = nameFieldUpdate.getText();
        String number = numberFieldUpdate.getText();
        String address = addressFieldUpdate.getText();
        String city = cityFieldUpdate.getText();
        String country = countryFieldUpdate.getText();
        String zip = zipFieldUpdate.getText();
        String address2 = address2FieldUpdate.getText();
        String addressId = Address.selectAddressId(address);
        Boolean active = selectedCustomer.isActive();
        LocalDateTime createDate = selectedCustomer.getCreateDate();
        String createdBy = selectedCustomer.getCreatedBy();
        LocalDateTime lastUpdate = selectedCustomer.getLastUpdate();
        String lastUpdatedBy = selectedCustomer.getLastUpdateBy();
  
        Country countryObject;
        City cityObject;
        Address addressObject;
        Customer customerObject;

        if (name.isEmpty() || number.isEmpty() || address.isEmpty() || city.isEmpty() || country.isEmpty() || zip.isEmpty() || address2.isEmpty()) {
            
            System.out.println("Please enter a value for each field!");
            
        } else {

            if (Country.selectStatement(country) == 0) {
                Country.insertStatement(country, currentUser.getUsername());

                countryObject = new Country(Country.selectCountryId(country), country);
                countryObject.setCountryId(Country.selectCountryId(country));
                countryObject.setCountry(country);

            } else {

                countryObject = Country.getCountry(country);

            }

            if (City.selectStatement(city) == 0) {

                City.insertStatement(city, country, currentUser.getUsername());
                cityObject = new City(City.selectCityId(city), city, countryObject);

            } else {

                cityObject = City.getCity(city, countryObject);

            }

            if (Address.selectStatement(address) == 0) {

                Address.insertStatement(address, city, address2, zip, number, currentUser.getUsername()); 
                addressObject = new Address(Address.selectAddressId(address), address, address2, zip, number, cityObject);

            } else {

                addressObject = Address.updateAddress(addressId, address, address2, zip, number, cityObject, currentUser.getUsername());

            }

            customerObject = Customer.getCustomer(active, name, createDate, createdBy, lastUpdate, lastUpdatedBy, addressObject);

            PreparedStatement psUpdateCustomer = DBConnection.getConnection().prepareStatement("update customer set customerName = \"" + name + "\", addressId = \"" + addressObject.getAddressId() + "\", lastUpdate = \"" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")) + "\", lastUpdateBy = \"" + currentUser.getUsername() + "\" where customerId = \"" + id + "\"");
            psUpdateCustomer.executeUpdate();
            System.out.println("Update Complete");

            allCustomers.set((allCustomers.indexOf(selectedCustomer)), customerObject);
            updateCustomerTV();
        }
        
    }
    
    public void customerDeleteButtonPushed (ActionEvent event) throws SQLException {
        
        customerToDelete = customerTV.getSelectionModel().getSelectedItem();
        allCustomers.remove(customerToDelete);
        updateCustomerTV();
        
        PreparedStatement psRemoveCustomer = DBConnection.getConnection().prepareStatement("delete from customer where customerName = \"" + customerToDelete.getCustomerName() + "\"");
        psRemoveCustomer.execute();

    }
    
    public void appointmentAddButtonPushed (ActionEvent event) throws SQLException {
        
        Appointment appointmentToAdd;
        String customerId = Customer.selectCustomerId(customerAppointmentTV.getSelectionModel().getSelectedItem().getCustomerName());
        String userId = currentUser.getUserId(currentUser.getUsername());
        String title = "Appointment for " + customerAppointmentTV.getSelectionModel().getSelectedItem().getCustomerName();
        String description = typeField.getText();
        String location = "location";
        String contact = "contact";
        String type = typeField.getText();
        String url = "url";
        LocalDateTime start = LocalDateTime.of(datePicker.getValue(), (LocalTime.parse(startTime.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcStart = Time.toUTCTime(start, ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.of(datePicker.getValue(), (LocalTime.parse(endTime.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcEnd = Time.toUTCTime(end, ZoneId.systemDefault());
        LocalDateTime createDate = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        String createdBy = currentUser.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        String lastUpdateBy = currentUser.getUsername();

        if (utcStart.getDayOfYear() > start.getDayOfYear()) {
            
            utcStart.minusDays(1);
            utcEnd.minusDays(1);
            
        }
        
        if (utcEnd.isBefore(utcStart)) {
            
            System.out.println("Appointment ends before it starts! Please change end time.");
            
        } else if (timeCheck(utcStart, utcEnd, datePicker.getValue()) == false) {
            
            System.out.println("Appointment conflicts with another appointment. Please check start and end times!");
            
        } else {
            
            Appointment.addAppointment(customerId, userId, title, description, location, contact, type, url, utcStart, utcEnd, createDate, createdBy, lastUpdate, lastUpdateBy);
            appointmentToAdd = Appointment.selectAppointment(Appointment.mostRecentAddedAppointment());
            todaysAppointments.add(appointmentToAdd);
            allAppointments.add(appointmentToAdd);
            updateScheduleTV();
            
        }
    }
    
    public void changeDay (ActionEvent event) {
        
        todaysAppointments.clear();
        
        try {
            
            todaysAppointments(scheduleDatePicker.getValue());
            
        } catch (SQLException ex) {
            
            System.out.println("Error: " + ex);
            
        }
        
        clearAppointmentUpdateFields(scheduleDatePicker.getValue());
        
    }
    
    public void reportChangeDay (ActionEvent event) throws SQLException {
        
        numberAppointments.clear();
        
        selectCurrentMonthAppointments(reportDatePicker.getValue());
        monthLabel.setText(reportDatePicker.getValue().getMonth().toString());
        ReportInterface report = a -> a.size();
        monthField.setText("" + report.addAppointments(numberAppointments));
        
        types.clear();
        typeCount(reportDatePicker.getValue().getMonthValue());
        updateTypeTV();
        
    }
    
    public void selectReportCustomer (MouseEvent event) throws SQLException {
        
        customerAppointments.clear();
        
        String reportCustomerId = customerReportTV.getSelectionModel().getSelectedItem().getCustomerName();
        reportCustomerId = Customer.selectCustomerId(reportCustomerId);
        
        reportTV.setItems(customerAppointments(reportCustomerId));

    }
    
    public void monthChangeDay (ActionEvent event) {
        
        monthAppointments.clear();
        
        try {
            
            selectMonthAppointments(monthDatePicker.getValue());
            updateMonthTV();
            
        } catch (SQLException ex) {
            
            System.out.println("Error: " + ex);
            
        }
 
    }
    
    public void weekChangeDay (ActionEvent event) {
        
        weekAppointments.clear();
        
        try {
            
            selectWeekAppointments(weekDatePicker.getValue());
            updateWeekTV();
            
        } catch (SQLException ex) {
            
            System.out.println("Error: " + ex);
            
        }
        
    }
    
    public void selectAppointment (MouseEvent event) throws SQLException {
        
        Appointment selectedAppointment = scheduleTV.getSelectionModel().getSelectedItem();
        Customer selectedAppointmentCustomer = selectedAppointment.getCustomer();
        LocalTime selectedAppointmentStart = selectedAppointment.getStart();
        LocalTime selectedAppointmentEnd = selectedAppointment.getEnd();
        String selectedAppointmentType = selectedAppointment.getType();
        LocalDate selectedAppointmentDate = selectedAppointment.getDate();
        
        datePickerUpdate.setValue(selectedAppointmentDate);
        startTimeUpdate.setItems(times);
        startTimeUpdate.getSelectionModel().select(selectedAppointmentStart);
        endTimeUpdate.setItems(times);
        endTimeUpdate.getSelectionModel().select(selectedAppointmentEnd);
        typeFieldUpdate.setText(selectedAppointmentType);
        customerNameUpdate.setText(selectedAppointmentCustomer.getCustomerName());
        
    }
    
    public void appointmentSaveButtonPushed (ActionEvent event) throws SQLException {

        Appointment appointmentToModify = scheduleTV.getSelectionModel().getSelectedItem();
        Appointment modifiedAppointment = appointmentToModify;
        String appointmentId = appointmentToModify.getAppointmentId();
        LocalDateTime updatedStart = LocalDateTime.of(datePickerUpdate.getValue(), (LocalTime.parse(startTimeUpdate.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcUpdatedStart = Time.toUTCTime(updatedStart, ZoneId.systemDefault());
        LocalDateTime updatedEnd = LocalDateTime.of(datePickerUpdate.getValue(), (LocalTime.parse(endTimeUpdate.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcUpdatedEnd = Time.toUTCTime(updatedEnd, ZoneId.systemDefault());
        String updatedType = typeFieldUpdate.getText();
        
        if(utcUpdatedStart.getDayOfYear() > updatedStart.getDayOfYear()) {
            utcUpdatedStart.minusDays(1);
            utcUpdatedEnd.minusDays(1);
        }

        modifiedAppointment.setStart(updatedStart.toLocalTime());
        modifiedAppointment.setEnd(updatedEnd.toLocalTime());
        modifiedAppointment.setDate(datePickerUpdate.getValue());
        modifiedAppointment.setType(typeFieldUpdate.getText());
        
        if (utcUpdatedEnd.isBefore(utcUpdatedStart)) {
            System.out.println("Appointment ends before it starts! Please change end time.");
        } else if (timeCheck(appointmentId, utcUpdatedStart, utcUpdatedEnd, datePickerUpdate.getValue()) == false) {
            System.out.println("Appointment conflicts with another appointment. Please check start and end times!");
        } else {
            Appointment.updateAppointment(appointmentId, utcUpdatedStart, utcUpdatedEnd, updatedType, currentUser.getUsername());
            todaysAppointments.set(todaysAppointments.indexOf(appointmentToModify), modifiedAppointment);
        }
        
        updateScheduleTV();
        System.out.println("done");
        
    }
    
    public void appointmentDeleteButtonPushed (ActionEvent event) throws SQLException {
        
        Appointment appointmentToDelete = scheduleTV.getSelectionModel().getSelectedItem();
        todaysAppointments.remove(appointmentToDelete);
        Appointment.deleteAppointment(appointmentToDelete.getAppointmentId());
        updateScheduleTV();
        clearAppointmentUpdateFields(scheduleDatePicker.getValue());        
        
    }
    
    public void reportTabClicked (Event event) throws SQLException {
        
        typeToCount.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getType()));
        typeCount.setCellValueFactory(new PropertyValueFactory("count"));
        
        types.clear();
        typeCount(reportDatePicker.getValue().getMonthValue());
        updateTypeTV();
        
        //Lambda to count appointments for a given month
        ReportInterface report = a -> a.size();
        monthField.setText("" + report.addAppointments(numberAppointments));

        //Lambda to count appointments of a given length
        AppointmentLength length = a -> (int) a.getStart().until(a.getEnd(), ChronoUnit.MINUTES);
        int countHalf = 0;
        int countHour = 0;
        int countOverAnHour = 0;
        
        int i = 0;
        while (i < (allAppointments.size())) {
            
            switch (length.countAppointment(allAppointments.get(i))) {
                case 30:
                    countHalf++;
                    i++;
                    break;
                case 60:
                    countHour++;
                    i++;
                    break;
                default:
                    countOverAnHour++;
                    i++;
                    break;
                    
            }
            
        }
        
        halfAppointmentField.setText(Integer.toString(countHalf));
        hourAppointmentField.setText(Integer.toString(countHour));
        overAnHourAppointmentField.setText(Integer.toString(countOverAnHour));
        
        updateCustomerReportTV();
        if (customerReportTV.getSelectionModel().getSelectedItem() != null) {
            
            customerAppointments.clear();
            String reportCustomerId = customerReportTV.getSelectionModel().getSelectedItem().getCustomerName();
            reportCustomerId = Customer.selectCustomerId(reportCustomerId);
            reportTV.setItems(customerAppointments(reportCustomerId));
            
        }

 
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomerName()));
        addressColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAddress().getAddress()));
        cityColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAddress().getCity().getCity()));
        countryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAddress().getCity().getCountry().getCountry()));
        numberColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getAddress().getPhone()));
        
        customerAppointmentNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomerName()));
        startColumn.setCellValueFactory(new PropertyValueFactory("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory("end"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        customerNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomer().getCustomerName()));
        scheduleDatePicker.setValue(LocalDate.now());
        datePickerUpdate.setValue(LocalDate.now());
        datePicker.setValue(LocalDate.now());
  
        monthDateColumn.setCellValueFactory(new PropertyValueFactory("date" ));
        monthStartColumn.setCellValueFactory(new PropertyValueFactory("start"));
        monthEndColumn.setCellValueFactory(new PropertyValueFactory("end"));
        monthTypeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        monthCustomerNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomer().getCustomerName()));
        monthDatePicker.setValue(LocalDate.now());
        
        weekDateColumn.setCellValueFactory(new PropertyValueFactory("date" ));
        weekStartColumn.setCellValueFactory(new PropertyValueFactory("start"));
        weekEndColumn.setCellValueFactory(new PropertyValueFactory("end"));
        weekTypeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        weekCustomerNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomer().getCustomerName()));
        weekDatePicker.setValue(LocalDate.now());
                        
        reportDateColumn.setCellValueFactory(new PropertyValueFactory("date" ));
        reportStartColumn.setCellValueFactory(new PropertyValueFactory("start"));
        reportEndColumn.setCellValueFactory(new PropertyValueFactory("end"));
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        reportCustomerNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomer().getCustomerName()));
        
        customerReportNameColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCustomerName()));
        reportDatePicker.setValue(LocalDate.now());
        monthLabel.setText(reportDatePicker.getValue().getMonth().toString());

        try {

            if (appointmentWithin15Minutes(LocalDateTime.now()) == true) {
                
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Check Appointment Time");
                alert.setHeaderText("Reminder!");
                alert.setContentText("There is an appointment within 15 minutes!");
                alert.showAndWait();
                
            } else;
            
        } catch (SQLException ex) {
            
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        try {
            
            getCurrentCustomers();
            todaysAppointments(scheduleDatePicker.getValue());
            selectMonthAppointments(monthDatePicker.getValue());
            selectCurrentMonthAppointments(reportDatePicker.getValue());
            selectWeekAppointments(weekDatePicker.getValue());
            updateMonthTV();
            updateWeekTV();
            
        } catch (SQLException ex) {
            
            System.out.println("Error: " + ex);
            
        }

        updateCustomerTV();
        updateScheduleTV();
        updateCustomerAppointmentTV();
        updateCustomerReportTV();

        //Adds in business hours of 8:00 - 21:00 Chicago time
        times.addAll(LocalTime.of(8, 00), LocalTime.of(8, 30), LocalTime.of(9, 00), LocalTime.of(9, 30), LocalTime.of(10, 00),LocalTime.of(10, 30), LocalTime.of(11, 00),LocalTime.of(11, 30), LocalTime.of(12, 00),LocalTime.of(12, 30), LocalTime.of(13, 00),LocalTime.of(13, 30), LocalTime.of(14, 00),LocalTime.of(14, 30), LocalTime.of(15, 00),LocalTime.of(15, 30), LocalTime.of(16, 00),LocalTime.of(16, 30), LocalTime.of(17, 00),LocalTime.of(17, 30), LocalTime.of(18, 00),LocalTime.of(18, 30), LocalTime.of(19, 00),LocalTime.of(19, 30), LocalTime.of(20, 00),LocalTime.of(20, 30), LocalTime.of(21, 00));
        
        //Changes business hours to UTC time and then to current time based on system's location
        int i = 0;
        while (i < times.size()) {
            
            times.set(i, Time.chicagoToUTCTime(times.get(i), datePicker.getValue()));
            times.set(i, Time.utcToCurrentTime(times.get(i), datePicker.getValue()));
            ++i;
            
        }
        
        //Adds the business hours to choice boxes in the local time
        startTime.setItems(times);
        endTime.setItems(times);
   
    }   

    public static ObservableList<Customer> getCurrentCustomers() throws SQLException {
        
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
        
        PreparedStatement psCurrentCustomer = DBConnection.getConnection().prepareStatement("select customer.customerName, customer.createDate, customer.createdBy, customer.lastUpdate, customer.lastUpdateBy, address.address, city.city, country.country, address.phone, address.address2, address.postalCode from customer join address on customer.addressId = address.addressId join city on city.cityId = address.cityId join country on country.countryId = city.countryId");
        ResultSet rs = psCurrentCustomer.executeQuery();
        
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
            allCustomers.add(customerObject);
            
        }
        
        return allCustomers;
    }

    public static ObservableList<Appointment> todaysAppointments(LocalDate datePicker) throws SQLException {
        
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
                
                todaysAppointments.add(appointment);
                allAppointments.add(appointment);
                
            } else
                
                allAppointments.add(appointment);
            
        }
        
        return todaysAppointments;
        
    }
    
    public static ObservableList<Appointment> selectCurrentMonthAppointments(LocalDate datePicker) throws SQLException {
        
        Appointment appointment;
        int month = datePicker.getMonthValue();

        PreparedStatement psMonthAppointment = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where month(start) = \"" + month +"\"");
        ResultSet rs = psMonthAppointment.executeQuery();
        
        while(rs.next()) {
            
            appointment = Appointment.selectAppointment(rs.getString(1));

            numberAppointments.add(appointment);
            
        }
        
        return numberAppointments;
    }
    
    public static ObservableList<Appointment> selectMonthAppointments(LocalDate datePicker) throws SQLException {
                
        Appointment appointment;

        PreparedStatement psMonthAppointment = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where date(start) between \"" + datePicker + "\" and \"" + datePicker.plusDays(30) + "\"");
        ResultSet rs = psMonthAppointment.executeQuery();
        
        while(rs.next()) {
            
            appointment = Appointment.selectAppointment(rs.getString(1));
            monthAppointments.add(appointment);
            
        }

        return monthAppointments;
    }
    
    public static ObservableList<Appointment> selectWeekAppointments(LocalDate datePicker) throws SQLException {
        
        Appointment appointment;

        PreparedStatement psWeekAppointment = DBConnection.getConnection().prepareStatement("select appointmentId from appointment where date(start) between \"" + datePicker + "\" and \"" + datePicker.plusDays(6) + "\"");
        ResultSet rs = psWeekAppointment.executeQuery();
        
        while(rs.next()) {
            
            appointment = Appointment.selectAppointment(rs.getString(1));

            weekAppointments.add(appointment);
            
        }
        
        
        return weekAppointments;
    }
    
    public static ObservableList<Appointment> customerAppointments(String currentCustomer) throws SQLException {
        
        String appointmentId;
        String userId;
        String customerId;
        String type;
        LocalDate date;
        LocalTime start;
        LocalTime end;
        Appointment appointment;
        Customer customer;
        
        PreparedStatement psCustomerAppointment = DBConnection.getConnection().prepareStatement("select * from appointment where customerId = \"" + currentCustomer + "\"");
        ResultSet rs = psCustomerAppointment.executeQuery();
        
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
            
            customerAppointments.add(appointment);
            
        }
        
        return customerAppointments;
        
    }
    
    public static ObservableList typeCount (int month) throws SQLException {
        
        String type1;
        int count1;
        TypeReport typeR;
        
        PreparedStatement typePS = DBConnection.getConnection().prepareStatement("select type, count(type) from appointment where month(start) = \"" + month + "\" group by type");
        ResultSet rs = typePS.executeQuery();
        
        while (rs.next()){
            
            type1 = rs.getString(1);
            count1 = rs.getInt(2);
            typeR = new TypeReport (type1, count1);
            types.add(typeR);

        }
        
        return types;
        
    }
    
    public void updateCustomerTV() {
        
        customerTV.setItems(allCustomers);
        
    }
    
    public void updateScheduleTV() {
        
        scheduleTV.setItems(todaysAppointments);
        
    }
    
    public void updateMonthTV() throws SQLException {
        
        monthScheduleTV.setItems(monthAppointments);
        
    }
    
    public void updateWeekTV() throws SQLException {
        
        weekScheduleTV.setItems(weekAppointments);
        
    }

    public void updateCustomerAppointmentTV() {
        
        customerAppointmentTV.setItems(allCustomers);
        
    }
    
    public void updateCustomerReportTV() {
        
        customerReportTV.setItems(allCustomers);
        
    }
    
    public void updateTypeTV() throws SQLException {
        
        typeTV.setItems(types);
        
    }
    
    public void clearAppointmentUpdateFields(LocalDate datePicker) {
        
        datePickerUpdate.setValue(datePicker);
        startTimeUpdate.setItems(empty);
        endTimeUpdate.setItems(empty);
        typeFieldUpdate.setText("");
        customerNameUpdate.setText("");
        
    }

    public Boolean timeCheck (String appointmentId, LocalDateTime startTime, LocalDateTime endTime, LocalDate datePicker) throws SQLException {
        
        Boolean isTrue = true;
        LocalDateTime checkTimeStart;
        LocalDateTime checkTimeEnd;
        
        PreparedStatement psTimeCheck = DBConnection.getConnection().prepareStatement("select start, end from appointment where date(start) = \"" + datePicker + "\" and appointmentId !=\"" + appointmentId + "\"");
        ResultSet rs = psTimeCheck.executeQuery();
        
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
    
    public Boolean timeCheck (LocalDateTime startTime, LocalDateTime endTime, LocalDate datePicker) throws SQLException {
        
        Boolean isTrue = true;
        LocalDateTime checkTimeStart;
        LocalDateTime checkTimeEnd;
        
        PreparedStatement psTimeCheck = DBConnection.getConnection().prepareStatement("select start, end from appointment where date(start) = \"" + datePicker + "\"");
        ResultSet rs = psTimeCheck.executeQuery();
        
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
        
    public Boolean appointmentWithin15Minutes (LocalDateTime currentTime) throws SQLException {
        
        Boolean isWithin15Minutes = false;
        LocalDateTime time = Time.toUTCTime(currentTime, ZoneId.systemDefault());

        PreparedStatement ps15Minutes = DBConnection.getConnection().prepareStatement("select start from appointment where start between \"" + time + "\" and \"" + time.plusMinutes(15) + "\"");
        ResultSet rs = ps15Minutes.executeQuery();
        
        if (rs.next()) {
            
            isWithin15Minutes = true;
            
        }

        return isWithin15Minutes;
        
    }
    
}
