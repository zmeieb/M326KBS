package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class KinoSystem extends Application {


    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(KinoSystem.class.getResource("viewRes/guiView.fxml"));
        Parent rootLayout = loader.load();

        // Presentation the scene containing the root layout.
        primaryStage.setTitle("Huso");
        primaryStage.setScene(rootLayout.getScene());
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Ende");
    }

    public static void main(String[] args) {
        launch(args);
    }

}