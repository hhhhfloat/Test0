package view.scene.rsc;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.nio.file.Paths;

public class BgImage {
    String fileName;

    public BgImage(String fileName) {
        this.fileName = fileName;
    }

    private Image image = new Image(Paths.get(fileName).toUri().toString());

    public double getWidth() {
        return image.getWidth();
    }

    public double getHeight() {
        return image.getHeight();
    }

    public Background getBgImage() {
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
