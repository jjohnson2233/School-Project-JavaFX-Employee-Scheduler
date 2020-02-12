package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import application.ScheduledAppointments;
import exceptions.InvalidHoursException;
import exceptions.OverlapException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import managers.AppointmentManager;
import managers.CustomerManager;
import managers.ReminderManager;
import tables.Appointment;
import tables.Reminder;

public class AddAppointmentScreenController implements Initializable {
	//Title text field
	@FXML private TextField titleText;
	
	//Description text field
	@FXML private TextArea descriptionText;
	
	//Location  text field
	@FXML private TextField locationText;
	
	//Contact text field
	@FXML private TextField contactText;
	
	//URL text field
	@FXML private TextField urlText;
	
	//Start date picker
	@FXML private DatePicker startDate;
	
	//Start time hour field
	@FXML private TextField startHourField;
	
	//Start time minute field
	@FXML private TextField startMinuteField;
	
	//Start am/pm picker
	@FXML private ChoiceBox<String> startChoiceBox;
	
	//End date picker
	@FXML private DatePicker endDate;
	
	//End time hour field
	@FXML private TextField endHourField;
	
	//End time minute field
	@FXML private TextField endMinuteField;
	
	//End am/pm picker
	@FXML private ChoiceBox<String> endChoiceBox;
	
	//Save button
	@FXML private Button saveButton;
	
	//Save button action
	private void saveButtonAction(ActionEvent event) throws InvalidHoursException, OverlapException {
		int customerID;
		LocalTime startTime;
		LocalTime endTime;
		if (startChoiceBox.getValue().equals("AM")) {
			startTime = LocalTime.of(Integer.parseInt(startHourField.getText().trim()), Integer.parseInt(startMinuteField.getText().trim()));
		} else {
			if (startHourField.getText().equals("12")) {
				startTime = LocalTime.of(Integer.parseInt(startHourField.getText().trim()), Integer.parseInt(startMinuteField.getText().trim()));
			} else {
				startTime = LocalTime.of(Integer.parseInt(startHourField.getText().trim()), Integer.parseInt(startMinuteField.getText().trim())).plusHours(12);
			}
		}
		if (endChoiceBox.getValue().equals("AM")) {
			endTime = LocalTime.of(Integer.parseInt(endHourField.getText().trim()), Integer.parseInt(endMinuteField.getText().trim()));
		} else {
			if (endHourField.getText().equals("12")) {
				endTime = LocalTime.of(Integer.parseInt(endHourField.getText().trim()), Integer.parseInt(endMinuteField.getText().trim()));
			} else {
				endTime = LocalTime.of(Integer.parseInt(endHourField.getText().trim()), Integer.parseInt(endMinuteField.getText().trim())).plusHours(12);
			}
		}
		//Add time zone to the times
		ZonedDateTime zoneStart = ZonedDateTime.of(startDate.getValue(), startTime, ZoneId.systemDefault());
		ZonedDateTime zoneEnd = ZonedDateTime.of(endDate.getValue(), endTime, ZoneId.systemDefault());
		//Check if appointment is within business hours
		if (startTime.isBefore(LocalTime.of(9, 0)) || endTime.isAfter(LocalTime.of(17, 0)) || startDate.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) 
				|| startDate.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY) || endDate.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) 
				|| endDate.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new InvalidHoursException();
		}
		try {
			//Get customer ID from selected customer
			customerID  = CustomerManager.getRow(CustomerScreenController.selectedItem.getName()).getCustomerID();
			//Create object for data
			Appointment appointment = new Appointment(customerID, titleText.getText(), zoneStart, zoneEnd);
			appointment.setDescription(descriptionText.getText());
			appointment.setLocation(locationText.getText());
			appointment.setContact(contactText.getText());
			appointment.setUrl(urlText.getText());
			
			//Check if appointment overlaps with other appointments
			if (ScheduledAppointments.doesOverlap(appointment)) throw new OverlapException();
			
			//Add appointment to scheduled list
			ScheduledAppointments.add(appointment);
			
			//Add appointment to database
			AppointmentManager.add(appointment);
			
			//Create reminder
			Reminder reminder = new Reminder(appointment.getStart(), appointment.getAppointmentID());
			//Schedule reminder
			reminder.schedule(appointment);
			//Add reminder to the database
			ReminderManager.add(reminder);
			
			//Go back to the appointment screen
			Parent root;
			Stage stage = (Stage) saveButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//Cancel button
	@FXML private Button cancelButton;
	
	//Cancel button action
	private void cancelButtonAction(ActionEvent event) {
		try {
			Parent root;
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set button actions
		saveButton.setOnAction(event -> {
			try {
				saveButtonAction(event);
			} catch (InvalidHoursException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Appointment times must be within business hours (9:00AM - 5:00PM MON - FRI)");
				alert.showAndWait();
			} catch (OverlapException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Appointment times must not overlap with other appointments");
				alert.setContentText("Please change the start and end times of this appointment");
				alert.showAndWait();
			}
		});
		cancelButton.setOnAction(event -> cancelButtonAction(event));
		
		//Set AM/PM choices
		String am = "AM";
		String pm = "PM";
		startChoiceBox.getItems().addAll(am, pm);
		endChoiceBox.getItems().addAll(am, pm);
	}
}
