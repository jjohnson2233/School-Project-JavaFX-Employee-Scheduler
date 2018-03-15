package tables;

import java.sql.SQLException;
import java.sql.Timestamp;

import managers.CityManager;

public class City {
	private int cityID;
	private String city;
	private int countryID;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	

	public City(String city, int countryID) throws SQLException {
		if (CityManager.count() == 0) {
			cityID = 1;
		} else {
			cityID = CityManager.getMax() + 1;
		}
		this.city = city;
		this.countryID = countryID;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
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
