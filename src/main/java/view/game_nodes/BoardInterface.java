package view.game_nodes;

import model.entity.Crd;
import java.util.ArrayList;

public interface BoardInterface {
    void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route);
}
