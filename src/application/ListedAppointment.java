package application;

public class ListedAppointment {
	private String appointmentID;
	private String customerID;
	private String title;
	private String description;
	private String location;
	private String contact;
	private String url;
	private String start;
	private String end;
	private String startHour;
	private String startMinute;
	private String endHour;
	private String endMinute;
	private String ampmStart;
	private String ampmEnd;
	
	public ListedAppointment(String appointmentID, String customerID, String title, String description, String location,
			String contact, String url, String start, String end, String startHour, String startMinute, String endHour,
			String endMinute, String ampmStart, String ampmEnd) {
		this.appointmentID = appointmentID;
		this.customerID = customerID;
		this.title = title;
		this.description = description;
		this.location = location;
		this.contact = contact;
		this.url = url;
		this.start = start;
		this.end = end;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
		this.ampmStart = ampmStart;
		this.ampmEnd = ampmEnd;
	}

	public String getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public String getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	

	public String getAmpmStart() {
		return ampmStart;
	}
	

	public void setAmpmStart(String ampmStart) {
		this.ampmStart = ampmStart;
	}
	

	public String getAmpmEnd() {
		return ampmEnd;
	}
	

	public void setAmpmEnd(String ampmEnd) {
		this.ampmEnd = ampmEnd;
	}

	
}
