package figures;


import application.FigurePathController;
import application.MenuController;
import element.Element;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import static application.GameController.showFigurePathButton;
import static application.MenuController.handler;

public abstract class Figure extends Element {
    public enum Colors {
        RED, GREEN, BLUE, YELLOW
    }

    public enum FigureStatus {
        FINISHED, DEAD, PLAYING, WAITING
    }

    private FigureStatus status;
    private int x, y;
    public ArrayList<Integer> visitedFieldsX = new ArrayList<>();
    public ArrayList<Integer> visitedFieldsY = new ArrayList<>();
    public long beginTime, ellapsedTime;
    Button button;
    public abstract Color getColor();

    public abstract Image getImage();


    {
        // ime logger-a je naziv klase

        Logger.getLogger(Figure.class.getName()).addHandler(handler);
    }

    public  FigureStatus getFigureStatus()
    {
        return this.status;
    }



    public void setFigureStatus(FigureStatus status) {
        this.status=status;
    }


    public void setX(int x) {this.x=x; this.visitedFieldsX.add(x);}
    public void setY(int y) {this.y=y; this.visitedFieldsY.add(y);}
    public int getX(){return this.x;}
    public int getY() {return this.y;}


    public void beginTime() {
        this.beginTime=System.currentTimeMillis();
    }

    public void elapsedTime()
    {
        this.ellapsedTime += System.currentTimeMillis() - this.beginTime;
    }

    public void pauseTime()
    {
        if(this.status == FigureStatus.PLAYING)
            this.ellapsedTime += System.currentTimeMillis() - this.beginTime;
    }

    public void resumeTime()
    {
        if(this.status == FigureStatus.PLAYING)
            this.beginTime = System.currentTimeMillis();
    }

    public void setButton(Button button) {

        button.setMaxWidth(200);
        button.setMaxHeight(20);


        button.setStyle("-fx-border-width: 2; -fx-border-color: BLACK");


        button.setBackground(new Background(new BackgroundFill(getColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        if(getColor().equals(Color.BLUE) || getColor().equals(Color.RED) || getColor().equals(Color.GREEN))
        {
            button.setStyle("-fx-text-fill: WHITE");
            button.setStyle("-fx-border-width: 2; -fx-border-color: BLACK");
        }


        this.button = button;
        button.setOnAction((event)->{
            try {
                Logger.getLogger(MenuController.class.getName()).addHandler(handler);
                Logger.getLogger(MenuController.class.getName()).setUseParentHandlers(false);
                FigurePathController.activeFigure= !getFigureStatus().equals(FigureStatus.WAITING);

                FigurePathController.setColor(this.getColor());
                FigurePathController.setX(this.getX());
                FigurePathController.setY(this.getY());
                showFigurePathButton(event);
            } catch (IOException e) {
                Logger.getLogger(Figure.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
        });

    }

}
