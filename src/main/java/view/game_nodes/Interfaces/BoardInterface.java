package view.game_nodes.Interfaces;

import controller.GameCtrl;
import model.entity.Crd;
import model.entity.LinkyMap;
import view.game_nodes.CellNode;

import java.util.ArrayList;

public interface BoardInterface {
    void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route);
    void showHint(Crd c1, Crd c2);
}
