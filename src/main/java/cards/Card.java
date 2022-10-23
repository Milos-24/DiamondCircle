package cards;

import javafx.scene.image.Image;

public abstract class Card {

    protected Image image;

    public abstract Image getImage();

    public abstract int getCardValue();
}
