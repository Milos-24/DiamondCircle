package application;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static application.GameController.sizeOfGame;
import static application.MenuController.*;

public class FigurePathController implements Initializable {

    @FXML
    GridPane gridPane;

    Pane[][] panes;
    static Color color;

    public static boolean activeFigure;

    public final int prefWidth = 700;
    public final int prefHeight = 450;

    public static int x;
    public static int y;
    public int startingX, startingY;

    public void returnButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        drawGrid();

        if(activeFigure) {
            setStartingPosition();
            showPath();
        }
    }

    public static void setColor(Color color1) {
        color = color1;
    }

    public static void setX(int x1) {
        x = x1;
    }

    public static void setY(int y1) {
        y = y1;
    }

    public void drawGrid()
    {
        int sizeOfGame;

        if(Objects.equals(gameSize, size1))
            sizeOfGame=7;
        else if(Objects.equals(gameSize, size2))
            sizeOfGame=8;
        else if(Objects.equals(gameSize, size3))
            sizeOfGame=9;
        else
            sizeOfGame=10;



        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        gridPane.setPrefSize(prefWidth, prefHeight);

        for (int i = 0; i < (sizeOfGame-3); i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100d / sizeOfGame);
            gridPane.getColumnConstraints().add(column);
        }

        for(int i = 0; i < (sizeOfGame-3); i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100d / sizeOfGame);
            gridPane.getRowConstraints().add(row);
        }

        panes = new Pane[sizeOfGame][sizeOfGame];

        for (int i = 0 ; i < sizeOfGame ; i++ )
        {
            for (int j = 0 ; j < sizeOfGame ; j++ )
            {
                panes[i][j]=new Pane();
                gridPane.add(panes[i][j], i, j);
            }
        }

    }
    
    public void showPath()
    {
        paintPane(this.startingX,this.startingY, gridPane);

        while(this.startingX!= x || this.startingY!= y)
        {
            incrementPosition(this.startingX, this.startingY, gridPane);
        }
    }

    public void incrementPosition(int x,int y, GridPane gridPane) {

        if (y != (sizeOfGame - 1) / 2 || x != (sizeOfGame / 2)) {
            if (y >= sizeOfGame / 2 && x < (sizeOfGame - 1) / 2) {
                downRight(gridPane);
            } else if (y > (sizeOfGame - 1) / 2 && x >= (sizeOfGame - 1) / 2) {
                downLeft(gridPane);
            } else if (y <= (sizeOfGame - 1) / 2 && x > sizeOfGame / 2) {
                upLeft(gridPane);
            } else if (y < (sizeOfGame - 2) / 2 && x <= sizeOfGame / 2) {
                upRight(gridPane);
            } else if (y == (sizeOfGame - 2) / 2 && x <= sizeOfGame / 2) {
                right(gridPane);
            }
        } else {
            System.out.println("figure finished");
        }
    }

    private void downRight(GridPane gridPane) {
        this.startingX++;
        this.startingY++;
        paintPane(this.startingX,this.startingY, gridPane);
    }

    private void downLeft(GridPane gridPane) {
        this.startingX++;
        this.startingY--;
        paintPane(this.startingX,this.startingY, gridPane);
    }

    private void upLeft(GridPane gridPane) {
        this.startingX--;
        this.startingY--;
        paintPane(this.startingX,this.startingY, gridPane);
    }

    private void upRight(GridPane gridPane) {
        this.startingX--;
        this.startingY++;
        paintPane(this.startingX,this.startingY, gridPane);
    }

    private void right(GridPane gridPane) {
        this.startingY++;
        paintPane(this.startingX,this.startingY, gridPane);
    }

    synchronized public void paintPane(final int row, final int column, GridPane gridPane) {

        ObservableList<Node> children = gridPane.getChildren();


        for (Node node : children) {
            if (node instanceof Pane && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                Platform.runLater(()-> {
                    Pane currentPane = (Pane)node;
                    currentPane.setBackground(new Background(
                            new BackgroundFill(color, new CornerRadii(0), new Insets(0))));

                });
                break;
            }
        }
    }

    private void setStartingPosition()
    {
        if(sizeOfGame == 7)
        {
            this.startingX=0;
            startingY=3;
        }
        else if(sizeOfGame == 8)
        {
            startingX=0;
            startingY=4;
        }
        else if(sizeOfGame == 9)
        {
            startingX=0;
            startingY=4;
        }
        else
        {
            startingX=0;
            startingY=5;
        }
    }
}
