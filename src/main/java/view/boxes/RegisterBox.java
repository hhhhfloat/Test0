package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterBox extends VBox{
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField confirmFiled;

    public RegisterBox(LoginCtrl loginCtrl) {
        super(15);

        Label messageLabel = new Label("Register");

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password");
        confirmFiled = new PasswordField();
        confirmFiled.setPromptText("Please confirm your password");

        // restrict input upper bound to 2000 chars
        usernameField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 2000) {
                return null;
            }
            return change;
        }));
        passwordField.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getControlNewText().length()>2000){
                return null;
            }
            return change;
        }));
        confirmFiled.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getControlNewText().length()>2000){
                return null;
            }
            return change;
        }));

        Button cancelBtn = new Button("Cancel"),
                confirmBtn = new Button("Confirm");

        cancelBtn.setOnAction(event -> loginCtrl.handleRegisterCancel());
        confirmBtn.setOnAction(event -> loginCtrl.handleRegisterConfirm(usernameField.getText(), passwordField.getText(), confirmFiled.getText()));
        HBox choiceBox = new HBox(80, cancelBtn, confirmBtn);

        getChildren().addAll(messageLabel, usernameField, passwordField, confirmFiled, choiceBox);
    }
}
