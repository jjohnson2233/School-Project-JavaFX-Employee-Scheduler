package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import managers.AppointmentManager;
import managers.CityManager;
import managers.CustomerManager;
import tables.Appointment;
import tables.City;
import tables.Customer;

public class ReportsScreenController  implements Initializable {
	//Appointment types by month button
	@FXML private Button appointmentButton;
	
	//Appointment types by month button action
	private void appointmentButtonAction(ActionEvent event) throws SQLException {
		Map<Month, ArrayList<Integer>> map = new HashMap<>();
		List<Appointment> list = AppointmentManager.getAllRows();
		while (!list.isEmpty()) {
			Month month = list.get(0).getStart().getMonth();
			int questions = 0;
			int problems = 0;
			questions += list.stream()
					.filter(a -> a.getStart().getMonth().equals(month))
					.filter(a -> a.getDescription().equalsIgnoreCase("question"))
					.count();
			problems += list.stream()
					.filter(a -> a.getStart().getMonth().equals(month))
					.filter(a -> a.getDescription().equalsIgnoreCase("problem"))
					.count();
			ArrayList<Integer> types = new ArrayList<Integer>();
			types.add(questions);
			types.add(problems);
			map.put(month, types);
			list = list.stream()
					.filter(a -> !(a.getStart().getMonth().equals(month)))
					.collect(Collectors.toList());
		}
		
		map.keySet().stream()
				.forEachOrdered(month -> {
					System.out.println(month.toString());
					System.out.println("Questions: " + map.get(month).get(0));
					System.out.println("Problems: " + map.get(month).get(1));
					System.out.println("");
				});
	}
	
	//Consultant schedules button
	@FXML private Button consultantButton;
	
	//Consultant schedules button action
	private void consultantButtonAction(ActionEvent event) throws SQLException {
		//Get's a list of all current customers on file
		ArrayList<Customer> customers = CustomerManager.getAllRows();
		//Print the report for each customer
		for (Customer customer : customers) {
			//Print the name of the customer
			System.out.println(customer.getCustomerName());
			//Get list of appointments for this customer
			AppointmentManager.getAllRows().stream()
				.filter(a -> a.getCustomerID() == customer.getCustomerID())
				.forEach(a -> {
					System.out.println(DateTimeFormatter.ofPattern("MM/dd/yyyy \t hh:mm - ").format(a.getStart()) + DateTimeFormatter.ofPattern("hh:mm").format(a.getEnd()));
					System.out.println("");
				});;
		}
	}
	
	//City Population button
	@FXML private Button cityPopulationButton;
	
	//City population button action
	private void cityPopulationButtonAction(ActionEvent event) throws SQLException {
		//Get list of cities on record
		List<City> cities = CityManager.getAllRows();
		//Run until list is empty
		while (!cities.isEmpty()) {
			//Get the first city on the list
			String city = cities.get(0).getCity();
			//Count variable for population
			int count = 0;
			count += cities.stream()
					.map(c -> c.getCity())
					.filter(c -> c.equals(city))
					.count();
			//Print out the city and count
			System.out.println(city + ": " + count);
			//Remove the city from the list
			cities = cities.stream()
					.filter(c -> !(c.getCity().equals(city)))
					.collect(Collectors.toList());
		}
	}
	
	//Back button
	@FXML private Button backButton;
	
	//Back button action
	private void backButtonAction(ActionEvent event) {
		Parent root = null;
		Stage stage = (Stage) backButton.getScene().getWindow();
		try {
			root = FXMLLoader.load(getClass().getResource("/views/AppointmentScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set button actions
		appointmentButton.setOnAction(event -> {
			try {
				appointmentButtonAction(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		consultantButton.setOnAction(event -> {
			try {
				consultantButtonAction(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		backButton.setOnAction(event -> backButtonAction(event));
		cityPopulationButton.setOnAction(event -> {
			try {
				cityPopulationButtonAction(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}
