package figures;

import Simulation.Simulation;
import application.GameController;
import application.MenuController;
import element.Diamond;
import fields.Fields;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.GameController.sizeOfGame;
import static application.MenuController.handler;

public class Ghost extends Thread {


    public static boolean paused=false;
    public static String path="src"+File.separator+"main"+File.separator+"resources"+File.separator+"application"+File.separator+"Diamond.png";
    public static final File file = new File(path);
    public final Image image = new Image(file.toURI().toString());
    private static int x, y;
    public int startingPositionX, startingPositionY;
    public int numOfSteps, numOfDiamonds;
    GridPane gridPane;
    Set<Integer> diamonds;
    Fields[][] fields;

    {
        // ime logger-a je naziv klase

        Logger.getLogger(Ghost.class.getName()).addHandler(handler);
    }

    public Ghost(GridPane gridPane, Fields[][] fields)
    {
        this.gridPane=gridPane;
        this.fields=fields;
        setStartingPosition();
    }


    public void run()
    {
        setDiamond();
    }

    private void setDiamond() {

        while(true)
        {



            this.numOfDiamonds = ThreadLocalRandom.current().nextInt(sizeOfGame-1)+2;

            diamonds=new HashSet<>();


            while(diamonds.size() != this.numOfDiamonds) {

                int n = ThreadLocalRandom.current().nextInt(numOfSteps) + 1;

                x = this.startingPositionX;
                y = this.startingPositionY;

                for (int i = 0; i < n; i++) {
                    getToDiamondPosition(gridPane);
                }

                if (!(fields[x][y].isSingleFigure() || fields[x][y].isHole()))
                    diamonds.add(n);

                if(diamonds.size()>numOfDiamonds) {
                    throw new IndexOutOfBoundsException();
                }
            }


            for (int diamondPosition: diamonds
                 ) {
                x= this.startingPositionX;
                y= this.startingPositionY;

                for(int i = 0 ; i<diamondPosition ; i++)
                    getToDiamondPosition(gridPane);

                setDiamondPicture(gridPane, fields);
            }


            try {
                Thread.sleep(5000);
                Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
            } catch (Exception e) {
                Logger.getLogger(Ghost.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }


            while(Simulation.paused)
            {
                synchronized (this)
                {
                    try {
                        this.wait();
                        Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                        Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                    }
                    catch (Exception e)
                    {
                        Logger.getLogger(Simulation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                    }
                }
            }


            for (int diamondPosition: diamonds
                 ) {
                x = this.startingPositionX;
                y = this.startingPositionY;

                for(int i = 0 ; i < diamondPosition ; i++)
                {
                    getToDiamondPosition(gridPane);
                }

                deleteDiamond(gridPane, fields);
            }


        }

    }



    private void setStartingPosition()
    {

        numOfSteps = (sizeOfGame*sizeOfGame+1)/2;

        if(sizeOfGame == 7)
        {
            this.startingPositionX=0;
            this.startingPositionY=3;
        }
        else if(sizeOfGame == 8)
        {
            this.startingPositionX=0;
            this.startingPositionY=4;
        }
        else if(sizeOfGame == 9)
        {
            this.startingPositionX=0;
            this.startingPositionY=4;
        }
        else
        {
            this.startingPositionX=0;
            this.startingPositionY=5;
        }
    }


    public static void getToDiamondPosition(GridPane gridPane) {

        if (y != (sizeOfGame - 1) / 2 || x != (sizeOfGame / 2)) {
            if (y >= sizeOfGame / 2 && x < (sizeOfGame - 1) / 2) {
                downRight();
            } else if (y > (sizeOfGame - 1) / 2 && x >= (sizeOfGame - 1) / 2) {
                downLeft();
            } else if (y <= (sizeOfGame - 1) / 2 && x > sizeOfGame / 2) {
                upLeft();
            } else if (y < (sizeOfGame - 2) / 2 && x <= sizeOfGame / 2) {
                upRight();
            } else if (y == (sizeOfGame - 2) / 2 && x <= sizeOfGame / 2) {
                right();
            }
        }

        }

        private static void downRight() {
            x++;
            y++;
        }

        private static void downLeft() {
            x++;
            y--;
        }

        private static void upLeft() {
            x--;
            y--;
        }

        private static void upRight() {
            x--;
            y++;
        }

        private static void right() {
            y++;
        }


    public void setDiamondPicture(GridPane gridPane, Fields[][] fields) {

        if(!(fields[x][y].isSingleFigure() || fields[x][y].isHole())) {
            ObservableList<Node> children = gridPane.getChildren();


            fields[x][y].elements.add(new Diamond());

            for (Node node : children) {
                if (node instanceof Pane && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                    Platform.runLater(() -> {
                        Pane currentPane = (Pane) node;
                        currentPane.setBackground(
                                new Background(
                                        new BackgroundImage(
                                                Diamond.image,
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
    }

    public static void deleteDiamond(GridPane gridPane, Fields[][] fields) {

        ObservableList<Node> children = gridPane.getChildren();

        if(fields[x][y].isDiamond()) {

            fields[x][y].elements.remove(fields[x][y].indexOfDiamond());

            for (Node node : children) {
                if (node instanceof Pane && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                    Platform.runLater(() -> {
                        Pane currentPane = (Pane) node;
                        currentPane.setBackground(new Background(
                                new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

                    });
                    break;
                }
            }
        }
    }

}
