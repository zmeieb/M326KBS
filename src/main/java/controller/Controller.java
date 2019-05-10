package main.java.controller;

import model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Controller {
	ReservationList reservationList;
	PresentationList presentationList;
	MovieList movieList;
	RoomList roomList;

	// Meherer Reservations erstellen
	// -------------------------------------------------------------------------------------
	public boolean createNewReservations(Presentation presentation, ArrayList<String> seatNumbers, String phoneNumber) {

		Date dateTime;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String timeStamp = format.format(Calendar.getInstance().getTime());

		// Akuteller Zeitstempfel erzeugen
		try {
			dateTime = format.parse(timeStamp);
		} catch (ParseException e) {
			return false;
		}

		// �berpr�fen ob Presentation null ist
		if (presentation == null)
			return false;

		for (String seatNr : seatNumbers) {

			// �berpr�ft ob Reservation bereits existiert
			if (reservationList.doReservationExist(presentation, seatNr) == false) {
				reservationList.addReservation(reservationList.getNewId(), presentation, seatNr, phoneNumber, dateTime);
//				reservationList = fileStream.deserializeReservationList();
			} else {
				return false;
			}
		}
		return true;
	}

	// Eine neue Presentation erstellen
	// -------------------------------------------------------------------------------------------
	public String createNewPresentation(Room room, Movie movie, LocalDate startDate, String startTime) {

		Date date;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startDateTime = startDate + " " + startTime;

		// �berp�ft ob Datum angegeben wurde
		if (startDate == null)
			return "e23";
		// �berpr�ft ob Datum valid ist
		try {
			date = format.parse(startDateTime);
		} catch (ParseException e) {
			return "e19"; // Fehler beim Konvertieren des Datums
		}

		// �berpr�ft ob room & movie null sind
		if (room == null || movie == null)
			return "e20"; // Raum und Movie ausw�hlen

		// �berpr�ft ob zu dieser Zeit kein anderer Movie l�uft
		// if (presentationList.isAvailable(date, movie) == true) {
		presentationList.addPresentation(presentationList.getNewId(), room, movie, date, presentationList.getEndTime(date, movie),
				movie.getMinutes());
		return "s21"; // Vorstellung erfolgreich gespeichert
		// }else{
		// return "i22"; //Zu dieser Zeit l�uft ein Movie bereits
		// }
	}

	// Einen neuen Movie erstellen
	// -----------------------------------------------------------------------------------------
	public String createNewMovie(String durationInMinutes, String title) {

		String duration = durationInMinutes;

		// �berpr�ft ob Movie bereits existiert

			movieList.addMovie(movieList.getNewId(), duration, title);
			return "s18";

	}

	// Einen neuen Room erstellen
	// -----------------------------------------------------------------------------------------
	public boolean createNewRoom(String name) {

		if (roomList.doRoomExist(name) == false) {
			roomList.addRoom(name);
			return true;
		}
		return false;
	}

	// Gibt alle nicht besetzten R�ume zur�ck (geplante Startzeit & geplanter
	// Movie muss �bergeben werden) -----------------
	public RoomList getAllAvailableRooms(LocalDate startDate, String startTime, Movie movie) {

		Date date = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startDateTime = startDate + " " + startTime;

		try {
			date = format.parse(startDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		RoomList tmpRoomList = this.presentationList.getAvailableRooms(date, movie, this.roomList);
		tmpRoomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

		for (Room room : tmpRoomList) {
			System.out.println(room.getName());
		}

		return tmpRoomList;
	}

	// Gibt alle Presentations zur�ck die an einem bestimmten Tag stattfinden
	// -----------------------------------------------------
	public PresentationList getPresentationsByDate(String date) throws Exception {

		return presentationList.getPresentationsByDate(date);
	}

	// L�scht eine Presentation mit all ihren Reservierungen
	// ----------------------------------------------------------------------
	public String deletePresentationAndReservations(Presentation presentation) {

		if (!presentationList.deletePresentation(presentation))
			return "e24";

		for (Reservation reservation : getAllReservations()) {
			if (reservation.getPresentation() == presentation) {
				if (!reservationList.deleteReservation(reservation))
					return "e25";
			}
		}
		return "s26";
	}

	// Gibt eine Liste mit den bereits reservierten Pl�tzen zur�ck
	// --------------------------------------------------------
	public ArrayList<String> getReservedSeats(Presentation presentation) {

		return reservationList.getReservedSeats(presentation);
	}

	// Gibt alle dazugeh�rigen Reservationen zur�ck
	// -----------------------------------------------------------------------
	public ReservationList getAttendantReservations(Reservation reservation) throws Exception {

		return reservationList.getAttendantReservations(reservation);
	}

	// L�scht alle zusammengeh�rigen Reservationen (Date & Phonenumber)
	// ---------------------------------------------------
	public void deleteAllAttendantReservations(Reservation reservation) throws Exception {

		for (Reservation tmpReservation : reservationList.getAttendantReservations(reservation)) {
			reservationList.deleteReservation(tmpReservation);
		}
	}

	// Einzelne Reservation l�schen
	// ---------------------------------------------------------------------------------------
	public void deleteSingleReservation(Reservation reservation) {

		reservationList.deleteReservation(reservation);
	}

	// Eine Reservation bearbeiten
	// ----------------------------------------------------------------------------------------
	public void editReservation(Reservation reservation) {

		reservationList.editReservation(reservation);
	}

	// Gibt eine Reservation anhand eines Sitzplatzes zur�ck
	// --------------------------------------------------------------
	public Reservation getReservationBySeatNumber(Presentation presentation, String seatNumber) {

		return reservationList.getReservationBySeatNumber(presentation, seatNumber);
	}

	// Gibt alle Reservationen die zur gleichen Presentation geh�ren zur�ck
	// -------------------------------------------------------
	public ReservationList getReservationsByPresentation(Presentation presentation) throws Exception {

		return reservationList.getReservationsByPresentation(presentation);
	}

	// Movie l�schen
	// -------------------------------------------------------------------------------------------------------
	public String deleteMovie(Movie movie) {

		// �berpr�fen ob Movie in einer zuk�nftigen Presentation gebraucht wird
		if (presentationList.isPresentationDepending(movie) == false) {
			// gebraucht wird
			if (movieList.deleteMovie(movie)) {
				return "s0"; // erfolgreich
			} else {
				return "e3"; // fehler beim l�schen
			}

		} else {
			return "w1"; // wird von einer zuk�nftigen Presentation gebraucht wird
		}

	}

	// Movie bearbeiten
	// ----------------------------------------------------------------------------------------------------
	public String editMovie(Movie newmovie) {

		Movie oldMovie = movieList.getMovieById(newmovie.getId());


		// Wenn der Movietitel ge�ndert wurde
		if (!newmovie.getName().equals(oldMovie.getName())) {
			if (!movieList.doMovieExist(newmovie.getName())) {
				movieList.editMovie(newmovie);
				return "s4"; // successful
			} else {
				return "i7"; // existiert bereits
			}
		} else {
			movieList.editMovie(newmovie);
			return "s4"; // successful
		}

	}

	// Edit Presentation �------------------------------------------------------------�
	  public String editPresentation(Presentation presentation){
	    return presentationList.editPresentation(presentation);
	  }
	
	// Room l�schen
	// -------------------------------------------------------------------------------------------------------
	public String deleteRoom(Room room) {

		// �berpr�fen ob Room in zuk�nftiger Presentation gebraucht wird
		if (presentationList.isPresentationDepending(room) == false) {
			roomList.deleteRoom(room);
			return "s13"; // successful
		}

		return "w15"; // room is blocked by presentation
	}

	// Room bearbeiten
	// ----------------------------------------------------------------------------------------------------
	public void editRoom(String oldName, Room room) {

		roomList.editRoom(oldName, room);
	}

	// Gibt alle Reservationen zur�ck
	// -------------------------------------------------------------------------------------
	public ReservationList getAllReservations() {

		if (reservationList.size() > 1) {
			this.reservationList.sort((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
		}
		return this.reservationList;
	}

	// Gibt alle Presentations zur�ck
	// ---------------------------------------------------------------------------------------------
	public PresentationList getAllPresentations() {

		if (presentationList.size() > 1) {
			this.presentationList.sort((o1, o2) -> o1.getStartDateTime().compareTo(o2.getStartDateTime()));
		}
		return this.presentationList;
	}

	public Presentation getPresentationById(int id) {
		for (Presentation presentation : presentationList) {
			if (presentation.getId() == id) {
				return presentation;
			}
		}
		return null;
	}

	// Gibt alle Moviee zur�ck
	// ---------------------------------------------------------------------------------------------
	public MovieList getAllMovies() {

		if (movieList.size() > 1) {
			this.movieList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		}
		return this.movieList;
	}

	// Gibt den gesuchten Movie zur�ck
	public Movie getMovieByName(String name) {
		movieList = getAllMovies();
		for (Movie movie : movieList) {
			if (movie.getName().equals(name))
				return movie;
		}
		return null;
	}

	// Gibt alle Rooms zur�ck
	// ---------------------------------------------------------------------------------------------
	public RoomList getAllRooms() {

		if (roomList.size() > 1) {
			this.roomList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		}
		return this.roomList;
	}

	// Gibt den gesuchten Raum zu�rck
	public Room getRoomByName(String name) {
		roomList = getAllRooms();
		for (Room room : roomList) {
			if (room.getName().equals(name))
				return room;
		}
		return null;
	}
}
