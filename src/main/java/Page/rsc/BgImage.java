package Page.rsc;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.nio.file.Paths;

public class BgImage {
    public static Background bgimage(){
        Image image = new Image(Paths.get("src/main/resources/bg.png").toUri().toString());
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false, false, true, false
                )
        );
        return new Background(backgroundImage);
    }
}
