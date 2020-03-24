package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.ListedAppointment;
import application.ListedCustomer;
import application.ScheduledAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import managers.AppointmentManager;
import managers.ReminderManager;
import tables.Reminder;

public class AppointmentScreenController implements Initializable {
	//List of appointments in the database
	ObservableList<ListedAppointment> listOfAppointments = FXCollections.observableArrayList();

	//Object for selected item
	public static ListedAppointment selectedItem;

	//Search text field
	@FXML private TextField searchTextField;

	//Search button
	@FXML private Button searchButton;
	//Search button action
	private void searchButtonAction(ActionEvent event) {
		//Get the search term
		String searchTerm = searchTextField.getText();
		//Get a list of the customers in the database and filter them by the search term
		List rawList = listOfAppointments.stream()
				.filter(appointment ->
						appointment.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
						appointment.getContact().toLowerCase().contains(searchTerm.toLowerCase()) ||
						appointment.getDescription().toLowerCase().contains(searchTerm.toLowerCase()) ||
						appointment.getLocation().toLowerCase().contains(searchTerm.toLowerCase()) ||
						appointment.getUrl().toLowerCase().contains(searchTerm.toLowerCase())
				)
				.collect(Collectors.toList());
		//Make the list observable
		ObservableList<ListedAppointment> observableList = FXCollections.observableList(rawList);
		//Set the table to show the new filtered list
		appointmentsTable.setItems(observableList);
	}

	//Reports button
	@FXML private Button reportsButton;
	//Reports button action
	private void reportsButtonAction(ActionEvent event) {
		try {
			Parent root;
			Stage stage = (Stage) reportsButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/views/ReportsScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Calendar button
	@FXML private Button calendarButton;
	//Calendar button action
	private void calendarButtonAction(ActionEvent event) {
		try {
			Parent root;
			Stage stage = (Stage) calendarButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/views/CalendarScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Appointments Table
	@FXML private TableView<ListedAppointment> appointmentsTable;
	@FXML private TableColumn<ListedAppointment, String> idColumn;
	@FXML private TableColumn<ListedAppointment, String> titleColumn;
	@FXML private TableColumn<ListedAppointment, String> locationColumn;
	@FXML private TableColumn<ListedAppointment, String> contactColumn;
	@FXML private TableColumn<ListedAppointment, String> startColumn;
	@FXML private TableColumn<ListedAppointment, String> endColumn;
	
	//Back to customers button
	@FXML private Button customersButton;
	//Back to customers button action
	private void customersButtonAction(ActionEvent event) {
		try {
			Parent root;
			Stage stage = (Stage) customersButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/views/CustomerScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Edit button
	@FXML private Button editButton;
	//Edit button action
	private void editButtonAction(ActionEvent event) {
		try {
			//Set selected item
			selectedItem = appointmentsTable.getSelectionModel().getSelectedItem();
			//Go to the edit appointment screen
			Parent root;
			Stage stage = (Stage) editButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/views/EditAppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Delete button
	@FXML private Button deleteButton;
	//Delete button action
	private void deleteButtonAction(ActionEvent event) {
		//Delete from database
		Reminder reminder = null;
		try {
			reminder = ReminderManager.getRow(Integer.parseInt(appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentID()));
		} catch (NumberFormatException | SQLException e1) {
			e1.printStackTrace();
		}
		//Delete from database
		ReminderManager.delete(reminder);
		AppointmentManager.delete(appointmentsTable.getSelectionModel().getSelectedItem().getTitle());
		//Delete from scheduled list
		try {
			ScheduledAppointments.remove(AppointmentManager.getRow(appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentID()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Delete from table
		appointmentsTable.getItems().remove(appointmentsTable.getSelectionModel().getSelectedItem());
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set button actions
		searchButton.setOnAction(event -> searchButtonAction(event));
		reportsButton.setOnAction(event -> reportsButtonAction(event));
		calendarButton.setOnAction(event -> calendarButtonAction(event));
		customersButton.setOnAction(event -> customersButtonAction(event));
		editButton.setOnAction(event -> editButtonAction(event));
		deleteButton.setOnAction(event -> deleteButtonAction(event));
		
		//Gather data from database
		try {
				AppointmentManager.getAllRows().stream().forEach(appointment -> {
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyy");
					DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("hh");
					DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("mm");
					DateTimeFormatter ampmFormatter = DateTimeFormatter.ofPattern("a");
					String start = appointment.getStart().toLocalDate().format(dateFormatter).toString();
					String end = appointment.getEnd().toLocalDate().format(dateFormatter).toString();
					String startHour = appointment.getStart().toLocalTime().format(hourFormatter).toString();
					String startMinute = appointment.getStart().toLocalTime().format(minuteFormatter).toString();
					String endHour = appointment.getEnd().toLocalTime().format(hourFormatter).toString();
					String endMinute = appointment.getEnd().toLocalTime().format(minuteFormatter).toString();
					String ampmStart = appointment.getStart().toLocalTime().format(ampmFormatter).toString();
					String ampmEnd = appointment.getEnd().toLocalTime().format(ampmFormatter).toString();
					ListedAppointment listedAppointment = new ListedAppointment(Integer.toString(appointment.getAppointmentID()), Integer.toString(appointment.getCustomerID()), appointment.getTitle(), 
							appointment.getDescription(), appointment.getLocation(), appointment.getContact(), appointment.getUrl(), start, end, startHour, startMinute, endHour, endMinute, ampmStart, ampmEnd);
					listOfAppointments.add(listedAppointment);
				});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Put data in table
		appointmentsTable.setItems(listOfAppointments);
		
		//Set column values
		idColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("appointmentID"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("title"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("location"));
		contactColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("contact"));
		startColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("start"));
		endColumn.setCellValueFactory(new PropertyValueFactory<ListedAppointment, String>("end"));
		
	}
}
