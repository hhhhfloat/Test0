package org.example.Functions;

import java.util.*;

public class LinkyMap {
    /// VVVVVV 重要常数以及成员变量声明
    // 四个方向 0上 1右 2下 3左
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    static final int[][][] Pos =
    {
        {
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,0,0,0,0,0,0,0},
            {0,1,1,1,1,0,0,0,0,0,0,0},
            {0,1,1,1,1,0,0,0,0,0,0,0},
            {0,1,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,1,1,0},
            {0,0,0,0,0,0,0,1,1,1,1,0},
            {0,0,0,0,0,0,0,1,1,1,1,0},
            {0,0,0,0,0,0,0,1,1,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0}
        },
        {
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0}
        }
    };
    static final int[] nType = {6,12};
    static final int[] nToPut = {32, 100};

    private int MAPX_, MAPY_;
    private int[][] map;
    private int[][] map_T;
    private int[][][][] NumMap;
    private int[][][][] NumMap_T;
    private int MapType = 0;
    private int[] Count;
    private int ChangeCount = 0;

    /// VVVVVV
    // getters

    public int[][] getMap() {
        return map;
    }

    public int[][][][] getNumMap() {
        return NumMap;
    }

    public int getMAPX_() {return MAPX_;}

    public int getMAPY_() {return MAPY_;}

    public int getChangeCount() {
        return ChangeCount;
    }
    
    /// 构造函数，会生成地图以及对应数表
    public LinkyMap(int MAPX, int MAPY, int mpType) throws InterruptedException {
        MAPX_ = MAPX;
        MAPY_ = MAPY;
        MapType = mpType;
        map = new int[MAPX_][MAPY_];
        map_T = new int [MAPY_][MAPX_];

        //随机生成初始地图
        initMap();
        // 生成数表
        initNumMap();
    }

    /// 自动生成地图
    public void initMap() throws InterruptedException {
        Count = new int[nType[MapType]];
        int[][] pos = new int[MAPX_][MAPY_];
        int[][] buf_map = new int[MAPX_][MAPY_];
        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                map[x][y] = -1;
                buf_map[x][y] = -1;
            }
        }

        for (int i = 0; i < MAPX_; i++) {
            System.arraycopy(Pos[MapType][i],0,pos[i],0,MAPY_);
        }
        do {
            sRandCount();
            sRandMap(pos, buf_map);
            //PrintMap(map);
            //Sleep(60000);
            initNumMap();
        }while(!canComplete());
        for (int i = 0; i < MAPX_; i++) {
            System.arraycopy(buf_map[i],0,map[i],0,MAPY_);
        }
    }
    public void sRandCount()
    {
        Random rand = new Random();
        int n = nType[MapType];
        int times = nToPut[MapType];
        for (int i = 0; i < n; i++) {
            Count[i] = 2;
        }
        for (int i = 0; i < times/2 - n; i++) {
            Count[rand.nextInt(n)]+=2;
        }
    }
    public void sRandMap(int[][] pos, int[][] buf_map)
    {
        int n = nType[MapType];
        Random rand = new Random();
        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                if(pos[x][y]==1)
                {
                    int r = rand.nextInt(n);
                    while(Count[r] == 0)r = rand.nextInt(n);
                    map[x][y] = r;
                    buf_map[x][y] = r;
                    Count[r]--;
                }
            }
        }
    }
    public boolean canComplete()
    {
        while(!isComplete())
        {
            HashSet<Point> path = autoFindPath();
            delNumMap(path);
            if(path.isEmpty() && !isComplete()) return false;
        }
        return true;
    }

    /// 生成初始数表
    public void initNumMap() {
        NumMap = new int[MAPX_][MAPY_][4][2];
        // 扫描四元组
        // 按非零格枚举，直接四个方向都一起
        // 这次不扫直线
        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                if (map[x][y] != -1) // 仅枚举非零格
                {
                    int val = map[x][y];
                    for (int i = 0; i < 4; i++) {// 枚举四个方向
                        int t0 = 1;
                        int dx = dir[i][0], dy = dir[i][1];
                        while ((x + t0 * dx != MAPX_ && x + t0 * dx != -1) && (y + t0 * dy != MAPY_ && y + t0 * dy != -1)) {// 判断下标不在地图外
                            NumMap[x + t0 * dx][y + t0 * dy][(i + 2) % 4][0] = val;
                            NumMap[x + t0 * dx][y + t0 * dy][(i + 2) % 4][1] = t0;
                            // 走到终点
                            if (map[x + t0 * dx][y + t0 * dy] != -1) {
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
            while (t0 != MAPX_) {// 判断下标不在地图外
                NumMap[t0][i][0][0] = -1;
                NumMap[t0][i][0][1] = t0 + 1;
                // 走到终点
                if (map[t0][i] != -1) break;

                t0++;
            }
            // 底行

            int t1 = MAPX_ - 1;
            while (t1 != -1) {
                NumMap[t1][i][2][0] = -1;
                NumMap[t1][i][2][1] = MAPX_ - t1;
                if (map[t1][i] != -1) break;
                t1--;
            }
        }
        for (int i = 0; i < MAPX_; i++) {
            // 左列

            int t2 = 0;
            while (t2 != MAPY_) {// 判断下标不在地图外
                NumMap[i][t2][3][0] = -1;
                NumMap[i][t2][3][1] = t2 + 1;
                // 走到终点
                if (map[i][t2] != -1) break;
                t2++;
            }
            // 右列
            int t3 = MAPY_ - 1;
            while (t3 != -1) {
                NumMap[i][t3][1][0] = -1;
                NumMap[i][t3][1][1] = MAPY_ - t3;
                if (map[i][t3] != -1) break;
                t3--;
            }
        }

        NumMap_T = Tsp(NumMap);
        // 数表生成完成
    }

    ///  VVVVVV
    /// 消去后更新数表与地图（给定消去的非零点）
    public void delNumMap(HashSet<Point> points) {

        for (Point p : points) {
            int x = p.x(), y = p.y();
            if(map[x][y] == -1) continue;
            //延申原先方向的（不知道如何避免重复）（知道如何避免重复了但是好复杂）
            for (int i = 0; i < 4; i++) {
                int t0 = 1;
                int dx = dir[i][0], dy = dir[i][1];
                int px = x + t0 * dx, py = y + t0 * dy;// 此时走一步的坐标
                int ii = (i + 2) % 4; // 需要记录的方向
                int val = NumMap[x][y][ii][0]; // 覆写要用到的两个值
                int dis = NumMap[x][y][ii][1];

                if (px < 0 || px >= MAPX_ || py < 0 || py >= MAPY_) continue; // 如果再走一步就出去了，没有东西需要改
                if (
                        val == NumMap[px][py][ii][0] // 数字相同
                                && dis == NumMap[px][py][ii][1] - 1 // 步数加一
                ) { // 这说明这个方向已经被覆写过了
                    continue;
                }

                // 开始覆写
                while (x + t0 * dx < MAPX_ && x + t0 * dx >= 0 && y + t0 * dy < MAPY_ && y + t0 * dy >= 0) {// 在地图内，向方向 i 行进，检测 (i+2)%4 方向的点
                    px = x + t0 * dx;
                    py = y + t0 * dy; // 临时点坐标
                    // 走到这里了就先改了
                    NumMap[px][py][ii][0] = val;
                    NumMap[px][py][ii][1] = dis + t0;
                    NumMap_T[py][px][3-ii][0] = val;
                    NumMap_T[py][px][3-ii][1] = dis + t0;
                    boolean b = points.contains(new Point(px, py));
                    // System.out.println(b);
                    if (map[x + t0 * dx][y + t0 * dy] != -1 && !b) // 不是空格且哈希匹配不是消掉的——停步
                    {
                        break;
                    }
                    t0++;
                }
            }
            // 不能在地图上抹除，因为还需要结合哈希匹配来去重
        }
        // 覆写全部完成，更新地图
        for (Point p : points) {
            map[p.x()][p.y()] = -1;
            map_T[p.y()][p.x()]=-1;
        }

    }

    /// VVVVVV
    /// 自动寻找路径（无路则返回空HashSet）
    public HashSet<Point> autoFindPath() {
        if(isComplete())return new HashSet<>();

        // 全部一次性枚举！

        for (int x = 0; x < MAPX_; x++) {
            for (int y = 0; y < MAPY_; y++) {
                if (map[x][y] != -1)    // 非空格，找直线
                {
                    for (int i = 0; i < 4; i++) {
                        if (map[x][y] == NumMap[x][y][i][0]) //因为map[x][y]不是-1，不用考虑空条
                        {
                            HashSet<Point> path = new HashSet<>();
                            int dx = dir[i][0], dy = dir[i][1];
                            for (int z = 0; z <= NumMap[x][y][i][1]; z++) {
                                path.add(new Point(x + dx * z, y + dy * z));
                            }
                            return path;
                        }
                    }
                } else // 空格，枚举同行拐点！
                {
                    HashSet<Point> path = new HashSet<>();
                    // 取右侧还有空格的空格
                    int k = NumMap[x][y][1][1];
                    // 枚举步数内的所有空格对
                    for (int i = 0; i < k - 1; i++) {
                        // 既然在枚举空格，那就干脆把单拐点也做掉！
                        path = OneTwiPath(x, y + i);
                        if (!path.isEmpty()) return path;
                        // 此时再来枚举其余右侧空格，组成空格对
                        for (int j = i + 1; j < k; j++) {
                            int[] mmm = {0, 2}; // 枚举空格对中每一个的上/下数字
                            for (int z : mmm) {
                                for (int w : mmm) {
                                    if (NumMap[x][y + i][z][0] != -1 && NumMap[x][y + i][z][0] == NumMap[x][y + j][w][0]) {
                                        // 先加中间
                                        for (int poi = i; poi <= j; poi++) {
                                            path.add(new Point(x, y + poi));
                                        }
                                        // i一侧
                                        for (int poi = 1; poi <= NumMap[x][y + i][z][1]; poi++) {
                                            path.add(new Point(x + poi * dir[z][0], y + i));
                                        }
                                        // j一侧
                                        for (int poi = 1; poi <= NumMap[x][y + j][w][1]; poi++) {
                                            path.add(new Point(x + poi * dir[w][0], y + j));
                                        }
                                        return path;
                                    }
                                }
                            }
                        }
                    }
                    // 别忘了最后这一个单拐点还没检测
                    path = OneTwiPath(x, y + k - 1);
                    if (!path.isEmpty()) return path;
                    y += k-1;
                }
            }
        }

        // 纵向双拐点分析，是的原本的函数可以这样用
        return Tsp(rowTwoTwi(Tsp(NumMap), Tsp(map)));
    }

    /// 自动单拐点路径返回(输入已知合法的拐点坐标)
    HashSet<Point> OneTwiPath(int x, int y) {
        // 四个方向 0上 1右 2下 3左
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        HashSet<Point> path = new HashSet<>();
        for (int d = 0; d < 4; d++) {
            if (NumMap[x][y][d][0] != -1 && NumMap[x][y][d][0] == NumMap[x][y][(d + 1) % 4][0]) {
                int k1 = NumMap[x][y][d][1], k2 = NumMap[x][y][(d + 1) % 4][1]; // 记录两个方向的步数
                for (int z = 0; z <= k1; z++) {
                    path.add(new Point(x + dir[d][0] * z, y + dir[d][1] * z));
                }
                // 使用新方向
                int t = (d + 1) % 4;
                for (int z = 1; z <= k2; z++) {
                    path.add(new Point(x + dir[t][0] * z, y + dir[t][1] * z));
                }
                return path;
            }
        }
        return path;
    }

    /// 横向自动寻找双拐点路径，已经发挥最大作用
    HashSet<Point> rowTwoTwi(int[][][][] NumMap_, int[][] map_) {
        // 四个方向 0上 1右 2下 3左
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        int MAPX = map_.length;
        int MAPY = map_[0].length;
        HashSet<Point> path = new HashSet<>();
        for (int x = 0; x < MAPX; x++) {
            for (int y = 0; y < MAPY; y++) {
                if (map_[x][y] == -1 && NumMap_[x][y][1][1] >= 2) {
                    // 取右侧还有空格的空格
                    int k = NumMap_[x][y][1][1]; // 枚举步数内的所有空格对
                    for (int i = 0; i < k - 1; i++) {
                        for (int j = i + 1; j < k; j++) {
                            int[] mmm = {0, 2}; // 枚举空格对中每一个的上/下数字
                            for (int z : mmm) {
                                for (int w : mmm) {
                                    if (NumMap_[x][y + i][z][0] != -1 && NumMap_[x][y + i][z][0] == NumMap_[x][y + j][w][0]) {
                                        // 先加中间
                                        for (int poi = i; poi <= j; poi++) {
                                            path.add(new Point(x, y + poi));
                                        }
                                        // i一侧
                                        for (int poi = 1; poi <= NumMap_[x][y + i][z][1]; poi++) {
                                            path.add(new Point(x + poi * dir[z][0], y + i));
                                        }
                                        // j一侧
                                        for (int poi = 1; poi <= NumMap_[x][y + j][w][1]; poi++) {
                                            path.add(new Point(x + poi * dir[w][0], y + j));
                                        }
                                        return path;
                                    }
                                }
                            }
                        }
                    }
                    y += k;
                }
            }
        }
        return path;
    }

    /// NumMap用的转置函数
    public int[][][][] Tsp(int[][][][] Nm) {
        int[][][][] nt = new int[MAPY_][MAPX_][4][2];
        for (int i = 0; i < MAPX_; i++) {
            for (int j = 0; j < MAPY_; j++) {
                for (int k = 0; k < 4; k++) {
                    nt[j][i][3 - k][0] = Nm[i][j][k][0];
                    nt[j][i][3 - k][1] = Nm[i][j][k][1];
                }
            }
        }
        return nt;
    }

    /// map用的转置函数
    public int[][] Tsp(int[][] mp) {
        int[][] map_T = new int[MAPY_][MAPX_];
        for (int i = 0; i < MAPY_; i++) {
            for (int j = 0; j < MAPX_; j++) {
                map_T[i][j] = mp[j][i];
            }
        }
        return map_T;
    }

    /// path用的转置函数
    public HashSet<Point> Tsp(HashSet<Point> path) {
        HashSet<Point> pt = new HashSet<>();
        for (Point p : path) {
            pt.add(new Point(p.y(), p.x()));
        }
        return pt;
    }

    /// 检测map完成的函数（无参默认检查自己的地图）
    public boolean isComplete() {
        for (int i = 0; i < MAPX_; i++) {
            for (int j = 0; j < MAPY_; j++) {
                if (map[i][j] != -1) return false;
            }
        }
        return true;
    }
    public boolean isComplete(int[][] map_) {
        for (int i = 0; i < MAPX_; i++) {
            for (int j = 0; j < MAPY_; j++) {
                if (map_[i][j] != -1) return false;
            }
        }
        return true;
    }

    /// 给定点找连线的函数(无路径则返回空路径)
    public HashSet<Point> pickPath(Point p1, Point p2)
    {
        int x1 = p1.x(), x2 = p2.x();
        int y1 = p1.y(), y2 = p2.y();
        HashSet<Point> path = new HashSet<>();
        int val = map[x1][y1];

        for (int i = 0; i < 4; i++) {
            if(NumMap[x1][y1][i][0] == val && x1+NumMap[x1][y1][i][1]*dir[i][0] == x2 && y1 + NumMap[x1][y1][i][1] * dir[i][1] == y2)
            {
                for (int j = 0; j <= NumMap[x1][y1][i][1]; j++) {
                    Point p = new Point(x1+dir[i][0]*j,y1+dir[i][1]*j);
                    path.add(p);
                }
                return path;
            }
        }
        // 剩下的情况必定不在直线上
        // 单拐点
        if(x1>x2 && y1>y2)
        {
            path = Rdown1_Lup2(p1,p2);
            if(!path.isEmpty())return path;
        }
        if(x1<x2 && y1<y2)
        {
            path = Rdown1_Lup2(p2,p1);
            if(!path.isEmpty())return path;
        }
        if(x1>x2 && y1<y2)
        {
            path = Rup1_Ldown2(p2,p1);
            if(!path.isEmpty())return path;
        }
        if(x1<x2 && y1>y2)
        {
            path = Rup1_Ldown2(p1,p2);
            if(!path.isEmpty())return path;
        }

        // 双拐点检测
        int oney = (y1>y2)?-1:1;
        for(int i = x1-NumMap[x1][y1][0][1]+1;i<=x1+NumMap[x1][y1][2][1]-1;i++)
        {
            if(map[i][y2] == -1 && NumMap[i][y1][2-oney][1] > (y2-y1)*oney)
            {
                for(int j = y1;j!=y2+oney;j+=oney)
                {
                    path.add(new Point(i,j));
                }
                int one1 = (i>x1)?1:-1;
                for (int j = x1; j != i; j+=one1) {
                    path.add(new Point(j,y1));
                }
                int one2 = (i>x2)?1:-1;
                for (int j = x2; j != i; j+=one2) {
                    path.add(new Point(j,y2));
                }
                return path;
            }
        }
        x1 = p1.y();
        x2 = p2.y();
        y1 = p1.x();
        y2 = p2.x();
        for(int i = x1-NumMap_T[x1][y1][0][1]+1;i<=x1+NumMap_T[x1][y1][2][1]-1;i++)
        {
            if(map_T[i][y2] == -1 && NumMap_T[i][y1][2-oney][1] > (y2-y1)*oney)
            {
                for(int j = y1;j!=y2+oney;j+=oney)
                {
                    path.add(new Point(i,j));
                }
                int one1 = (i>x1)?1:-1;
                for (int j = x1; j != i; j+=one1) {
                    path.add(new Point(j,y1));
                }
                int one2 = (i>x2)?1:-1;
                for (int j = x2; j != i; j+=one2) {
                    path.add(new Point(j,y2));
                }
                return Tsp(path);
            }
        }
        return path;
    }
    public HashSet<Point> Rdown1_Lup2(Point p1, Point p2)
    {
        int x1 = p1.x(), x2 = p2.x();
        int y1 = p1.y(), y2 = p2.y();
        HashSet<Point> path = new HashSet<>();
        if(NumMap[x1][y2][0][1] == x1-x2)
        {
            for (int i = 0; i < x1-x2; i++) {
                Point p = new Point(x1 - i, y2);
                path.add(p);
            }
            for (int i = 0; i <= y1-y2; i++) {
                Point p = new Point(x1, y1-i);
                path.add(p);
            }
            return path;
        }
        if(NumMap[x2][y1][2][1] == x1-x2) {
            for (int i = 0; i < x1 - x2; i++) {
                Point p = new Point(x1 - i, y1);
                path.add(p);
            }
            for (int i = 0; i <= y1 - y2; i++) {
                Point p = new Point(x2, y1 - i);
                path.add(p);
            }
            return path;
        }
        return path;
    }
    public HashSet<Point> Rup1_Ldown2(Point p1, Point p2)
    {
        int x1 = p1.x(), x2 = p2.x();
        int y1 = p1.y(), y2 = p2.y();
        HashSet<Point> path = new HashSet<>();
        if(NumMap[x2][y1][0][1] == x2-x1)
        {
            for (int i = 0; i < x2-x1; i++) {
                Point p = new Point(x1 + i, y1);
                path.add(p);
            }
            for (int i = 0; i <= y1-y2; i++) {
                Point p = new Point(x2, y1-i);
                path.add(p);
            }
            return path;
        }
        if(NumMap[x1][y2][2][1] == x2-x1) {
            for (int i = 0; i < x2 - x1; i++) {
                Point p = new Point(x2 - i, y2);
                path.add(p);
            }
            for (int i = 0; i <= y1 - y2; i++) {
                Point p = new Point(x1, y1 - i);
                path.add(p);
            }
            return path;
        }
        return path;
    }

    // 测试用函数
    public void PrintMap(int[][] map_) {
        for (int i = 0; i < MAPX_; i++) {
            for (int j = 0; j < MAPY_; j++) {
                int t = map_[i][j];
                if (t == -1) System.out.print("    ");
                else if (t == '#') System.out.print(" ## ");
                else System.out.printf(" %-3d", t);
            }
            System.out.println();
        }
    }
    public void Sleep(int MiliS) throws InterruptedException {
        Thread.sleep(MiliS);
    }
}
