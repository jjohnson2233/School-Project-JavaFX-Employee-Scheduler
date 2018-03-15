package tables;

import java.sql.SQLException;
import java.sql.Timestamp;

import managers.CountryManager;

public class Country {
	private int countryID;
	private String country;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp lastUpdate;
	private String lastUpdateBy;
	
	

	public Country(String country) throws SQLException {
		if (CountryManager.count() == 0) {
			countryID = 1;
		} else {
			countryID = CountryManager.getMax() + 1;
		}
		this.country = country;
	}

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
