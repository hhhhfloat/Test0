package GamePage;

import Model.entity.LinkyMap;

public class Board {
    LinkyMap linkyMap = new LinkyMap(10,10,10);
    int[][] board = linkyMap.getMap();

    public Board() throws InterruptedException {
    }
}
