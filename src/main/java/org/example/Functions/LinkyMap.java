package org.example.Functions;

import java.util.ArrayDeque;

public class LinkyMap {

    // 四个方向 0上 1右 2下 3左
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int MAPX_, MAPY_;
    private int[][] map;
    private int[][][][] NumMap;
    LinkyMap(int MAPX, int MAPY)
    {
        map = new int[MAPX][MAPY];
        //随机生成初始地图

        // 生成数表

    }

    // 生成数表，不筛直线（哭）
    public void initNumMap()
    {
        // 扫描四元组
        // 按非零格枚举，直接四个方向都一起
        // 这次不扫直线
        for (int x = 0; x < MAPX_; x++)
        {
            for (int y = 0; y < MAPY_; y++)
            {
                if(map[x][y] != -1) // 仅枚举非零格
                {
                    int val = map[x][y];
                    for(int i = 0;i<4;i++)
                    {// 枚举四个方向
                        int t0 = 1;
                        int dx = dir[i][0], dy = dir[i][1];
                        while ((x + t0*dx != MAPX_ && x + t0*dx != -1) && (y + t0*dy != MAPY_ && y + t0*dy != -1))
                        {// 判断下标不在地图外
                            NumMap[x + t0*dx][y + t0*dy][(i+2)%4][0] = val;
                            NumMap[x + t0*dx][y + t0*dy][(i+2)%4][1] = t0;
                            // 走到终点
                            if(map[x + t0*dx][y + t0*dy] != -1)
                            {
                                break;
                            }
                            t0++;
                        }
                    }
                }
            }
        }
        // 理论上此时我们有一个完整的数据地图NumMap[][][4][2]，外周尚未给与特殊照顾
        // 需要从边缘上的空格向内扫出一些不可用的值 (用-1标记，但长度仍要记！！！)
        for (int i = 0; i < MAPY_; i++) {
            // 顶行
            if(map[0][i] == -1)
            {
                int t0 = 0;
                while (t0 != MAPX_)
                {// 判断下标不在地图外
                    NumMap[t0][i][0][0] = -1;
                    NumMap[t0][i][0][1] = t0 + 1;
                    // 走到终点
                    if(map[t0][i] != -1)break;

                    t0++;
                }
            }
            // 底行
            if(map[MAPX_-1][i] == -1)
            {
                int t0 = MAPX_ - 1;
                while(t0 != -1) {
                    NumMap[t0][i][2][0] = -1;
                    NumMap[t0][i][2][1] = MAPX_ - t0;
                    if(map[t0][i] != -1)break;
                    t0--;
                }
            }
            // 左列
            if(map[i][0] == -1)
            {
                int t0 = 0;
                while (t0 != MAPX_)
                {// 判断下标不在地图外
                    NumMap[i][t0][3][0] = -1;
                    NumMap[i][t0][3][1] = t0 + 1;
                    // 走到终点
                    if(map[i][t0] != -1)break;
                    t0++;
                }
            }
            // 右列
            if(map[i][MAPX_-1] == -1)
            {
                int t0 = MAPY_ - 1;
                while(t0 != -1) {
                    NumMap[i][t0][1][0] = -1;
                    NumMap[i][t0][1][1] = MAPY_ - t0;
                    if(map[i][t0] != -1)break;
                    t0--;
                }
            }
        }

        // 数表生成完成
    }

    // 消去后更新数表（给定消去的非零点）
    public void delNumMap(ArrayDeque<int[]>points, int[][][][]NumMap, int[][]map)
    {

        for(int[]p:points)
        {
            int x = p[0],y = p[1];
            //延申原先方向的（不知道如何避免重复）
            for (int i = 0; i < 4; i++) {
                int t0 = 1;
                int dx = dir[i][0],dy = dir[i][1];
                while(x+t0*dx<MAPX_ && x+t0*dx>=0 && y+t0*dy<MAPY_ && y+t0*dy>=0)
                {

                }
            }
        }
    }

