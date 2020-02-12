package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import managers.AppointmentManager;
import tables.Appointment;

public class CalendarScreenController implements Initializable {
	//Calendar object for calendar
	private static Calendar calendar = new GregorianCalendar();
	
	//Name of current month
	@FXML private Label monthName;
	
	//Change month buttons
	@FXML private Button nextButton;
	@FXML private Button previousButton;
	
	//Radio button group
	@FXML RadioButton monthRadio;
	@FXML RadioButton weekRadio;
	@FXML ToggleGroup calendarGroup;
	
	//Back to appointments button
	@FXML private Button backToAppointmentsButton;
	
	//Panes for month and week view
	@FXML private GridPane monthPane;
	@FXML private GridPane weekPane;
	
	//Day appointments section for month
	@FXML private TextArea weekOneSunday;
	@FXML private TextArea weekOneMonday;
	@FXML private TextArea weekOneTuesday;
	@FXML private TextArea weekOneWednesday;
	@FXML private TextArea weekOneThursday;
	@FXML private TextArea weekOneFriday;
	@FXML private TextArea weekOneSaturday;
	@FXML private TextArea weekTwoSunday;
	@FXML private TextArea weekTwoMonday;
	@FXML private TextArea weekTwoTuesday;
	@FXML private TextArea weekTwoWednesday;
	@FXML private TextArea weekTwoThursday;
	@FXML private TextArea weekTwoFriday;
	@FXML private TextArea weekTwoSaturday;
	@FXML private TextArea weekThreeSunday;
	@FXML private TextArea weekThreeMonday;
	@FXML private TextArea weekThreeTuesday;
	@FXML private TextArea weekThreeWednesday;
	@FXML private TextArea weekThreeThursday;
	@FXML private TextArea weekThreeFriday;
	@FXML private TextArea weekThreeSaturday;
	@FXML private TextArea weekFourSunday;
	@FXML private TextArea weekFourMonday;
	@FXML private TextArea weekFourTuesday;
	@FXML private TextArea weekFourWednesday;
	@FXML private TextArea weekFourThursday;
	@FXML private TextArea weekFourFriday;
	@FXML private TextArea weekFourSaturday;
	@FXML private TextArea weekFiveSunday;
	@FXML private TextArea weekFiveMonday;
	@FXML private TextArea weekFiveTuesday;
	@FXML private TextArea weekFiveWednesday;
	@FXML private TextArea weekFiveThursday;
	@FXML private TextArea weekFiveFriday;
	@FXML private TextArea weekFiveSaturday;
	@FXML private TextArea weekSixSunday;
	@FXML private TextArea weekSixMonday;
	
	//Day appointments section for week
	@FXML private TextArea weekSunday;
	@FXML private TextArea weekMonday;
	@FXML private TextArea weekTuesday;
	@FXML private TextArea weekWednesday;
	@FXML private TextArea weekThursday;
	@FXML private TextArea weekFriday;
	@FXML private TextArea weekSaturday;
	
