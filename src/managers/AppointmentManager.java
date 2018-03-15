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
import tables.Appointment;

public class AppointmentManager {
	//Add an appointment
	public static boolean add(Appointment appointment) {
		
		//SQL statement
		String sql = "INSERT into appointment "
							+ "(appointmentId, customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, appointment.getAppointmentID());
			stmt.setInt(2, appointment.getCustomerID());
			stmt.setString(3, appointment.getTitle());
			stmt.setString(4, appointment.getDescription());
			stmt.setString(5, appointment.getLocation());
			stmt.setString(6, appointment.getContact());
			stmt.setString(7, appointment.getUrl());
			stmt.setTimestamp(8, Timestamp.valueOf(appointment.getStart().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
			stmt.setTimestamp(9, Timestamp.valueOf(appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
			stmt.setString(10, DB.currentUser.getUserName());
			stmt.setString(11, DB.currentUser.getUserName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit an appointment
	public static boolean edit(Appointment appointment) {
		
		//SQL statement
				String sql = "UPDATE appointment SET "
									+ "customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end = ? "
									+ "WHERE appointmentId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setInt(1, appointment.getCustomerID());
					stmt.setString(2, appointment.getTitle());
					stmt.setString(3, appointment.getDescription());
					stmt.setString(4, appointment.getLocation());
					stmt.setString(5, appointment.getContact());
					stmt.setString(6, appointment.getUrl());
					stmt.setTimestamp(7, Timestamp.valueOf(appointment.getStart().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
					stmt.setTimestamp(8, Timestamp.valueOf(appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC+0")).toLocalDateTime()));
					stmt.setInt(9, appointment.getAppointmentID());
					
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
	
	//Delete an appointment
	public static boolean delete(Appointment appointment) {
		//SQL statement
		String sql = "DELETE FROM appointment WHERE appointmentId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, appointment.getAppointmentID());
			
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

	public static boolean delete(String title) {
		//SQL statement
		String sql = "DELETE FROM appointment WHERE title = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setString(1, title);
			
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
	
	//Get an appointment
	public static Appointment getRow(int appointmentID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM appointment WHERE appointmentId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, appointmentID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				ZonedDateTime adjustedStart = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				ZonedDateTime adjustedEnd = rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				Appointment appointment = new Appointment(rs.getInt("customerId"), rs.getString("title"), adjustedStart, adjustedEnd);
				appointment.setAppointmentID(appointmentID);
				appointment.setDescription(rs.getString("description"));
				appointment.setLocation(rs.getString("location"));
				appointment.setContact(rs.getString("contact"));
				appointment.setUrl(rs.getString("url"));
				appointment.setCreateDate(rs.getDate("createDate"));
				appointment.setCreatedBy(rs.getString("createdBy"));
				appointment.setLastUpdate(rs.getTimestamp("lastUpdate"));
				appointment.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return appointment;
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

	public static Appointment getRow(String title) throws SQLException {
		String sql = "SELECT * FROM appointment WHERE title = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, title);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				ZonedDateTime adjustedStart = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				ZonedDateTime adjustedEnd = rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				Appointment appointment = new Appointment(rs.getInt("customerId"), rs.getString("title"), adjustedStart, adjustedEnd);
				appointment.setAppointmentID(rs.getInt("appointmentId"));
				appointment.setDescription(rs.getString("description"));
				appointment.setLocation(rs.getString("location"));
				appointment.setContact(rs.getString("contact"));
				appointment.setUrl(rs.getString("url"));
				appointment.setCreateDate(rs.getDate("createDate"));
				appointment.setCreatedBy(rs.getString("createdBy"));
				appointment.setLastUpdate(rs.getTimestamp("lastUpdate"));
				appointment.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return appointment;
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
	
	//Get all appointments
	public static ArrayList<Appointment> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM appointment");
			ArrayList<Appointment> appointments = new ArrayList<Appointment>();
			//Create objects for database items and add them to the appointment list
			while (rs.next()) {
				ZonedDateTime adjustedStart = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				ZonedDateTime adjustedEnd = rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC+0")).withZoneSameInstant(ZoneId.systemDefault());
				Appointment appointment = new Appointment(rs.getInt("customerId"), rs.getString("title"), adjustedStart, adjustedEnd);
				appointment.setAppointmentID(rs.getInt("appointmentId"));
				appointment.setDescription(rs.getString("description"));
				appointment.setLocation(rs.getString("location"));
				appointment.setContact(rs.getString("contact"));
				appointment.setUrl(rs.getString("url"));
				appointment.setCreateDate(rs.getDate("createDate"));
				appointment.setCreatedBy(rs.getString("createdBy"));
				appointment.setLastUpdate(rs.getTimestamp("lastUpdate"));
				appointment.setLastUpdateBy(rs.getString("lastUpdateBy"));
				appointments.add(appointment);
			}
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	//Get highest appointment ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(appointmentId) AS appointmentId FROM appointment");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("appointmentId");
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM appointment");
			
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

	//Look up an appointment
	public static boolean lookup(String title) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT title FROM appointment WHERE title = ?");
				) {
			
			stmt.setString(1, title);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
}
