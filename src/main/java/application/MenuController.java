package application;

import exception.DuplicateException;
import exception.GameSizeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MenuController implements Initializable {

    @FXML
    Button playButton;
    @FXML
    ChoiceBox sizeChoiceBox;
    @FXML
    public ChoiceBox<String> numberChoiceBox;
    @FXML
    TextField player1TextField;
    @FXML
    TextField player2TextField;
    @FXML
    TextField player3TextField;
    @FXML
    TextField player4TextField;


    public static String gameSize;
    public static int numberOfPlayers;
    public static boolean noDuplicates;
    public static boolean usernamesSet;
    public static String player1Name;
    public static String player2Name;
    public static String player3Name;
    public static String player4Name;

    public static final String size1 = "7x7";
    public static final String size2 = "8x8";
    public static final String size3 = "9x9";
    public static final String size4 = "10x10";
    ObservableList<String> sizes = FXCollections.observableArrayList(size1, size2, size3, size4);

    public static final String number1 = "2";
    public static final String number2 = "3";
    public static final String number3 = "4";
    ObservableList<String> numbers = FXCollections.observableArrayList(number1, number2, number3);
    private final String logPath="LogFile.log";
    public static Handler handler;

    {
        try {
            handler = new FileHandler(logPath);
            Logger.getLogger(MenuController.class.getName()).addHandler(handler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        // ime logger-a je naziv klase


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeChoiceBox.setItems(sizes);
        numberChoiceBox.setItems(numbers);
        numberChoiceBox.setOnAction(this::getNumberChoice);
    }

    public void getNumberChoice(ActionEvent event)
    {
        int numberOfPlayers = Integer.parseInt(numberChoiceBox.getValue());

        if(numberOfPlayers == 2)
        {
            player3TextField.setEditable(false);
            player4TextField.setEditable(false);
        }
        else if (numberOfPlayers == 3)
        {
            player3TextField.setEditable(true);
            player4TextField.setEditable(false);
        }
        else {
            player3TextField.setEditable(true);
            player4TextField.setEditable(true);
        }
    }

    public void playButton(ActionEvent event) throws IOException {
        try {
                Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                assignGameStats();
                assignPlayers();
        }
        catch (DuplicateException | GameSizeException e)
        {
            Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());

            MenuExceptionController.error = String.valueOf(e.getMessage());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("exceptionMenu.fxml")));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Error");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch (Exception e)
        {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());

        }

        if(numberOfPlayers != 0 && gameSize != null && noDuplicates && usernamesSet)
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.setTitle("DiamondCircle");
            stage.show();
        }

    }

    public void assignGameStats()throws Exception
    {
        if(numberChoiceBox.getValue() == null)
            throw new GameSizeException();
        else if (sizeChoiceBox.getValue() == null)
            throw new GameSizeException();
        else {
            numberOfPlayers = Integer.parseInt(numberChoiceBox.getValue());
            gameSize = (String) sizeChoiceBox.getValue();
        }
    }

    public void assignPlayers() throws Exception {
        Set<String> players = new HashSet<>();
        player1Name = player1TextField.getText();
        player2Name = player2TextField.getText();
        player3Name = player3TextField.getText();
        player4Name = player4TextField.getText();

        if(numberOfPlayers == 2)
        {
            players.add(player1Name);
            players.add(player2Name);
        }
        else if(numberOfPlayers == 3)
        {
            players.add(player1Name);
            players.add(player2Name);
            players.add(player3Name);
        }
        else
        {
            players.add(player1Name);
            players.add(player2Name);
            players.add(player3Name);
            players.add(player4Name);
        }

        if(players.size()!=numberOfPlayers)
        {
            noDuplicates=false;
            throw new DuplicateException();
        }


        usernamesSet= !players.contains("");
        noDuplicates=true;

    }

}