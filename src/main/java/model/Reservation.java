package main.java.model;

import java.util.Date;

public class Reservation {
	
	int id;
	Presentation presentation;
	String seatNumber;
	String phoneNumber;
	Date dateTime;
	
	// Gibt eine neue Reservation zurück
	public Reservation(int id, Presentation presentation, String seatNumber, String phoneNumber, Date dateTime){
		this.id = id;
		this.presentation = presentation;
		this.seatNumber = seatNumber;
		this.phoneNumber = phoneNumber;
		this.dateTime = dateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}



	
	
}
