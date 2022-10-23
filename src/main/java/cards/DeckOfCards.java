package cards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DeckOfCards {


    public ArrayList<Card> cards = new ArrayList<>();
    public static Card currentCard;
    private final int[] cardValue = { 0, 1, 2, 3, 4 };

    public DeckOfCards()
    {
        for(int i = 0 ; i < 10 ; i++)
        {
            cards.add(new RegularCard(cardValue[1]));
            cards.add(new RegularCard(cardValue[2]));
            cards.add(new RegularCard(cardValue[3]));
            cards.add(new RegularCard(cardValue[4]));
            cards.add(new SpecialCard(cardValue[0]));
        }
        cards.add(new SpecialCard(cardValue[0]));
        cards.add(new SpecialCard(cardValue[0]));

        Collections.shuffle(cards);
    }



    public Image drawACard()
    {
        Collections.rotate(cards,-1);
        currentCard=this.cards.get(cards.size() - 1);
        return (this.cards.get(cards.size() - 1).getImage());
    }
}
