package before.view.game_nodes.Interfaces;

import before.model.entity.Crd;
import before.view.game_nodes.CellNode;

import java.util.ArrayList;

public interface BoardInterface {
    void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route);
    void showHint(Crd c1, Crd c2);
}
