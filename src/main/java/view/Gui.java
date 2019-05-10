package main.java.view;

import controller.EventHandlingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Gui extends Application {

	@Override
	public void init() {
	}

	@Override
	public void start(Stage stage) {
		// Init stage position and size
		final double ypos = Screen.getPrimary().getVisualBounds().getMinY();
		final double xpos = Screen.getPrimary().getVisualBounds().getMinX();
		final double width = Screen.getPrimary().getVisualBounds().getWidth();
		final double height = Screen.getPrimary().getVisualBounds().getHeight();

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/viewRes/Gui.fxml"));
			Parent root = loader.load();

			EventHandlingController eventHandlingController = (EventHandlingController) loader.getController();
			eventHandlingController.setStage(stage);

			Scene scene = new Scene(root);
			String css = getClass().getResource("/viewRes/application.css").toExternalForm();
			scene.getStylesheets().add(css);

			stage.setTitle("Cinema Booking System");
			stage.setScene(scene);
			stage.getIcons().add(new Image("File:" + "images/tab-icon.png"));
			// Set position and size
			stage.setX(xpos);
			stage.setY(ypos);
			stage.setWidth(width);
			stage.setHeight(height);
			stage.setMaximized(true);
			stage.setResizable(true);

			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("Ende");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
