package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setFullScreen(true);
        stage.setResizable(false);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setTitle("DiamondCircle");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}