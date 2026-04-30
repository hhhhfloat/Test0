package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginBox extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;

    public LoginBox(LoginCtrl loginCtrl) {
        super(15);

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> loginCtrl.handleLoginCancel());
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText()));
        Button registerBtn = new Button("Don't have an account yet? Click here to register");
        registerBtn.setOnAction(e -> loginCtrl.handleRegister());

        HBox hBox = new HBox(80, cancelBtn, confirmBtn);

        Label messageLabel;
        messageLabel = new Label("Login Your Account");

        getChildren().addAll(messageLabel, usernameField, passwordField, hBox, registerBtn);
    }
}
