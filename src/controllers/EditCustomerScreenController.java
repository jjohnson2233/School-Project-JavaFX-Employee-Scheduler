package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.ListedCustomer;
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

public class EditCustomerScreenController implements Initializable {
	//Selected item from the customer screen
	ListedCustomer selectedItem;
	
	//Name field
	@FXML private TextField nameText;
	
	//Address field
	@FXML private TextField addressTextOne;
	
	//Address field 2
	@FXML private TextField addressTextTwo;
	
	//City field
	@FXML private TextField cityText;
	
	//Country field
	@FXML private TextField countryText;
	
	//Postal code field
	@FXML private TextField postalCodeText;
	
	//Phone field
	@FXML private TextField phoneText;
	
	//Save button
	@FXML private Button saveButton;
	
	//Save button action
	private void saveButtonAction(ActionEvent event) throws InvalidDataException {
		if (countryText.getText().isEmpty() || countryText.getText().matches(".*\\d+.*") 
				|| cityText.getText().isEmpty() || addressTextOne.getText().isEmpty()
				|| !phoneText.getText().matches("[0-9]+") 
				|| !postalCodeText.getText().matches("[0-9]+") 
				|| cityText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
		if (addressTextOne.getText().isEmpty() || !phoneText.getText().matches("[0-9]+") || !postalCodeText.getText().matches("[0-9]+") || cityText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
		if (nameText.getText().isEmpty() || nameText.getText().matches(".*\\d+.*")) throw new InvalidDataException();
		try {
			//Create objects from typed data
			Country country;
			country = new Country(countryText.getText());
			City city = new City(cityText.getText(), country.getCountryID());
			Address address = new Address(addressTextOne.getText(), addressTextTwo.getText(), city.getCityID(), Integer.parseInt(postalCodeText.getText()), Long.parseLong(phoneText.getText()));
			Customer customer = new Customer(nameText.getText(), address.getAddressID());
			
			//Add objects to database
			if (!nameText.getText().equals(selectedItem.getName())) {
				Customer existingCustomer = CustomerManager.getRow(selectedItem.getName());
				customer.setCustomerID(existingCustomer.getCustomerID());
				customer.setAddressID(existingCustomer.getAddressID());
				CustomerManager.edit(customer);
			}
			if (!addressTextOne.getText().equals(selectedItem.getAddress()) || !addressTextTwo.getText().equals(selectedItem.getAddress2()) || !postalCodeText.getText().equals(selectedItem.getPostalCode()) || 
					!phoneText.getText().equals(selectedItem.getPhone())) {
				Address existingAddress = AddressManager.getRow(selectedItem.getAddress(), selectedItem.getAddress2());
				address.setAddressID(existingAddress.getAddressID());
				address.setCityID(existingAddress.getCityID());
				AddressManager.edit(address);
			}
			if (!cityText.getText().equals(selectedItem.getCity())) {
				City existingCity = CityManager.getRow(selectedItem.getCity());
				city.setCityID(existingCity.getCityID());
				city.setCountryID(existingCity.getCountryID());
				CityManager.edit(city);
			}
			if (!countryText.getText().equals(selectedItem.getCountry())) {
				Country existingCountry = CountryManager.getRow(selectedItem.getCountry());
				country.setCountryID(existingCountry.getCountryID());
				CountryManager.edit(country);
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set button actions
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
		
		//Populate text fields with selected item info
		selectedItem = CustomerScreenController.selectedItem;
		nameText.setText(selectedItem.getName());
		addressTextOne.setText(selectedItem.getAddress());
		addressTextTwo.setText(selectedItem.getAddress2());
		cityText.setText(selectedItem.getCity());
		countryText.setText(selectedItem.getCountry());
		postalCodeText.setText(selectedItem.getPostalCode());
		phoneText.setText(selectedItem.getPhone());
	}
}
