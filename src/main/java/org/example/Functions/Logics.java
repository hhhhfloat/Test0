package org.example.Functions;

import java.util.ArrayDeque;


public class Logics {

    /*
    * 一下一批是基础连连看的逻辑
    * 适用于矩形地图
    * 输入——地图大小，地图本身，选取的点坐标
    * 允许不合法点，会返回不合法值
    * Case1 -- 点不在地图上/选择同一个点/选择不同类型的点 -- 返回路径唯一点 (-1, -1)
    * Case2 -- 不存在合法路径 -- 返回空路径（已初始化，不是null）
    * Case3 -- 有合法路径 -- 返回完整路径的点坐标列表（顺序）
    */
    public ArrayDeque<int[]> Linky(int MAPX, int MAPY, char[][] map_, int[] p1, int[] p2)
    {
        // 先检测直线
        ArrayDeque<int[]> path = StraightPathDetect(MAPX, MAPY, map_, p1, p2);

        if(path.isEmpty()) // 如果没有直路
        {
            // 再横向检测
            path = RowDetect(MAPX, MAPY, map_, p1, p2);

            if(path.isEmpty()) // 如果没有横向检测得出的
            {
                // 对地图转置再传入，返回的路径再转置一次
                path = Tsp(RowDetect(MAPY, MAPX, Tsp(map_, MAPX, MAPY), Tsp(p1),Tsp(p2)));
            }
        }
        return path;


    }
    // 直线检测函数
    public ArrayDeque<int[]> StraightPathDetect(int MAPX, int MAPY, char[][] map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = new ArrayDeque<>();
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];
        // 同行
        if(p1x == p2x)
        {
            int one = (p1y - p2y > 0)?1:-1;
            for(int s = p2y + one; s != p1y; s += one)
            {
                if(map_[p1x][s] != ' ')return path;
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
                if(map_[s][p1y] != ' ')return path;
            }
            for(int s = p2x; s != p1x + one; s += one)
            {
                int[] p = {s, p1y};
                path.addLast(p);
            }
        }
        return path;
    }

    // 横向检测函数
    public ArrayDeque<int[]> RowDetect(int MAPX, int MAPY, char[][] map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = new ArrayDeque<>();
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];

        // 对p1检测横向自由度
        int t1 = 1;
        while(p1y-t1 >= 0 && map_[p1x][p1y - t1] == ' '){t1++;}
        int Min1 = p1y - t1 + 1;
        t1 = 1;
        while(p1y + t1 < MAPY && map_[p1x][p1y + t1] == ' '){t1++;}
        int Max1 = p1y + t1 - 1;

        // 对p2检测横向自由度
        int t2 = 1;
        while(p2y-t2 >= 0 && map_[p2x][p2y -t2] == ' '){t2++;}
        int Min2 = p2y - t2 + 1;
        t2 = 1;
        while(p2y + t2 < MAPY && map_[p2x][p2y + t2] == ' '){t2++;}
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
                    if(map_[s][i] != ' ')
                    {
                        msg = false;
                        break;
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
                        int[] p = {ss, i};
                        path.addLast(p);
                    }
                    // p2端横段
                    one = (i - p2y > 0)? 1:-1;
                    for(int ss = i - one; ss != p2y - one;ss -= one){
                        int[] p = {p2x, ss};
                        path.addFirst(p);
                    }
                    // p1端横段
                    one = (i - p1y > 0)? 1:-1;
                    for(int ss = i - one; ss != p1y - one; ss -= one)
                    {
                        int[] p = {p1x, ss};
                        path.addLast(p);
                    }
                    return path;
                }
            }
        }


        return path;
    }

    // 转置地图（拷贝）
    public char[][] Tsp(char[][] map_, int MAPX, int MAPY)
    {
        char[][] map_T = new char[MAPY][MAPX];
        for(int i = 0;i<MAPY;i++)
        {
            for (int j = 0; j < MAPX; j++) {
                map_T[i][j] = map_[j][i];
            }
        }
        return map_T;
    }

    // 转置点坐标
    public int[] Tsp(int[] p)
    {
        return new int[]{p[1], p[0]};
    }

    // 转置路径数组
    public ArrayDeque<int[]> Tsp(ArrayDeque<int[]> path)
    {
        if(!path.isEmpty()){
            for(int[] p : path)
            {
                int temp = p[0];
                p[0] = p[1];
                p[1] = temp;
            }
        }
        return path;
    }


    /*
    * 下面是检测完成函数，纯粹是检测是否盘面为空
    * 适用矩形地图
    */
    public boolean isComplete(int MAPX, int MAPY , char[][] map_)
    {

        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                if(map_[i][j] != ' ')return false;
            }
        }
        return true;
    }

    /*
    * 下面是检测是否还有可能连法并返回可能选择的函数
    * 仅仅适用原版连连看（虽然原版不可能没有）
    * 使用了与连连看消去路径搜索不一样的逻辑
    * 适用矩形地图
    * */
    public int[][] HintSolution()
    {


        return null;
    }


}
