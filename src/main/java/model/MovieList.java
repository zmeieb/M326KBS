package main.java.model;

import java.util.ArrayList;


public class MovieList extends ArrayList<Movie> {


	transient Movie movie;

	public MovieList() {
	}

	// Neuen Movie hinzuf�gen
	public void addMovie(Movie movie){
		this.add(movie);
	//	save();
	}
	
	// Neuen Movie erstellen und hinzuf�gen
	public void addMovie(int id, String durationInMinutes, String name){
		Movie movie = new Movie(id, name, durationInMinutes);
		this.add(movie);

	//	Dbc.AddMovie();
	}
	
	// Movie editieren (id muss nat�rlich die des alten Moviees sein)
	public void editMovie(Movie editedMovie){
		//movie.setId(getMovieById(editedMovie.getId()));
		
		movie.setMinutes(editedMovie.getMinutes());
		movie.setName(editedMovie.getName());

		//save();
	}
	
	
	// Movie l�schen mit Objekt (boolean gibt Wert zur�ck ob wirklich gel�scht wurde)
	public boolean deleteMovie(Movie movie){
		movie = getMovieById(movie.getId());
		
		boolean hasRemoved = this.remove(movie);
		//save();
		
		return hasRemoved;
	}
	

	
	// Pr�fen ob ein Movie schon existiert mit Variablen
	public boolean doMovieExist(String title){
		
		if(this.size() != 0){
			for(Movie movie : this){
				if(title.equals(movie.getName())){
					return true;
				}			
			}
		}
		return false;
	}
	
	// Sucht Movie per Id, wenn keine gefunden dann wird null zur�ckgegeben
	public Movie getMovieById(int id){
		
		if(this.size() != 0){
			for(Movie movie : this){
				
				if(movie.getId() == id){
					return movie;
				}
			}
		}
		return null;
	}
	
	// Gibt gr�sste bis jetzt vorhandene ID zur�ck +1
	public int getNewId(){
		if(this.size() == 0){
			System.out.println("Liste ist leer");
			return 1;
		}
		int id = 0;
			
		for(Movie movie : this){
			if(movie.getId() > id){
				id = movie.getId();
			}
		}
		return id+1;
	}
	
}
