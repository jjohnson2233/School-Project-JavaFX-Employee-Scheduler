package controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.ListedAppointment;
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
import managers.ReminderManager;
import tables.Appointment;
import tables.Reminder;

public class EditAppointmentScreenController implements Initializable {
	//Title field
	@FXML private TextField titleText;
	
	//Description field
	@FXML private TextArea descriptionText;
	
	//Location filed
	@FXML private TextField locationText;
	
	//Contact field
	@FXML private TextField contactText;
	
	//URL field
	@FXML private TextField urlText;
	
	//start date
	@FXML private DatePicker startDate;
	
	//Start hour
	@FXML private TextField startHourField;
	
	//Start minute
	@FXML private TextField startMinuteField;
	
	//Start am/PM picker
	@FXML private ChoiceBox<String> startChoiceBox;
	
	//End date
	@FXML private DatePicker endDate;
	
	//End hour
	@FXML private TextField endHourField;
	
	//End minute
	@FXML private TextField endMinuteField;
	
	//End am/PM picker
	@FXML private ChoiceBox<String> endChoiceBox;
	
	//Save button
	@FXML private Button saveButton;
	
	//Save button action
	private void saveButtonAction(ActionEvent event) throws InvalidHoursException, OverlapException {
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
			//Get the selected Item from the appointments table
			ListedAppointment selectedItem = AppointmentScreenController.selectedItem;
			//Create appointment object from typed values
			Appointment appointment = new Appointment(AppointmentManager.getRow(selectedItem.getTitle()).getCustomerID(), titleText.getText(), zoneStart, zoneEnd);
			appointment.setAppointmentID(AppointmentManager.getRow(selectedItem.getTitle()).getAppointmentID());
			appointment.setDescription(descriptionText.getText());
			appointment.setLocation(locationText.getText());
			appointment.setContact(contactText.getText());
			appointment.setUrl(urlText.getText());
			
			//Remove appointment from schedule
			ScheduledAppointments.remove(appointment);
			
			//Check if appointment overlaps with other appointments
			if (ScheduledAppointments.doesOverlap(appointment)) {
				ScheduledAppointments.add(appointment);
				throw new OverlapException();
			}
			
			//Edit appointment in the database
			AppointmentManager.edit(appointment);
			
			//Add new one to schedule
			ScheduledAppointments.add(appointment);
			
			//Edit reminder in the database
			Reminder reminder = ReminderManager.getRow(appointment.getAppointmentID());
			reminder.setReminderDate(appointment.getStart());
			reminder.schedule(appointment);
			ReminderManager.edit(reminder);
			
			//Go back to appointment screen
			Parent root;
			Stage stage = (Stage) saveButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//Cancel button
	@FXML private Button cancelButton;
	
	//Cancel button action
	private void cancelButtonAction(ActionEvent event) {
		//Go back to appointment screen
		try {
			Parent root;
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
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
		
		//Get selected appointment
		ListedAppointment selectedItem = AppointmentScreenController.selectedItem;
		
		//Load text fields with values from selected Appointment
		titleText.setText(selectedItem.getTitle());
		descriptionText.setText(selectedItem.getDescription());
		locationText.setText(selectedItem.getLocation());
		contactText.setText(selectedItem.getContact());
		urlText.setText(selectedItem.getUrl());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		startDate.setValue(LocalDate.parse(selectedItem.getStart(), formatter));
		endDate.setValue(LocalDate.parse(selectedItem.getEnd(), formatter));
		startHourField.setText(selectedItem.getStartHour());
		endHourField.setText(selectedItem.getEndHour());
		startMinuteField.setText(selectedItem.getStartMinute());
		endMinuteField.setText(selectedItem.getEndMinute());
		if (selectedItem.getAmpmStart().equals("AM")) {
			startChoiceBox.setValue("AM");
		} else {
			startChoiceBox.setValue("PM");
		}
		if (selectedItem.getAmpmEnd().equals("AM")) {
			endChoiceBox.setValue("AM");
		} else {
			endChoiceBox.setValue("PM");
		}
	}
}