package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.DB;
import tables.User;

public class UserManager {
	//Add a user
	public static boolean add(User user) {
		//SQL statement
		String sql = "INSERT into user "
				+ "(userId, userName, password, active, createdBy, createDate, lastUpdate, lastUpdateBy) "
				+ "VALUES (?, ?, ?, 1, ?, NOW(), NOW(), ?)";
		
		//Database Connection
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values
			stmt.setInt(1, user.getUserID());
			stmt.setString(2, user.getUserName());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getCreateBy());
			stmt.setString(5, user.getLastUpdatedBy());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit a user
	public static boolean edit(User user) {
		//SQL statement
				String sql = "UPDATE user SET "
									+ "userName = ?, password = ?, active = ?, lastUpdate = NOW(), lastUdatedBy = ?"
									+ "WHERE userId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setString(1, user.getUserName());
					stmt.setString(2, user.getPassword());
					stmt.setInt(3, user.getActive());
					stmt.setString(4, user.getLastUpdatedBy());
					
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
		
	//Delete a user
	public static boolean delete(User user) {
		//SQL statement
		String sql = "DELETE FROM user WHERE userId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, user.getUserID());
			
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

	//Get a user from ID
	public static User getRow(int userID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM user WHERE userId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				User user = new User(rs.getString("userName"), rs.getString("password"));
				user.setUserID(userID);
				user.setCreateDate(rs.getTimestamp("createDate"));
				user.setCreateBy(rs.getString("createdBy"));
				user.setLastUpdate(rs.getTimestamp("lastUpdate"));
				user.setLastUpdatedBy(rs.getString("lastUpdateBy"));
				return user;
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
	
	//Get a user from user name
	public static User getRow(String userName) throws SQLException {
		String sql = "SELECT * FROM user WHERE userName = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, userName);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				User user = new User(rs.getString("userName"), rs.getString("password"));
				user.setUserID(rs.getInt("userId"));
				user.setCreateDate(rs.getTimestamp("createDate"));
				user.setCreateBy(rs.getString("createdBy"));
				user.setLastUpdate(rs.getTimestamp("lastUpdate"));
				user.setLastUpdatedBy(rs.getString("lastUpdateBy"));
				return user;
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
	
	//Get all users
	public static ArrayList<User> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM user");
			ArrayList<User> users = new ArrayList<User>();
			//Create objects for database items and add them to the user list
			while (rs.next()) {
				User user = new User(rs.getString("userName"), rs.getString("password"));
				user.setUserID(rs.getInt("userId"));
				user.setCreateDate(rs.getTimestamp("createDate"));
				user.setCreateBy(rs.getString("createdBy"));
				user.setLastUpdate(rs.getTimestamp("lastUpdate"));
				user.setLastUpdatedBy(rs.getString("lastUpdateBy"));
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	//Get highest user ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(userId) AS userId FROM user");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("userId");
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM user");
			
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

	//Look up a user
	public static boolean lookup(String userName) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT userName FROM user WHERE userName = ?");
				) {
			
			stmt.setString(1, userName);
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
