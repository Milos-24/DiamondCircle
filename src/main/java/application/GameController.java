package application;


import Simulation.Simulation;
import figures.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import player.Player;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.MenuController.*;



public class GameController implements Initializable {

    @FXML
    Label timeLabel;
    @FXML
    Label player1Label;
    @FXML
    Label player2Label;
    @FXML
    Label player3Label;
    @FXML
    Label player4Label;
    @FXML
    GridPane gridPane;
    @FXML
    Label gamesPlayedLabel;
    @FXML
    ImageView cardImageView;
    @FXML
    VBox buttonsVBox;
    @FXML
    Button resultsButton;
    @FXML
    Button pauseGameButton;
    @FXML
    public TextArea descriptionTextArea;

    public static Player[] players;
    public final int prefWidth = 700;
    public final int prefHeight = 450;

    private final int numOfFigures = 4;
    public static int sizeOfGame;
    private String[] playerNames;
    Simulation game;

    Pane[][] panes;
    Timeline timeline, description;
    LocalTime time = LocalTime.parse("00:00:00");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    {
        // ime logger-a je naziv klase
        Logger.getLogger(GameController.class.getName()).addHandler(handler);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        buttonsVBox.setAlignment(Pos.CENTER);
        buttonsVBox.setSpacing(10);
        if (numberOfPlayers == 2) {
            playerNames = new String[]{player1Name, player2Name};
            player3Label.setText("");
            player4Label.setText("");
        } else if (numberOfPlayers == 3) {
            playerNames = new String[]{player1Name, player2Name, player3Name};
            player4Label.setText("");
        } else
            playerNames = new String[]{player1Name, player2Name, player3Name, player4Name};


        //zapocni tajmer
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        description = new Timeline(new KeyFrame(Duration.millis(100), ae -> setDescription()));
        description.setCycleCount(Animation.INDEFINITE);
        description.play();
        //nacrtaj grid
        drawGrid();

        try {
            startGame();
        } catch (InterruptedException e) {
            Logger.getLogger(GameController.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }

    public void startGame() throws InterruptedException {

        String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"diamondcircleresults";
        File folder = new File(path);
        gamesPlayedLabel.setText("Games played: " + Objects.requireNonNull(folder.list()).length);

        players = new Player[numberOfPlayers];
        Figure.Colors[] colors = Figure.Colors.values();
        Collections.shuffle(Arrays.asList(colors));

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(playerNames[i]);
            players[i].setFigures(assignFigures(colors[i]));
        }

        player1Label.setText(players[0].getName());
        player1Label.setTextFill(players[0].figures[0].getColor());

        player2Label.setText(players[1].getName());
        player2Label.setTextFill(players[1].figures[0].getColor());

        if (numberOfPlayers == 3 || numberOfPlayers == 4) {
            player3Label.setText(players[2].getName());
            player3Label.setTextFill(players[2].figures[0].getColor());
        }
        if (numberOfPlayers == 4) {
            player4Label.setText(players[3].getName());
            player4Label.setTextFill(players[3].figures[0].getColor());
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numOfFigures; j++) {
                Button button = new Button("Figura " + (j + 1));

                players[i].figures[j].setButton(button);
                buttonsVBox.getChildren().add(button);
            }
        }


        game = new Simulation(cardImageView, gridPane, panes, timeLabel);
        game.start();


    }

    public Figure[] assignFigures(Figure.Colors color) {

        Figure[] figures = new Figure[this.numOfFigures];

        for (int i = 0; i < this.numOfFigures; i++) {
            int tmp = random();
            if (tmp == 1) {
                figures[i] = new FlyingFigure(color);
            } else if (tmp == 2) {
                figures[i] = new RegularFigure(color);
            } else
                figures[i] = new SuperFastFigure(color);
        }

        return figures;
    }

    private void incrementTime() {
        time = time.plusSeconds(1);
        timeLabel.setText("Time: " + time.format(dtf));
    }

    private int random() {
        int max = 3;
        return ThreadLocalRandom.current().nextInt(max) + 1;
    }

    public void drawGrid() {
        if (Objects.equals(gameSize, size1))
            sizeOfGame = 7;
        else if (Objects.equals(gameSize, size2))
            sizeOfGame = 8;
        else if (Objects.equals(gameSize, size3))
            sizeOfGame = 9;
        else
            sizeOfGame = 10;

        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        gridPane.setPrefSize(prefWidth, prefHeight);

        for (int i = 0; i < (sizeOfGame - 3); i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100d / sizeOfGame);
            gridPane.getColumnConstraints().add(column);
        }

        for (int i = 0; i < (sizeOfGame - 3); i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100d / sizeOfGame);
            gridPane.getRowConstraints().add(row);
        }

        panes = new Pane[sizeOfGame][sizeOfGame];

        for (int i = 0; i < sizeOfGame; i++) {
            for (int j = 0; j < sizeOfGame; j++) {
                panes[i][j] = new Pane();
                gridPane.add(panes[i][j], i, j);
            }
        }

    }

    public void pauseGameButtonClick(ActionEvent event) {
        if (Simulation.paused) {
            Ghost.paused = false;
            Simulation.paused = false;

            try {
                synchronized (this) {
                    this.timeline.play();
                }
                synchronized (game) {
                    game.notify();
                    game.resumeFigureTime();
                }
                synchronized (game.getGhost()) {
                    game.getGhost().notify();
                }
            } catch (Exception e) {
                Logger.getLogger(GameController.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }


            pauseGameButton.setText("Pause");
        } else {
            Ghost.paused = true;
            Simulation.paused = true;
            game.pauseFigureTime();
            synchronized (this) {
                this.timeline.stop();
            }
            pauseGameButton.setText("Continue");
        }
    }

    public void setDescription()
    {
        descriptionTextArea.setText(game.description);
    }


    public static void showFigurePathButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(GameController.class.getResource("figurePath.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DiamondCircle");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }



    public void showResultFilesButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(GameController.class.getResource("resultsFile.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DiamondCircle");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

}
