package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import application.DB;
import exceptions.LoginException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import managers.UserManager;
import tables.User;

public class LoginScreenController {
	//Resource bundle for Internationalization
	ResourceBundle rb;
	Locale locale;
	
	//Login label
	@FXML private Label loginLabel;
	
	//User name label
	@FXML private Label usernameLabel;
	
	//Password label
	@FXML private Label passwordLabel;
	
	//User name field
	@FXML private TextField usernameText;
	
	//Password field
	@FXML private TextField passwordText;
	
	//Sign in button
	@FXML private Button signInButton;
	
	//Change to the Customer screen and add login to log file
	private void login() {
		//Log the sign-in in the log file
		String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date()) + " " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\t" + DB.currentUser.getUserName();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true)))
		{
			writer.append(timeStamp);
			writer.newLine();
		} catch (Exception e) {
			System.out.println("Timestamp failed");
		}
		
		//Change to customer screen
		Parent root = null;
		Stage stage = (Stage) signInButton.getScene().getWindow();
		try {
			root = FXMLLoader.load(getClass().getResource("/views/CustomerScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	//Sign in button action
	private void signInButtonAction(ActionEvent event) throws LoginException {
		//Check if user name or password fields are empty
		if (usernameText.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(rb.getString("blank_username_warning"));
			alert.showAndWait();
		} else if (passwordText.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(rb.getString("blank_password_warning"));
			alert.showAndWait();
		} else {
			try {
				String username = usernameText.getText().toLowerCase();
				//Check if user exists in the database
				if (UserManager.lookup(username)) {
					//Create a user object for existing user
					User user = UserManager.getRow(usernameText.getText());
					//Check if password matches
					if (user.getPassword().equals(passwordText.getText())) {
						//Set the current user
						DB.currentUser = user;
						//Change to the customer screen
						login();
					} else { //Password doesn't match
						throw new LoginException();
					}
				} else { //User name doesn't exist in the database
					//Ask user if they want to create a user
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("User doesn't exist");
					alert.setContentText("Do you want to create a new user?");
					alert.getDialogPane().setPrefSize(350, 300);
					alert.showAndWait()
							.filter(response -> response == ButtonType.OK)
							.ifPresent(response -> {
								//Create new user and set it current user
								try {
									DB.currentUser = new User(usernameText.getText().toLowerCase(), passwordText.getText());
								} catch (SQLException e) {
									e.printStackTrace();
								}
								DB.currentUser.setActive(1);
								DB.currentUser.setCreateBy(usernameText.getText());
								DB.currentUser.setLastUpdatedBy(usernameText.getText());
								//Add new user to database
								UserManager.add(DB.currentUser);
								//Change to the customer screen
								login();
							});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@FXML
	public void initialize() {
		//Set button actions
		signInButton.setOnAction(event -> {
			try {
				signInButtonAction(event);
			} catch (LoginException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(rb.getString("error"));
				alert.getDialogPane().setPrefSize(350, 300);
				alert.showAndWait();
			}
		});
		
		//Set the locale to the user's current location
		locale = Locale.getDefault();
		
		//Set the resource bundle to the locale
		rb = ResourceBundle.getBundle("LoginScreen", locale);
		
		//Set the labels to the correct language
		loginLabel.setText(rb.getString("login_label"));
		usernameLabel.setText(rb.getString("username"));
		passwordLabel.setText(rb.getString("password"));
		signInButton.setText(rb.getString("sign_in_button"));
	}
}
