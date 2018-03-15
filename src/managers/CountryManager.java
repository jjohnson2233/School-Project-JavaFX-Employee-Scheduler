package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.DB;
import tables.Country;

public class CountryManager {
	
	//Add a country
	public static boolean add(Country country) {
		//SQL statement
		String sql = "INSERT into country "
							+ "(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy) "
							+ "VALUES (?, ?, NOW(), ?, NOW(), ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, country.getCountryID());
			stmt.setString(2, country.getCountry());
			stmt.setString(3, DB.currentUser.getUserName());
			stmt.setString(4, DB.currentUser.getUserName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit a country
	public static boolean edit(Country country) {
		//SQL statement
				String sql = "UPDATE country SET "
									+ "country = ?"
									+ "WHERE countryId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setString(1, country.getCountry());
					stmt.setInt(2, country.getCountryID());
					
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
	
	//Delete a country
	public static boolean delete(Country country) {
		//SQL statement
		String sql = "DELETE FROM country WHERE countryId = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setInt(1, country.getCountryID());
			
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

	public static boolean delete(String country) {
		//SQL statement
		String sql = "DELETE FROM country WHERE country = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setString(1, country);
			
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
	
	//Get a country
	public static Country getRow(int countryID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM country WHERE countryId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, countryID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Country country = new Country(rs.getString("country"));
				country.setCountryID(countryID);
				country.setCreateDate(rs.getTimestamp("createDate"));
				country.setCreatedBy(rs.getString("createdBy"));
				country.setLastUpdate(rs.getTimestamp("lastUpdate"));
				country.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return country;
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
	
	public static Country getRow(String country) throws SQLException {
		String sql = "SELECT * FROM country WHERE country = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, country);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				Country country1 = new Country(rs.getString("country"));
				country1.setCountryID(rs.getInt("countryId"));
				country1.setCreateDate(rs.getTimestamp("createDate"));
				country1.setCreatedBy(rs.getString("createdBy"));
				country1.setLastUpdate(rs.getTimestamp("lastUpdate"));
				country1.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return country1;
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
	
	//Get all Countries
	public static ArrayList<Country> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM country");
			ArrayList<Country> countries = new ArrayList<Country>();
			//Create objects for database items and add them to the country list
			while (rs.next()) {
				Country country = new Country(rs.getString("country"));
				country.setCountryID(rs.getInt("countryId"));
				country.setCreateDate(rs.getTimestamp("createDate"));
				country.setCreatedBy(rs.getString("createdBy"));
				country.setLastUpdate(rs.getTimestamp("lastUpdate"));
				country.setLastUpdateBy(rs.getString("lastUpdateBy"));
				countries.add(country);
			}
			return countries;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	//Get highest country ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(countryId) AS countryId FROM country");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("countryId");
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM country");
			
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

	//Look up a country
	public static boolean lookup(String country) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT country FROM country WHERE country = ?");
				) {
			
			stmt.setString(1, country);
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
