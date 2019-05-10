package main.java.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.*;
import view.Message;
import view.Seat;
import view.PresentationItem;


public class EventHandlingController extends Controller {
	private static final String PHONE = "[0][0-9]{2} [0-9]{3} [0-9]{2}( [0-9]{2})";
	private static final String TIMEREGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	private static final String NUMBERREGEX = "[0-9]{4}"; // regex für Zahlen
	private static final Pattern TIME24HOURS_PATTERN = Pattern.compile(TIMEREGEX);
	private static final Pattern NUMBERPATTERN = Pattern.compile(NUMBERREGEX);
	private static final Pattern PHONEPATTERN = Pattern.compile(PHONE);
	private static final String PIC_DIR = "../";
	// private final ScheduledExecutorService schedulerLoader =
	// Executors.newScheduledThreadPool(1);

	// private ScheduledFuture<?> presentationLoader;
	private boolean firstrun = true;
	private FileChooser mediaChooser;
	private ExtensionFilter extFilter;
	private Stage stage;
	private Controller controller;
	private Movie movie = null;
	private Room room = null;
	private Presentation presentation = null;
	private String returncode;
	private Message message;

	@FXML
	private MenuItem btn_createmovie, btn_exitprogramm, btn_editmovie, btn_deletemovie;
	@FXML
	private MenuItem btn_createpresentation, btn_editpresentation, btn_deletepresentation;
	@FXML
	private MenuItem btn_createroom, btn_editroom, btn_deleteroom;
	@FXML
	private MenuItem btn_helpme;

	@FXML
	private GridPane pane_movie, pane_main, pane_presentation, pane_seats;
	@FXML
	private ScrollPane sp_presentation;
	@FXML
	private HBox vb_wrapper_presentation;
	@FXML
	private VBox pane_overview;
	@FXML
	private BorderPane pane_seatsarr;
	@FXML
	private TabPane pane_help;

	@FXML
	private Button btn_moviesave, btn_presentationsave, btn_reservationsave;
	@FXML
	private Hyperlink btn_cancel, btn_cancelpresentation, btn_cancelNewRes;
	@FXML
	private DatePicker dp_startdate;

	@FXML
	private TextField tf_movietitle, tf_movieduration, tf_starttime, tf_phonenumber;
	@FXML
	private TextArea ta_moviedesc;

	@FXML
	private Label lbl_message, lbl_movie, lbl_presentation;
	@FXML
	private Label lbl_movietitle, lbl_movieduration;

	@FXML
	private ListView<String> lv_room;
	@FXML
	private ListView<String> lv_movie;
	@FXML
	private ListView<Pane> lv_presentations;
	@FXML
	private ListView<String> lv_reservation;

	private int presentationclicked;
	int oldclick = -1, newclick = -1;

	public EventHandlingController() {
		mediaChooser = new FileChooser();
		extFilter = new ExtensionFilter("ImageFormat", "*.png", "*.jpg", "*.jpeg");
		controller = new Controller();
		// new Thread(new Runnable() {
		// public void run() {
		// while (true) {
		// Platform.runLater(() -> {
		// loadPresentationsToOverview();
		//
		// });
		// }
		// }
		// }).start();
	}

