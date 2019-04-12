package application.controller;

import application.model.Movie;
import application.model.Presentation;
import application.model.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandlingController {

    private static final String PHONE = "[0][0-9]{2} [0-9]{3} [0-9]{2}( [0-9]{2})";
    private static final String TIMEREGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private static final String NUMBERREGEX = "[0-9]{4}"; // regex für Zahlen
    private static final Pattern TIME24HOURS_PATTERN = Pattern.compile(TIMEREGEX);
    private static final Pattern NUMBERPATTERN = Pattern.compile(NUMBERREGEX);
    private static final Pattern PHONEPATTERN = Pattern.compile(PHONE);
    private static final String PIC_DIR = "../";
    // private final ScheduledExecutorService schedulerLoader =
    // Executors.newScheduledThreadPool(1);

    // private ScheduledFuture<?> showLoader;
    private boolean firstrun = true;
    private FileChooser mediaChooser;
    private FileChooser.ExtensionFilter extFilter;
    private Stage stage;
    private File cover;
    private String defaultpath = "File:images/standard-cover.png";
    private Controller controller;
    private Movie movie = null;
    private Room room = null;
    private Presentation presentation = null;
    private String returncode;

    @FXML
    private MenuItem btn_createfilm, btn_exitprogramm, btn_editfilm, btn_deletemovie;
    @FXML
    private MenuItem btn_createshow, btn_editshow, btn_deleteshow;
    @FXML
    private MenuItem btn_createroom, btn_editroom, btn_deleteroom;
    @FXML
    private MenuItem btn_helpme;

    @FXML
    private GridPane pane_film, pane_main, pane_show, pane_seats;
    @FXML
    private ScrollPane sp_show;
    @FXML
    private HBox vb_wrapper_show;
    @FXML
    private VBox pane_overview;
    @FXML
    private BorderPane pane_seatsarr;
    @FXML
    private TabPane pane_help;

    @FXML
    private Button btn_filmsave, btn_showsave, btn_reservationsave;
    @FXML
    private Hyperlink btn_cancel, btn_cancelshow, btn_cancelNewRes;
    @FXML
    private DatePicker dp_startdate;

    @FXML
    private TextField tf_filmtitle, tf_filmduration, tf_starttime, tf_phonenumber;
    @FXML
    private TextArea ta_filmdesc;

    @FXML
    private Label lbl_message, lbl_film, lbl_show;
    @FXML
    private Label lbl_filmtitle, lbl_filmduration;

    @FXML
    private ImageView iv_filmcover, iv_filmcovershow;
    @FXML
    private ListView<String> lv_room;
    @FXML
    private ListView<String> lv_movie;
    @FXML
    private ListView<Pane> lv_shows;
    @FXML
    private ListView<String> lv_reservation;

    private int showclicked;
    int oldclick = -1, newclick = -1;

    public EventHandlingController() {
        mediaChooser = new FileChooser();
        extFilter = new FileChooser.ExtensionFilter("ImageFormat", "*.png", "*.jpg", "*.jpeg");
        controller = new Controller();
        // new Thread(new Runnable() {
        // public void run() {
        // while (true) {
        // Platform.runLater(() -> {
        // loadShowsToOverview();
        //
        // });
        // }
        // }
        // }).start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {

        if (firstrun) {
            lv_movie.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            lv_room.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            lv_shows.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


            backToMenu(true);
            // createShowLoader();
            firstrun = false;

        }
        btn_helpme.setOnAction((event) -> {
            backToMenu(false);
            pane_help.setVisible(true);
            pane_help.setDisable(false);
        });
        // Film controlling start-----
        btn_createfilm.setOnAction((event) -> {
            backToMenu(false);
            pane_film.setVisible(true);
            pane_film.setDisable(false);
            // Init all inputfields
            iv_filmcover.setImage(new Image(defaultpath));
            tf_filmduration.setText("");
            tf_filmtitle.setText("");
            ta_filmdesc.setText("");
            btn_cancel.setUnderline(false);
            lbl_film.setText("Create new film");
        });
        btn_exitprogramm.setOnAction((event) -> {
            // showLoader.cancel(true);
            System.exit(0);
        });
        btn_cancel.setOnAction((event) -> {
            backToMenu(true);
        });



        btn_filmsave.setOnAction((event) -> {
                    Movie movie = new Movie();
                    movie.setName(tf_filmtitle.getText());
                    movie.setMinutes(Integer.parseInt(tf_filmduration.getText()));
                    controller.createNewMovie(movie);


        });

        btn_createroom.setOnAction((event) -> {
            backToMenu(true);
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create new Room");
            dialog.setHeaderText("Create new Room");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String name = result.get();
                Room room = new Room();
                room.setId(5);
                controller.createNewRoom(room);
                    backToMenu(true);
            }
        });
        btn_editroom.setOnAction((event) -> {
            //TODO

        });
        btn_deleteroom.setOnAction((event) -> {
            //TODO
        });


        lv_movie.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    movie = controller.getFilmByName(newValue);
                    lbl_filmduration.setText(Integer.toString(movie.getMinutes()) + " min");
                    lbl_filmtitle.setText(movie.getName());
                }
            }
        });

    }




    private void loadEditShow(Presentation editPresentation) {
        backToMenu(false);
        pane_show.setVisible(true);
        pane_show.setDisable(false);
        // Init all inputfields
        Movie movie = editPresentation.getMovie();

        // date formater
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String starttime = formatter.format(editPresentation.getStartDateTime());

        // convert date to LocalDate for datePicker
        LocalDate startdate = editPresentation.getStartDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        tf_starttime.setText(starttime);
        dp_startdate.setValue(startdate);
        lbl_filmduration.setText(Integer.toString(movie.getMinutes()));
        lbl_filmtitle.setText(movie.getName());


        // film list handling
        lv_movie.setItems(loadLVFilm());
        lv_movie.getSelectionModel().select(movie.getName());

        // room list handling -> load as default and add presentation room -> sort
        lv_room.setItems(loadLVRoom(startdate, starttime, movie));
        lv_room.getItems().add(editPresentation.getRoom().getId().toString());
        lv_room.getItems().sort(((o1, o2) -> editPresentation.getRoom().getId().toString().compareTo(editPresentation.getRoom().getId().toString())));
        lv_room.getSelectionModel().select(editPresentation.getRoom().getId().toString());
        lv_room.setDisable(false);

        lbl_show.setText("Edit presentation");
        btn_cancelshow.setUnderline(false);
    }

    // laden der Raumliste
    @FXML
    private void checkAndLoadRooms() {
        Matcher timeMatcher = TIME24HOURS_PATTERN.matcher(tf_starttime.getText());
        if (dp_startdate.getValue() != null && timeMatcher.matches() && lv_movie.getSelectionModel().getSelectedItem() != null) {
            lv_room.setItems(loadLVRoom(dp_startdate.getValue(), tf_starttime.getText(),
                    controller.getFilmByName(lv_movie.getSelectionModel().getSelectedItem())));
            if (lv_room.getItems().size() == 0) {
                lv_room.setDisable(true);
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
    private ObservableList<String> loadLVRoom(LocalDate value, String text, Movie filmByName) {
        List<Room> roomlist = controller.getAllRooms();
        ObservableList<String> content = FXCollections.observableArrayList();
        for (Room room : roomlist) {
            content.add(room.getId().toString());
        }
        return content;
    }
    private ObservableList<String> loadLVFilm() {
        List<Movie> filmlist = controller.getAllMovies();
        ObservableList<String> content = FXCollections.observableArrayList();
        for (Movie movie : filmlist) {
            content.add(movie.getName());
        }

        return content;
    }


    private List<Movie> loadMovieList() {
        List<Movie> movieList = controller.getAllMovies();
        if (movieList.size() <= 0) {
            return null;
        }
        return movieList;
    }

    private List<Room> loadRoomList() {
        List<Room> roomList = controller.getAllRooms();
        if (roomList.size() <= 0) {
            return null;
        }
        return roomList;
    }




    // All check methode for validating input

    private void backToMenu(Boolean hide) {

        pane_main.setVisible(true);
        pane_main.setDisable(false);
        pane_film.setVisible(false);
        pane_film.setDisable(true);
        pane_show.setVisible(false);
        pane_show.setDisable(true);
        pane_overview.setVisible(false);
        pane_overview.setDisable(true);
        pane_seatsarr.setVisible(false);
        pane_seatsarr.setDisable(true);
        pane_help.setVisible(false);
        pane_help.setDisable(true);

        if (hide) {
            pane_overview.setVisible(true);
            pane_overview.setDisable(false);
        }

    }


}
