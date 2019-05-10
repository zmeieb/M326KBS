package main.java.model;

import dbConnection.Dbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PresentationList extends ArrayList<Presentation> {
	transient Presentation presentation;

	public PresentationList() throws Exception {
		//this.presentation = Dbc.loadPresentations();
	}

	// Neue Presentation hinzufügen
	public void addPresentation(Presentation presentation) {
		this.add(presentation);

		//save();
	}

	// Neue Presentation erstellen und hinzufügen
	public void addPresentation(int id, Room room, Movie movie, Date startDateTime, Date endDateTime, String durationInMinutes) {
		presentation = new Presentation(id, room, movie, startDateTime, endDateTime, durationInMinutes);
		this.add(presentation);

		//save();
	}

	// Presentation editieren (id muss natürlich die der alten Presentation sein)
	public String editPresentation(Presentation editedPresentation) {
		try {
			presentation = getPresentationById(editedPresentation.id);
			presentation.room = editedPresentation.room;
			presentation.movie = editedPresentation.movie;
			presentation.startDateTime = editedPresentation.startDateTime;
			presentation.endDateTime = editedPresentation.endDateTime;
			presentation.durationInMinutes = editedPresentation.durationInMinutes;

		//	save();
			return "s31";

		} catch (Exception e) {
			return "e30";
		}
	}

	// Presentation löschen mit Objekt (boolean gibt Wert zurück ob wirklich gelöscht
	// wurde)
	public boolean deletePresentation(Presentation presentation) {
		presentation = getPresentationById(presentation.id);

		boolean hasRemoved = this.remove(presentation);
	//	save();

		return hasRemoved;
	}

	// Sucht Presentation per Id, wenn keine gefunden dann wird null zurückgegeben
	public Presentation getPresentationById(int id) {

		if (this.size() != 0) {
			for (Presentation presentation : this) {
				if (presentation.id == id) {
					return presentation;
				}
			}
		}
		return null;
	}

	// Überprüfen ob es in Zukunft eine Presentation mit diesem Movie gibt
	public boolean isPresentationDepending(Movie movie) {
		Date date = new Date();

		for (Presentation presentation : this) {
			if (presentation.movie == movie && presentation.startDateTime.after(date) && presentation.movie == movie
					&& presentation.startDateTime.before(date)) {
				return true;
			}
		}

		return false;
	}

	// Überprüfen ob es in Zukunft eine Presentation mit diesem Room gibt
	public boolean isPresentationDepending(Room room) {
		Date date = new Date();

		for (Presentation presentation : this) {
			if (presentation.room == room && presentation.startDateTime.after(date) && presentation.room == room
					&& presentation.startDateTime.before(date)) {
				return true;
			}
		}

		return false;
	}

	// Gibt alle Presentations zurück die an einem bestimmten Tag stattfinden
	public PresentationList getPresentationsByDate(String mainDate) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		PresentationList tmpPresentationList = new PresentationList();

		// Überprüft ob mainDate valid ist
		try {
			Date tmpDate = format.parse(mainDate);
		} catch (ParseException e) {
			return null;
		}

		if (this.size() != 0) {
			for (Presentation presentation : this) {
				if (mainDate.equals(format.format(presentation.startDateTime))) {
					tmpPresentationList.add(presentation);
				}
			}
		}
		return tmpPresentationList;
	}

	// Man übergibt beim Erstellen einer neuen Presentation die geplante Startzeit und
	// den Movie
	// Eine Liste mit nicht besetzen Räumen wird zurückgegeben
	public RoomList getAvailableRooms(Date startDateTime, Movie movie, RoomList roomList) {
		// System.out.println("StartDateTime" + startDateTime.toString());
		// create new tmp Room list
		RoomList tmpRoomList = new RoomList();

		long newPresentationStartTime = startDateTime.getTime();
		long movieDurationMillisec = Integer.parseInt(movie.getMinutes()) * 60000;
		long newPresentationEndTime = newPresentationStartTime + movieDurationMillisec + 0;

		if (tmpRoomList.size() > 0) {
			for (Presentation presentation : this) {
				long existPresentationStart = presentation.startDateTime.getTime();
				long existPresentationEnd = presentation.endDateTime.getTime();
				if ((existPresentationStart <= newPresentationStartTime && newPresentationStartTime <= existPresentationEnd)
						|| (existPresentationStart <= newPresentationEndTime && existPresentationEnd >= newPresentationEndTime)) {
					for (Room room : tmpRoomList) {
						if (room.getName().equals(presentation.room.getName())) {
							tmpRoomList.remove(room);
							break;
						}
					}

				}

				// if (movieStartMillisec >= presentationStartMillisec && movieEndMillisec
				// <= presentationEndMillisec) {
				// tmpRoomList.remove(presentation.room);
				// }
				//
				// else if (movieEndMillisec >= presentationStartMillisec &&
				// movieEndMillisec <= presentationEndMillisec) {
				// tmpRoomList.remove(presentation.room);
				// } else {
				//
				// }
			}
		}
		return tmpRoomList;
	}

	// Überprüfen ob neue Presentation in geplantem Saal erstellt werden kann
	public boolean isAvailable(Date startDateTime, Movie movie) {
		long movieStartMillisec = startDateTime.getTime();
		long movieDurationMillisec = Integer.parseInt(movie.getMinutes()) * 60000;
		long movieEndMillisec = movieStartMillisec + movieDurationMillisec;

		if (!this.isEmpty()) {
			for (Presentation presentation : this) {
				long presentationStartMillisec = presentation.startDateTime.getTime() - 1800000;
				long presentationEndMillisec = presentation.endDateTime.getTime() + 1800000;

				if (movieStartMillisec >= presentationStartMillisec && movieEndMillisec <= presentationEndMillisec) {
					return false;
				}

				else if (movieEndMillisec >= presentationStartMillisec && movieEndMillisec <= presentationEndMillisec) {
					return false;

				}
			}
		}
		return true;
	}

	// Rechnet anhand von Moviedauer das Spielende aus und gibt dieses im Date
	// Format zurück
	public Date getEndTime(Date startDateTime, Movie movie) {
		long movieStartMillisec = startDateTime.getTime();
		long durationMillisec = Integer.parseInt(movie.getMinutes()) * 60000;
		long movieEndMillisec = movieStartMillisec + durationMillisec;
		return new Date(movieEndMillisec);
	}

	// Gibt grösste bis jetzt vorhandene ID zurück +1
	public int getNewId() {
		if (this.size() == 0) {
			return 1;
		}
		int id = 0;

		for (Presentation presentation : this) {
			if (presentation.id > id) {
				id = presentation.id;
				System.out.println(id);
			}
		}
		id++;
		System.out.println(id);

		return id;
	}

}
