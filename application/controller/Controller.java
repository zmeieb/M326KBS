package application.controller;

import application.model.Movie;
import application.model.Presentation;
import application.model.Reservation;
import application.model.Room;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    private Movie movieClass;
    private Room room;

    private List<Movie> movieList = new ArrayList<>();
    private List<Room> roomList  = new ArrayList<>();

    public Controller() {

        //TODO Database Connection loadings

    }



    public void createNewMovie(Movie movie) {
        // Überprüft ob Film bereits existiert
        if (!movie.doesMovieExist(movie, movieList)) {
            movieList.add(movie);
        }
    }

    public void createNewRoom(Room room) {

        if (!room.doesRoomExist(room, roomList)) {
            roomList.add(room);
        }
    }


    // Gibt alle Filme zurück
    // ---------------------------------------------------------------------------------------------
    public List<Movie> getAllMovies() {
        return movieList;
    }

    public List<Room> getAllRooms() {

        return roomList;
    }

    // Gibt den gesuchten Film zurück
    public Movie getFilmByName(String name) {
        movieList = getAllMovies();
        for (Movie movie : movieList) {
            if (movie.getName().equals(name))
                return movie;
        }
        return null;
    }


    // Gibt den gesuchten Raum zuürck
    public Room getRoomByName(int id) {
        roomList = getAllRooms();
        for (Room room : roomList) {
            if (room.getId().equals(id))
                return room;
        }
        return null;
    }
}