	@FXML
	private void initialize() {

		if (firstrun) {
			message = new Message(lbl_message);
			lv_movie.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			lv_room.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			lv_presentations.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			
			backToMenu(true);
			// createPresentationLoader();
			firstrun = false;

			loadPresentationToOverview();
			loadSeatPane();
//			  Runnable r = new Runnable() {
//			         public void run() {
//			             loadSeatPane();
//			         }
//			     };
//
//			     ExecutorService executor = Executors.newCachedThreadPool();
//			     executor.submit(r);
		}
		btn_helpme.setOnAction((event) -> {
			backToMenu(false);
			pane_help.setVisible(true);
			pane_help.setDisable(false);
		});
		// Movie controlling start-----
		btn_createmovie.setOnAction((event) -> {
			backToMenu(false);
			pane_movie.setVisible(true);
			pane_movie.setDisable(false);
			// Init all inputfields
			tf_movieduration.setText("");
			tf_movietitle.setText("");
			ta_moviedesc.setText("");
			btn_cancel.setUnderline(false);
			lbl_movie.setText("Create new movie");
		});
		btn_exitprogramm.setOnAction((event) -> {
			// presentationLoader.cancel(true);
			System.exit(0);
		});
		btn_cancel.setOnAction((event) -> {
			backToMenu(true);
		});
		btn_cancelNewRes.setOnAction((event) -> {
			backToMenu(true);
		});
		btn_cancelpresentation.setOnAction((event) -> {
			lv_movie.getSelectionModel().clearSelection();
			lv_room.getSelectionModel().clearSelection();
			backToMenu(true);
		});

		btn_moviesave.setOnAction((event) -> {
			// check the label in what function is needed
			if (!tf_movieduration.getText().isEmpty() && !tf_movietitle.getText().isEmpty()
					&& !ta_moviedesc.getText().isEmpty()) {
				if (lbl_movie.getText().equals("Create new movie")) {

					returncode = controller.createNewMovie(tf_movieduration.getText(), tf_movietitle.getText());

				} else if (lbl_movie.getText().equals("Edit movie")) {
					Movie editedMovie = new Movie();
					// Überprüft ob Movielänge eine Zahl ist
					try {
						editedMovie.setId(movie.getId());
						editedMovie.setMinutes(tf_movieduration.getText());
						editedMovie.setName(tf_movietitle.getText());
						returncode = controller.editMovie(editedMovie);

					} catch (Exception e) {
						returncode = "e5";
					}

				}
				if (message.presentationMsg(returncode)) {
					backToMenu(true);
				}
			} else {
				message.presentationMsg("i9");
			}

		});

		btn_editmovie.setOnAction((event) -> {
			if (loadMovieList() != null) {
				movie = choosePopupMovie(loadMovieList(), "Edit");
				if (movie != null) {
					backToMenu(false);
					pane_movie.setVisible(true);
					pane_movie.setDisable(false);
					tf_movieduration.setText(movie.getMinutes());
					tf_movietitle.setText(movie.getName());
					lbl_movie.setText("Edit movie");
				}
			} else {
				message.presentationMsg("i17");
				backToMenu(true);
			}

		});
		btn_deletemovie.setOnAction((event) -> {
			if (loadMovieList() != null) {
				movie = choosePopupMovie(loadMovieList(), "Delete");
				if (movie != null) {
					returncode = controller.deleteMovie(movie);
					message.presentationMsg(returncode);
				}
			} else {
				message.presentationMsg("i17");
				backToMenu(true);
			}

		});

		// Movie controlling end------------

		// Room controlling start--------------------

		btn_createroom.setOnAction((event) -> {
			backToMenu(true);
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Create new Room");
			dialog.setHeaderText("Create new Room");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String name = result.get();
				if (controller.createNewRoom(name)) {
					message.presentationMsg("s10");
					backToMenu(true);
				} else {
					message.presentationMsg("e11");
				}
			}
		});
	/**	btn_editroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				EditRoomDialog dialog = new EditRoomDialog();
				Pair<String, String> eingabe = dialog.presentation(loadRoomList());
				if(eingabe != null){
					if (!eingabe.getValue().isEmpty() && !eingabe.getKey().isEmpty() ) {
						Room editedRoom = new Room(eingabe.getValue().toString());
						controller.editRoom(eingabe.getKey().toString(), editedRoom);
						message.presentationMsg("s12");
					}
				}
			} else {
				message.presentationMsg("i16");
			}
			backToMenu(true);

		}); */
		btn_deleteroom.setOnAction((event) -> {
			if (loadRoomList() != null) {
				Room delroom = choosePopupRoom(loadRoomList());
				if (delroom != null) {
					returncode = controller.deleteRoom(delroom);
					message.presentationMsg(returncode);
				}
			} else {
				message.presentationMsg("i16");
				backToMenu(true);
			}
		});
		// Room controlling end ------
		// Presentation controlling start -------

		btn_createpresentation.setOnAction((event) -> {
			backToMenu(false);
			pane_presentation.setVisible(true);
			pane_presentation.setDisable(false);
			// Init all inputfields
			tf_starttime.setText("");
			dp_startdate.setValue(null);
			lbl_movieduration.setText("");
			lbl_movietitle.setText("");
			// set Cover to null
			// movie list handling
			lv_movie.setItems(loadLVMovie());
			lv_movie.getSelectionModel().clearSelection();
			// room list handling -> default empty and locked
			lv_room.setItems(null);
			lv_room.setDisable(true);
			lv_room.getSelectionModel().clearSelection();

			lbl_presentation.setText("Create new presentation");
			btn_cancelpresentation.setUnderline(false);
		});

