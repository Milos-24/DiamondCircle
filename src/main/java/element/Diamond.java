package element;

import javafx.scene.image.Image;

import java.io.File;

public class Diamond extends Element{
    public static String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"application"+File.separator+"Diamond.png";
    public static final File file = new File(path);
    public static final Image image = new Image(file.toURI().toString());
}
