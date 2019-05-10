package main.java.dbConnection;

import model.Movie;
import model.Presentation;
import model.Room;

import java.sql.*;
import java.util.ArrayList;

public class Dbc {

    private static Connection ActConnection;

    public static Connection initConnection() throws Exception{

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/kbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
        return con;
    }

    public static ArrayList<Movie> loadMovies() throws Exception {
        String query = "Select * from movie";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                Movie tempMovie = new Movie(rs.getInt("id_movie"), rs.getString("name"), rs.getString("lenght"));
                movies.add(tempMovie);
            }

            return movies;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void AddMovie(Movie movie) throws Exception {
        String query = "{ call AddMovie(?,?)}";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            CallableStatement cs = ActConnection.prepareCall(query);
            cs.setInt("id_movie", loadMovies().size() + 1);
            cs.setString("name", movie.getName());
            cs.setString("length", movie.getMinutes());
            rs = cs.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Room> loadRooms() throws Exception {
        String query = "Select * from room";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                Room tempRoom = new Room(rs.getInt("id_room"), rs.getString("name"));
                rooms.add(tempRoom);
            }
            return rooms;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void AddRoom(String name) throws Exception {
        String query = "{ call AddRoom(?)}";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            CallableStatement cs = ActConnection.prepareCall(query);
            cs.setInt("id_room", loadRooms().size() + 1);
            cs.setString("name", name);
            rs = cs.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Presentation> loadPresentations() throws Exception {
        try {
        String query = "Select * from presentation";
        ResultSet rs;
        ActConnection = initConnection();
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<Presentation> presentations = new ArrayList<>();
            while (rs.next()) {
                Presentation presentation = new Presentation(rs.getInt("id_presentation"), getRoomById(rs.getInt("fk_room")),
                        getMovieById(rs.getInt("fk_movie")),rs.getDate("startdate"), rs.getDate("enddate"), rs.getString("time"));
            }
            return presentations;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void AddPresentations(String name) throws Exception {
        String query = "{ call AddPresentations(?,?)}";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            CallableStatement cs = ActConnection.prepareCall(query);
            cs.setInt("id_movie", loadMovies().size() + 1);
            cs.setString("name", name);
            rs = cs.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Movie getMovieById(int id) throws Exception {
        ActConnection = initConnection();
        String query = "Select * from movie where id=?";
        ResultSet rs;
        try {
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            Movie movie = new Movie();
            movie.setId(rs.getInt("id_movie"));
            movie.setName(rs.getString("name"));
            movie.setMinutes(rs.getString("length"));
            return movie;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Room getRoomById(int id) throws Exception {
        ActConnection = initConnection();
        String query = "Select * from room where id=?";
        ResultSet rs;
        try {
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            Room room = new Room();
            room.setId(rs.getInt("id_room"));
            room.setName(rs.getString("name"));
            return room;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Presentation getPresentationById(int id) throws Exception {
        ActConnection = initConnection();
        String query = "Select * from presentation where id=?";
        ResultSet rs;
        try {
            PreparedStatement ps = ActConnection.prepareStatement(query);
            rs = ps.executeQuery();
            Presentation presentation = new Presentation();
            presentation.setId(rs.getInt("id_presentation"));
            presentation.setMovie(getMovieById(rs.getInt("fk_movie")));
            presentation.setRoom(getRoomById(rs.getInt("fk_room")));
            presentation.setStartDateTime(rs.getDate("startdate"));
            presentation.setEndDateTime(rs.getDate("enddate"));
            presentation.setDurationInMinutes(rs.getString("time"));
            return presentation;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
    public static void editMovie(Movie movie) throws Exception {
        String query = "UPDATE table movie where id=?";
        ResultSet rs;
        ActConnection = initConnection();
        try {
            CallableStatement cs = ActConnection.prepareCall(query);
            cs.setInt("id_movie", loadMovies().size() + 1);
            cs.setString("name", name);
            rs = cs.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
     */
}
