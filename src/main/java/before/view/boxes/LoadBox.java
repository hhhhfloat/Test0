package before.view.boxes;

import before.controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class LoadBox extends VBox{
    private final int totLoadNumber = 3;
    public LoadBox(LoginCtrl loginCtrl) {
        super(20);
        Button save1 = new Button("Load 1"), save2 = new Button("Load 2"), save3 = new Button("Load 3"), delete1 = new Button(), delete2 = new Button(), delete3 = new Button(), back = new Button("Back");

        List<Button> saves = Arrays.asList(save1, save2, save3);
        List<Button> deletes = Arrays.asList(delete1, delete2,delete3);

        for (int i = 1; i <= totLoadNumber; i++) {
            Button save = saves.get(i-1);
            Button delete = deletes.get(i-1);
            final int k = i;
            save.setOnAction(e->loginCtrl.handleLoad(k));
            delete.setOnAction(e->loginCtrl.handleLoadDelete(k));
            save.getStyleClass().add("load");
            delete.getStyleClass().add("cross");
        }
        back.setOnAction(event -> loginCtrl.showAccountScene());
        back.getStyleClass().add("back");
        HBox load1 = new HBox(20, save1, delete1), load2 = new HBox(20, save2, delete2), load3 = new HBox(20, save3, delete3);
        List<HBox> loads = Arrays.asList(load1, load2, load3);
        getChildren().addAll(loads);
        getChildren().add(back);
    }
}
