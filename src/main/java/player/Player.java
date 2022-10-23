package player;

import cards.DeckOfCards;
import figures.Figure;

public class Player {
    String name;
    public Figure[] figures;
    public boolean dead;
    int numberOfFigures;
    public static DeckOfCards deck=new DeckOfCards();

    public Player(String name)
    {
        this.name=name;
        this.numberOfFigures=4;
    }

    public String getName()
    {
        return this.name;
    }

    public void setFigures(Figure[] figures)
    {
        this.figures=figures;
    }

}
