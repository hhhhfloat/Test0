package model.entity;

import java.util.ArrayList;
import java.util.HashSet;

public class HR_Block {
    final int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private HashSet<Crd> allCrd;
    private ArrayList<HashSet<Crd>> dirCrd;

    public HashSet<Crd> getAllCrd() {
        return allCrd;
    }

    public ArrayList<HashSet<Crd>> getDirCrd() {
        return dirCrd;
    }

    public HR_Block(HashSet<Crd> allBlk)
    {
        this.allCrd = new HashSet<>(allBlk);
        dirCrd = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            dirCrd.add(new HashSet<>());
        }
        InitDirs();
    }
    public void InitDirs()
    {
        for(Crd c : allCrd)
        {
            for (int i = 0; i < 4; i++) {
                if(!allCrd.contains(new Crd(c.x()+dir[i][0],c.y()+dir[i][1])))
                {
                    dirCrd.get(i).add(c);
                }
            }
        }
    }

}
