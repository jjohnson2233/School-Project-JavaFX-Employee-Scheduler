package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import application.DB;
import tables.Reminder;

public class ReminderManager {
	//Add a Reminder
	public static boolean add(Reminder reminder) {
		
		//SQL statement
		String sql = "INSERT into reminder "
							+ "(reminderId, reminderDate, appointmentId, createdBy, createdDate, snoozeIncrement, snoozeIncrementTypeId, remindercol) "
							+ "VALUES (?, ?, ?, ?, NOW(), ?, ?, ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, reminder.getReminderID());
			stmt.setTimestamp(2, Timestamp.valueOf(reminder.getReminderDate().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
			stmt.setInt(3, reminder.getAppointmentID());
			stmt.setString(4, DB.currentUser.getUserName());
			stmt.setInt(5, 0);
			stmt.setInt(6, 0);
			stmt.setString(7, "Yeah boi");
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit a reminder
	public static boolean edit(Reminder reminder) {
		
		//SQL statement
				String sql = "UPDATE reminder SET "
									+ "reminderDate = ?, appointmentId = ? "
									+ "WHERE reminderId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setTimestamp(1, Timestamp.valueOf(reminder.getReminderDate().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
					stmt.setInt(2, reminder.getAppointmentID());
					stmt.setInt(3, reminder.getReminderID());
					
					//Check if successful
					int changed = stmt.executeUpdate();
					if (changed == 1) {
						return true;
					} else {
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
	}
	
	//Delete a reminder
	public static boolean delete(Reminder reminder) {
		//SQL statement
		String sql = "DELETE FROM reminder WHERE reminderId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, reminder.getReminderID());
			
			//Check if successful
			int changed = stmt.executeUpdate();
			if (changed == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean delete(int appointmentID) {
		//SQL statement
		String sql = "DELETE FROM reminder WHERE appointmentId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, appointmentID);
			
			//Check if successful
			int changed = stmt.executeUpdate();
			if (changed == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Get a reminder
	public static Reminder getRow(int reminderID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM reminder WHERE reminderId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, reminderID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				ZonedDateTime adjustedDate = rs.getTimestamp("reminderDate").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				Reminder reminder = new Reminder(adjustedDate, rs.getInt("appointmentId"));
				reminder.setReminderID(reminderID);
				reminder.setCreatedBy(rs.getString("createdBy"));
				reminder.setCreatedDate(rs.getDate("createdDate"));
				return reminder;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	//Get all reminders
	public static ArrayList<Reminder> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM reminder");
			ArrayList<Reminder> reminders = new ArrayList<Reminder>();
			//Create objects for database items and add them to the reminder list
			while (rs.next()) {
				ZonedDateTime adjustedDate = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				Reminder reminder = new Reminder(adjustedDate, rs.getInt("appointmentId"));
				reminder.setReminderID(rs.getInt("reminderId"));
				reminder.setCreatedBy(rs.getString("createdBy"));
				reminder.setCreatedDate(rs.getDate("createdDate"));
				reminders.add(reminder);
			}
			return reminders;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	//Get highest reminder ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(reminderId) AS reminderId FROM reminder");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("reminderId");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	//Get number of rows in table
	public static int count() throws SQLException {
		ResultSet rs = null;
		
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT COUNT(*) FROM reminder");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
}
