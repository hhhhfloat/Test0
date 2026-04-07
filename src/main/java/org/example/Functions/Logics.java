package org.example.Functions;

import java.util.ArrayDeque;


public class Logics {
    public ArrayDeque<int[]> Linky(int MAPX, int MAPY, String map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = null;
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];
        path = StraightPathDetect(MAPX, MAPY, map_, p1, p2);
        if(path.isEmpty())
        {
            // 先横向检测
            path = RowDetect(MAPX, MAPY, map_, p1, p2);
            if(path.isEmpty())
            {
                // 对地图转置再传入
                path = Tsp(RowDetect(MAPY, MAPX, Tsp(map_, MAPX, MAPY), Tsp(p1),Tsp(p2)));
            }
            else
            {
                return path;
            }
        }
        else {
            return path;
        }



        return path;
    }
    public ArrayDeque<int[]> StraightPathDetect(int MAPX, int MAPY, String map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = null;
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];
        // 同行
        if(p1x == p2x)
        {
            int one = (p1y - p2y > 0)?1:-1;
            for(int s = p2y + one; s != p1y; s += one)
            {
                if(map_.charAt(p1x * MAPY + s) != ' ')return path;
            }
            for(int s = p2y; s != p1y + one; s += one)
            {
                int[] p = {p1x , s};
                path.addLast(p);
            }
        }
        if(p1y == p2y)
        {
            int one = (p1x - p2x > 0) ? 1:-1;
            for(int s = p2x + one; s != p1x; s += one)
            {
                if(map_.charAt(s*MAPX + p1y) != ' ')return path;
            }
            for(int s = p2x; s != p1x + one; s += one)
            {
                int[] p = {s, p1y};
                path.addLast(p);
            }
        }
        return path;
    }

    public ArrayDeque<int[]> RowDetect(int MAPX, int MAPY, String map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = null;
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];

        // 对p1检测横向自由度
        int t1 = 1;
        while(p1y-t1 >= 0 && map_.charAt(p1x * MAPY + p1y -t1) == ' '){t1++;}
        int Min1 = p1y - t1 + 1;
        t1 = 1;
        while(p1y + t1 < MAPY && map_.charAt(p1x * MAPY + p1y + t1) == ' '){t1++;}
        int Max1 = p1y + t1 - 1;

        // 对p2检测横向自由度
        int t2 = 1;
        while(p2y-t2 >= 0 && map_.charAt(p2x * MAPY + p2y -t2) == ' '){t2++;}
        int Min2 = p2y - t2 + 1;
        t2 = 1;
        while(p2y + t2 < MAPY && map_.charAt(p2x * MAPY + p2y + t2) == ' '){t2++;}
        int Max2 = p2y + t2 - 1;

        // 寻找重合部分并按列检测

        boolean msg = true;
        for (int i = Min1; i <= Max1; i++) {
            if(i >= Min2 && i <= Max2)
            {
                // 重合部分
                // 找到一个就可以了
                // 设定循环方向；从p2 -> p1
                int one = (p1x - p2x > 0)? 1:-1;
                int s = p2x + one;
                msg = true;
                while(s != p1x)
                {
                    if(map_.charAt(s * MAPY + i) != ' ')
                    {
                        msg = false;
                    }
                    s += one;
                }
                if(msg)
                {
                    // 写出要返回的路径
                    // 从p1出发，记录经过的点

                    // 先写入中间列
                    for(int ss = p2x;ss != p1x + one;ss+=one)
                    {
                        int[] p = {s, i};
                        path.addLast(p);
                    }
                    // p2端横段
                    one = (i - p2y > 0)? 1:-1;
                    for(int ss = i - one; s != p2y - one;s -= one){
                        int[] p = {p2x, s};
                        path.addLast(p);
                    }
                    // p1端横段
                    one = (i - p2y > 0)? 1:-1;
                    for(int ss = i - one; ss != p2y - one; ss -= one)
                    {
                        int[] p = {p1x, s};
                        path.addLast(p);
                    }
                }
            }
        }


        return path;
    }

    public String Tsp(String map_, int MAPX, int MAPY)
    {
        String map_T = "";
        for(int i = 0;i<MAPY;i++)
        {
            for (int j = 0; j < MAPX; j++) {
                map_T += map_.charAt(j * MAPY + i);
            }
        }
        return map_T;
    }

    public int[] Tsp(int[] p)
    {
        return new int[]{p[1], p[0]};
    }

    public ArrayDeque<int[]> Tsp(ArrayDeque<int[]> path)
    {
        if(path.isEmpty())return null;
        else{
            for(int[] p : path)
            {
                p = Tsp(p);
            }
            return path;
        }
    }
}
