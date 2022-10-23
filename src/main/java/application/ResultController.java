package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.MenuController.handler;


public class ResultController implements Initializable {

    @FXML
    TextArea resultTextArea;
    public static String pressedButton;

    {
        // ime logger-a je naziv klase
        Logger.getLogger(ResultController.class.getName()).addHandler(handler);
    }

    public void returnButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void setString(ActionEvent event)
    {
        Button clickedButton = (Button) event.getTarget();
        pressedButton=clickedButton.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        String fileName = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"diamondcircleresults"+File.separator + pressedButton;
        String st = null;

        try {
            st = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            Logger.getLogger(ResultController.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }

        resultTextArea.setText(st);
        resultTextArea.setEditable(false);
    }
}
