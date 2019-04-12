package sql;

import application.model.Presentation;
import application.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlManager {

    private static Connection ActConnection;

    public SqlManager() {
        ActConnection = InitConnection();

    }

    public static Connection InitConnection(){
        Connection conn = null;

        try {
            String url = "";
            conn = DriverManager.getConnection(url, "root", "");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

}
