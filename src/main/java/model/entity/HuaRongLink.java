package model.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class HuaRongLink {
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int MAPX;
    private int MAPY;
    private LinkyMap level;
    private ArrayList<HR_Block> blks;
    private int[][] blkMap;
    private int nBlk;

    /// 支持UNDO所使用的列表，其中一个整数记录两个值
    /// 以 4 作为进制单位
    /// 十位是 index, 个位是方向
    private ArrayDeque<Integer> Moves;

    public HuaRongLink(int MAPX, int MAPY, int[][]mp, ArrayList<HashSet<Crd>> crds)
    {
        this.MAPX = MAPX;
        this.MAPY = MAPY;
        level = new LinkyMap(MAPX, MAPY, mp);
        nBlk = crds.size();
        blks = new ArrayList<>();
        for (int i = 0; i < nBlk; i++) {
            HR_Block b = new HR_Block(crds.get(i));
            blks.add(b);
        }
        blkMap = new int[MAPX][MAPY];
        InitBlkMap();

        syncObstaclesToLinkyMap();

        Moves = new ArrayDeque<>();
    }

    public void syncObstaclesToLinkyMap()
    {
        for (HR_Block b : blks) {
            for (Crd c : b.getAllCrd()) {
                level.getMap()[c.x()][c.y()] = -2;
            }
        }
    }

    public void InitBlkMap()
    {
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                blkMap[i][j] = -1;
            }
        }
        for(int i = 0;i<blks.size();i++)
        {
            for(Crd c : blks.get(i).getAllCrd())
            {
                blkMap[c.x()][c.y()] = i;
            }
        }
    }

    public boolean canMove(Crd c, int dirct)
    {
        int index = blkMap[c.x()][c.y()];
        if(index == -1) return false;
        HR_Block b = blks.get(index);
        for(Crd p : b.getDirCrd().get(dirct))
        {
            // 需要检测的坐标
            int x = p.x()+dir[dirct][0], y = p.y()+dir[dirct][1];
            // 边界检查（显式，避免异常）
            if (x < 0 || x >= MAPX || y < 0 || y >= MAPY) return false;
            if(blkMap[x][y] != -1 || level.getMap()[x][y] != -1)
                return false;
        }
        return true;
    }

    public void Move(Crd c, int dirct, boolean isRecorded)
    {
        int index = blkMap[c.x()][c.y()];
        HR_Block b = blks.get(index);
        if(canMove(c,dirct))
        {
            // 记录操作
            if(isRecorded)
            {
                Moves.add((index << 2) | dirct);
            }
            // 具体移动
            HashSet<Crd> newBlk = new HashSet<>();
            for(Crd p : b.getAllCrd())
            {
                newBlk.add(new Crd(p.x()+dir[dirct][0], p.y()+ dir[dirct][1]));
            }
            for(Crd p:b.getDirCrd().get((dirct+2)%4))
            {
                blkMap[p.x()][p.y()] = -1;
            }
            b = new HR_Block(newBlk);
            blks.set(index,b);
            for(Crd p: b.getDirCrd().get(dirct))
            {
                blkMap[p.x()][p.y()] = index;
            }
        }
    }

    public void UNDO()
    {
        if(Moves.isEmpty())return;
        int packed = Moves.pop(); // 掰下末尾元素（已经删除）
        int index = packed >> 2;
        int oppDir = packed & 0x3;
        HR_Block b = blks.get(index);
        if(b == null) return;
        Crd anyCrd = b.getAllCrd().iterator().next();
        Move(anyCrd, oppDir,false);
    }

    public ArrayList<Crd> FindPath(Crd p1, Crd p2)
    {
        if(!level.isValidPick(p1,p2))return new ArrayList<>();

        syncObstaclesToLinkyMap();

        level.initNumMap();

        ArrayList<Crd> path = level.pickPath(p1,p2);

        if(path.isEmpty())return path;
        // 手动擦除两个棋子位置（设为 -1）
        level.getMap()[p1.x()][p1.y()] = -1;
        level.getMap()[p2.x()][p2.y()] = -1;

        return path;
    }

}
