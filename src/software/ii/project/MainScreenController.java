package software.ii.project;

import SQLObjects.Address;
import SQLObjects.Appointment;
import SQLObjects.City;
import java.net.URL;
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

    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> times = FXCollections.observableArrayList();
    private static ObservableList<Appointment> todaysAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> empty = FXCollections.observableArrayList();
    private static ObservableList<Appointment> numberAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> filteredTypes = FXCollections.observableArrayList();
    private static ObservableList<String> typeListCount = FXCollections.observableArrayList();
    private static ObservableList<TypeReport> types = FXCollections.observableArrayList();
    
    private Customer customerToDelete;
    private String selectedCustomerId;

    //Adds a customer into the database
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
            
            //Checks if the country is already in the database
            if (Country.selectStatement(country) == 0) {
                Country.insertStatement(country, currentUser.getUsername());
                countryObject = new Country(Country.selectCountryId(country), country);
                countryObject.setCountryId(Country.selectCountryId(country));
                countryObject.setCountry(country);
            } else {
                countryObject = Country.getCountry(country);
            }
            
            //Checks if the city is already in the database
            if (City.selectStatement(city) == 0) {
                City.insertStatement(city, country, currentUser.getUsername());
                cityObject = new City(City.selectCityId(city), city, countryObject);
            } else {
                cityObject = City.getCity(city, countryObject);
            }
            
            //Checks if the address is already in the database
            if (Address.selectStatement(address) == 0) {
                Address.insertStatement(address, city, address2, zip, number, currentUser.getUsername()); 
                addressObject = new Address(Address.selectAddressId(address), address, address2, zip, number, cityObject);
            } else {
                addressObject = Address.getAddress(address, address2, zip, number, cityObject);
            }
            
            //Checks if the customer is already in the database
            if (Customer.selectStatement(name) == 0) {
                Customer.insertStatement(address, name, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername());
                customerObject = new Customer(Customer.selectCustomerId(name), true, name, LocalDateTime.now(), currentUser.getUsername(), LocalDateTime.now(), currentUser.getUsername(), addressObject);
            } else {
                customerObject = Customer.getCustomer(true, name, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")), currentUser.getUsername(), addressObject);
            }
            
            allCustomers.add(customerObject);
        }
    }
    
    //Takes the selected customer in the table view and enters the data into the text fields
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
    
    //Saves the changes made to the selected customer
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
            
            //Checks if the country is already in the database
            if (Country.selectStatement(country) == 0) {
                Country.insertStatement(country, currentUser.getUsername());
                countryObject = new Country(Country.selectCountryId(country), country);
                countryObject.setCountryId(Country.selectCountryId(country));
                countryObject.setCountry(country);
            } else {
                countryObject = Country.getCountry(country);
            }

            //Checks if the city is already in the database
            if (City.selectStatement(city) == 0) {
                City.insertStatement(city, country, currentUser.getUsername());
                cityObject = new City(City.selectCityId(city), city, countryObject);
            } else {
                cityObject = City.getCity(city, countryObject);
            }

            //Checks if the address is already in the database
            if (Address.selectStatement(address) == 0) {
                Address.insertStatement(address, city, address2, zip, number, currentUser.getUsername()); 
                addressObject = new Address(Address.selectAddressId(address), address, address2, zip, number, cityObject);
            } else {
                addressObject = Address.updateAddress(addressId, address, address2, zip, number, cityObject, currentUser.getUsername());
            }

            customerObject = Customer.getCustomer(active, name, createDate, createdBy, lastUpdate, lastUpdatedBy, addressObject);
            Customer.updateCustomer(id, name, addressId, currentUser.getUsername());
            System.out.println("Update Complete");

            allCustomers.set((allCustomers.indexOf(selectedCustomer)), customerObject);
            updateCustomerTV();
        }
    }
    
    //Deletes the customer the customer table view currently has selected
    public void customerDeleteButtonPushed (ActionEvent event) throws SQLException {
        
        customerToDelete = customerTV.getSelectionModel().getSelectedItem();
        allCustomers.remove(customerToDelete);
        updateCustomerTV();

        Customer.deleteCustomer(Customer.selectCustomerId(customerToDelete.getCustomerName()));
    }
    
    //Adds an appointment into the database
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

        //Checks if the start time is after midnight and pushes back the day if it is
        if (utcStart.getDayOfYear() > start.getDayOfYear()) {
            utcStart.minusDays(1);
            utcEnd.minusDays(1);
        }
        
        //Checks the appointment times to ensure there isn't any conflicts before adding the appointment
        if (utcEnd.isBefore(utcStart)) {
            System.out.println("Appointment ends before it starts! Please change end time.");
        } else if (Time.timeCheck(utcStart, utcEnd, datePicker.getValue()) == false) {
            System.out.println("Appointment conflicts with another appointment. Please check start and end times!");
        } else {
            Appointment.addAppointment(customerId, userId, title, description, location, contact, type, url, utcStart, utcEnd, createDate, createdBy, lastUpdate, lastUpdateBy);
            appointmentToAdd = Appointment.selectAppointment(Appointment.mostRecentAddedAppointment());
            
            if (datePicker.getValue().getDayOfYear() == LocalDate.now().getDayOfYear()) {
                todaysAppointments.add(appointmentToAdd);
                allAppointments.add(appointmentToAdd);
            } else
                allAppointments.add(appointmentToAdd);
            
            updateScheduleTV();
        }
    }
    
    //Clears today's appointments and retrieves the new day's appointments
    public void changeDay (ActionEvent event) {
        
        todaysAppointments.clear();
        try {
            Appointment.todaysAppointments(scheduleDatePicker.getValue(), todaysAppointments, allAppointments);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        
        clearAppointmentUpdateFields(scheduleDatePicker.getValue());
    }
    
    //Clears the current day's data and retrieves the new day's data
    public void reportChangeDay (ActionEvent event) throws SQLException {
        
        numberAppointments.clear();
        
        Appointment.selectCurrentMonthAppointments(reportDatePicker.getValue(), monthAppointments);
        monthLabel.setText(reportDatePicker.getValue().getMonth().toString());
        ReportInterface report = a -> a.size();
        monthField.setText("" + report.addAppointments(numberAppointments));
        
        types.clear();
        Appointment.typeCount(reportDatePicker.getValue().getMonthValue(), types);
        updateTypeTV();
    }
    
    //Changes whose appointments are reported in the reports tab
    public void selectReportCustomer (MouseEvent event) throws SQLException {
        
        customerAppointments.clear();
        
        String reportCustomerId = customerReportTV.getSelectionModel().getSelectedItem().getCustomerName();
        reportCustomerId = Customer.selectCustomerId(reportCustomerId);
        
        reportTV.setItems(Appointment.customerAppointments(reportCustomerId, customerAppointments));
    }
    
    //Changes the months table view to show a different set of 30 days
    public void monthChangeDay (ActionEvent event) {
        
        monthAppointments.clear();
        try {
            Appointment.selectMonthAppointments(monthDatePicker.getValue(), monthAppointments);
            updateMonthTV();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    //Changes the weeks table view to show a different set of 7 days
    public void weekChangeDay (ActionEvent event) {
        
        weekAppointments.clear();
        try {
            Appointment.selectWeekAppointments(weekDatePicker.getValue(), weekAppointments);
            updateWeekTV();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    //Takes the selected appointment in the table view and enters the data into the text fields
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
    
    //Saves the changes made to the selected appointment
    public void appointmentSaveButtonPushed (ActionEvent event) throws SQLException {

        Appointment appointmentToModify = scheduleTV.getSelectionModel().getSelectedItem();
        Appointment modifiedAppointment = appointmentToModify;
        String appointmentId = appointmentToModify.getAppointmentId();
        LocalDateTime updatedStart = LocalDateTime.of(datePickerUpdate.getValue(), (LocalTime.parse(startTimeUpdate.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcUpdatedStart = Time.toUTCTime(updatedStart, ZoneId.systemDefault());
        LocalDateTime updatedEnd = LocalDateTime.of(datePickerUpdate.getValue(), (LocalTime.parse(endTimeUpdate.getSelectionModel().getSelectedItem().toString())));
        LocalDateTime utcUpdatedEnd = Time.toUTCTime(updatedEnd, ZoneId.systemDefault());
        String updatedType = typeFieldUpdate.getText();
        
        //Checks if the start time is after midnight and pushes back the day if it is
        if(utcUpdatedStart.getDayOfYear() > updatedStart.getDayOfYear()) {
            utcUpdatedStart.minusDays(1);
            utcUpdatedEnd.minusDays(1);
        }

        modifiedAppointment.setStart(updatedStart.toLocalTime());
        modifiedAppointment.setEnd(updatedEnd.toLocalTime());
        modifiedAppointment.setDate(datePickerUpdate.getValue());
        modifiedAppointment.setType(typeFieldUpdate.getText());
        
        //Checks the appointment times to ensure there isn't any conflicts before updating the appointment
        if (utcUpdatedEnd.isBefore(utcUpdatedStart)) {
            System.out.println("Appointment ends before it starts! Please change end time.");
        } else if (Time.timeCheck(appointmentId, utcUpdatedStart, utcUpdatedEnd, datePickerUpdate.getValue()) == false) {
            System.out.println("Appointment conflicts with another appointment. Please check start and end times!");
        } else {
            Appointment.updateAppointment(appointmentId, utcUpdatedStart, utcUpdatedEnd, updatedType, currentUser.getUsername());
            todaysAppointments.set(todaysAppointments.indexOf(appointmentToModify), modifiedAppointment);
        }
        
        updateScheduleTV();
        System.out.println("done");
    }
    
    //Deletes the appointment currently selected in the appointment table view
    public void appointmentDeleteButtonPushed (ActionEvent event) throws SQLException {
        
        Appointment appointmentToDelete = scheduleTV.getSelectionModel().getSelectedItem();
        todaysAppointments.remove(appointmentToDelete);
        Appointment.deleteAppointment(appointmentToDelete.getAppointmentId());
        updateScheduleTV();
        clearAppointmentUpdateFields(scheduleDatePicker.getValue());        
    }
    
    //Populates the reports tab with current information when the tab is selected
    public void reportTabClicked (Event event) throws SQLException {
        
        typeToCount.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getType()));
        typeCount.setCellValueFactory(new PropertyValueFactory("count"));
        
        types.clear();
        Appointment.typeCount(reportDatePicker.getValue().getMonthValue(), types);
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
            reportTV.setItems(Appointment.customerAppointments(reportCustomerId, customerAppointments));
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
            //Checks to see if there is an appointment within 15 minutes of logging in
            if (Time.appointmentWithin15Minutes(LocalDateTime.now()) == true) {
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
            Customer.getCurrentCustomers(allCustomers);
            Appointment.todaysAppointments(scheduleDatePicker.getValue(), todaysAppointments, allAppointments);
            Appointment.selectMonthAppointments(monthDatePicker.getValue(), monthAppointments);
            Appointment.selectCurrentMonthAppointments(reportDatePicker.getValue(), monthAppointments);
            Appointment.selectWeekAppointments(weekDatePicker.getValue(), weekAppointments);
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
}
