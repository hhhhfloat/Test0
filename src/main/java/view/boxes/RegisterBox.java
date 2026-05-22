package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterBox extends VBox{
    private final TextField usernameField;
    private final PasswordField passwordField;

    public RegisterBox(LoginCtrl loginCtrl) {
        super(15);

        Label messageLabel = new Label("Register");

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");

        Button cancelBtn = new Button("Cancel"), confirmBtn = new Button("Confirm");
        cancelBtn.setOnAction(event -> loginCtrl.handleRegisterCancel());
        confirmBtn.setOnAction(event -> loginCtrl.handleRegisterConfirm(usernameField.getText(), passwordField.getText()));
        HBox choiceBox = new HBox(80, cancelBtn, confirmBtn);

        getChildren().addAll(messageLabel, usernameField, passwordField, choiceBox);
    }
}
