package view.scene.Boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class RegisterBox {
    public static VBox registerBox(){
        TextField account = new TextField();
        TextField password = new PasswordField();
        account.setPromptText("Please enter your account:");
        password.setPromptText("Please enter your password:");
        Button confirm = ButtonUtils.createButton("Confirm");
        confirm.setOnAction(event -> LoginCtrl.handleRegisterConfirm());
        Button cancel = ButtonUtils.createButton("Cancel");
        cancel.setOnAction(event -> LoginCtrl.handleRegisterCancel());
        HBox hBox = new HBox(50, cancel, confirm);
        VBox vBox = new VBox(10, account, password, hBox);
        return vBox;
    }
}
