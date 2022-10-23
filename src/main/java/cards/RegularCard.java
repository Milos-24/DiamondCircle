package cards;

import javafx.scene.image.Image;

import java.io.File;

public class RegularCard extends Card{
    public  int cardValue;
    public final File file;


    RegularCard(int value)
    {
        this.cardValue=value;
        String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"application"+File.separator+"Card_" + cardValue + ".png";
        file = new File(path);
        image = new Image(file.toURI().toString());
    }

    @Override
    public Image getImage()
    {
        return this.image;
    }

    @Override
    public int getCardValue()
    {
        return this.cardValue;
    }
}
