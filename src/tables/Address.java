package tables;

import java.sql.SQLException;
import java.sql.Timestamp;

import managers.AddressManager;

public class Address {
	private int addressID;
	private String address;
	private String address2;
	private int cityID;
	private int postalCode;
	private long phone;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	
	
	public Address(String address, String address2, int cityID, int postalCode, long phone) throws SQLException {
		if (AddressManager.count() == 0) {
			addressID = 1;
		} else {
			addressID = AddressManager.getMax() + 1;
		}
		this.address = address;
		this.address2 = address2;
		this.cityID = cityID;
		this.postalCode = postalCode;
		this.phone = phone;
	}

	public int getAddressID() {
		return addressID;
	}
	
	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public int getCityID() {
		return cityID;
	}
	
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	
	public int getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}
	
	public long getPhone() {
		return phone;
	}
	
	public void setPhone(long phone) {
		this.phone = phone;
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