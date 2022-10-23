package cards;


import javafx.scene.image.Image;

import java.io.File;

public class SpecialCard extends Card{

    public final File file;
    private final String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"application"+File.separator+"SpecialCard.png";
    int cardValue;

    SpecialCard(int value)
    {
        file = new File(this.path);
        image = new Image(file.toURI().toString());
        this.cardValue=value;
    }

    @Override
    public Image getImage()
    {
        return image;
    }

    @Override
    public int getCardValue() {
        return 0;
    }

}
