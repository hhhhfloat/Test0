package modifying.auth.view.sceneRoots;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

import java.nio.file.Paths;
import java.util.ArrayList;

public class LoadView extends StackPane {

    private static final int totLoadNumber = 3;

    private LoginCtrl loginCtrl;
    private VBox loadBox;
    public LoadView(LoginCtrl loginCtrl){
        this.loginCtrl = loginCtrl;

        loadBox = new VBox(15);
        loadBox.setAlignment(Pos.CENTER);
        initLoadBox();
        getChildren().add(loadBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "AuthSceneStyle", "authSceneStyle.css").toUri().toString());
    }

    private void initLoadBox() {

        ArrayList<Button> saves = new ArrayList<>();
        ArrayList<Button> deletes = new ArrayList<>();
        ArrayList<HBox> loads = new ArrayList<>();
        saves.add(null);
        deletes.add(null);
        loads.add(null);
        for (int i = 1; i <= totLoadNumber; i++) {
            Button save = new Button("Load "+i);
            Button delete = new Button();
            saves.addLast(save);
            deletes.addLast(delete);

            final int k = i;
            save.setOnAction(e->loginCtrl.handleLoad(k));
            delete.setOnAction(e->loginCtrl.handleLoadDelete(k));

            save.getStyleClass().add("action-button");
            delete.getStyleClass().add("cross");

            HBox load = new HBox(15,save,delete);
            loads.add(load);
        }

        Button back = new Button("Back");
        back.setOnAction(event -> loginCtrl.showAccountScene());
        back.getStyleClass().add("action-button");


        loadBox.getChildren().addAll(loads);
        loadBox.getChildren().add(back);
    }
}
