package tables;

import java.sql.SQLException;
import java.sql.Timestamp;

import managers.UserManager;

public class User {
	private int userID;
	private String userName;
	private String password;
	private int active;
	private String createBy;
	private Timestamp createDate;
	private Timestamp lastUpdate;
	private String lastUpdatedBy;

	public User(String userName, String password) throws SQLException {
		if (UserManager.count() == 0) {
			userID = 1;
		} else {
			userID = UserManager.getMax() + 1;
		}
		this.userName = userName;
		this.password = password;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp timestamp) {
		this.createDate = timestamp;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
}
