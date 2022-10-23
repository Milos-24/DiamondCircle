package application;

import Simulation.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.MenuController.handler;


public class ResultsFileController implements Initializable {

    @FXML
    VBox vBoxResultFiles;

    {
        // ime logger-a je naziv klase

        Logger.getLogger(ResultsFileController.class.getName()).addHandler(handler);
    }

    public void returnButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String path="src"+File.separator+"main"+File.separator+"resources"+File.separator+"diamondcircleresults";
        File folder = new File(path);
        int numberOfFiles = Objects.requireNonNull(folder.list()).length;

        File[] listOfFiles = folder.listFiles();

        vBoxResultFiles.setAlignment(Pos.CENTER);
        vBoxResultFiles.setSpacing(10);


        for (int j = 0; j < numberOfFiles; j++) {
            Button button = new Button(listOfFiles[j].getName());
            button.setMaxWidth(200);
            button.setMaxHeight(20);
            button.setStyle("-fx-border-width: 2; -fx-border-color: BLACK");
            button.setStyle("-fx-font-style: bold");
            button.setStyle("-fx-background-color:  #008CBA");

            button.setOnAction((event) -> {
                ResultController.setString(event);
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(GameController.class.getResource("result.fxml")));
                } catch (IOException e) {
                    Logger.getLogger(ResultsFileController.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("DiamondCircle");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();
            });
            vBoxResultFiles.getChildren().add(button);
        }


    }
}