		btn_presentationsave.setOnAction((event) -> {
		      String moviename = lv_movie.getSelectionModel().getSelectedItem();
		      String roomname = lv_room.getSelectionModel().getSelectedItem();
		      LocalDate startDate = dp_startdate.getValue();
		      String startTime = tf_starttime.getText();

		      if (moviename != null && roomname != null && startDate != null && !startTime.isEmpty() &&  checkStartTime() && checkStartDate()) {
		        if (lbl_presentation.getText().equals("Create new presentation")) {
		          returncode = controller.createNewPresentation(controller.getRoomByName(roomname),
		              controller.getMovieByName(moviename), startDate, startTime);
		          if (message.presentationMsg(returncode))
		            backToMenu(true);
		          
		        } else if (lbl_presentation.getText().equals("Edit presentation")) {
		          movie = controller.getMovieByName(lv_movie.getSelectionModel().getSelectedItem());
		          Presentation editedPresentation = new Presentation();
		          Date date = null;
		          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		          String startDateTime = dp_startdate.getValue() + " " + tf_starttime.getText();
		          try { date = format.parse(startDateTime); } catch (ParseException e) {}
		          
		          try{
					  editedPresentation.setId(presentation.getId());
					  editedPresentation.setDurationInMinutes(movie.getMinutes());
					  editedPresentation.setMovie(movie);
					  editedPresentation.setRoom(controller.getRoomByName(lv_room.getSelectionModel().getSelectedItem()));
					  editedPresentation.setStartDateTime(date);
					  editedPresentation.setEndDateTime(controller.presentationList.getEndTime(date, movie));
		            returncode = controller.editPresentation(editedPresentation);
		          }catch(Exception e){
		            returncode = "e30";
		          }


		          if (message.presentationMsg(returncode))
		            backToMenu(true);
		        }

		      } else {
		        if (!checkStartDate()) {
		          message.presentationMsg("e28");
		          return;
		        }else if (!checkStartTime()) {
		          message.presentationMsg("e29");
		          return;
		        }
		        message.presentationMsg("i9");
		      }

		    });

		// Eventhandling for editing presentations
	    btn_editpresentation.setOnAction((event) -> {
	      if (loadPresentationList() != null) {
	        presentation = choosePopupPresentation(loadPresentationList(), "Edit");
	        if (presentation != null) {
	          loadEditPresentation(presentation);
	        }
	      } else {
	        message.presentationMsg("i27");
	        backToMenu(true);
	      }
	    });

		btn_deletepresentation.setOnAction((event) -> {
			if (loadPresentationList() != null) {
				Presentation delpresentation = choosePopupPresentation(loadPresentationList(), "Delete");
				if (delpresentation != null) {
					returncode = controller.deletePresentationAndReservations(delpresentation);
					message.presentationMsg(returncode);
					backToMenu(true);
				}
			} else {
				message.presentationMsg("i27");
				backToMenu(true);
			}
		});

