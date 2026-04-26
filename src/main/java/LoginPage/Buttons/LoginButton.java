package LoginPage.Buttons;

import LoginPage.Account;
import LoginPage.AccountScene;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class LoginButton {
    public static Button login(Stage stage, Account account) {
        Map<String, String> users = account.getMap();

        Stage loginStage = new Stage();
        loginStage.setTitle("Account Login");

        VBox loginBox = new VBox(8), registerBox = new VBox(8);
        Scene loginScene = new Scene(loginBox,300,120), registerScene = new Scene(registerBox, 300, 90);

        Button registerButton = new Button("Don't have an account yet? Click here to register");

        registerButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-underline: true;"
        );
        registerButton.setOnMouseEntered(e ->
                registerButton.setStyle(registerButton.getStyle() + "-fx-text-fill: #0066cc;")
        );
        registerButton.setOnMouseExited(e ->
                registerButton.setStyle(registerButton.getStyle().replace("-fx-text-fill: #0066cc;", "-fx-text-fill: #333333;"))
        );
        registerButton.setOnAction(event -> {
            TextField accountIn = new TextField();
            TextField passwordIn = new PasswordField();
            accountIn.setPromptText("Please enter your account:");
            passwordIn.setPromptText("Please enter your password:");

            Button confirm = new Button("Register and login");
            confirm.setOnAction(actionEvent1 -> {
                String s1 = accountIn.getText(), s2 = passwordIn.getText();
                try {
                    FileWriter fileWriter = new FileWriter("src/main/userdata/users.txt", true);
                    if (s1.trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Username can't be null!");
                        alert.showAndWait();
                    } else if (users.containsKey(s1)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Username's already used!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Register succeeded!Login");
                        alert.showAndWait();
                        fileWriter.write(s1 + "&" + s2 + "\n");
                        fileWriter.close();
                        loginStage.close();
                        stage.setScene(AccountScene.getAccountScene(stage, account, false));
                    }
                } catch (IOException e) {
                }
            });

            Button cancel = new Button("Cancel");
            cancel.setOnAction(actionEvent1 -> loginStage.setScene(loginScene));

            HBox hBox = new HBox(50);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(cancel, confirm);

            if (registerBox.getChildren().isEmpty()) {
                registerBox.getChildren().addAll(accountIn, passwordIn, hBox);
            }

            loginStage.setScene(registerScene);
        });

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(400, 50);
        loginButton.setOnAction(actionEvent -> {
            TextField accountIn = new TextField();
            TextField passwordIn = new PasswordField();
            accountIn.setPromptText("Please enter your account:");
            passwordIn.setPromptText("Please enter your password:");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            HBox hBox = new HBox(150);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(cancel, confirm);

            confirm.setOnAction(actionEvent1 -> {
                String s1 = accountIn.getText(), s2 = passwordIn.getText();
                if (s1.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Username can't be null!");
                    alert.showAndWait();
                } else if (!users.containsKey(s1)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Username doesn't exist!");
                    alert.showAndWait();
                } else {
                    if (s2.equals(users.get(s1))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Login succeeded!");
                        alert.showAndWait();
                        account.setAccountName(s1);
                        loginStage.close();
                        stage.setScene(AccountScene.getAccountScene(stage, account, false));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Password is incorrect!");
                        alert.showAndWait();
                    }
                }
            });
            cancel.setOnAction(actionEvent1 -> loginStage.close());

            if (loginBox.getChildren().isEmpty()) {
                loginBox.getChildren().addAll(accountIn, passwordIn, hBox, registerButton);
                loginBox.setAlignment(Pos.CENTER);
            }

            loginStage.setScene(loginScene);
            loginStage.show();
        });
        return loginButton;
    }
}
