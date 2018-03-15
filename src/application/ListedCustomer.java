package application;

public class ListedCustomer {
	private String name;
	private String address;
	private String address2;
	private String city;
	private String country;
	private String postalCode;
	private String phone;
	
	public ListedCustomer(String name, String address, String address2, String city, String country, String postalCode, String phone) {
		this.name = name;
		this.address = address;
		this.address2 = address2;
		this.city = city;
		this.country = country;
		this.postalCode = postalCode;
		this.phone = phone;
	}

	//Getters and setters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPosalCode(String posalCode) {
		this.postalCode = posalCode;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
}