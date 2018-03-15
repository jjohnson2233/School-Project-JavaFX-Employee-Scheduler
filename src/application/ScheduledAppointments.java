package application;

import java.util.ArrayList;

import tables.Appointment;

public class ScheduledAppointments {
	//List of scheduled appointments
	private static ArrayList<Appointment> list = new ArrayList<Appointment>();
	
	
	
	public static ArrayList<Appointment> getList() {
		return list;
	}

	//Add an appointment to the list
	public static void add(Appointment appointment) {
		list.add(appointment);
	}
	
	//Remove an appointment from the list
	public static boolean remove(Appointment appointment) {
		for (Appointment a : list) {
			if (a.getAppointmentID() == appointment.getAppointmentID()) {
				list.remove(a);
				return true;
			}
		}
		return false;
	}
	
	public static boolean doesOverlap(Appointment newer) {
		if (list.isEmpty()) {
			return false;
		} else {
			for (Appointment existing : list) {
				if ((newer.getStart().isBefore(existing.getStart()) && newer.getEnd().isAfter(existing.getEnd()))
						|| (newer.getStart().isAfter(existing.getStart()) && newer.getStart().isBefore(existing.getEnd()))
						|| newer.getEnd().isAfter(existing.getStart()) && newer.getEnd().isBefore(existing.getEnd())) {
					return true;
				} 
			}
			return false;
		}
	}
}
