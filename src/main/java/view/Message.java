package main.java.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Message {

	private Label lbl_message;
	private boolean backToMenu = false;
	private String type;

	public Message(Label obj) {
		this.lbl_message = obj;
	}

	public boolean presentationMsg(String msgCode) {
		backToMenu = false;
		char msgType = msgCode.charAt(0);
		msgCode = msgCode.substring(1);
		switch (msgType) {
		case 's':
			type = "msg_success";
			backToMenu = true;
			break;
		case 'i':
			type = "msg_info";
			break;
		case 'w':
			type = "msg_warning";
			break;
		case 'e':
			type = "msg_error";
			break;
		}

		switch (msgCode) {
		case "0":
			setProperties(type, "Movie successfully deleted!");
			break;
		case "1":
			setProperties(type, "Can't delete movie. Movie is in use!");
			break;
		case "2":
			setProperties(type, "Can't delete moviecover. Cover is in use!");
			break;
		case "3":
			setProperties(type, "An error occurred while deleting the movie!");
			break;
		case "4":
			setProperties(type, "Movie successfully saved");
			break;
		case "5":
			setProperties(type, "Duration is not a number!");
			break;
		case "6":
			setProperties(type, "Could not load image. Invalid path or file!");
			break;
		case "7":
			setProperties(type, "Movie already exists!");
			break;
		case "8":
			setProperties(type, "An error occurred while deleting the moviecover!");
			break;
		case "9":
			setProperties(type, "Please fill everything out!");
			break;
		case "10":
			setProperties(type, "Room successfully created!");
			break;
		case "11":
			setProperties(type, "Room already exists!");
			break;
		case "12":
			setProperties(type, "Room successfully saved!");
			break;
		case "13":
			setProperties(type, "Room successfully deleted!");
			break;
		case "14":
			setProperties(type, "Room successfully created!");
			break;
		case "15":
			setProperties(type, "Can't delete Room. Room is in use!");
			break;
		case "16":
			setProperties(type, "No Rooms are existing! Please create one!");
			break;
		case "17":
			setProperties(type, "No movies are existing! Please create one!");
			break;
		case "18":
			setProperties(type, "Movie successfully created!");
			break;
		case "19":
			setProperties(type, "Date or time invalid!");
			break;
		case "20":
			setProperties(type, "No movie or room selected!");
			break;
		case "21":
			setProperties(type, "Presentation successfully created!");
			break;
		case "22":
			setProperties(type, "This room is already occupied!");
			break;
		case "23":
			setProperties(type, "No Date given. Please choose a start date!");
			break;
		case "24":
			setProperties(type, "An error occurred while deleting the presentation!");
			break;
		case "25":
			setProperties(type, "An error occurred while deleting the reservation!");
			break;
		case "26":
			setProperties(type, "Presentation successfully deleted!");
			break;
		case "27":
			setProperties(type, "No presentations are existing! Please create one!");
			break;
		case "28":
			setProperties(type, "Presentation start date can not be in the past!");
			break;
		case "29":
			setProperties(type, "Presentation start time can not be in the past!");
			break;
		case "30":
			setProperties(type, "Please enter a phone number!");
			break;
		case "31":
			setProperties(type, "Reservation successfully saved!");
			break;
		case "32":
			setProperties(type, "Please select at least one seat!");
			break;
		case "33":
			setProperties(type, "");
			break;
		case "99":
			setProperties(type, "This Feature is not available yet!");
			break;
		}
		removeMsg();
		return backToMenu;
	}

	private void setProperties(String cssClass, String msg) {
		lbl_message.getStyleClass().add(cssClass);
		lbl_message.setText(msg);
		lbl_message.setVisible(true);
		lbl_message.setDisable(false);
	}

	private void deleteStyleMsg(String name) {
		ObservableList<String> list = lbl_message.getStyleClass();
		try {
			for (String cssclass : list) {
				if (cssclass.matches(name)) {
					list.remove(cssclass);
					lbl_message.setText("");
					lbl_message.setVisible(false);
					lbl_message.setDisable(true);
					return;
				}
			}
		} catch (Exception e) {
			return;
		}

	}

	private void removeMsg() {
		// Label & Button nach bestimmter zeit not Visible
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(() -> {
			Platform.runLater(() -> {
				deleteStyleMsg("msg_.*");
			});

		}, 5, TimeUnit.SECONDS);

	}
}
