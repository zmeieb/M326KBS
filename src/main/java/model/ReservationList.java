package main.java.model;

import dbConnection.Dbc;

import java.util.ArrayList;
import java.util.Date;

public class ReservationList extends ArrayList<Reservation> {

	private Reservation reservation;

	public ReservationList() throws Exception {

	}

	// Neue Reservation hinzufügen
	public void addReservation(Reservation reservation) {
		this.add(reservation);

	//	save();
	}
	
	// Neue Reservation erstellen und hinzufügen
	public void addReservation(int id, Presentation presentation, String seatNumber, String phoneNumber, Date dateTime) {
		reservation = new Reservation(id, presentation, seatNumber, phoneNumber, dateTime);
		this.add(reservation);

		//save();
	}

	// Reservation editieren (id muss natürlich die der alten Reservierung sein)
	public void editReservation(Reservation editedReservation) {
		reservation = getReservationById(editedReservation.id);
		reservation.presentation = editedReservation.presentation;
		reservation.seatNumber = editedReservation.seatNumber;
		reservation.phoneNumber = editedReservation.phoneNumber;
		reservation.dateTime = editedReservation.dateTime;

		//save();
	}


	// Reservation löschen mit Objekt (boolean gibt Wert zurück ob wirklich gelöscht wurde)
	public boolean deleteReservation(Reservation reservation) {
		reservation = getReservationById(reservation.id);
		
		boolean hasRemoved = this.remove(reservation);
		//save();
		
		return hasRemoved;

	}


	
	// Gibt eine Reservation anhand eines Sitzplatzes zurück
	public Reservation getReservationBySeatNumber(Presentation presentation, String seatNumber){
		
		for(Reservation reservation : this){
			if(reservation.presentation == presentation && reservation.seatNumber.equals(seatNumber)){
				return reservation;
			}
		}
		
		return null;
	}

	// Sucht Reservation per Id, wenn keine gefunden dann wird null zurückgegeben
	public Reservation getReservationById(int id) {

		if(this.size() != 0){
			for (Reservation reservation : this) {
	
				if (reservation.id == id) {
					return reservation;
				}
			}
		}
		return null;
	}

	// Prüfen ob eine Reservation schon existiert mit Objekt
	public boolean doReservationExist(Reservation reservation) {
		
		if(this.size() != 0){
			for (Reservation item : this) {
				if (reservation.getPresentation() == item.getPresentation() && reservation.seatNumber == item.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

	// Prüfen ob eine Reservation schon existiert mit Variablen
	public boolean doReservationExist(Presentation presentation, String seatNumber) {
		
		if(this.size() != 0){
			for (Reservation reservation : this) {
				if (presentation == reservation.getPresentation() && seatNumber == reservation.seatNumber) {
					return true;
				}
			}
		}
		return false;
	}

// 	Gibt alle dazugehörigen Reservationen zurück
	public ReservationList getAttendantReservations(Reservation reservation) throws Exception {
		ReservationList attendantReservationList = new ReservationList();
		
		if(this.size() != 0){
			for(Reservation tmpReservation : this){
				
				// Wenn Reservierung gleiche Telefonnummer, gleicher Zeitstempfel aber nicht gleiche Id hat
				if(reservation.dateTime == tmpReservation.dateTime && reservation.phoneNumber.equals(tmpReservation.phoneNumber)){
					if(reservation.id != tmpReservation.id){
						attendantReservationList.add(tmpReservation);
					}
				}
			}
		}
		
		return attendantReservationList;
	}
	
	
	
	// Gibt eine Liste mit den bereits reservierten Plätzen zurück
	public ArrayList<String> getReservedSeats(Presentation presentation){
		ArrayList<String> tmpList = new ArrayList<>();
		
		for(Reservation reservation : this){
			if(reservation.getPresentation().getId() == presentation.getId()){
				tmpList.add(reservation.seatNumber);
			}
		}
		
		return tmpList;
	}
	
	// Gibt alle Reservationen die zur gleichen Presentation gehören zurück
	public ReservationList getReservationsByPresentation(Presentation presentation) throws Exception {
		ReservationList presentationReservationList = new ReservationList();
		
		if(this.size() != 0){
			for(Reservation reservation : this){
				if(reservation.getPresentation().getId() == presentation.getId()){
					presentationReservationList.add(reservation);
				}
			}
		}
		return presentationReservationList;
	}
	
	
	// Gibt grösste bis jetzt vorhandene ID zurück +1
	public int getNewId() {
		if (this.size() == 0) {
			return 1;
		}
		int id = 0;

		for (Reservation reservation : this) {
			if (reservation.id > id) {
				id = reservation.id;
			}
		}

		return id+1;
	}

}

