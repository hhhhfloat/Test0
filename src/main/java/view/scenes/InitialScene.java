package view.scenes;


import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.InitialBox;

public class InitialScene extends Scene{
    public InitialScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 600);
    }
    private static BorderPane createRoot(LoginCtrl loginCtrl) {
        InitialBox initialBox = new InitialBox(loginCtrl);
        BorderPane root = new BorderPane(initialBox);
        return root;
    }
}
