package model.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class HRLinkyMap {
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private int MAPX;
    private int MAPY;
    private LinkyMap level;
    private ArrayList<HR_Block> hrBlocks;
    private int[][] blkMap;
    private int nBlk;

    /// blkMap中：非 hrBlock:-1 ; 是 hrBlock:index
    /// numberMap中：非可连方块:-1 ; 是可连方块:type
    /// 如何构建 lastBlkState


    public LinkyMap getLevel() {
        return level;
    }

    /// 支持UNDO所使用的列表，其中一个整数记录两个值
    /// 以 4 作为进制单位
    /// 十位是 index, 个位是方向
    private ArrayDeque<Integer> Moves;

    public HRLinkyMap(int MAPX, int MAPY, int[][]numMp, ArrayList<HashSet<Crd>> crds)
    {
        this.MAPX = MAPX;
        this.MAPY = MAPY;
        nBlk = crds.size();
        init_hrBlock(crds);
        initBlkMap();
        level = new LinkyMap(MAPX,MAPY,initBufMap(numMp,crds));
        Moves = new ArrayDeque<>();
    }
    private int[][] initBufMap(int[][]numMp,ArrayList<HashSet<Crd>> crds){
        int[][] bufMap = new int[MAPX][MAPY];
        // 写入原numMp
        for (int i = 0; i < MAPX; i++) {
            System.arraycopy(numMp[i], 0, bufMap[i], 0,numMp[0].length);
        }
        // 无差别写入华容道方块
        for (int i = 0; i < nBlk; i++) {
            for(Crd c : crds.get(i)) {
                bufMap[c.x()][c.y()] = -3;
            }
        }
        return bufMap;
    }
    private void init_hrBlock(ArrayList<HashSet<Crd>>crds) {
        hrBlocks = new ArrayList<>();
        for (int i = 0; i < nBlk; i++) {
            hrBlocks.add(new HR_Block(crds.get(i)));
        }
    }
    public void initBlkMap() {
        blkMap = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                blkMap[i][j] = -1;
            }
        }
        for(int i = 0; i< hrBlocks.size(); i++)
        {
            for(Crd c : hrBlocks.get(i).getAllCrd())
            {
                blkMap[c.x()][c.y()] = i;
            }
        }
    }

    public boolean canMove(Crd c, int dirct)
    {
        int index = blkMap[c.x()][c.y()];
        if(index == -1) return false;
        HR_Block b = hrBlocks.get(index);
        for(Crd p : b.getDirCrd().get(dirct))
        {
            // 需要检测的坐标
            int x = p.x() + dir[dirct][0], y = p.y() + dir[dirct][1];
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
        HR_Block b = hrBlocks.get(index);
        if(canMove(c,dirct))
        {
            // 记录操作
            if(isRecorded) {
                Moves.push((index << 2) | dirct);
            }
            // 具体移动
            HashSet<Crd> newBlk = new HashSet<>();
            for(Crd p : b.getAllCrd()) {
                newBlk.add(new Crd(p.x()+dir[dirct][0], p.y()+ dir[dirct][1]));
            }
            for(Crd p:b.getDirCrd().get((dirct+2)%4)) {
                // 二者同步消除
                blkMap[p.x()][p.y()] = -1;
                level.getMap()[p.x()][p.y()] = -1;
            }

            b = new HR_Block(newBlk);
            hrBlocks.set(index,b);
            for(Crd p: b.getDirCrd().get(dirct)) {
                // 二者同步增加
                blkMap[p.x()][p.y()] = index;
                level.getMap()[p.x()][p.y()] = -3;
            }
        }
    }

    public void UNDO()
    {
        if(Moves.isEmpty())return;
        int packed = Moves.pop(); // 掰下末尾元素（已经删除）
        int index = packed >> 2;
        int oppDir = packed & 0x3;
        HR_Block b = hrBlocks.get(index);
        if(b == null) return;
        Crd anyCrd = b.getAllCrd().iterator().next();
        Move(anyCrd, oppDir,false);
    }

    public ArrayList<Crd> findPath(Crd p1, Crd p2)
    {
        if(!level.isValidPick(p1,p2)) {
            return new ArrayList<>();
        }
        level.initNumMap();
        ArrayList<Crd> path = level.pickPath(p1,p2);

        if(path.isEmpty()) {
            return path;
        }
        HashSet<Crd> del = new HashSet<>();
        del.add(p1);
        del.add(p2);
        level.delNumMap(del);
        // 手动擦除两个棋子位置（设为 -1）

        return path;
    }

}
