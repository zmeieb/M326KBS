package main.java.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Movie;
import model.Presentation;
import model.Room;

public class PresentationItem extends BorderPane{

	// Data
	private Movie movie;
	private Room room;
	private int presentationId;

	// Layer 1
	// Visible at start
	private Image cover;
	private ImageView iv_cover;
	private String imagePath;
	private VBox presentationinfo;
	private Label lbl_start;
	// Layer 2
	private BorderPane pane;
	private Boolean clicked = false;

	// Layer 3
	private VBox dataholder;
	private Label lbl_title, lbl_duration, lbl_desc;

	public Pane createPresentationItem(Presentation presentation) {
		movie = presentation.getMovie();
		room = presentation.getRoom();
		presentationId = presentation.getId();

		//presentation daten verarbeiten
		Date presentationstart =presentation.getStartDateTime();
		Date presentationend = presentation.getEndDateTime();
		String starttime = LocalTimeToString(presentationstart);
		String endtime = LocalTimeToString(presentationend);
		String startdate = LocalDateToString(presentationstart);
		String enddate = LocalDateToString(presentationend);
		String presentationlaunch = startdate + " " + starttime + " - " + endtime;

		// presentation daten laden
		presentationinfo = new VBox();
		lbl_start = new Label(presentationlaunch);
		lbl_start.getStyleClass().add("presentationinfo");
		lbl_start.setAlignment(Pos.CENTER);
		lbl_start.setFont(Font.font("System", 15));
		lbl_start.setPrefHeight(35);
		lbl_start.setPadding(new Insets(0, 0, 0, 5));

		presentationinfo.getChildren().addAll(iv_cover, lbl_start);
		// Vorschau laden und mit daten abfüllen
		dataholder = new VBox();
		dataholder.setPrefWidth(220);
		dataholder.setPadding(new Insets(0, 0, 5, 10));
		lbl_title = new Label(movie.getName());
		lbl_title.getStyleClass().add("presentationinfo");
		lbl_title.setFont(new Font("System", 25));
		lbl_title.setPrefHeight(35);


		lbl_duration = new Label(movie.getMinutes() + " Minutes" + "\t : " + room.getName());
		lbl_duration.getStyleClass().add("presentationinfo");
		lbl_duration.setFont(Font.font("System", FontWeight.BOLD, 15));
		lbl_duration.setPrefHeight(35);

		dataholder.getChildren().addAll(lbl_title, lbl_desc, lbl_duration);
		pane = new BorderPane();
		this.setPrefHeight(330);
		this.setPrefWidth(220);
		this.setStyle(" -fx-background-color: rgb(72, 72, 72);");
		this.setLeft(presentationinfo);

		pane.setAlignment(presentationinfo, Pos.CENTER_LEFT);
		return this;
	}
	
	public void hide(){
		setPrefWidth(220);
		setRight(null);
		clicked = false;
	}
	public void presentation(){
		setPrefWidth(440);
		setRight(dataholder);
		clicked = true;
	}
	private String LocalDateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}
	private String LocalTimeToString(Date time){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(time);
	}
	public boolean isClicked(){
		return clicked;
	}
	public void setClicked(boolean bool){
		this.clicked = bool;
	}
	public int getPresentationId(){
		return presentationId;
	}
}