		// check if entered value is valid to time 24 hours. otherwise reset
		// field
		tf_starttime.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					checkTimeFormat();
				}
			}
		});

		dp_startdate.setOnAction((event) -> {
			checkStartDate();
		});
		// Presentation controlling end -------------

		// ListView controlling start ----------------
		lv_movie.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					movie = controller.getMovieByName(newValue);
					lbl_movieduration.setText(movie.getMinutes() + " min");
					lbl_movietitle.setText(movie.getName());
				}
			}
		});
		lv_room.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					room = controller.getRoomByName(newValue);
				}
			}
		});

		lv_presentations.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
			@Override
			public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
				PresentationItem item = null;
				if(lv_presentations.getSelectionModel().getSelectedItem() == null){
					backToMenu(true);
				}
				// alte selektion löschen -> verstecken
				if (oldValue != null) {
					item = (PresentationItem) oldValue;
					item.hide();
					item.setClicked(false);
				}
				item = (PresentationItem) newValue;
				if (item != null) {
					item.setClicked(true);
					item.presentation();
					loadReservationToPane(item.getPresentationId());
					//loadSeatPane(item.getPresentationId());
					try {
						loadReservation(item.getPresentationId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		// reservation controlling start -----------------

		tf_phonenumber.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					checkPhoneFormat(tf_phonenumber.getText());
				}
			}
		});

		// reservation controlling end ---------

	}

	@FXML
	private void deselectPresentation() {
		PresentationItem item = (PresentationItem) lv_presentations.getSelectionModel().getSelectedItem();
		if (!item.isClicked()) {
			backToMenu(true);
		}

	}

	private void loadPresentationToOverview() {
		if (loadPresentationList() != null) {
			PresentationList presentationList = loadPresentationList();
			ObservableList<Pane> content = FXCollections.observableArrayList();
			for (Presentation presentation : presentationList) {
				if (!presentation.getStartDateTime().before(new Date())) {
					PresentationItem presentationitem = new PresentationItem();
					Pane pane = presentationitem.createPresentationItem(presentation);
					content.add(pane);
				}
			}
			lv_presentations.setItems(content);
		}
	}

	
	@FXML
	private void refreshPresentations() {
		loadPresentationToOverview();
		backToMenu(true);
	}
	
	private void loadEditPresentation(Presentation editpresentation) {
		backToMenu(false);
		pane_presentation.setVisible(true);
		pane_presentation.setDisable(false);
		// Init all inputfields
		Movie movie = editpresentation.getMovie();

		// date formater
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String starttime = formatter.format(editpresentation.getStartDateTime());

		// convert date to LocalDate for datePicker
		LocalDate startdate = editpresentation.getStartDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		tf_starttime.setText(starttime);
		dp_startdate.setValue(startdate);
		lbl_movieduration.setText(movie.getMinutes());
		lbl_movietitle.setText(movie.getName());

		// movie list handling
		lv_movie.setItems(loadLVMovie());
		lv_movie.getSelectionModel().select(movie.getName());

		// room list handling -> load as default and add presentation room -> sort
		lv_room.setItems(loadLVRoom(startdate, starttime, movie));
		lv_room.getItems().add(editpresentation.getRoom().getName());
		lv_room.getItems().sort(((o1, o2) -> editpresentation.getRoom().getName().compareTo(editpresentation.getRoom().getName())));
		lv_room.getSelectionModel().select(editpresentation.getRoom().getName());
		lv_room.setDisable(false);

		lbl_presentation.setText("Edit presentation");
		btn_cancelpresentation.setUnderline(false);
	}
	
	
	private void loadReservation(int presentationnr) throws Exception {
		ArrayList<Reservation> reservationList= controller.getReservationsByPresentation(controller.getPresentationById(presentationnr));
		ObservableList<String> content = FXCollections.observableArrayList();
		for(Reservation reservation : reservationList){
			content.add(reservation.getSeatNumber() + "\t\t" + reservation.getPhoneNumber());
		}
		lv_reservation.setItems(content);
		
	}
	private void loadSeatPane() {

		Seat seatobj;
		int seatnr = 0;
		for (int row = 1; row < 16; row++) {
			seatnr = 0;
			for (int seat = 0; seat < 31; seat++) {
				if (seat == 0) {
					pane_seats.add(new Label(Integer.toString(row)), seat, row);
					continue;
				}
				seatnr++;
				seatobj = new Seat(row, seatnr);

				// walkway
				if (seat == 5 || seat == 26) {
					seatnr--;
					continue;
				}
				pane_seats.add(seatobj, seat + 1, row);
			}
		}
	}

	private void loadReservationToPane(int presentationnr){
		btn_cancelpresentation.setUnderline(false);
		pane_seatsarr.setVisible(true);
		pane_seatsarr.setDisable(false);
		tf_phonenumber.setEditable(false);
		tf_phonenumber.setDisable(true);
		tf_phonenumber.setVisible(false);
		tf_phonenumber.setText("");
		ObservableList<Node> children = pane_seats.getChildren();
		ArrayList<String> reservedSeats = controller.getReservedSeats(controller.getPresentationById(presentationnr));
		for(int i = 0 ; i < children.size(); i++){
			try {
				Seat seat = (Seat) children.get(i);
				seat.enable();
				seat.setCursor(Cursor.HAND);
				// alle sitze mit einer reservierung ausschalten
				for (String reservation : reservedSeats) {
					String[] part = reservation.split("-");
					if (part[0].equals(Integer.toString(seat.getRow())) && part[1].equals(Integer.toString(seat.getSeat()))) {
						seat.setCursor(Cursor.DEFAULT);
						seat.disable();
						break;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	// laden der Raumliste
	@FXML
	private void checkAndLoadRooms() {
		Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
		if (dp_startdate.getValue() != null && timeMatcher.matches()
				&& lv_movie.getSelectionModel().getSelectedItem() != null) {
			lv_room.setItems(loadLVRoom(dp_startdate.getValue(), tf_starttime.getText(),
					controller.getMovieByName(lv_movie.getSelectionModel().getSelectedItem())));
			if (lv_room.getItems().size() == 0) {
				lv_room.setDisable(true);
				message.presentationMsg("i16");
			} else {
				lv_room.setDisable(false);
			}

		} else {
			lv_room.setItems(null);
			lv_room.setDisable(true);
			lv_room.getSelectionModel().clearSelection();
		}

	}


	// Mehtode to load lv_room with all available rooms.
	private ObservableList<String> loadLVRoom(LocalDate startDate, String startTime, Movie movie) {
		RoomList roomlist = controller.getAllAvailableRooms(startDate, startTime, movie);
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Room room : roomlist) {
			content.add(room.getName());
		}		
		return content;
	}
	private ObservableList<String> loadLVMovie() {
		MovieList movielist = controller.getAllMovies();
		ObservableList<String> content = FXCollections.observableArrayList();
		for (Movie movie : movielist) {
			content.add(movie.getName());
		}

		return content;
	}


	// Listen holen und zurückgeben
	private MovieList loadMovieList() {
		MovieList movielist = new MovieList();
		movielist = controller.getAllMovies();
		if (movielist.size() <= 0) {
			return null;
		}
		return movielist;
	}

	private RoomList loadRoomList() {
		RoomList roomlist = new RoomList();
		roomlist = controller.getAllRooms();
		if (roomlist.size() <= 0) {
			return null;
		}
		return roomlist;
	}

	private PresentationList loadPresentationList() {
		PresentationList presentationList = controller.getAllPresentations();
		if (presentationList.size() <= 0) {
			return null;
		}
		return presentationList;
	}

	// Popoup zur Auswahl von Movie und Raum
	private Movie choosePopupMovie(MovieList movielist, String modus) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Movie current : movielist) {
			choices.add(current.getName());
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle(modus + " an existing movie");
		dialog.setContentText("Choose a movie:");
		dialog.setHeaderText(modus + " an existing movie");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// get movie by name
			for (Movie current : movielist) {
				if (result.get().equals(current.getName())) {
					return current;
				}
			}
		}
		return null;
	}

	private Room choosePopupRoom(RoomList roomlist) {
		ArrayList<String> choices = new ArrayList<String>();
		for (Room current : roomlist) {
			choices.add(current.getName());
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle("Delete an existing room");
		dialog.setContentText("Choose a room:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Room current : roomlist) {
				if (result.get().equals(current.getName())) {
					return current;
				}
			}
		}
		return null;
	}

	private Presentation choosePopupPresentation(PresentationList presentationlist, String modus) {
		ArrayList<String> choices = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		for (Presentation current : presentationlist) {
			String starttime = format.format(current.getStartDateTime());
			choices.add(current.getMovie().getName() + " - " + starttime);
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>("please choose", choices);
		dialog.setTitle(modus + " an existing presentation");
		dialog.setContentText("Choose a presentation:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Presentation current : presentationlist) {
				if (result.get().equals(current.getMovie().getName() + " - " + format.format(current.getStartDateTime()))) {
					return current;
				}
			}
		}
		return null;
	}
// All check methode for validating input
	private boolean checkStartDate() {
		if (dp_startdate.getValue() != null) {
			if (dp_startdate.getValue().isBefore(LocalDate.now())) {
				message.presentationMsg("e28");
				return false;
			}
			checkAndLoadRooms();
			return true;
		}
		return false;
	}

	private boolean checkStartTime() {
	     if (!tf_starttime.getText().isEmpty()) {
	      SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	      Date tmpDate = null;
	      String now = "";
	      if(dp_startdate.getValue() == null){
	        now = LocalDateToString(new Date()) + " " + tf_starttime.getText();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	        LocalDate date = LocalDate.parse(now, formatter);
	        dp_startdate.setValue(date);
	        return true;

	      }else if(dp_startdate.getValue() != null){
	        try {
	          tmpDate = format.parse(dp_startdate.getValue().toString() + " " + tf_starttime.getText());
	        } catch (ParseException e) {
	          return true;
	        }
	      }
	        
	      if (tmpDate.before(new Date())) {
	        message.presentationMsg("e29");
	        return false;
	      }
	      return true;
	    }
	    return false;
	  }
	private void checkPhoneFormat(String number) {
		String formatedphone = "";
		Pattern sevennum = Pattern.compile("[0-9]{10}");
		Matcher numMatcher = sevennum.matcher(number);
		if (numMatcher.matches()) {
			formatedphone = number.substring(0, 3) + " " + number.substring(3, 6) + " " + number.substring(6, 8) + " "
					+ number.substring(8, 10);
		}
		Matcher phonematcher = PHONEPATTERN.matcher(formatedphone);
		if (phonematcher.matches()) {
			tf_phonenumber.setText(formatedphone);
		} else {
			tf_phonenumber.setText("");
		}
	}

	public void checkTimeFormat() {
		String time = "";
		Matcher numMatcher = NUMBERPATTERN.matcher(tf_starttime.getText());
		if (numMatcher.matches()) {
			time = tf_starttime.getText().substring(0, 2) + ":" + tf_starttime.getText().substring(2, 4);
			tf_starttime.setText(time);
		}
		Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
		if (timeMatcher.matches()) {
			if (checkStartTime()) {
				checkAndLoadRooms();
				return;
			}
		} else {
			tf_starttime.setText("");
		}
	}


	// general methods
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void backToMenu(Boolean hide) {

		pane_main.setVisible(true);
		pane_main.setDisable(false);
		pane_movie.setVisible(false);
		pane_movie.setDisable(true);
		pane_presentation.setVisible(false);
		pane_presentation.setDisable(true);
		pane_overview.setVisible(false);
		pane_overview.setDisable(true);
		pane_seatsarr.setVisible(false);
		pane_seatsarr.setDisable(true);
		pane_help.setVisible(false);
		pane_help.setDisable(true);

		if (hide) {
			pane_overview.setVisible(true);
			pane_overview.setDisable(false);
			loadPresentationToOverview();
		}

	}

	private String LocalDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}
	// private void createPresentationLoader() {
	// Runnable run_presentationLoader = new Runnable() {
	// public void run() {
	// loadPresentationsToOverview();
	// // System.out.println("hier");
	// }
	// };
	// presentationLoader = schedulerLoader.scheduleAtFixedRate(run_presentationLoader, 1, 1,
	// TimeUnit.SECONDS);
	// }




	@FXML
	private void saveReservation() throws Exception {
		ObservableList<Node> seatList = pane_seats.getChildren();
		ArrayList<String> reservationList = new ArrayList<String>();
		reservationList.clear();
		PresentationItem item = (PresentationItem) lv_presentations.getSelectionModel().getSelectedItem();
		Seat seat;
		Presentation presentation = controller.getPresentationById(item.getPresentationId());
		for (int i = 0; i < seatList.size(); i++) {
			try {
				seat = (Seat) seatList.get(i);
			} catch (Exception e) {
				continue;
			}
			if (seat.isSelected()) {
				reservationList.add(seat.getRow() + "-" + seat.getSeat());
			}
		}
		if(reservationList.size() == 0){
			message.presentationMsg("i32");
			return;
		}
		if (reservationList.size() != 0 && tf_phonenumber.getText().isEmpty()) {
			message.presentationMsg("i30");
			tf_phonenumber.setEditable(true);
			tf_phonenumber.setDisable(false);
			tf_phonenumber.setVisible(true);
			tf_phonenumber.requestFocus();
			return;
		} else if (reservationList.size() != 0 && !tf_phonenumber.getText().isEmpty()) {
			if (controller.createNewReservations(presentation, reservationList, tf_phonenumber.getText())) {
				message.presentationMsg("s31");
				loadReservation(item.getPresentationId());
				loadReservationToPane(item.getPresentationId());
			}
		}

	}
}
