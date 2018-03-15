package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tables.Customer;
import application.DB;

public class CustomerManager {

	//Add a customer
	public static boolean add(Customer customer) {
		//SQL statement
		String sql = "INSERT into customer "
							+ "(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
							+ "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, customer.getCustomerID());
			stmt.setString(2, customer.getCustomerName());
			stmt.setInt(3, customer.getAddressID());
			stmt.setString(4, DB.currentUser.getUserName());
			stmt.setString(5, DB.currentUser.getUserName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit a customer
	public static boolean edit(Customer customer) {
		//SQL statement
				String sql = "UPDATE customer SET "
									+ "customerName = ?, addressId = ? "
									+ "WHERE customerId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setString(1, customer.getCustomerName());
					stmt.setInt(2, customer.getAddressID());
					stmt.setInt(3, customer.getCustomerID());
					
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
	
	//Delete a customer
	public static boolean delete(Customer customer) {
		//SQL statement
		String sql = "DELETE FROM customer WHERE customerId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, customer.getCustomerID());
			
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

	public static boolean delete(String customerName) {
		//SQL statement
		String sql = "DELETE FROM customer WHERE customerName = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setString(1, customerName);
			
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
	
	//Get a customer
	public static Customer getRow(int customerID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM customer WHERE customerId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, customerID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Customer customer = new Customer(rs.getString("customerName"), rs.getInt("addressId"));
				customer.setCustomerID(customerID);
				customer.setActive(rs.getInt("active"));
				customer.setCreateDate(rs.getTimestamp("createDate"));
				customer.setCreatedBy(rs.getString("createdBy"));
				customer.setLastUpdate(rs.getTimestamp("lastUpdate"));
				customer.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return customer;
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

	public static Customer getRow(String customerName) throws SQLException {
		String sql = "SELECT * FROM customer WHERE customerName = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, customerName);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Customer customer = new Customer(rs.getString("customerName"), rs.getInt("addressId"));
				customer.setCustomerID(rs.getInt("customerId"));
				customer.setCreateDate(rs.getTimestamp("createDate"));
				customer.setCreatedBy(rs.getString("createdBy"));
				customer.setLastUpdate(rs.getTimestamp("lastUpdate"));
				customer.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return customer;
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
	
	//Get all customers
	public static ArrayList<Customer> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM customer");
			ArrayList<Customer> customers = new ArrayList<Customer>();
			//Create objects for database items and add them to the customer list
			while (rs.next()) {
				Customer customer = new Customer(rs.getString("customerName"), rs.getInt("addressId"));
				customer.setCustomerID(rs.getInt("customerId"));
				customer.setActive(rs.getInt("active"));
				customer.setCreateDate(rs.getTimestamp("createDate"));
				customer.setCreatedBy(rs.getString("createdBy"));
				customer.setLastUpdate(rs.getTimestamp("lastUpdate"));
				customer.setLastUpdateBy(rs.getString("lastUpdateBy"));
				customers.add(customer);
			}
			return customers;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	//Get highest customer ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(customerId) AS customerId FROM customer");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("customerId");
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM customer");
			
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
	public static boolean lookup(String customerName) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT customerName FROM customer WHERE customerName = ?");
				) {
			
			stmt.setString(1, customerName);
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