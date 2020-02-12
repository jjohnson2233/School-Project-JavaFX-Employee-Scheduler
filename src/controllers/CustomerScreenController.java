package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.DB;
import application.ListedCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import managers.AddressManager;
import managers.CityManager;
import managers.CountryManager;
import managers.CustomerManager;

public class CustomerScreenController implements Initializable{
	//Object for selected item
	public static ListedCustomer selectedItem;
	
	//Add appointment button
	@FXML private Button addAppointmentButton;
	
	//Add appointment button action
	private void addAppointmentButtonAction (ActionEvent event) {
		if (customersTable.getItems().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("No customer selected");
			alert.setContentText("You must select a customer to schedule an appointment");
			alert.showAndWait();
		} else {
			try {
				selectedItem = customersTable.getSelectionModel().getSelectedItem();
				//Go to add appointment screen
				Parent root;
				Stage stage = (Stage) appointmentsButton.getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("../views/AddAppointmentScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Customers Table
		@FXML private TableView<ListedCustomer> customersTable;
		@FXML private TableColumn<ListedCustomer, String> nameColumn;
		@FXML private TableColumn<ListedCustomer, String> addressColumn;
		@FXML private TableColumn<ListedCustomer, String> cityColumn;
		@FXML private TableColumn<ListedCustomer, String> countryColumn;
		@FXML private TableColumn<ListedCustomer, String> postalCodeColumn;
		@FXML private TableColumn<ListedCustomer, String> phoneColumn;
		
		//Appointments List button
		@FXML private Button appointmentsButton;
		//Appointments list button action
		private void appointmentsButtonAction(ActionEvent event) {
			try {
				Parent root;
				Stage stage = (Stage) appointmentsButton.getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Add button
		@FXML private Button addButton;
		//Add button action
		private void addButtonAction(ActionEvent event) {
			try {
				Parent root;
				Stage stage = (Stage) appointmentsButton.getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("../views/AddCustomerScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Edit button
		@FXML private Button editButton;
		//Edit button action
		private void editButtonAction(ActionEvent event) {
			//Assign selected item
			selectedItem = customersTable.getSelectionModel().getSelectedItem();
			
			try {
				//Go to edit screen
				Parent root;
				Stage stage = (Stage) appointmentsButton.getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("../views/EditCustomerScreen.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Delete button
		@FXML private Button deleteButton;
		//Delete button action
		private void deleteButtonAction(ActionEvent event) {
			//Get the selected item
			selectedItem = customersTable.getSelectionModel().getSelectedItem();
			
			//Delete items from database 
			CustomerManager.delete(selectedItem.getName());
			AddressManager.delete(selectedItem.getAddress(), selectedItem.getAddress2());
			CityManager.delete(selectedItem.getCity());
			CountryManager.delete(selectedItem.getCountry());
			
			//Delete from table
			customersTable.getItems().remove(customersTable.getSelectionModel().getSelectedIndex());
		}

		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			//Set button actions
			addAppointmentButton.setOnAction(event -> addAppointmentButtonAction(event));
			appointmentsButton.setOnAction(event -> appointmentsButtonAction(event));
			addButton.setOnAction(event -> addButtonAction(event));
			editButton.setOnAction(event -> editButtonAction(event));
			deleteButton.setOnAction(event -> deleteButtonAction(event));
			
			//Display data in table
			try {
				customersTable.setItems(DB.gatherCustomers());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//Set column values
			nameColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("name"));
			addressColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("address"));
			cityColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("city"));
			countryColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("country"));
			postalCodeColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("postalCode"));
			phoneColumn.setCellValueFactory(new PropertyValueFactory<ListedCustomer, String>("phone"));
		}
}