	//Day number labels for month
	@FXML private Label weekOneSundayLabel;
	@FXML private Label weekOneMondayLabel;
	@FXML private Label weekOneTuesdayLabel;
	@FXML private Label weekOneWednesdayLabel;
	@FXML private Label weekOneThursdayLabel;
	@FXML private Label weekOneFridayLabel;
	@FXML private Label weekOneSaturdayLabel;
	@FXML private Label weekTwoSundayLabel;
	@FXML private Label weekTwoMondayLabel;
	@FXML private Label weekTwoTuesdayLabel;
	@FXML private Label weekTwoWednesdayLabel;
	@FXML private Label weekTwoThursdayLabel;
	@FXML private Label weekTwoFridayLabel;
	@FXML private Label weekTwoSaturdayLabel;
	@FXML private Label weekThreeSundayLabel;
	@FXML private Label weekThreeMondayLabel;
	@FXML private Label weekThreeTuesdayLabel;
	@FXML private Label weekThreeWednesdayLabel;
	@FXML private Label weekThreeThursdayLabel;
	@FXML private Label weekThreeFridayLabel;
	@FXML private Label weekThreeSaturdayLabel;
	@FXML private Label weekFourSundayLabel;
	@FXML private Label weekFourMondayLabel;
	@FXML private Label weekFourTuesdayLabel;
	@FXML private Label weekFourWednesdayLabel;
	@FXML private Label weekFourThursdayLabel;
	@FXML private Label weekFourFridayLabel;
	@FXML private Label weekFourSaturdayLabel;
	@FXML private Label weekFiveSundayLabel;
	@FXML private Label weekFiveMondayLabel;
	@FXML private Label weekFiveTuesdayLabel;
	@FXML private Label weekFiveWednesdayLabel;
	@FXML private Label weekFiveThursdayLabel;
	@FXML private Label weekFiveFridayLabel;
	@FXML private Label weekFiveSaturdayLabel;
	@FXML private Label weekSixSundayLabel;
	@FXML private Label weekSixMondayLabel;
	
	//Day number labels for week
	@FXML private Label weekSundayLabel;
	@FXML private Label weekMondayLabel;
	@FXML private Label weekTuesdayLabel;
	@FXML private Label weekWednesdayLabel;
	@FXML private Label weekThursdayLabel;
	@FXML private Label weekFridayLabel;
	@FXML private Label weekSaturdayLabel;
	
	//Change to next month
	private static void nextMonthButtonAction(ActionEvent event) {
		calendar.roll(Calendar.MONTH, true);
		if (calendar.get(Calendar.MONTH) == 0) {
			calendar.roll(Calendar.YEAR, true);
		}
		clearCalendar();
		setMonthDayNumbers();
		setAppointments();
	}
	
	//Change to next week
	private static void nextWeekButtonAction(ActionEvent event) {
		if (calendar.get(Calendar.WEEK_OF_MONTH) == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
			calendar.roll(Calendar.MONTH, true);
			calendar.set(Calendar.DATE, 1);
			if (calendar.get(Calendar.MONTH) == 0) {
				calendar.roll(Calendar.YEAR, true);
			}
		} else {
			calendar.roll(Calendar.WEEK_OF_MONTH, true);
		}
		clearCalendar();
		setWeekDayNumbers();
		setAppointments();
	}
	
	//Change to previous month
	private static void previousMonthButtonAction(ActionEvent event) {
		calendar.roll(Calendar.MONTH, false);
		if (calendar.get(Calendar.MONTH) == 11) {
			calendar.roll(Calendar.YEAR, false);
		}
		clearCalendar();
		setMonthDayNumbers();
		setAppointments();
	}
	
	//Change to previous week
	private static void previousWeekButtonAction(ActionEvent event) {
		calendar.roll(Calendar.WEEK_OF_MONTH, false);
		if (calendar.get(Calendar.WEEK_OF_MONTH) == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
			calendar.roll(Calendar.MONTH, false);
			if (calendar.get(Calendar.MONTH) == 11) {
				calendar.roll(Calendar.YEAR, false);
			}
		}
		clearCalendar();
		setWeekDayNumbers();
		setAppointments();
		}
	
