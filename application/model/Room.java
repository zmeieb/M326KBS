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


    public List<Room> addRoom(Room room, List<Room> roomList){
        roomList.add(room);

        return roomList;
    }

    public boolean doesRoomExist(Room room, List<Room> roomList){
        boolean exists = false;
        for(Room roomToSearch : roomList){
            if (room.getId() == room.getId()){
                exists = true;
            }
        }
        return exists;
    }


    public List<Room> deleteRoom(Room room, List<Room> roomList){
        for(Room roomToDelete : roomList){
            if (roomToDelete.getId()== (room.getId())){
                roomList.remove(roomToDelete);
            }
        }
        return roomList;
    }
}
