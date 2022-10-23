package figures;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

public class RegularFigure extends Figure{

    Colors figureColor;

    public static String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"application"+File.separator+"RegularFigure.png";
    public static final File file = new File(path);
    public final Image image = new Image(file.toURI().toString());

    public RegularFigure(Colors color)
    {
        figureColor=color;
        setFigureStatus(FigureStatus.WAITING);

    }
    public Color getColor()
    {
        if(figureColor.equals(Colors.BLUE))
            return Color.BLUE;
        else if(figureColor.equals(Colors.GREEN))
            return Color.GREEN;
        else if(figureColor.equals(Colors.RED))
            return Color.RED;
        else
            return Color.YELLOW;
    }



    @Override
    public Image getImage() {
        return this.image;
    }

}
