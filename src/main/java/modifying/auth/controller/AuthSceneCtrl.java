package modifying.auth.controller;


import before.view.MouseGlowEffect;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import modifying.auth.view.sceneRoots.AccountView;
import modifying.auth.view.sceneRoots.LoginView;
import modifying.auth.view.sceneRoots.RegisterView;
import modifying.shared.model.TOAST_TYPE;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AuthSceneCtrl {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;

    private static final double TOAST_SLIDE_RATIO = 0.6;
    private static final double TOAST_TOP_OFFSET_RATIO = 0.04;
    private static final double TOAST_DISPLAY_SECONDS = 2.5;
    private static final double TOAST_ANIMATION_MILLIS = 400;
    private static final double TOAST_WIDTH_RATIO = 0.35;
    private static final double TOAST_PADDING_HORIZONTAL_RATIO = 0.025;
    private static final double TOAST_FONT_SIZE_RATIO = 0.02;
    private static final double TOAST_LINE_SPACING = 4;
    private static final double TOAST_CORNER_RADIUS_RATIO = 0.0175;
    private static final double TOAST_PADDING_RATIO = 0.015;
    private static final double TOAST_OVERSHOOT_MILLIS_RATIO = 0.5;
    private static final double TOAST_UNDERSHOOT_MILLIS_RATIO = 1.2;
    private static final double TOAST_SETTLE_MILLIS_RATIO = 1.5;

    // ===== 确认框（Confirm Dialog）布局常量 =====
    private static final double CONFIRM_WIDTH_RATIO = 0.4;               // 弹窗宽度占屏幕宽度的 40%
    private static final double CONFIRM_BG_OPACITY = 0.75;               // 遮罩不透明度
    private static final double CONFIRM_CORNER_RADIUS_RATIO = 0.02;      // 圆角占屏幕宽度的 2% (800*0.02=16px)
    private static final double CONFIRM_TITLE_FONT_RATIO = 0.0275;       // 标题字体大小 (800*0.0275≈22px)
    private static final double CONFIRM_MSG_FONT_RATIO = 0.02;           // 消息字体大小 (800*0.02=16px)
    private static final double CONFIRM_BUTTON_WIDTH_RATIO = 0.15;       // 按钮宽度 (800*0.15=120px)
    private static final double CONFIRM_BUTTON_HEIGHT_RATIO = 0.05625;   // 按钮高度 (800*0.05625=45px)
    private static final double CONFIRM_BUTTON_FONT_RATIO = 0.0225;      // 按钮字体大小 (800*0.0225=18px)
    private static final double CONFIRM_BOX_HEIGHT_RATIO = 0.6;


    public static int getScreenWidth(){
        return SCREEN_WIDTH;
    }
    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    private Stage authStage;
    private Scene authScene;
    private final Map<String, Parent> viewCache = new HashMap<>();

    public Scene getAuthScene() {
        return authScene;
    }

    /// Scenes without animation should be held
    private LoginView loginView;
    private AccountView accountView;
    private RegisterView registerView;

    private StackPane currentRoot;

    private Node currentToast = null;

    private final StackPane permanentRoot;
    private final StackPane contentContainer;

    public AuthSceneCtrl(Stage authStage){
        this.authStage = authStage;

        // empty stackPane temp
        permanentRoot = new StackPane();
        this.authScene = new Scene(permanentRoot, SCREEN_WIDTH,SCREEN_HEIGHT);
        permanentRoot.getStylesheets().add(Paths.get("src","main","resources","css","authSceneBgStyle.css").toUri().toString());


        MouseGlowEffect.attach(authScene,permanentRoot);
        contentContainer = new StackPane();
        permanentRoot.getChildren().add(contentContainer);

        authScene.getStylesheets().add(Paths.get("src","main","resources","css","toastStyle.css").toUri().toString());

        authStage.setScene(authScene);
    }

    public void showLoginScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType) {
        loginView = (LoginView) viewCache.computeIfAbsent("LOGIN", k -> new LoginView(loginCtrl));
        loginView.clearTextField();
        contentContainer.getChildren().clear();
        contentContainer.getChildren().add(loginView);
        currentRoot = loginView;

        if(toastMessage != null && toastType != null){
            showToast(currentRoot,toastMessage,toastType);
        }

    }
    public void showLoginScene(LoginCtrl loginCtrl){
        showLoginScene(loginCtrl,null,null);
    }


    public void showAccountScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType){
        accountView = (AccountView) viewCache.computeIfAbsent("ACCOUNT", k-> new AccountView(loginCtrl));
        accountView.syncUser();
        contentContainer.getChildren().clear();
        contentContainer.getChildren().add(accountView);
        currentRoot = accountView;

        if(toastMessage != null && toastType != null){
            showToast(currentRoot,toastMessage,toastType);
        }
    }
    public void showAccountScene(LoginCtrl loginCtrl){
        showAccountScene(loginCtrl,null,null);
    }

    public void showRegisterScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType){
        registerView = (RegisterView) viewCache.computeIfAbsent("REGISTER", k-> new RegisterView(loginCtrl));
        registerView.clearTextField();
        contentContainer.getChildren().clear();
        contentContainer.getChildren().add(registerView);
        currentRoot = registerView;
        if(toastMessage != null && toastType != null){
            showToast(currentRoot,toastMessage,toastType);
        }

    }
    public void showRegisterScene(LoginCtrl loginCtrl){
        showRegisterScene(loginCtrl,null,null);
    }


    /// Common toast showing function
    public void showToast(StackPane aimRoot, String message, TOAST_TYPE TYPE) {
        if (aimRoot != null) {
            aimRoot.getChildren().remove(currentToast);
            currentToast = null;
        }

        // 1. 计算实际尺寸
        double toastWidth = SCREEN_WIDTH * TOAST_WIDTH_RATIO;           // 400px
        double labelMaxWidth = toastWidth - (TOAST_PADDING_HORIZONTAL_RATIO * SCREEN_HEIGHT * 2);
        double fontSize = SCREEN_WIDTH * TOAST_FONT_SIZE_RATIO;         // 20px
        double padding = SCREEN_HEIGHT * TOAST_PADDING_RATIO;           // ~28px
        double cornerRadius = SCREEN_WIDTH * TOAST_CORNER_RADIUS_RATIO; // ~14px

        // 2. 构建弹窗
        HBox toastBox = new HBox();
        toastBox.getStyleClass().add("toast-box");
        toastBox.getStyleClass().add(TYPE.getCssClass());

        // 🔥 固定宽度
        toastBox.setPrefWidth(toastWidth);
        toastBox.setMaxWidth(toastWidth);

        // 🔥 禁止垂直拉伸
        toastBox.setMaxHeight(Region.USE_PREF_SIZE);

        // 🔥 设置内边距（上/下/左/右统一用 padding，或者左右用 padding*2）
        toastBox.setPadding(new Insets(padding, padding * 2, padding, padding * 2));

        // 🔥 设置圆角 + 背景颜色 + 边框（用 setStyle 内联，避免 CSS 被覆盖）
        toastBox.setStyle(
                String.format(
                        "-fx-background-radius: %.0fpx; " +
                                "-fx-border-radius: %.0fpx; " +
                                "-fx-border-width: 1.5px;",
                        cornerRadius, cornerRadius
                )
        );

        // 🔥 让子节点（Label）在 HBox 中水平和垂直居中
        toastBox.setAlignment(Pos.CENTER);

        // 3. 构建文字
        Label label = new Label(message);
        label.setId("toast-label");
        label.setWrapText(true);
        label.setPrefWidth(labelMaxWidth);
        label.setMaxWidth(labelMaxWidth);
        label.setStyle(String.format("-fx-font-size: %.0fpx;", fontSize));
        // 文字垂直居中（默认就是居中的，不用额外设置）

        toastBox.getChildren().add(label);

        // 4. 定位计算
        double slideDistance = SCREEN_WIDTH * TOAST_SLIDE_RATIO;
        double topOffset = SCREEN_HEIGHT * TOAST_TOP_OFFSET_RATIO;

        StackPane.setAlignment(toastBox, Pos.TOP_RIGHT);
        toastBox.setTranslateY(topOffset);
        toastBox.setTranslateX(slideDistance);

        aimRoot.getChildren().add(toastBox);
        currentToast = toastBox;

        // 动画部分（保持不变，同样使用 slideDistance）
        // 1. 设定初始状态（瞬间完成）
        toastBox.setOpacity(0);                     // 完全透明
        toastBox.setScaleX(0.5);                   // 缩小到一半
        toastBox.setScaleY(0.5);

// 2. 创建一个时间轴，包含 4 个关键帧，产生弹性效果
        Timeline popInTimeline = new Timeline();

        double overshootTime = TOAST_ANIMATION_MILLIS * TOAST_OVERSHOOT_MILLIS_RATIO;  // 过冲（弹到最大）
        double undershootTime = TOAST_ANIMATION_MILLIS * TOAST_UNDERSHOOT_MILLIS_RATIO; // 回缩（压到最小）
        double settleTime = TOAST_ANIMATION_MILLIS * TOAST_SETTLE_MILLIS_RATIO;      // 稳定归位

        // 关键帧 1 ：初始位置（右侧外），透明，缩小
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(toastBox.translateXProperty(), slideDistance),
                new KeyValue(toastBox.opacityProperty(), 0),
                new KeyValue(toastBox.scaleXProperty(), 0.5),
                new KeyValue(toastBox.scaleYProperty(), 0.5)
        );

        // 关键帧 2 ：滑入到目标位置，完全可见，并放大到 1.15 倍（过冲）
        KeyFrame overshoot = new KeyFrame(Duration.millis(overshootTime),
                new KeyValue(toastBox.translateXProperty(), 0),
                new KeyValue(toastBox.opacityProperty(), 1),
                new KeyValue(toastBox.scaleXProperty(), 1.05),
                new KeyValue(toastBox.scaleYProperty(), 1.05)
        );

        // 关键帧 3 ：回缩到 0.9 倍（低于正常值）
        KeyFrame undershoot = new KeyFrame(Duration.millis(undershootTime),
                new KeyValue(toastBox.scaleXProperty(), 0.95),
                new KeyValue(toastBox.scaleYProperty(), 0.95)
        );

        // 关键帧 4 ：稳定在 1.0 倍（弹性效果结束）
        KeyFrame settle = new KeyFrame(Duration.millis(settleTime), // 总共 600ms
                new KeyValue(toastBox.scaleXProperty(), 1.0),
                new KeyValue(toastBox.scaleYProperty(), 1.0)
        );

        popInTimeline.getKeyFrames().addAll(start, overshoot, undershoot, settle);

        popInTimeline.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(TOAST_DISPLAY_SECONDS));
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(TOAST_ANIMATION_MILLIS), toastBox);
        slideOut.setFromX(0);
        slideOut.setToX(slideDistance);
        StackPane finalAimRoot = aimRoot;
        slideOut.setOnFinished(e -> {
            finalAimRoot.getChildren().remove(toastBox);
            if (currentToast == toastBox) {
                currentToast = null;
            }
        });

        pause.setOnFinished(e -> slideOut.play());
        pause.play();
    }
    /// toast without scene switch
    public void showToast(String message, TOAST_TYPE TYPE){
        showToast(currentRoot, message, TYPE);
    }
    /**
     * 显示模态确认对话框（异步回调模式）
     * @param title 标题
     * @param message 消息内容
     * @param onConfirm 点击“确定”后的回调
     * @param onCancel 点击“取消”后的回调（可为 null）
     */
    public void showConfirmDialog(String title, String message, Runnable onConfirm, Runnable onCancel) {
        // 1. 创建遮罩层（半透明黑，阻止点击穿透）
        Rectangle dimBackground = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT);
        dimBackground.setFill(Color.rgb(0, 0, 0, CONFIRM_BG_OPACITY));
        dimBackground.setOnMouseClicked(e -> e.consume()); // 关键：点击背景不关闭弹窗

        // 2. 计算弹窗尺寸
        double confirmWidth = SCREEN_WIDTH * CONFIRM_WIDTH_RATIO;
        double padding = SCREEN_HEIGHT * TOAST_PADDING_RATIO;
        double cornerRadius = SCREEN_WIDTH * CONFIRM_CORNER_RADIUS_RATIO;
        double titleFontSize = SCREEN_WIDTH * CONFIRM_TITLE_FONT_RATIO;
        double msgFontSize = SCREEN_WIDTH * CONFIRM_MSG_FONT_RATIO;
        double btnWidth = SCREEN_WIDTH * CONFIRM_BUTTON_WIDTH_RATIO;
        double btnHeight = SCREEN_HEIGHT * CONFIRM_BUTTON_HEIGHT_RATIO;
        double btnFontSize = SCREEN_WIDTH * CONFIRM_BUTTON_FONT_RATIO;

        // 3. 构建内容面板
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(confirmWidth);
        contentBox.setPrefWidth(confirmWidth);
        contentBox.setPadding(new Insets(padding * 2));
        contentBox.setStyle(
                "-fx-background-color: rgba(20, 20, 30, 0.95); " +
                        "-fx-background-radius: " + cornerRadius + "px; " +
                        "-fx-border-color: rgba(120, 80, 200, 0.5); " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: " + cornerRadius + "px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 5);"
        );

        // 标题
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: #ffffff; " +
                        "-fx-font-size: " + titleFontSize + "px; " +
                        "-fx-font-weight: bold;"
        );

        // 消息内容（支持自动换行）
        Label msgLabel = new Label(message);
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(confirmWidth - padding * 4);
        msgLabel.setStyle(
                "-fx-text-fill: #cccccc; " +
                        "-fx-font-size: " + msgFontSize + "px;"
        );
        msgLabel.setAlignment(Pos.CENTER);

        String baseBtnStyle =
                "-fx-background-color: rgba(50, 215, 193, 0.8); " +
                        "-fx-text-fill: #000000; " +
                        "-fx-font-size: " + btnFontSize + "px; " +
                        "-fx-pref-width: " + btnWidth + "px; " +
                        "-fx-pref-height: " + btnHeight + "px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: rgba(30, 160, 140, 0.9); " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px;";

        String cancelBtnStyle =
                "-fx-background-color: rgba(200, 80, 80, 0.7); " +
                        "-fx-text-fill: #ffffff; " +
                        "-fx-font-size: " + btnFontSize + "px; " +
                        "-fx-pref-width: " + btnWidth + "px; " +
                        "-fx-pref-height: " + btnHeight + "px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: rgba(160, 50, 50, 0.8); " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px;";

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");
        confirmBtn.setStyle(baseBtnStyle);
        cancelBtn.setStyle(cancelBtnStyle);

        // 悬停效果（可选，直接用 setOnMouseEntered 增强，或保持 CSS 原样）
        confirmBtn.setOnMouseEntered(e -> confirmBtn.setStyle(baseBtnStyle + "-fx-background-color: rgba(80, 235, 213, 0.9);"));
        confirmBtn.setOnMouseExited(e -> confirmBtn.setStyle(baseBtnStyle));
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(cancelBtnStyle + "-fx-background-color: rgba(220, 100, 100, 0.8);"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(cancelBtnStyle));

        HBox buttonBox = new HBox(30, confirmBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(titleLabel, msgLabel, buttonBox);
        contentBox.setMaxHeight(SCREEN_HEIGHT * CONFIRM_BOX_HEIGHT_RATIO);
        contentBox.setMaxHeight(Region.USE_PREF_SIZE);

        // 4. 将遮罩和内容组合到一个 StackPane 中
        StackPane overlay = new StackPane();
        overlay.getChildren().addAll(dimBackground, contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        // 5. 添加到永久根节点（覆盖一切）
        permanentRoot.getChildren().add(overlay);
        overlay.toFront();

        // 6. 按钮事件
        confirmBtn.setOnAction(e -> {
            permanentRoot.getChildren().remove(overlay);
            if (onConfirm != null) onConfirm.run();
        });

        cancelBtn.setOnAction(e -> {
            permanentRoot.getChildren().remove(overlay);
            if (onCancel != null) onCancel.run();
        });

        // 7. 窗口大小自适应（让遮罩跟随 permanentRoot 大小变化）
        permanentRoot.widthProperty().addListener((obs, old, val) -> {
            dimBackground.setWidth(val.doubleValue());
            // 内容不需要拉伸，保持居中
        });
        permanentRoot.heightProperty().addListener((obs, old, val) -> {
            dimBackground.setHeight(val.doubleValue());
        });
    }
}
