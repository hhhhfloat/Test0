package org.example.Functions;

import java.text.spi.NumberFormatProvider;
import java.util.ArrayDeque;


public class Logics
{

    /*
    * 整体说明——矩形地图，MAPX-行数，MAPY-列数
    * 方向 0上 1右 2下 3左
    * 非负数代表不同种类块
    * -1是空格
    * */

    /*
    * 以下一批是基础连连看的逻辑
    * 适用于矩形地图
    * 输入——地图大小，地图本身，选取的点坐标
    * 允许不合法点，会返回不合法值
    * Case1 -- 点不在地图上/选择同一个点/选择不同类型的点 -- 返回路径唯一点 (-1, -1)
    * Case2 -- 不存在合法路径 -- 返回空路径（已初始化，不是null）
    * Case3 -- 有合法路径 -- 返回完整路径的点坐标列表（顺序）
    */
    public ArrayDeque<int[]> Linky(int MAPX, int MAPY, int[][] map_, int[] p1, int[] p2)
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
    public ArrayDeque<int[]> StraightPathDetect(int MAPX, int MAPY, int[][] map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = new ArrayDeque<>();
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];
        // 同行
        if(p1x == p2x)
        {
            int one = (p1y - p2y > 0)?1:-1;
            for(int s = p2y + one; s != p1y; s += one)
            {
                if(map_[p1x][s] != -1)return path;
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
                if(map_[s][p1y] != -1)return path;
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
    public ArrayDeque<int[]> RowDetect(int MAPX, int MAPY, int[][] map_, int[] p1, int[] p2)
    {
        ArrayDeque<int[]> path = new ArrayDeque<>();
        int p1x = p1[0], p1y = p1[1], p2x = p2[0], p2y = p2[1];

        // 对p1检测横向自由度
        int t1 = 1;
        while(p1y-t1 >= 0 && map_[p1x][p1y - t1] == -1){t1++;}
        int Min1 = p1y - t1 + 1;
        t1 = 1;
        while(p1y + t1 < MAPY && map_[p1x][p1y + t1] == -1){t1++;}
        int Max1 = p1y + t1 - 1;

        // 对p2检测横向自由度
        int t2 = 1;
        while(p2y-t2 >= 0 && map_[p2x][p2y -t2] == -1){t2++;}
        int Min2 = p2y - t2 + 1;
        t2 = 1;
        while(p2y + t2 < MAPY && map_[p2x][p2y + t2] == -1){t2++;}
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
                    if(map_[s][i] != -1)
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
    public int[][] Tsp(int[][] map_, int MAPX, int MAPY)
    {
        int[][] map_T = new int[MAPY][MAPX];
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
    public boolean isComplete(int MAPX, int MAPY , int[][] map_)
    {

        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                if(map_[i][j] != -1)return false;
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
    public int[][] HintSolution(int MAPX, int MAPY, int[][] map)
    {
        // 最高效的搜索方式是什么？
        /*
        * 给每一个方格定义一个四元数组  NumMap[MAPX][MAPY][4][2]，其中 NumMap[x][y] = {{value, distance},{...}, ..., ...}
        * 记录其向上下左右分别能看到的数字（代表种类）
        * 同时记录距离（走几步可以到）
        * Part1——最高效的记录四元组的方法
        *   对每一个非空元素，让它向上下左右走，遇到第一个非空格停下（包括这个非空格）
        *   每走一步都在走到的格子记录自己的值和当前步数
        *   这样没有重复运算，已经最简
        * Part2——记录过程中的筛选
        *   1) 如果终点格子的数和自己一样，直接筛掉直线
        *   2) 先用竖向走筛一遍。在横向走的时候，如果遇到一个空格子，它的侧边（必须是侧边，不是同向）有相等记录，则筛掉单拐点
        * Part3——双拐点筛选
        *   先按行枚举
        *       从左到右枚举空格：
        *           枚举到一个空格，开启一个双层循环，以右侧步数(k)为最大值，枚举 0 ≤ i<j ≤ k-1 。（k等于1直接跳过）
        *               如果有侧边（侧边！）有相同数字的对，就说明找到了
        *           跑完这个双层循环，将循环记号 y 更新为 y + k（已经被枚举过了）
        *   转置，按列枚举
        *
        * 最终为了代码简短，选择了在四元组全部记录完成后再筛选单拐点情形
        *
        * 你学会了吗？
        * */

        int[][][][] NumMap = new int[MAPX][MAPY][4][2];

        // 四个方向 0上 1右 2下 3左
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // 扫描四元组以及直线
        // 按非零格枚举，直接四个方向都一起
        for (int x = 0; x < MAPX; x++)
        {
            for (int y = 0; y < MAPY; y++)
            {
                if(map[x][y] != -1) // 仅枚举非零格
                {
                    int val = map[x][y];
                    for(int i = 0;i<4;i++)
                    {
                        int t0 = 1;
                        int dx = dir[i][0], dy = dir[i][1];
                        while ((x + t0*dx != MAPX && x + t0*dx != -1) && (y + t0*dy != MAPY && y + t0*dy != -1))
                        {// 判断下标不在地图外
                            NumMap[x + t0*dx][y + t0*dy][(i+2)%4][0] = val;
                            NumMap[x + t0*dx][y + t0*dy][(i+2)%4][1] = t0;
                            // 走到终点
                            if(map[x + t0*dx][y + t0*dy] != -1)
                            {
                                // 检测直线
                                if(map[x + t0*dx][y + t0*dy] == val)
                                    return new int[][] {{x+t0*dx, y+t0*dy}, {x, y}};
                                break;
                            }
                            t0++;
                        }
                    }
                }
            }
        }
        // 理论上此时我们有一个完整的数据地图NumMap[][][4][2]，外周尚未给与特殊照顾
        for (int i = 0; i < MAPY; i++) {
            NumMap[0][i][0][0] = -1;
            NumMap[MAPX-1][i][2][0] = -1;
        }
        for (int i = 0; i < MAPX; i++) {
            NumMap[i][0][3][0] = -1;
            NumMap[i][MAPY-1][1][0] = -1;
        }

        // 单拐点分析
        for (int x = 0; x < MAPX; x++) {
            for (int y = 0; y < MAPY; y++) {
                if(map[x][y] == -1) // 仅看空格
                {
                    for (int i = 0; i < 4; i++) {
                        int dx = dir[i][0], dy = dir[i][1];
                        int val = NumMap[x][y][i][0];
                        if(val!=-1 && val == NumMap[x][y][(i+1)%4][0]) {
                            return new int[][]
                            {
                                {x + dx * NumMap[x][y][i][1], y + dy * NumMap[x][y][i][1]},
                                {x + dx * NumMap[x][y][(i + 1)%4][1], y + dy * NumMap[x][y][(i + 1)%4][1]}
                            };
                        }
                    }
                }
            }
        }

        // 双拐点分析

        int[][] ans1 = RowFindPath(MAPX,MAPY,map,NumMap);
        int[][] ans2;
        if(ans1 == null)
        {
            ans2 = Tsp(RowFindPath(MAPY,MAPX,Tsp(map,MAPX,MAPY),Tsp(MAPX,MAPY,NumMap)));
            return ans2;
        }
        else return ans1;
    }
    // NumMap用的转置函数
    public int[][][][] Tsp(int MAPX, int MAPY, int[][][][] NumMap)
    {
        int[][][][] nt = new int[MAPY][MAPX][4][2];
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                for (int k = 0; k < 4; k++) {
                    nt[j][i][3-k][0] = nt[i][j][k][0];
                    nt[j][i][3-k][1] = nt[i][j][k][1];
                }
            }
        }
        return nt;
    }
    // ans 用的转置函数
    public int[][] Tsp(int[][] ans)
    {
        return new int[][]{{ans[0][1],ans[0][0]},{ans[1][1],ans[1][0]}};
    }
    // 双拐点横向检测
    public int[][] RowFindPath(int MAPX, int MAPY, int[][]map, int[][][][] NumMap)
    {
        /*
        * Part3——双拐点筛选
         *   先按行枚举
         *       从左到右枚举空格：
         *           枚举到一个空格，开启一个双层循环，以右侧步数(k)为最大值，枚举 0 ≤ i<j ≤ k-1 。（k等于1直接跳过）
         *               如果有侧边（侧边！）有相同数字的对，就说明找到了
         *           跑完这个双层循环，将循环记号 y 更新为 y + k（已经被枚举过了）
         *   转置，按列枚举
        * */
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        for (int x = 0; x <MAPX; x++) {
            for (int y = 0; y < MAPY; y++) {
                if(map[x][y] == -1 && NumMap[x][y][1][0] >= 2)
                {
                    int k = NumMap[x][y][1][0];
                    for (int i = 0; i < k - 1; i++) {
                        for (int j = i+1; j < k; j++) {
                            int[] mmm = {0,2};
                            for(int z : mmm) {
                                for(int w:mmm) {
                                    if(NumMap[x][y+i][z][0] == NumMap[x][y+j][w][0]){
                                        return new int[][]
                                        {
                                                {x + dir[z][0]*NumMap[x][y+i][z][1], y + i},
                                                {x + dir[w][0]*NumMap[x][y+j][w][1], y + j}
                                        };
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        return null;
    }
}
