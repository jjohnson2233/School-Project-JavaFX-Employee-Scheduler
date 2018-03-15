package tables;

import java.sql.SQLException;
import java.sql.Timestamp;

import managers.CustomerManager;

public class Customer {
	private int customerID;
	private String customerName;
	private int addressID;
	private int active;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;

	public Customer(String customerName, int addressID) throws SQLException {
		if (CustomerManager.count() == 0) {
			customerID = 1;
		} else {
			customerID = CustomerManager.getMax() + 1;
		}
		this.customerName = customerName;
		this.addressID = addressID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp timestamp) {
		this.createDate = timestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	
	
}