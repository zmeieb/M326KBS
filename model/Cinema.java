package model;

import java.util.ArrayList;
import java.util.List;

public class Cinema {

    private String name;
    private List<Room> rooms = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String toString() {
        return null;
    }
}
