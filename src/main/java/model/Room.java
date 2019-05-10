package main.java.model;

public class Room {

	int id;
	String name;
	
	public Room(int id, String name){
		this.id = id;
		this.name = name;
	}

	public Room(){

	}
	
	// Gibt einen neuen Room zurück
	public Room(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
