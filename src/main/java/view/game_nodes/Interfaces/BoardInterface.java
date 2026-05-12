package view.game_nodes.Interfaces;

import model.entity.Crd;
import view.game_nodes.CellNode;

import java.util.ArrayList;

public interface BoardInterface {
    void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route);
}
