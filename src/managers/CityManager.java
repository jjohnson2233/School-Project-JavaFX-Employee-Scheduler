package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.DB;
import tables.City;

public class CityManager {
	
	//Add a city
	public static boolean add(City city) {
		//SQL statement
		String sql = "INSERT into city "
							+ "(cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
							+ "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Set values in database
			stmt.setInt(1, city.getCityID());
			stmt.setString(2, city.getCity());
			stmt.setInt(3, city.getCountryID());
			stmt.setString(4, DB.currentUser.getUserName());
			stmt.setString(5, DB.currentUser.getUserName());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Edit a city
	public static boolean edit(City city) {
		//SQL statement
				String sql = "UPDATE city SET "
									+ "city = ?, countryId = ?"
									+ "WHERE cityId = ?";
				
				//Connection to database
				try (
						Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					//Set values in database
					stmt.setString(1, city.getCity());
					stmt.setInt(2, city.getCountryID());
					stmt.setInt(3, city.getCityID());
					
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
	
	//Delete a city
	public static boolean delete(City city) {
			//SQL statement
			String sql = "DELETE FROM city WHERE cityId = ?";
			
			//Connection to database
			try (
					Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
					PreparedStatement stmt = conn.prepareStatement(sql);
					) {
				
				//Delete row from database
				stmt.setInt(1, city.getCityID());
				
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

	public static boolean delete(String city) {
		//SQL statement
		String sql = "DELETE FROM city WHERE city = ?";
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Delete row from database
			stmt.setString(1, city);
			
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
	
	//Get a city
	public static City getRow(int cityID) throws SQLException {
		//SQL statement
		String sql = "SELECT * FROM city WHERE cityId = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setInt(1, cityID);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				City city = new City(rs.getString("city"), rs.getInt("countryId"));
				city.setCityID(cityID);
				city.setCreateDate(rs.getTimestamp("createDate"));
				city.setCreatedBy(rs.getString("createdBy"));
				city.setLastUpdate(rs.getTimestamp("lastUpdate"));
				city.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return city;
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
	
	public static City getRow(String cityName) throws SQLException {
		String sql = "SELECT * FROM city WHERE city = ?";
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			//Select row from database
			stmt.setString(1, cityName);
			rs = stmt.executeQuery();
			
			//Check if successful
			if (rs.next()) {
				City city = new City(rs.getString("city"), rs.getInt("countryId"));
				city.setCityID(rs.getInt("countryId"));
				city.setCreateDate(rs.getTimestamp("createDate"));
				city.setCreatedBy(rs.getString("createdBy"));
				city.setLastUpdate(rs.getTimestamp("lastUpdate"));
				city.setLastUpdateBy(rs.getString("lastUpdateBy"));
				return city;
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
	
	//Get all cities
	public static ArrayList<City> getAllRows() throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM city");
			ArrayList<City> cities = new ArrayList<City>();
			//Create objects for database items and add them to the city list
			while (rs.next()) {
				City city = new City(rs.getString("city"), rs.getInt("countryId"));
				city.setCityID(rs.getInt("cityId"));
				city.setCreateDate(rs.getTimestamp("createDate"));
				city.setCreatedBy(rs.getString("createdBy"));
				city.setLastUpdate(rs.getTimestamp("lastUpdate"));
				city.setLastUpdateBy(rs.getString("lastUpdateBy"));
				cities.add(city);
			}
			return cities;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	//Get highest city ID
	public static int getMax() throws SQLException {
		ResultSet rs = null;
		
		//Connect to the database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				) {
			rs = stmt.executeQuery("SELECT MAX(cityId) AS cityId FROM city");
			
			//Check if successful
			if (rs.next()) {
				return rs.getInt("cityId");
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
			rs = stmt.executeQuery("SELECT COUNT(*) FROM city");
			
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

	//Look up a city
	public static boolean lookup(String city) throws SQLException {
		ResultSet rs = null;
		
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(DB.URL, DB.USERNAME, DB.PASSWORD);
				PreparedStatement stmt = conn.prepareStatement("SELECT city FROM city WHERE city = ?");
				) {
			
			stmt.setString(1, city);
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