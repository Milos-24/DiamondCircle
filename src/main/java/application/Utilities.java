package application;

import fields.Fields;
import figures.Figure;
import figures.FlyingFigure;
import figures.Ghost;
import element.Hole;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import player.Player;


import static application.GameController.sizeOfGame;

public class Utilities {

    public static int x, y;

    public static void getToHolePosition(GridPane gridPane) {

        if (x != (sizeOfGame - 1) / 2 || y != (sizeOfGame / 2)) {
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
        } else {
            System.out.println("hole deleted");
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

    public static boolean setHoleColor(GridPane gridPane, Fields[][] fields, Player[] players) {
        boolean isFlying = false;
        ObservableList<Node> children = gridPane.getChildren();

        for (int i = 0; i < MenuController.numberOfPlayers; i++)
            for (Figure figure : players[i].figures
            ) {
                if (figure.getX() == x && figure.getY() == y && Figure.FigureStatus.PLAYING.equals(figure.getFigureStatus())) {

                    if (figure instanceof FlyingFigure)
                        isFlying = true;
                    else
                    {
                        isFlying = false;


                        fields[x][y].elements.remove(figure);
                        figure.setFigureStatus(Figure.FigureStatus.DEAD);
                        figure.elapsedTime();
                    }
                }
            }

        if(!isFlying)
            fields[x][y].elements.add(new Hole());


        if(fields[x][y].isDiamond())
        {
            Ghost.deleteDiamond(gridPane, fields);

            fields[x][y].elements.add(new Hole());
        }


        if (!isFlying)
            for (Node node : children) {
                if (node instanceof Pane && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                    Platform.runLater(() -> {
                        Pane currentPane = (Pane) node;
                        currentPane.setBackground(new Background(
                                new BackgroundFill(Hole.color, new CornerRadii(0), new Insets(0))));

                    });
                    break;
                }
            }

        return isFlying;
    }

    public static void deleteHole(GridPane gridPane, Fields[][] fields) {


        if(fields[x][y].isHole())
        {
            ObservableList<Node> children = gridPane.getChildren();

            fields[x][y].elements.remove(fields[x][y].indexOfHole());

            for (Node node : children) {
                if (node instanceof Pane && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                    Platform.runLater(()-> {
                        Pane currentPane = (Pane)node;
                        currentPane.setBackground(new Background(
                                new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

                    });
                    break;
                }
            }
        }
    }
}
