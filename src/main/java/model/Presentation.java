package main.java.model;

import java.util.Date;


public class Presentation {

	int id;
	Room room;
	Movie movie;
	Date startDateTime;
	Date endDateTime;
	String durationInMinutes;
	
	// Gibt eine neue Presentation zurück (Wird in PresentationList gebraucht & Controller)
	public Presentation(int id, Room room, Movie movie, Date startDateTime, Date endDateTime, String durationInMinutes){
		this.id = id;
		this.room = room;
		this.movie = movie;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.durationInMinutes = durationInMinutes;
	}

	public Presentation() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(String durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	


}
