package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import exceptions.InvalidDataException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import managers.AddressManager;
import managers.CityManager;
import managers.CountryManager;
import managers.CustomerManager;
import tables.Address;
import tables.City;
import tables.Country;
import tables.Customer;

public class AddCustomerScreenController implements Initializable {
	//Name text field
	@FXML private TextField nameText;
	
	//Address text field line 1
	@FXML private TextField addressTextOne;
	
	//Address text filed line 2
	@FXML private TextField addressTextTwo;
	
	//City text field
	@FXML private TextField cityText;
	
	//Country text field
	@FXML private TextField countryText;
	
	//Postal code text field
	@FXML private TextField postalCodeText;
	
	//Phone text field
	@FXML private TextField phoneText;
	
	//Save button
	@FXML private Button saveButton;
	
	//Save button action
	private void saveButtonAction(ActionEvent event) throws InvalidDataException {
		//Check if the values already exist in the database and if not, add new ones
		try {
			//Create new objects
			Country country;
			City city;
			Address address;
			Customer customer;
			
			if (countryText.getText().isEmpty()) throw new InvalidDataException();
			if (countryText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
			if (CountryManager.lookup(countryText.getText())) {
				country = CountryManager.getRow(countryText.getText());
			} else {
				country = new Country(countryText.getText());
				CountryManager.add(country);
			}
			
			if (cityText.getText().isEmpty()) throw new InvalidDataException();
			if (CityManager.lookup(cityText.getText())) {
				city = CityManager.getRow(cityText.getText());
			} else {
				city = new City(cityText.getText(), country.getCountryID());
				CityManager.add(city);
			}
			
			if (addressTextOne.getText().isEmpty() || !phoneText.getText().matches("[0-9]+") || !postalCodeText.getText().matches("[0-9]+") || cityText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
			if (AddressManager.lookup(addressTextOne.getText(), addressTextTwo.getText())) {
				address = AddressManager.getRow(addressTextOne.getText(), addressTextTwo.getText());
			} else {
				address = new Address(addressTextOne.getText(), addressTextTwo.getText(), city.getCityID(), Integer.parseInt(postalCodeText.getText()), Long.parseLong(phoneText.getText()));
				AddressManager.add(address);
			}
			
			if (nameText.getText().isEmpty()) throw new InvalidDataException();
			if (CustomerManager.lookup(nameText.getText())) {
				customer = CustomerManager.getRow(nameText.getText());
			} else {
				if (nameText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
				customer = new Customer(nameText.getText(), address.getAddressID());
				CustomerManager.add(customer);
			}
			
			//Go back to the customer screen
			Parent root;
			Stage stage = (Stage) saveButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/CustomerScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//Cancel button
	@FXML private Button cancelButton;
	
	//Cancel button action
	private void cancelButtonAction(ActionEvent event) {
		try {
			//Go back to the customer screen
			Parent root;
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../views/CustomerScreen.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set actions to buttons
		saveButton.setOnAction(event -> {
			try {
				saveButtonAction(event);
			} catch (InvalidDataException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("One or more fields contains invalid characters or is empty");
				alert.showAndWait();
			}
		});
		cancelButton.setOnAction(event -> cancelButtonAction(event));
	}
}