package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.DB;
import tables.Address;

public class AddressManager {
	//Add an address
	public static boolean add(Address address) {
		//SQL statement
		String sql = "INSERT into address "
							+ "(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
							+ "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, address.getAddressID());
			stmt.setString(2, address.getAddress());
			stmt.setString(3, address.getAddress2());
			stmt.setInt(4, address.getCityID());
			stmt.setInt(5, address.getPostalCode());
			stmt.setLong(6, address.getPhone());
			stmt.setString(7, DB.currentUser.getUserName());
			stmt.setString(8, DB.currentUser.getUserName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit an address
	public static boolean edit(Address address) {
		//SQL statement
				String sql = "UPDATE address SET "
									+ "address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ? "
									+ "WHERE addressId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setString(1, address.getAddress());
					stmt.setString(2, address.getAddress2());
					stmt.setInt(3, address.getCityID());
					stmt.setInt(4, address.getPostalCode());
					stmt.setLong(5, address.getPhone());
					stmt.setInt(6, address.getAddressID());
					
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
	
	//Delete an address
	public static boolean delete(Address address) {
		//SQL statement
		String sql = "DELETE FROM address WHERE addressId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, address.getAddressID());
			
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

	public static boolean delete(String address, String address2) {
		//SQL statement
		String sql = "DELETE FROM address WHERE address = ? AND address2 = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setString(1, address);
			stmt.setString(2, address2);
			
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
	
	//Get an address
	public static Address getRow(int addressID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM address WHERE addressId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, addressID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Address address = new Address(rs.getString("address"), rs.getString("address2"), rs.getInt("cityId"), rs.getInt("postalCode"), rs.getLong("Phone"));
				address.setAddressID(addressID);
				address.setCreateDate(rs.getTimestamp("createDate"));
				address.setCreatedBy(rs.getString("createdBy"));
				address.setLastUpdate(rs.getTimestamp("lastUpdate"));
				address.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return address;
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
	
	public static Address getRow(String addressName, String addressName2) throws SQLException {
		String sql = "SELECT * FROM address WHERE address = ? AND address2 = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, addressName);
			stmt.setString(2, addressName2);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Address address = new Address(rs.getString("address"), rs.getString("address2"), rs.getInt("cityId"), rs.getInt("postalCode"), rs.getLong("phone"));
				address.setAddressID(rs.getInt("addressId"));
				address.setCreateDate(rs.getTimestamp("createDate"));
				address.setCreatedBy(rs.getString("createdBy"));
				address.setLastUpdate(rs.getTimestamp("lastUpdate"));
				address.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return address;
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
	
	//Get all addresses
	public static ArrayList<Address> getAllRows() throws SQLException {
			ResultSet rs = null;
			
			//Connection to database
			try (
					Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					) {
				//Get results from SQL query
				rs = stmt.executeQuery("SELECT * FROM address");
				ArrayList<Address> addresses = new ArrayList<Address>();
				//Create objects for database items and add them to the address list
				while (rs.next()) {
					Address address = new Address(rs.getString("address"), rs.getString("address2"), rs.getInt("cityId"), rs.getInt("postalCode"), rs.getInt("phone"));
					address.setAddressID(rs.getInt("addressId"));
					address.setCreateDate(rs.getTimestamp("createDate"));
					address.setCreatedBy(rs.getString("createdBy"));
					address.setLastUpdate(rs.getTimestamp("lastUpdate"));
					address.setLastUpdateBy(rs.getString("lastUpdateBy"));
					addresses.add(address);
				}
				return addresses;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} finally {
				if (rs != null) rs.close();
			}
		}

	//Get highest address ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(addressId) AS addressId FROM address");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("addressId");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (rs != null) rs.close();
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM address");
			
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
			if (rs != null) rs.close();
		}
	}

	//Look up a address
	public static boolean lookup(String address, String address2) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT address, address2 FROM address WHERE address = ? AND address2 = ?");
				) {
			
			stmt.setString(1, address);
			stmt.setString(2, address2);
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
			if (rs != null) rs.close();
		}
	}
}