    // 自动寻找路径
    public ArrayDeque<int[]> autoFindPath(int[][][][]NumMap, int[][]map)
    {
        // 全部一次性枚举！
        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                if(map[x][y]!=-1)    // 非空格，找直线
                {
                    for (int i = 0; i < 4; i++) {
                        if(map[x][y] == NumMap[x][y][i][0]) //因为map[x][y]不是-1，不用考虑空条
                        {
                            ArrayDeque<int[]> path = new ArrayDeque<>();
                            int dx = dir[i][0], dy = dir[i][1];
                            for(int z = 0; z <= NumMap[x][y][i][1]; z++)
                            {
                                path.addLast(new int[]{x+dx*z,y+dy*z});
                            }
                            return path;
                        }
                    }
                }
                else // 空格，枚举同行拐点！
                {
                    // 不能复用双拐点检测代码了（哭）
                    /*
                    for (int i = 0; i < 4; i++) {
                        if(NumMap[x][y][i][0] == NumMap[x][y][(i+1)%4][0])
                        {
                            ArrayDeque<int[]> path = new ArrayDeque<>();
                            int k1 = NumMap[x][y][i][1], k2 = NumMap[x][y][(i+1)%4][1]; // 记录两个方向的步数
                            for(int z = 0; z <= k1; z++)
                            {
                                path.addFirst(new int[]{x + dir[i][0] * z, y + dir[i][1] * z});
                            }
                            // 使用新方向
                            int t = (i+1)%4;
                            for(int z = 1; z <= k2; z++)
                            {
                                path.addLast(new int[]{x + dir[t][0] * z, y + dir[t][1] * z});
                            }
                            return path;
                        }
                    }

                     */
                    ArrayDeque<int[]> path = new ArrayDeque<>();
                    // 取右侧还有空格的空格
                    int k = NumMap[x][y][1][1];
                    // 枚举步数内的所有空格对
                    for (int i = 0; i < k - 1; i++) {
                        // 既然在枚举空格，那就干脆把单拐点也做掉！
                        path = OneTwiPath(x,y+i);
                        if(!path.isEmpty())return path;
                        // 此时再来枚举其余右侧空格，组成空格对
                        for (int j = i+1; j < k; j++) {
                            int[] mmm = {0,2}; // 枚举空格对中每一个的上/下数字
                            for(int z : mmm) {
                                for(int w:mmm) {
                                    if(NumMap[x][y+i][z][0] != -1 && NumMap[x][y+i][z][0] == NumMap[x][y+j][w][0]){
                                        // 先加中间
                                        for(int poi = i; poi <= j; poi++){
                                            path.addLast(new int[]{x,y+poi});
                                        }
                                        // i一侧
                                        for(int poi = 1; poi <= NumMap[x][y+i][z][1]; poi ++) {
                                            path.addFirst(new int[]{x+poi*dir[z][0], y+i});
                                        }
                                        // j一侧
                                        for(int poi = 1; poi <= NumMap[x][y+j][w][1]; poi ++) {
                                            path.addLast(new int[]{x+poi*dir[w][0], y+j});
                                        }
                                        return path;
                                    }
                                }
                            }
                        }
                    }
                    // 别忘了最后这一个单拐点还没检测
                    path = OneTwiPath(x,y+k);
                    if(!path.isEmpty())return path;

                    y+=k;
                }
            }
        }

        // 纵向双拐点分析，是的原本的函数可以这样用
        return Tsp(rowTwoTwi(Tsp(NumMap),Tsp(map)));
    }
    // 自动单拐点路径返回（已经找到合法连法）
    ArrayDeque<int[]> OneTwiPath(int x, int y)
    {
        // 四个方向 0上 1右 2下 3左
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        ArrayDeque<int[]>path = new ArrayDeque<>();
        for (int d = 0; d < 4; d++) {
            if(NumMap[x][y][d][0] == NumMap[x][y][(d+1)%4][0])
            {
                int k1 = NumMap[x][y][d][1], k2 = NumMap[x][y][(d+1)%4][1]; // 记录两个方向的步数
                for(int z = 0; z <= k1; z++)
                {
                    path.addFirst(new int[]{x + dir[d][0] * z, y + dir[d][1] * z});
                }
                // 使用新方向
                int t = (d+1)%4;
                for(int z = 1; z <= k2; z++)
                {
                    path.addLast(new int[]{x + dir[t][0] * z, y + dir[t][1] * z});
                }
                return path;
            }
        }
        return path;
    }
    // 横向自动寻找双拐点路径，已经发挥最大作用
    ArrayDeque<int[]> rowTwoTwi(int[][][][]NumMap_,int[][]map_)
    {
        // 四个方向 0上 1右 2下 3左
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        int MAPX = map_.length;
        int MAPY = map_[0].length;
        ArrayDeque<int[]> path = new ArrayDeque<>();
        for (int x = 0; x <MAPX; x++) {
            for (int y = 0; y < MAPY; y++) {
                if(map_[x][y] == -1 && NumMap_[x][y][1][1] >= 2)
                {
                    // 取右侧还有空格的空格
                    int k = NumMap_[x][y][1][1]; // 枚举步数内的所有空格对
                    for (int i = 0; i < k - 1; i++) {
                        for (int j = i+1; j < k; j++) {
                            int[] mmm = {0,2}; // 枚举空格对中每一个的上/下数字
                            for(int z : mmm) {
                                for(int w:mmm) {
                                    if(NumMap_[x][y+i][z][0] != -1 && NumMap_[x][y+i][z][0] == NumMap_[x][y+j][w][0]){
                                        // 先加中间
                                        for(int poi = i; poi <= j; poi++){
                                            path.addLast(new int[]{x,y+poi});
                                        }
                                        // i一侧
                                        for(int poi = 1; poi <= NumMap_[x][y+i][z][1]; poi ++)
                                        {
                                            path.addFirst(new int[]{x+poi*dir[z][0], y+i});
                                        }
                                        // j一侧
                                        for(int poi = 1; poi <= NumMap_[x][y+j][w][1]; poi ++)
                                        {
                                            path.addLast(new int[]{x+poi*dir[w][0], y+j});
                                        }
                                        return path;
                                    }
                                }
                            }
                        }
                    }
                    y+=k;
                }
            }
        }
        return path;
    }
    // NumMap用的转置函数
    public int[][][][] Tsp(int[][][][] Nm)
    {
        int[][][][] nt = new int[MAPY_][MAPX_][4][2];
        for (int i = 0; i < MAPX_; i++) {
            for (int j = 0; j < MAPY_; j++) {
                for (int k = 0; k < 4; k++) {
                    nt[j][i][3-k][0] = Nm[i][j][k][0];
                    nt[j][i][3-k][1] = Nm[i][j][k][1];
                }
            }
        }
        return nt;
    }
    // map用的转置函数
    public int[][] Tsp(int[][] mp)
    {
        int[][] map_T = new int[MAPY_][MAPX_];
        for(int i = 0;i<MAPY_;i++)
        {
            for (int j = 0; j < MAPX_; j++) {
                map_T[i][j] = mp[j][i];
            }
        }
        return map_T;
    }
    // path用的转置函数
    public ArrayDeque<int[]> Tsp(ArrayDeque<int[]> path)
    {
        ArrayDeque<int[]> pt = new ArrayDeque<>();
        for(int[] p : path)
        {
            path.addLast(new int[]{p[1],p[0]});
        }
        return path;
    }

}
