package modifying.auth.view.box;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

public class LoginBox extends VBox {
    private final TextField usernameField;
    private final PasswordField passwordField;

    public LoginBox(LoginCtrl loginCtrl){
        super(15);

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");

        Button quitBtn = new Button("Quit"),
                confirmBtn = new Button("Confirm"),
                registerBtn = new Button("No account yet? Click here to register");
        quitBtn.setOnAction(event -> loginCtrl.handleLoginQuit());
        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText()));
        registerBtn.setOnAction(e -> loginCtrl.handleRegister());

        HBox choiceBox = new HBox(80, quitBtn, confirmBtn);

        Label messageLabel = new Label("Login");

        getChildren().addAll(messageLabel, usernameField, passwordField, choiceBox, registerBtn);
    }
}
