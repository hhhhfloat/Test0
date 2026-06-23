package modifying.auth.view.tools;

import before.controller.AudioCtrl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;

public class PasswordRevealField extends HBox {

    private final StackPane inputStack = new StackPane();
    private final TextField textField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button revealButton = new Button();

    private final BooleanProperty showing = new SimpleBooleanProperty(false);

    public PasswordRevealField(AudioCtrl audioCtrl) {
        // 加载图片
        ImageView eyeOpen = new ImageView(
                getClass().getResource("/sprites/eyeopen.png").toExternalForm()
        );
        ImageView eyeClose = new ImageView(
                getClass().getResource("/sprites/eyeclose.png").toExternalForm()
        );

        // 初始化输入框...
        textField.getStyleClass().add("password-reveal-field");
        textField.setVisible(false);
        passwordField.setVisible(true);

        // 双向绑定文本
        textField.textProperty().bindBidirectional(passwordField.textProperty());

        // 堆叠输入框
        inputStack.getChildren().addAll(passwordField, textField);
        HBox.setHgrow(inputStack, Priority.ALWAYS);
        inputStack.setAlignment(Pos.CENTER);
        inputStack.setMaxWidth(460);

        // 眼睛按钮（初始为闭眼）
        revealButton.getStyleClass().add("reveal-button");
        revealButton.setFocusTraversable(false);
        revealButton.setGraphic(eyeClose);  // 初始闭眼
        revealButton.setOnAction(e -> {
            audioCtrl.playToggleSound();
            toggleReveal();
        });

        // 监听显示状态切换
        showing.addListener((obs, old, val) -> {
            textField.setVisible(val);
            passwordField.setVisible(!val);
            if (val) {
                textField.requestFocus();
                revealButton.setGraphic(eyeOpen);   // 显示明文 → 睁眼
            } else {
                passwordField.requestFocus();
                revealButton.setGraphic(eyeClose);  // 显示掩码 → 闭眼
            }
        });

        // 添加组件
        getChildren().addAll(inputStack, revealButton);
        setAlignment(Pos.CENTER);
        setSpacing(5);
    }

    private void toggleReveal() {
        showing.set(!showing.get());
    }

    // 代理方法

    public String getText() {
        return passwordField.getText();
    }

    public void setText(String text) {
        passwordField.setText(text);
    }

    public StringProperty textProperty() {
        return passwordField.textProperty();
    }

    public void setPromptText(String prompt) {
        passwordField.setPromptText(prompt);
        textField.setPromptText(prompt);
    }

    public String getPromptText() {
        return passwordField.getPromptText();
    }

    public void clear() {
        passwordField.clear();
        textField.clear();
    }

    // 公开显示状态属性（可选）
    public BooleanProperty showingProperty() {
        return showing;
    }

    public boolean isShowing() {
        return showing.get();
    }

    public void setShowing(boolean showing) {
        this.showing.set(showing);
    }

    public void setTextFormatter(TextFormatter<Object> objectTextFormatter) {
        passwordField.setTextFormatter(objectTextFormatter);
    }
}