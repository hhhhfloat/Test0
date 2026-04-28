package view.scene.Boxes;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegisterBox {
    public static VBox registerBox(){
        TextField accountIn = new TextField();
        TextField passwordIn = new PasswordField();
        accountIn.setPromptText("Please enter your account:");
        passwordIn.setPromptText("Please enter your password:");

        VBox vBox = new VBox();
        return vBox;
    }
}
