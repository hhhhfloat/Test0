package view.game_nodes;

import controller.GameCtrl;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.entity.Crd;
import model.entity.LinkyMap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.*;
import view.game_nodes.Interfaces.BoardInterface;


public class Board extends Pane implements BoardInterface {
    private final Pane lineLayer;
    private final double size;
    private final double gap = 9.7;

    public Board(int row, int col, double size, LinkyMap linkyMap, GameCtrl gameCtrl) {
        Random r = new Random();
        this.size = size;

        Path cssPath = Paths.get("src", "main", "resources", "css", "board.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);

        lineLayer = new Pane();
        lineLayer.setMouseTransparent(true);

        int imgSet = r.nextInt(0,2);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                CellNode cellNode = new CellNode(i, j, size, linkyMap.getMap()[i][j], gameCtrl,imgSet);
                cellNode.setLayoutX(i*(size+gap));
                cellNode.setLayoutY(j*(size+gap));
                getChildren().add(cellNode);
            }
        }

        getChildren().add(lineLayer);
    }

    private Line createLine(Crd from, Crd to) {
        double startX = from.x() * (size + gap) + size / 2;
        double startY = from.y() * (size + gap) + size / 2;
        double endX = to.x() * (size + gap) + size / 2;
        double endY = to.y() * (size + gap) + size / 2;

        return new Line(startX, startY, endX, endY);
    }

    @Override
    public void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route) {
        Group lineGroup = new Group();
        for (int i = 0; i < route.size() - 1; i++) {
            Line line = createLine(route.get(i), route.get(i + 1));
            lineGroup.getChildren().add(line);
        }
        lineLayer.getChildren().add(lineGroup);
        lineGroup.toFront();

        lineGroup.setOpacity(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lineGroup.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(50), new KeyValue(lineGroup.opacityProperty(), 1))
        );
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.millis(150));
        delay.setOnFinished(e -> {
            cellNode1.eliminateCell();
            cellNode2.eliminateCell();
            lineLayer.getChildren().remove(lineGroup);
        });
        delay.play();
    }

    @Override
    public void showHint(Crd c1, Crd c2){

    }
}
