package view.game_nodes;

import controller.GameCtrl;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.entity.Crd;
import model.entity.LinkyMap;
import java.util.ArrayList;

import javafx.animation.*;
import javafx.scene.layout.*;


public class Board extends StackPane {
    private final GridPane gridPane;
    private final Pane lineLayer;
    private final double size;
    private final double hgap = 10;
    private final double vgap = 10;

    public Board(int row, int col, double size, LinkyMap linkyMap, GameCtrl gameCtrl) {
        this.size = size;

        gridPane = new GridPane();
        lineLayer = new Pane();

        gridPane.setHgap(hgap);
        gridPane.setVgap(vgap);

        getChildren().addAll(gridPane, lineLayer);

        for (int i = 0; i < row; i++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setMinHeight(size);
            rowConstraint.setPrefHeight(size);
            rowConstraint.setMaxHeight(size);
            gridPane.getRowConstraints().add(rowConstraint);
            for (int j = 0; j < col; j++) {
                ColumnConstraints colConstraint = new ColumnConstraints();
                colConstraint.setMinWidth(size);
                colConstraint.setPrefWidth(size);
                colConstraint.setMaxWidth(size);
                gridPane.getColumnConstraints().add(colConstraint);
                CellNode cellNode = new CellNode(i, j, size, linkyMap.getMap()[i][j], gameCtrl);
                gridPane.add(cellNode, col, row);
            }
        }
    }

    private Line createLine(Crd from, Crd to) {
        double startX = from.y() * (size + hgap) + size / 2;
        double startY = from.x() * (size + vgap) + size / 2;
        double endX = to.y() * (size + hgap) + size / 2;
        double endY = to.x() * (size + vgap) + size / 2;

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.YELLOW);
        line.setStrokeWidth(10);
        line.setEffect(new DropShadow(10, Color.YELLOW));

        return line;
    }

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
