package Simulation;

import application.CreateFile;
import application.MenuController;
import application.Utilities;
import cards.DeckOfCards;
import cards.SpecialCard;
import fields.Fields;
import figures.Figure;
import figures.Ghost;
import figures.RegularFigure;
import figures.SuperFastFigure;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.Duration;
import player.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.GameController.players;
import static application.GameController.sizeOfGame;
import static application.MenuController.handler;

public class Simulation extends Thread {

    public static boolean paused = false;
    public int n, numOfSteps, currentPlayer;
    public int numberOfPlayers = application.MenuController.numberOfPlayers;
    private int[] sequence;
    private  int numOfAdditionalIncrements=0;
    private int startingPositionX, startingPositionY;
    private int currentCardValue;
    public Fields[][] fields = new Fields[sizeOfGame][sizeOfGame];
    Set<Integer> holes;
    boolean firstGame = true;
    private Label timeLabel;
    public ImageView cardImageView;
    public GridPane gridPane;
    public Pane[][] panes;
    int figureIndex;
    Ghost ghost;
    private boolean diamondPicked, fieldTaken;
    public String description;
    Timeline descriptionBox = new Timeline(new KeyFrame(Duration.millis(10), ae -> {
        if(this.getFigureIndex()!=-1)
            this.description(players[currentPlayer].figures[figureIndex]);

    }));

    {
        // ime logger-a je naziv klase

        Logger.getLogger(Simulation.class.getName()).addHandler(handler);
    }


    public Simulation(ImageView cardImageView, GridPane gridPane, Pane[][] panes, Label timeLabel) {
        this.cardImageView = cardImageView;
        this.gridPane = gridPane;
        this.panes = panes;
        setSequence();
        Collections.shuffle(Collections.singletonList(sequence));
        initializeFields();
        this.setStartingPosition();
        this.timeLabel = timeLabel;
    }

    public Ghost getGhost() {
        return this.ghost;
    }

    public void run() {
        try {
            Logger.getLogger(MenuController.class.getName()).addHandler(handler);
            Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
            this.startGame();

        } catch (InterruptedException e) {
            Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }

    public void startGame() throws InterruptedException {
        while (numberOfPlayers != 0) {
            while (Simulation.paused) {
                synchronized (this) {
                    try {
                        Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                        Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);

                        this.wait();
                    } catch (Exception e) {
                        Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                    }
                }
            }


            Image image = Player.deck.drawACard();

            this.cardImageView.setImage(image);


            if (DeckOfCards.currentCard instanceof SpecialCard) {
                this.currentCardValue=DeckOfCards.currentCard.getCardValue();
                holes = new HashSet<>();
                this.description="Izvučen je JOKER!";

                try {
                    Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                    Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }

                this.n = ThreadLocalRandom.current().nextInt(sizeOfGame) + 1;



                for (int i = 0; i < n; i++) {
                    if(!holes.add(ThreadLocalRandom.current().nextInt(numOfSteps) + 1))
                        i--;
                }


                Set<Integer> holesWithoutFlying = new HashSet<>();
                for (int hole : holes
                ) {
                    Utilities.x = this.startingPositionX;
                    Utilities.y = this.startingPositionY;
                    for (int i = 0; i < hole; i++) {
                        Utilities.getToHolePosition(gridPane);
                    }
//
                    if (!Utilities.setHoleColor(gridPane, fields, players))
                        holesWithoutFlying.add(hole);

                }

                try {
                    Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                    Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }

                while (Simulation.paused) {
                    synchronized (this) {
                        try {
                            Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                            Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);

                            this.wait();
                        } catch (Exception e) {
                            Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                        }
                    }
                }

                for (int hole : holesWithoutFlying
                ) {
                    Utilities.x = this.startingPositionX;
                    Utilities.y = this.startingPositionY;
                    for (int i = 0; i < hole; i++) {
                        Utilities.getToHolePosition(gridPane);
                    }

                    Utilities.deleteHole(gridPane, fields);
                }


            }

            this.currentCardValue = DeckOfCards.currentCard.getCardValue();

            this.numOfAdditionalIncrements=0;
            this.diamondPicked=false;
            this.fieldTaken=false;
            currentPlayer = getSequence();
            descriptionBox.setCycleCount(Animation.INDEFINITE);
            descriptionBox.play();

            if (this.currentCardValue != 0) {
                if ((this.figureIndex = activeFigures(players[currentPlayer])) == -1)    //active figures vraca redni broj PLAYING figure ili -1 ako nema nijedne
                {
                    if ((this.figureIndex = waitingFigures(players[currentPlayer])) == -1)   //waiting figures vraca redni broj WAITING figure ili -1 ako nema nijedne
                    {
                        //ovdje ulazi ako su sve DEAD
                        if (!players[currentPlayer].dead) {
                            players[currentPlayer].dead = true;
                            numberOfPlayers--;
                        }
                    } else {
                        //aktiviraj waiting figuru i pokreni je
                        //posto tek aktiviram datu figuru dodjeljujem joj pocetne x i y i pocetno vrijeme

                        Thread.sleep(1000);


                        if (firstGame) {
                            ghost = new Ghost(gridPane, fields);
                            ghost.start();
                            firstGame = false;
                        }

                        players[currentPlayer].figures[figureIndex].setFigureStatus(Figure.FigureStatus.PLAYING);
                        players[currentPlayer].figures[figureIndex].setX(this.startingPositionX);
                        players[currentPlayer].figures[figureIndex].setY(this.startingPositionY);


                        fields[this.startingPositionX][this.startingPositionY].elements.add(players[currentPlayer].figures[figureIndex]);       //dodaj u fields

                        removeNodeByRowColumnIndex(this.startingPositionX, this.startingPositionY, gridPane);           //ukloni trenutni pane sa grida

                        addNodeByRowColumnIndex(players[currentPlayer].figures[figureIndex].getX(), players[currentPlayer].figures[figureIndex].getY(), gridPane, players[currentPlayer].figures[figureIndex]);


                        players[currentPlayer].figures[figureIndex].beginTime();

                        if (currentCardValue != 0) {
                            try {
                                Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                                Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                                moveFigure(players[currentPlayer].figures[figureIndex], this.currentCardValue, gridPane);
                            } catch (InterruptedException e) {
                                Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                            }
                        }

                    }
                } else {
                    //zapocni put aktivne figure
                    try {
                        Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                        Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                        moveFigure(players[currentPlayer].figures[figureIndex], this.currentCardValue, gridPane);
                    } catch (InterruptedException e) {
                        Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                    }
                }
            }

        }

