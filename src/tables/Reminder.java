package tables;

import java.sql.Date;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import managers.ReminderManager;

public class Reminder {
	
	private int reminderID;
	private ZonedDateTime reminderDate;
	private int appointmentID;
	private String createdBy;
	private Date createdDate;
	public Timer timer;
	public TimerTask task;
	

	public Reminder(ZonedDateTime reminderDate, int appointmentID) throws SQLException {
		if (ReminderManager.count() == 0) {
			reminderID = 1;
		} else {
			reminderID = ReminderManager.getMax() + 1;
		}
		this.reminderDate = reminderDate;
		this.appointmentID = appointmentID;
	}

	public int getReminderID() {
		return reminderID;
	}

	public void setReminderID(int reminderID) {
		this.reminderID = reminderID;
	}

	public ZonedDateTime getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(ZonedDateTime reminderDate) {
		this.reminderDate = reminderDate;
	}
	
	public int getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date date) {
		this.createdDate = date;
	}
	
	public void schedule(Appointment appointment) throws SQLException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("15 Minute Reminder");
		alert.setContentText(appointment.getTitle() + " starts in 15 minutes");
		task = new TimerTask() {
			public void run() {
				Platform.runLater(() -> {
					alert.showAndWait();
			});
		};
	};
		timer = new Timer();
		timer.schedule(task, Date.from(reminderDate.minusMinutes(15).toInstant()));
	}
}
