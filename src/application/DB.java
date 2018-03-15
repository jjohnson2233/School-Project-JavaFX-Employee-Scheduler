package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tables.Address;
import tables.City;
import tables.Country;
import tables.Customer;
import tables.User;

public class DB {
	public static final String USERNAME = "U03ZZf";
	public static final String PASSWORD = "53688127253";
	public static final String URL = "jdbc:mysql://52.206.157.109/U03ZZf";
	public static User currentUser;
	
	public static ObservableList<ListedCustomer> gatherCustomers() throws SQLException {
		ObservableList<ListedCustomer> list = FXCollections.observableArrayList();
		ResultSet rs = null;
		//Connection to database
		try (
				Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
			if (rs != null) rs.close();
			//Get results from SQL query
			rs = stmt.executeQuery("SELECT * FROM address");
			ArrayList<Address> addresses = new ArrayList<Address>();
			//Create objects for database items and add them to the address list
			while (rs.next()) {
				Address address = new Address(rs.getString("address"), rs.getString("address2"), rs.getInt("cityId"), rs.getInt("postalCode"), rs.getLong("phone"));
				address.setAddressID(rs.getInt("addressId"));
				address.setCreateDate(rs.getTimestamp("createDate"));
				address.setCreatedBy(rs.getString("createdBy"));
				address.setLastUpdate(rs.getTimestamp("lastUpdate"));
				address.setLastUpdateBy(rs.getString("lastUpdateBy"));
				addresses.add(address);
			}
			if (rs != null) rs.close();
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
			if (rs != null) rs.close();
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
			if (rs != null) rs.close();
			for (Customer customer : customers) {
				String name = customer.getCustomerName();
				Address address = addresses.stream().filter(i -> i.getAddressID() == customer.getAddressID()).findAny().get();
				City city = cities.stream().filter(i -> i.getCityID() == address.getCityID()).findAny().get();
				Country country = countries.stream().filter(i -> i.getCountryID() == city.getCountryID()).findAny().get();
				ListedCustomer listedCustomer = new ListedCustomer(name, address.getAddress(), address.getAddress2(), city.getCity(), country.getCountry(), 
						Integer.toString((address.getPostalCode())), Long.toString(address.getPhone()));
				list.add(listedCustomer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return list;
	}
}
