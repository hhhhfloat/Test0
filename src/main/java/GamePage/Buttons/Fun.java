package GamePage.Buttons;


import GamePage.GameScene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Fun {
    public static Button fun(Stage stage) {
        Button funButton = new Button("Fun Mode");
        funButton.setPrefSize(600, 100);
       // funButton.setOnAction(event -> stage.setScene(GameScene.getGameScene()));
        return funButton;
    }
}