	//Go back to appointments screen
	private void backToAppointmentsButtonAction(ActionEvent event) {
		try {
			Parent root;
			Stage stage = (Stage) backToAppointmentsButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//List of appointments
	private static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
	
	//Adds an appointment to the list
	public static void addAppointment(Appointment appointment) {
		appointments.add(appointment);
	}
	
	//Map of labels to days for month
	private static Map<Label, TextArea> monthMap = new LinkedHashMap<Label, TextArea>(37);
	
	//Map of labels to days for week
	private static Map<Label, TextArea> weekMap = new LinkedHashMap<Label, TextArea>(7);
	
	//Set the day numbers for month view
	private static void setMonthDayNumbers() {
		calendar.set(Calendar.DATE, 1);
		int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int i = 1;
		ArrayList<Label> labels = new ArrayList<Label>(monthMap.keySet());
		for (Label label : labels.subList(firstDay, firstDay + lastDay)) {
			label.setText(String.valueOf(i));
			i++;
		}
	}
	
	//Set the day numbers for week 
	private static void setWeekDayNumbers() {
		int currentMonth = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		if (calendar.get(Calendar.MONTH) != currentMonth) {
			if (calendar.get(Calendar.MONTH) == 11) {
				calendar.roll(Calendar.YEAR, true);
			}
			calendar.roll(Calendar.MONTH, true);
			calendar.set(Calendar.DATE, 1);
		}
		int firstDay;
		int lastDay = 7;
		if (calendar.get(Calendar.DATE) == 1) {
			firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		} else {
			firstDay = 0;
		}
		int i = calendar.get(Calendar.DATE);
		ArrayList<Label> labels = new ArrayList<Label>(weekMap.keySet());
		for (Label label : labels.subList(firstDay, lastDay)) {
			label.setText(String.valueOf(i));
			if (i < calendar.getActualMaximum(Calendar.DATE)) {
				i++;
			} else {
				break;
			}
		}
	}
	
	//Clear the calendar
	private static void clearCalendar() {
		monthMap.keySet().stream().forEach(label -> label.setText(null));
		monthMap.values().stream().forEach(day -> day.clear());
		weekMap.keySet().stream().forEach(label -> label.setText(null));
		weekMap.values().stream().forEach(day -> day.clear());
	}
	
	//Put the appointments in their fields
	private static void setAppointments() {
		appointments.stream()
		   .filter(appointment -> appointment.getStart().getMonth().getValue() 
				  == calendar.get(Calendar.MONTH) + 1 && appointment.getStart().getYear() == calendar.get(Calendar.YEAR))
		   .forEach(appointment -> {
				for (Label label : monthMap.keySet()) {
					if (label.getText() != null) {
						int dayNumber = Integer.parseInt(label.getText());
						int startDate = appointment.getStart().getDayOfMonth();
						int endDate = appointment.getEnd().getDayOfMonth();
						if (dayNumber >= startDate && dayNumber <= endDate) {
							monthMap.get(label).setText(monthMap.get(label).getText() +  "\n\u2022 " +  appointment.getTitle());
						}
					}
				}
				for (Label label : weekMap.keySet()) {
					if (label.getText() != null) {
						int dayNumber = Integer.parseInt(label.getText());
						int startDate = appointment.getStart().getDayOfMonth();
						int endDate = appointment.getEnd().getDayOfMonth();
						if (dayNumber >= startDate && dayNumber <= endDate) {
							weekMap.get(label).setText(weekMap.get(label).getText() +  "\n\u2022 " +  appointment.getTitle());
						}
					}
				}
			   });
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set button actions
		nextButton.setOnAction(event -> {
			if (monthRadio.isSelected()) {
				nextMonthButtonAction(event);
			} else {
				nextWeekButtonAction(event);
			}
			monthName.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())
					+ " " + calendar.get(Calendar.YEAR));
		});
		previousButton.setOnAction(event -> {
			if (monthRadio.isSelected()) {
				previousMonthButtonAction(event);
			} else {
				previousWeekButtonAction(event);
			}
			monthName.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())
					+ " " + calendar.get(Calendar.YEAR));
		});
		backToAppointmentsButton.setOnAction(event -> backToAppointmentsButtonAction(event));
		
		//Listener for month or week toggle group
		calendarGroup.selectedToggleProperty().addListener((observable, wasSelected, isSelected) -> {
			clearCalendar();
			if (isSelected == monthRadio) {
				monthPane.setVisible(true);
				weekPane.setVisible(false);
				setMonthDayNumbers();
			} else {
				monthPane.setVisible(false);
				weekPane.setVisible(true);
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.WEEK_OF_MONTH, 1);
				setWeekDayNumbers();
			}
			setAppointments();
		});
		
		//Map number labels and days for month
		monthMap.put(weekOneSundayLabel, weekOneSunday);
		monthMap.put(weekOneMondayLabel, weekOneMonday);
		monthMap.put(weekOneTuesdayLabel, weekOneTuesday);
		monthMap.put(weekOneWednesdayLabel, weekOneWednesday);
		monthMap.put(weekOneThursdayLabel, weekOneThursday);
		monthMap.put(weekOneFridayLabel, weekOneFriday);
		monthMap.put(weekOneSaturdayLabel, weekOneSaturday);
		monthMap.put(weekTwoSundayLabel, weekTwoSunday);
		monthMap.put(weekTwoMondayLabel, weekTwoMonday);
		monthMap.put(weekTwoTuesdayLabel, weekTwoTuesday);
		monthMap.put(weekTwoWednesdayLabel, weekTwoWednesday);
		monthMap.put(weekTwoThursdayLabel, weekTwoThursday);
		monthMap.put(weekTwoFridayLabel, weekTwoFriday);
		monthMap.put(weekTwoSaturdayLabel, weekTwoSaturday);
		monthMap.put(weekThreeSundayLabel, weekThreeSunday);
		monthMap.put(weekThreeMondayLabel, weekThreeMonday);
		monthMap.put(weekThreeTuesdayLabel, weekThreeTuesday);
		monthMap.put(weekThreeWednesdayLabel, weekThreeWednesday);
		monthMap.put(weekThreeThursdayLabel, weekThreeThursday);
		monthMap.put(weekThreeFridayLabel, weekThreeFriday);
		monthMap.put(weekThreeSaturdayLabel, weekThreeSaturday);
		monthMap.put(weekFourSundayLabel, weekFourSunday);
		monthMap.put(weekFourMondayLabel, weekFourMonday);
		monthMap.put(weekFourTuesdayLabel, weekFourTuesday);
		monthMap.put(weekFourWednesdayLabel, weekFourWednesday);
		monthMap.put(weekFourThursdayLabel, weekFourThursday);
		monthMap.put(weekFourFridayLabel, weekFourFriday);
		monthMap.put(weekFourSaturdayLabel, weekFourSaturday);
		monthMap.put(weekFiveSundayLabel, weekFiveSunday);
		monthMap.put(weekFiveMondayLabel, weekFiveMonday);
		monthMap.put(weekFiveTuesdayLabel, weekFiveTuesday);
		monthMap.put(weekFiveWednesdayLabel, weekFiveWednesday);
		monthMap.put(weekFiveThursdayLabel, weekFiveThursday);
		monthMap.put(weekFiveFridayLabel, weekFiveFriday);
		monthMap.put(weekFiveSaturdayLabel, weekFiveSaturday);
		monthMap.put(weekSixSundayLabel, weekSixSunday);
		monthMap.put(weekSixMondayLabel, weekSixMonday);
		
		//Map number labels and days for week
		weekMap.put(weekSundayLabel, weekSunday);
		weekMap.put(weekMondayLabel, weekMonday);
		weekMap.put(weekTuesdayLabel, weekTuesday);
		weekMap.put(weekWednesdayLabel, weekWednesday);
		weekMap.put(weekThursdayLabel, weekThursday);
		weekMap.put(weekFridayLabel, weekFriday);
		weekMap.put(weekSaturdayLabel, weekSaturday);
		
		//Set the name label to the current month and year
		monthName.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault()) + " " + calendar.get(Calendar.YEAR));
		//Set the number labels for each date
		clearCalendar();
		setMonthDayNumbers();
		//Show the appointments in their respective dates
		try {
			appointments.addAll(AppointmentManager.getAllRows());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setAppointments();
		}
	}
