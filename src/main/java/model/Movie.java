package main.java.model;

import java.util.List;

public class Movie {

    private int id;
    private String name;
    private String minutes;

    public Movie(int id, String name, String minutes) {
        this.id = id;
        this.name = name;
        this.minutes = minutes;
    }

    public Movie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public List<Movie> addMovie(Movie movie, List<Movie> movieList) {
        movieList.add(movie);

        return movieList;
    }

    public boolean doesMovieExist(Movie movie, List<Movie> movieList) {
        boolean exists = false;
        for (Movie movieToSearch : movieList) {
            if (movie.getId() == movie.getId()) {
                exists = true;
            }
        }
        return exists;
    }


    public List<Movie> deleteMovie(Movie movie, List<Movie> movieList) {
        for (Movie movieToDelete : movieList) {
            if (movieToDelete.getId() == (movie.getId())) {
                movieList.remove(movieToDelete);
            }
        }
        return movieList;
    }
}