        CreateFile crt = new CreateFile(players, timeLabel.getText());

    }

    public void moveFigure(Figure figure, int currentCardValue, GridPane gridPane) throws InterruptedException {

        numOfAdditionalIncrements = 0;

        if (figure instanceof SuperFastFigure)
            currentCardValue *= 2;



        for (int i = 0; i < currentCardValue; i++) {
            if (figure.getFigureStatus().equals(Figure.FigureStatus.FINISHED))
                break;


            Thread.sleep(1000);
            incrementPosition(figure, gridPane);
            if (fields[figure.getX()][figure.getY()].isDiamond())   //AKO NADJE DIAMANT
            {
                    this.numOfAdditionalIncrements++;
                this.diamondPicked=true;
                i--;
                fields[figure.getX()][figure.getY()].removeDiamond();
            }

        }

        while (fields[figure.getX()][figure.getY()].isFigure() || fields[figure.getX()][figure.getY()].isDiamond())      //AKO NADJE FIGURU
        {
            if (figure.getFigureStatus().equals(Figure.FigureStatus.FINISHED))
                break;

           if(fields[figure.getX()][figure.getY()].isDiamond()){
                        this.numOfAdditionalIncrements++;
                        diamondPicked=true;
                        fields[figure.getX()][figure.getY()].removeDiamond();
            }else if(fields[figure.getX()][figure.getY()].isFigure())
           {
               this.numOfAdditionalIncrements++;
               diamondPicked=false;
               fieldTaken=true;
           }



            Thread.sleep(1000);
            incrementPosition(figure, gridPane);
        }
    }

    public void incrementPosition(Figure figure, GridPane gridPane) {

        while (Simulation.paused) {
            synchronized (this) {
                try {
                    Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                    Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                    this.wait();
                } catch (Exception e) {
                    Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }
            }
        }


        if (figure.getY() != (sizeOfGame - 1) / 2 || figure.getX() != (sizeOfGame / 2)) {
            if (figure.getY() >= sizeOfGame / 2 && figure.getX() < (sizeOfGame - 1) / 2) {
                downRight(figure, gridPane);
            } else if (figure.getY() > (sizeOfGame - 1) / 2 && figure.getX() >= (sizeOfGame - 1) / 2) {
                downLeft(figure, gridPane);
            } else if (figure.getY() <= (sizeOfGame - 1) / 2 && figure.getX() > sizeOfGame / 2) {
                upLeft(figure, gridPane);
            } else if (figure.getY() < (sizeOfGame - 2) / 2 && figure.getX() <= sizeOfGame / 2) {
                upRight(figure, gridPane);
            } else if (figure.getY() == (sizeOfGame - 2) / 2 && figure.getX() <= sizeOfGame / 2) {
                right(figure, gridPane);
            }
        } else {
            figure.setFigureStatus(Figure.FigureStatus.FINISHED);
            figure.elapsedTime();
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
            fields[figure.getX()][figure.getY()].elements.remove(figure);
        }
    }

    public void downRight(Figure figure, GridPane gridPane) {

        if (!fields[figure.getX()][figure.getY()].isFigure())                    //ako nemaju 2 fiugre
        {
            fields[figure.getX()][figure.getY()].elements.remove(figure);
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
        } else                                                                    //ako imaju 2
            fields[figure.getX()][figure.getY()].elements.remove(figure);

        figure.setX(figure.getX() + 1);           //stavi nove pozicije
        figure.setY(figure.getY() + 1);

        fields[figure.getX()][figure.getY()].elements.add(figure);              //stavi novo stanje

        if (!fields[figure.getX()][figure.getY()].isFigure())
            addNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane, figure);

    }

    public void downLeft(Figure figure, GridPane gridPane) {

        if (!fields[figure.getX()][figure.getY()].isFigure())                    //ako nemaju 2 fiugre
        {
            fields[figure.getX()][figure.getY()].elements.remove(figure);
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
        } else                                                                    //ako imaju 2
            fields[figure.getX()][figure.getY()].elements.remove(figure);

        figure.setX(figure.getX() + 1);           //stavi nove pozicije
        figure.setY(figure.getY() - 1);

        fields[figure.getX()][figure.getY()].elements.add(figure);              //stavi novo stanje

        if (!fields[figure.getX()][figure.getY()].isFigure())
            addNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane, figure);
    }

    public void upLeft(Figure figure, GridPane gridPane) {

        if (!fields[figure.getX()][figure.getY()].isFigure())                    //ako nemaju 2 fiugre
        {
            fields[figure.getX()][figure.getY()].elements.remove(figure);
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
        } else                                                                    //ako imaju 2
            fields[figure.getX()][figure.getY()].elements.remove(figure);


        figure.setX(figure.getX() - 1);           //stavi nove pozicije
        figure.setY(figure.getY() - 1);

        fields[figure.getX()][figure.getY()].elements.add(figure);              //stavi novo stanje.

        if (!fields[figure.getX()][figure.getY()].isFigure())
            addNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane, figure);

    }

    public void upRight(Figure figure, GridPane gridPane) {

        if (!fields[figure.getX()][figure.getY()].isFigure())                    //ako nemaju 2 fiugre
        {
            fields[figure.getX()][figure.getY()].elements.remove(figure);
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
        } else                                                                    //ako imaju 2
            fields[figure.getX()][figure.getY()].elements.remove(figure);

        figure.setX(figure.getX() - 1);           //stavi nove pozicije
        figure.setY(figure.getY() + 1);

        fields[figure.getX()][figure.getY()].elements.add(figure);              //stavi novo stanje

        if (!fields[figure.getX()][figure.getY()].isFigure())
            addNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane, figure);

    }

    public void right(Figure figure, GridPane gridPane) {

        if (!fields[figure.getX()][figure.getY()].isFigure())                    //ako nemaju 2 fiugre
        {
            fields[figure.getX()][figure.getY()].elements.remove(figure);
            removeNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane);
        } else                                                                    //ako imaju 2
            fields[figure.getX()][figure.getY()].elements.remove(figure);


        figure.setX(figure.getX());           //stavi nove pozicije
        figure.setY(figure.getY() + 1);

        fields[figure.getX()][figure.getY()].elements.add(figure);              //stavi novo stanje

        if (!fields[figure.getX()][figure.getY()].isFigure() )
            addNodeByRowColumnIndex(figure.getX(), figure.getY(), gridPane, figure);

    }

    synchronized public void removeNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {

        ObservableList<Node> children = gridPane.getChildren();


        for (Node node : children) {
            if (node instanceof Pane && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                Platform.runLater(() -> {
                    Pane currentPane = (Pane) node;
                    currentPane.setBackground(new Background(
                            new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));
                });
                break;
            }
        }
    }

    synchronized public void addNodeByRowColumnIndex(final int row, final int column, GridPane gridPane, Figure figure) {

        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (node instanceof Pane && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                Platform.runLater(() -> {
                    Pane currentPane = (Pane) node;
                    Image image1;

                    if (figure instanceof SuperFastFigure) {
                        image1 = reColor(figure.getImage(), Color.BLACK, figure.getColor(), figure);
                    } else
                        image1 = reColor(figure.getImage(), Color.WHITE, figure.getColor(), figure);

                    currentPane.setBackground(
                            new Background(
                                    new BackgroundImage(
                                            image1,
                                            BackgroundRepeat.NO_REPEAT,
                                            BackgroundRepeat.NO_REPEAT,
                                            BackgroundPosition.DEFAULT,
                                            new BackgroundSize(1.0, 1.0, true, true, false, false)
                                    )));

                });
                break;
            }
        }
    }

    public static Image reColor(Image inputImage, Color oldColor, Color newColor, Figure figure) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        int ob = (int) oldColor.getBlue() * 255;
        int or = (int) oldColor.getRed() * 255;
        int og = (int) oldColor.getGreen() * 255;

        int nb = (int) newColor.getBlue() * 255;
        int nr = (int) newColor.getRed() * 255;
        int ng = (int) newColor.getGreen() * 255;

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                if (g == og && r == or && b == ob) {
                    r = nr;
                    g = ng;
                    b = nb;
                }

                if(Color.GREEN.equals(figure.getColor()))
                    argb = (a << 24) | (0) | (g << 8);
                else
                    argb = (a << 24) | (r << 16) | (g << 8) | b;


                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    public int getSequence() {
        int first = sequence[0];

        for (int i = 0; i < sequence.length - 1; i++)
            sequence[i] = sequence[i + 1];

        sequence[sequence.length - 1] = first;

        return first;
    }

    private int activeFigures(Player player) {
        int numberOfFigures = 4;
        for (int i = 0; i < numberOfFigures; i++) {
            if (player.figures[i].getFigureStatus() == Figure.FigureStatus.PLAYING)
                return i;
        }

        return -1;
    }

    private int waitingFigures(Player player) {
        int numberOfFigures = 4;
        for (int i = 0; i < numberOfFigures; i++) {
            if (player.figures[i].getFigureStatus() == Figure.FigureStatus.WAITING)
                return i;

        }

        return -1;
    }

    private void setStartingPosition() {

        numOfSteps = (sizeOfGame * sizeOfGame + 1) / 2;

        if (sizeOfGame == 7) {
            startingPositionX = 0;
            startingPositionY = 3;
        } else if (sizeOfGame == 8) {
            startingPositionX = 0;
            startingPositionY = 4;
        } else if (sizeOfGame == 9) {
            startingPositionX = 0;
            startingPositionY = 4;
        } else {
            startingPositionX = 0;
            startingPositionY = 5;
        }
    }

    private void setSequence() {
        if (this.numberOfPlayers == 2) {
            sequence = new int[]{0, 1};
        } else if (this.numberOfPlayers == 3) {
            sequence = new int[]{0, 1, 2};
        } else {
            sequence = new int[]{0, 1, 2, 3};
        }
    }

    private void initializeFields() {

        for (int i = 0; i < this.fields.length; i++) {
            for (int j = 0; j < this.fields[i].length; j++) {
                this.fields[i][j] = new Fields();
            }
        }
    }

    public void description(Figure figure) {
        String str;
        int currentCardValue=this.currentCardValue;

        if (currentCardValue != 0) {
            String figureType;
            if (figure instanceof SuperFastFigure)
            {
                figureType = "SuperFastFigure";
                currentCardValue*=2;
            }
            else if (figure instanceof RegularFigure)
                figureType = "RegularFigure";
            else
                figureType = "FlyingFigure";

            if (!diamondPicked) {
                if(fieldTaken) {

                    str = "Na potezu je: " + players[currentPlayer].getName() + "\n \t" +
                            "Figura " + (figureIndex + 1) + " tipa: " + figureType + " prelazi " + (currentCardValue+this.numOfAdditionalIncrements) + " polja jer je naisla na igraca.";
                }
                else {
                    str = "Na potezu je igrač: " + players[currentPlayer].getName() + "\n \t" +
                            "Figura " + (figureIndex + 1) + " tipa: " + figureType + " prelazi " + currentCardValue + " polja.";
                }
             }
            else {

                str = "Na potezu je: " + players[currentPlayer].getName() + "\n \t" +
                        "Figura "+ (figureIndex+1)+ " tipa: " +figureType + " prelazi " + (currentCardValue+this.numOfAdditionalIncrements) + " polja jer je pokupila dijamant.";
            }
        } else {
            str = "Izvucena je JOKER karta";
        }

        this.description = str;
    }

    public int getFigureIndex() {
        return figureIndex;
    }

    public void pauseFigureTime()
    {
        for(Player p: players)
            for(Figure f: p.figures) {
                if(Figure.FigureStatus.PLAYING==f.getFigureStatus())
                {
                    f.pauseTime();
                }


            }
    }

    public void resumeFigureTime()
    {
        for(Player p: players)
            for(Figure f: p.figures) {
                if(Figure.FigureStatus.PLAYING==f.getFigureStatus())
                {
                    f.resumeTime();
                }
            }
    }
}
