package modifying.auth.view.sceneRoots;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.auth.controller.AuthSceneCtrl;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.dao.LoadDao;
import modifying.auth.view.tools.AuthToolTip;
import modifying.shared.model.TOAST_TYPE;

import java.nio.file.Paths;
import java.util.ArrayList;

public class LoadView extends StackPane {

    private static final int totLoadNumber = LoginCtrl.getTotLoadNumber();

    private LoginCtrl loginCtrl;
    private VBox loadBox;
    private final LoadDao loadDao;

    private final ArrayList<Button> saveButtons = new ArrayList<>();
    private final ArrayList<AuthToolTip> tooltips = new ArrayList<>();

    private String corruptedSave = "";
    private final AuthSceneCtrl authSceneCtrl;

    public LoadView(LoginCtrl loginCtrl, AuthSceneCtrl authSceneCtrl) {
        this.loginCtrl = loginCtrl;
        this.authSceneCtrl = authSceneCtrl;
        this.loadDao = loginCtrl.getLoadDao();

        loadBox = new VBox(15);
        loadBox.setAlignment(Pos.CENTER);

        initLoadBox();

        getChildren().add(loadBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "AuthSceneStyle", "authSceneStyle.css").toUri().toString());
    }

    private void initLoadBox() {
        String safeName = loginCtrl.getAccount().getSafeUserName();

        for (int i = 1; i <= totLoadNumber; i++) {
            Button save = new Button("Load "+i);
            Button delete = new Button();
            saveButtons.addLast(save);

            save.getStyleClass().add("action-button");
            delete.getStyleClass().add("cross-button");

            final int k = i;
            save.setOnAction(e->loginCtrl.handleLoad(k));
            delete.setOnAction(e->loginCtrl.handleDelete(k));

            HBox load = new HBox(15,save,delete);
            loadBox.getChildren().add(load);

            AuthToolTip tooltip = new AuthToolTip(this);
            tooltips.add(tooltip);

            String tooltipText = generateTooltipText(safeName, k);
            tooltip.install(save, tooltipText);
        }
        if(!corruptedSave.isEmpty()){
            authSceneCtrl.showToast(
                    "Load " + corruptedSave + "corrupted\nClick them to rescue"
                    , TOAST_TYPE.ERROR);
        }
        Button back = new Button("Back");
        back.setOnAction(event -> loginCtrl.handleBackToAccount());
        back.getStyleClass().add("action-button");
        loadBox.getChildren().add(back);
    }
    private String generateTooltipText(String safeName, int loadNumber){
        LoadDao.ValidationResult result = loadDao.validateSave(safeName, loadNumber);

        return switch (result) {
            case VALID -> loadDao.getSaveMetadata(safeName, loadNumber).map(
                    meta -> "🕝: " + meta.playTime() +
                            "\n⭐: " + meta.score()).orElse("INVALID SAVE DATA");
            case NOT_FOUND -> "EMPTY";
            case INVALID, CORRUPTED -> {
                corruptedSave += loadNumber + " ";
                yield "CORRUPTED\nclick to rescue";
            }
            default -> "U N K N O W N";
        };
    }

    public void refreshTooltips() {
        String safeName = loginCtrl.getAccount().getSafeUserName();
        for (int i = 0; i < saveButtons.size(); i++) {
            int loadNumber = i + 1;
            String newText = generateTooltipText(safeName, loadNumber);
            tooltips.get(i).updateText(newText);
        }
    }

}
