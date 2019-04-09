package application.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private Integer id;
    private Integer numberOfFreePlaces;
    private List<Row> rows = new ArrayList<>();

    public Room(){

    }

    public Room(int id){
        this.id = id;
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfFreePlaces() {
        return numberOfFreePlaces;
    }

    public void setNumberOfFreePlaces(Integer numberOfFreePlaces) {
        this.numberOfFreePlaces = numberOfFreePlaces;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String toString() {
        return null;
    }
}
