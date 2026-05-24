package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginBox extends VBox {
    private final TextField usernameField;
    private final PasswordField passwordField;

    public LoginBox(LoginCtrl loginCtrl) {
        super(15);

        Label messageLabel = new Label("Login");

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");

        Button cancelBtn = new Button("Cancel"),
                confirmBtn = new Button("Confirm"),
                registerBtn = new Button("No account yet? Click here to register");
        cancelBtn.setOnAction(event -> loginCtrl.handleLoginCancel());
        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText()));
        registerBtn.setOnAction(e -> loginCtrl.handleRegister());

        HBox choiceBox = new HBox(80, cancelBtn, confirmBtn);

        getChildren().addAll(messageLabel, usernameField, passwordField, choiceBox, registerBtn);
    }
}
