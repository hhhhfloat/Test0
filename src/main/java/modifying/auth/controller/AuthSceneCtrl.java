package modifying.auth.controller;


import modifying.auth.view.tools.MouseGlowEffect;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import modifying.auth.view.sceneRoots.AccountView;
import modifying.auth.view.sceneRoots.LoadView;
import modifying.auth.view.sceneRoots.LoginView;
import modifying.auth.view.sceneRoots.RegisterView;
import modifying.shared.model.TOAST_TYPE;
import modifying.shared.view.UIUtils;

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
    private static final double TOAST_OVERSHOOT_MILLIS_RATIO = 0.5;
    private static final double TOAST_UNDERSHOOT_MILLIS_RATIO = 1.2;
    private static final double TOAST_SETTLE_MILLIS_RATIO = 1.5;


    private static final double LOGIC_WIDTH = 800;
    private static final double LOGIC_HEIGHT = 800;

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
    private LoadView loadView;

    private StackPane currentRoot;

    private Node currentToast = null;

    private final StackPane authRoot;
    private final Pane mouseEffectPane;
    private final StackPane contentPane;
    private final Group messageGroup;
    private final Group rootGroup;
    private final StackPane messagePane;
    private final StackPane permanentRoot;
    private final StackPane contentContainer;

    public AuthSceneCtrl(Stage authStage){
        this.authStage = authStage;

        authRoot = new StackPane();
        authScene = new Scene(authRoot, LOGIC_WIDTH, LOGIC_HEIGHT);
        authScene.getStylesheets().add(Paths.get("src","main","resources","css","AuthSceneStyle","authSceneBgStyle.css").toUri().toString());
        authStage.setScene(authScene);

        mouseEffectPane = new Pane();
        mouseEffectPane.setMouseTransparent(true);
        mouseEffectPane.setPickOnBounds(false);
        mouseEffectPane.prefWidthProperty().bind(authRoot.widthProperty());
        mouseEffectPane.prefHeightProperty().bind(authRoot.heightProperty());
        authRoot.getChildren().add(mouseEffectPane);
        MouseGlowEffect.attach(authScene, mouseEffectPane);

        messagePane = new StackPane();
        messagePane.setPrefSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMinSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMaxSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMouseTransparent(true);

        messageGroup = new Group(messagePane);
        messageGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(()->{
                    double w = authScene.getWidth();
                    double h = authScene.getHeight();
                    if(w<=0||h<=0)return 1.0;
                    return Math.min(w/LOGIC_WIDTH,h/LOGIC_HEIGHT);
                }, authScene.widthProperty(), authScene.heightProperty())
        );
        messageGroup.scaleYProperty().bind(messageGroup.scaleXProperty());

        contentPane = new StackPane();
        contentPane.minWidthProperty().bind(authScene.widthProperty());
        contentPane.prefWidthProperty().bind(authScene.widthProperty());
        contentPane.maxWidthProperty().bind(authScene.widthProperty());
        contentPane.minHeightProperty().bind(authScene.heightProperty());
        contentPane.prefHeightProperty().bind(authScene.heightProperty());
        contentPane.maxHeightProperty().bind(authScene.heightProperty());
        contentPane.getStylesheets().add(Paths.get("src","main","resources","css","AuthSceneStyle","confirmStyle.css").toUri().toString());
        contentPane.getStylesheets().add(Paths.get("src","main","resources","css","AuthSceneStyle","toastStyle.css").toUri().toString());
        authRoot.getChildren().add(contentPane);

        permanentRoot = new StackPane();
        permanentRoot.setPrefSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        permanentRoot.setMinSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        permanentRoot.setMaxSize(LOGIC_WIDTH, LOGIC_HEIGHT);

        contentContainer = new StackPane();
        permanentRoot.getChildren().add(contentContainer);

        rootGroup = new Group(permanentRoot);
        contentPane.getChildren().add(rootGroup);
        contentPane.getChildren().add(messageGroup);

        rootGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(()->{
                    double w = authScene.getWidth();
                    double h = authScene.getHeight();
                    if(w<=0||h<=0)return 1.0;
                    return Math.min(w/LOGIC_WIDTH,h/LOGIC_HEIGHT);
                }, authScene.widthProperty(), authScene.heightProperty())
        );
        rootGroup.scaleYProperty().bind(rootGroup.scaleXProperty());

    }

    public void showLoginScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType) {
        loginView = (LoginView) viewCache.computeIfAbsent("LOGIN", k -> new LoginView(loginCtrl));
        loginView.clearTextField();
        switchToContent(loginView, toastMessage, toastType);
    }
    public void showLoginScene(LoginCtrl loginCtrl) {
        showLoginScene(loginCtrl, null, null);
    }

    public void showAccountScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType) {
        accountView = (AccountView) viewCache.computeIfAbsent("ACCOUNT", k -> new AccountView(loginCtrl));
        accountView.syncUser();
        switchToContent(accountView, toastMessage, toastType);
    }
    public void showAccountScene(LoginCtrl loginCtrl) {
        showAccountScene(loginCtrl, null, null);
    }

    public void showRegisterScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType) {
        registerView = (RegisterView) viewCache.computeIfAbsent("REGISTER", k -> new RegisterView(loginCtrl));
        registerView.clearTextField();
        switchToContent(registerView, toastMessage, toastType);
    }
    public void showRegisterScene(LoginCtrl loginCtrl) {
        showRegisterScene(loginCtrl, null, null);
    }

    public void showLoadScene(LoginCtrl loginCtrl, String toastMessage, TOAST_TYPE toastType) {
        loadView = (LoadView) viewCache.computeIfAbsent("LOAD",k->new LoadView(loginCtrl, this));
        loadView.refreshTooltips();
        switchToContent(loadView, toastMessage, toastType);
    }
    public void showLoadScene(LoginCtrl loginCtrl){
        showLoadScene(loginCtrl,null,null);
    }

    private void switchToContent(StackPane newRoot, String toastMessage, TOAST_TYPE toastType) {
        contentContainer.getChildren().clear();
        contentContainer.getChildren().add(newRoot);
        currentRoot = newRoot;

        if (toastMessage != null && toastType != null) {
            showToast(toastMessage, toastType);
        }
        UIUtils.addHoverToAllButtons(newRoot);
    }




    /// Common toast showing function
    public void showToast(String message, TOAST_TYPE TYPE) {
        if (messagePane == null) return;
        if (currentToast != null) {
            messagePane.getChildren().remove(currentToast);
            currentToast = null;
        }

        HBox toastBox = new HBox();
        toastBox.setMouseTransparent(true);
        toastBox.getStyleClass().addAll("toast-box", TYPE.getCssClass());
        toastBox.setMaxHeight(Region.USE_PREF_SIZE);

        Label label = new Label(message);
        label.setId("toast-label");
        label.setWrapText(true);

        toastBox.getChildren().add(label);

        // 定位（逻辑坐标固定值）
        double slideDistance = SCREEN_WIDTH * TOAST_SLIDE_RATIO; // 480px
        double topOffset = SCREEN_HEIGHT * TOAST_TOP_OFFSET_RATIO; // 32px

        StackPane.setAlignment(toastBox, Pos.TOP_RIGHT);
        toastBox.setTranslateY(topOffset);
        toastBox.setTranslateX(slideDistance);

        // 添加到 messagePane
        messagePane.getChildren().add(toastBox);
        currentToast = toastBox;

        // 动画（完全不变）
        toastBox.setOpacity(0);
        toastBox.setScaleX(0.5);
        toastBox.setScaleY(0.5);

        Timeline popInTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(toastBox.translateXProperty(), slideDistance),
                        new KeyValue(toastBox.opacityProperty(), 0),
                        new KeyValue(toastBox.scaleXProperty(), 0.5),
                        new KeyValue(toastBox.scaleYProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_OVERSHOOT_MILLIS_RATIO),
                        new KeyValue(toastBox.translateXProperty(), 0),
                        new KeyValue(toastBox.opacityProperty(), 1),
                        new KeyValue(toastBox.scaleXProperty(), 1.05),
                        new KeyValue(toastBox.scaleYProperty(), 1.05)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_UNDERSHOOT_MILLIS_RATIO),
                        new KeyValue(toastBox.scaleXProperty(), 0.95),
                        new KeyValue(toastBox.scaleYProperty(), 0.95)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_SETTLE_MILLIS_RATIO),
                        new KeyValue(toastBox.scaleXProperty(), 1.0),
                        new KeyValue(toastBox.scaleYProperty(), 1.0)
                )
        );
        popInTimeline.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(TOAST_DISPLAY_SECONDS));
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(TOAST_ANIMATION_MILLIS), toastBox);
        slideOut.setFromX(0);
        slideOut.setToX(slideDistance);
        slideOut.setOnFinished(e -> {
            messagePane.getChildren().remove(toastBox);
            if (currentToast == toastBox) currentToast = null;
        });

        pause.setOnFinished(e -> slideOut.play());
        pause.play();
    }

    /**
     * 显示模态确认对话框（异步回调模式）
     * @param title 标题
     * @param message 消息内容
     * @param onConfirm 点击“确定”后的回调
     * @param onCancel 点击“取消”后的回调（可为 null）
     */
    public void showConfirmDialog(String title, String message, Runnable onConfirm, Runnable onCancel) {
        rootGroup.setEffect(new GaussianBlur());

        messagePane.setMouseTransparent(false);

        if(currentToast != null){
            messagePane.getChildren().remove(currentToast);
            currentToast = null;
        }

        // 内容面板（样式全部在 CSS 中）
        VBox contentBox = new VBox(20);
        contentBox.getStyleClass().add("confirm-box");
        contentBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setId("confirm-title");

        Label msgLabel = new Label(message);
        msgLabel.setId("confirm-message");
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");
        confirmBtn.getStyleClass().add("confirm-btn-confirm");
        cancelBtn.getStyleClass().add("confirm-btn-cancel");

        // 悬停效果由 CSS 的 :hover 处理，不需要 Java 代码

        HBox buttonBox = new HBox(30, confirmBtn);
        if(onCancel != null) buttonBox.getChildren().add(cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(titleLabel, msgLabel, buttonBox);

        // 覆盖层
        StackPane overlay = new StackPane();
        overlay.getChildren().addAll(contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        messagePane.getChildren().add(overlay);
        overlay.toFront();

        // 按钮事件
        confirmBtn.setOnAction(e -> {
            messagePane.getChildren().remove(overlay);
            messagePane.setMouseTransparent(true);
            rootGroup.setEffect(null);
            if (onConfirm != null) onConfirm.run();
        });
        if(onCancel != null){
            cancelBtn.setOnAction(e -> {
                messagePane.getChildren().remove(overlay);
                messagePane.setMouseTransparent(true);
                rootGroup.setEffect(null);
                onCancel.run();
            });
        }
    }


    public void refreshTooltips(){
        if(loadView != null)
            loadView.refreshTooltips();
    }

}