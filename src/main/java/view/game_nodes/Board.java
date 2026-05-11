package view.game_nodes;

import controller.GameCtrl;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.entity.Crd;
import model.entity.LinkyMap;
import java.util.ArrayList;

import javafx.animation.*;
import javafx.scene.layout.*;


public class Board extends Pane implements BoardInterface{
    private StackPane grid;
    private Pane lineLayer;
    private final double size;
    private final double hgap = 10;
    private final double vgap = 10;

    public Board(int row, int col, double size, LinkyMap linkyMap, GameCtrl gameCtrl) {
        this.size = size;


        lineLayer = new Pane();
        lineLayer.setMouseTransparent(true);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                CellNode cellNode = new CellNode(i, j, size, linkyMap.getMap()[i][j], gameCtrl);
                cellNode.setLayoutX(i*(size+hgap));
                cellNode.setLayoutY(j*(size+hgap));
                getChildren().add(cellNode);

            }
        }

        getChildren().add(lineLayer);
    }

    private Line createLine(Crd from, Crd to) {
        double startX = from.x() * (size + hgap) + size / 2;
        double startY = from.y() * (size + vgap) + size / 2;
        double endX = to.x() * (size + hgap) + size / 2;
        double endY = to.y() * (size + vgap) + size / 2;

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.YELLOW);
        line.setStrokeWidth(10);
        line.setEffect(new DropShadow(10, Color.YELLOW));

        return line;
    }

    @Override
    public void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route) {
        Group lineGroup = new Group();
        for (int i = 0; i < route.size() - 1; i++) {
            Line line = createLine(route.get(i), route.get(i + 1));
            line.setStroke(Color.YELLOW);
            line.setStrokeWidth(10);
            line.setEffect(new DropShadow(10, Color.YELLOW));

            lineGroup.getChildren().add(line);
        }
        lineLayer.getChildren().add(lineGroup);
        lineGroup.toFront();

        lineGroup.setOpacity(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lineGroup.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(150), new KeyValue(lineGroup.opacityProperty(), 1))
        );
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.millis(300));
        delay.setOnFinished(e -> {
            cellNode1.eliminateCell();
            cellNode2.eliminateCell();
            lineLayer.getChildren().remove(lineGroup);
        });
        delay.play();
    }
}
