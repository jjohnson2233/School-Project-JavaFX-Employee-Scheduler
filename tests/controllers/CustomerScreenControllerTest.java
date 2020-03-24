package controllers;

import application.DB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerScreenControllerTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getResource("../views/LoginScreen.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    public void createNewCustomer() throws SQLException {
        //Log in credentials
        String username = "jared";
        String password = "boos";

        //Log in with credentials
        clickOn("#usernameText");
        write(username);
        clickOn("#passwordText");
        write(password);
        clickOn("#signInButton");

        //Get the current customer count
        int initialCustomerCount = DB.getCustomerCount();

        //Create the customer with test data
        clickOn("#addButton");
        clickOn("#nameText");
        write("test name");
        clickOn("#addressTextOne");
        write("test address");
        clickOn("#addressTextTwo");
        write("test address");
        clickOn("#cityText");
        write("test city");
        clickOn("#countryText");
        write("test country");
        clickOn("#postalCodeText");
        write("99999");
        clickOn("#phoneText");
        write("9999999999");
        clickOn("#saveButton");

        //Get the new customer count after adding a customer
        int newCustomerCount = DB.getCustomerCount();

        //Verify that the count is one more than before adding the customer
        assertThat(newCustomerCount).isEqualTo(initialCustomerCount + 1);
    }
}