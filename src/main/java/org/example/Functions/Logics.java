package org.example.Functions;

import java.util.ArrayDeque;


public class Logics {
    public ArrayDeque<int[]> Linky(int MAPX, int MAPY, String map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = null;
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];

        // 共线检测
        if(p1x == p2x)
        {
            int one = (p1y - p2y > 0)? 1 : -1;
        }

        return path;
    }
}
