package view.scene.Boxes;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginBox {
    public static VBox loginBox(){
        TextField accountIn = new TextField();
        TextField passwordIn = new PasswordField();
        accountIn.setPromptText("Please enter your account:");
        passwordIn.setPromptText("Please enter your password:");

        VBox vBox = new VBox();
        return vBox;
    }
}
