package org.example.Functions;

import java.util.ArrayDeque;
import java.util.Arrays;

import java.util.HashSet;

public class LinkyMap {

    // 四个方向 0上 1右 2下 3左
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int MAPX_, MAPY_;
    private int[][] map;
    private int[][][][] NumMap;

    // 构造函数，会生成地图以及对应数表
    public LinkyMap(int MAPX, int MAPY)
    {
        MAPX_ = MAPX;
        MAPY_ = MAPY;
        map = new int[MAPX_][MAPY_];
        //随机生成初始地图
        String SMap = "";
        SMap += "54321 ";
        SMap += "98765 ";
        SMap += "54321 ";
        SMap += "056787";
        SMap += "129270";
        SMap += "5   51";
        for (int i = 0; i < MAPX; i++){
            for (int j = 0; j < MAPY; j++) {
                char c = SMap.charAt(i * MAPY + j);
                map[i][j] = (c == ' ') ? -1 : (c-'0');
            }
        }
        // 生成数表
        initNumMap();

        System.out.println(Arrays.deepToString(NumMap[5][1]));
    }

    // 生成初始数表
    public void initNumMap()
    {
        NumMap = new int[MAPX_][MAPY_][4][2];
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

            int t0 = 0;
            while (t0 != MAPX_)
            {// 判断下标不在地图外
                NumMap[t0][i][0][0] = -1;
                NumMap[t0][i][0][1] = t0 + 1;
                // 走到终点
                if(map[t0][i] != -1)break;

                t0++;
            }
            // 底行

            int t1 = MAPX_ - 1;
            while(t1 != -1) {
                NumMap[t1][i][2][0] = -1;
                NumMap[t1][i][2][1] = MAPX_ - t1;
                if(map[t1][i] != -1)break;
                t1--;
            }

            // 左列

            int t2 = 0;
            while (t2 != MAPX_)
            {// 判断下标不在地图外
                NumMap[i][t2][3][0] = -1;
                NumMap[i][t2][3][1] = t2 + 1;
                // 走到终点
                if(map[i][t2] != -1)break;
                t2++;
            }
            // 右列
            int t3 = MAPY_ - 1;
            while(t3 != -1) {
                NumMap[i][t3][1][0] = -1;
                NumMap[i][t3][1][1] = MAPY_ - t3;
                if(map[i][t3] != -1)break;
                t3--;
            }
        }

        // 数表生成完成
    }

    // getters
    public int[][] getMap() {
        return map;
    }
    public int[][][][] getNumMap() {
        return NumMap;
    }

    // 消去后更新数表与地图（给定消去的非零点）
    public void delNumMap(HashSet<Point>points)
    {

        for(Point p : points)
        {
            int x = p.x(),y = p.y();
            //延申原先方向的（不知道如何避免重复）（知道如何避免重复了但是好复杂）
            for (int i = 0; i < 4; i++) {
                int t0 = 1;
                int dx = dir[i][0],dy = dir[i][1];
                int px = x+t0*dx, py = y+t0*dy;// 此时走一步的坐标
                int ii = (i+2)%4; // 需要记录的方向
                int val = NumMap[x][y][ii][0]; // 覆写要用到的两个值
                int dis = NumMap[x][y][ii][1];

                if(px<0||px>=MAPX_||py<0||py>=MAPY_)continue; // 如果再走一步就出去了，没有东西需要改
                if(
                        val == NumMap[px][py][ii][0] // 数字相同
                    && dis == NumMap[px][py][ii][1]-1 // 步数加一
                ){ // 这说明这个方向已经被覆写过了
                    continue;
                }

                // 开始覆写
                while(x+t0*dx<MAPX_ && x+t0*dx>=0 && y+t0*dy<MAPY_ && y+t0*dy>=0)
                {// 在地图内，向方向 i 行进，检测 (i+2)%4 方向的点
                    px = x+t0*dx;
                    py = y+t0*dy; // 临时点坐标
                    // 走到这里了就先改了
                    NumMap[px][py][ii][0] = val;
                    NumMap[px][py][ii][1] = dis+t0;
                    boolean b = points.contains(new Point(px,py));
                    // System.out.println(b);
                    if(map[x+t0*dx][y+t0*dy] != -1 && !b) // 不是空格且哈希匹配不是消掉的——停步
                    {
                        break;
                    }
                    t0++;
                }
            }
            // 不能在地图上抹除，因为还需要结合哈希匹配来去重
        }
        // 覆写全部完成，更新地图
        for(Point p : points)
        {
            map[p.x()][p.y()] = -1;
        }

    }

    // 自动寻找路径（无路则返回空Deque）
    public ArrayDeque<int[]> autoFindPath()
    {
        // 全部一次性枚举！
        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                if(map[x][y] != -1)    // 非空格，找直线
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
                    path = OneTwiPath(x,y+k-1);
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
            if(NumMap[x][y][d][0] != -1 && NumMap[x][y][d][0] == NumMap[x][y][(d+1)%4][0])
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
