package org.example;

import model.entity.HRLinkyMap;
import model.entity.LinkyMap;
import model.entity.Crd;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * 在这个类中定义的方法命名规则
 * 以 test 开头（全小写），会被调用并运行
 * 为了统一，以 _test 开头标记不想被运行的测试程序
 * 其余名称则只是普通成员方法，可以被 test 命名的方法调用
 *
 */


public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void _testApp() {
        assertTrue(true);
    }

    // Test Functions have to be named after "test", like "testABC"

    public int[][] showPath(ArrayList<Crd> path, int[][] map_, int MAPX, int MAPY) {
        int[][] temp = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            System.arraycopy(map_[i], 0, temp[i], 0, MAPY);
        }
        for (Crd p : path) {
            temp[p.x()][p.y()] = -2;
        }
        return temp;
    }

    // Use this to Sleep for X miliseconds (just for simpler codes)
    public void Sleep(int MiliS) throws InterruptedException {
        Thread.sleep(MiliS);
    }

    // Use this to (pretend to) clear the output
    public void Clear() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }

    // Use this to Print the rectangle map （目前这不是标准函数）
    public void PrintMap(int[][] map_) {
        int MAPX = map_.length;
        int MAPY = map_[0].length;
        for (int[] ints : map_) {
            for (int j = 0; j < MAPY; j++) {
                int t = ints[j];
                if (t == -1) System.out.print("    ");
                else if (t == -2) System.out.print(" ## ");
                else System.out.printf(" %-3d", t);
            }
            System.out.println();
        }
    }

    // Use this to Print the path
    public void printPath(ArrayList<Crd> path) {
        for (Crd p : path) {
            System.out.printf("(%d, %d) ", p.x(), p.y());
        }
        System.out.println();
    }

    // 检测完成的方法
    public boolean isComplete(int[][] map) {
        int MAPX = map.length;
        int MAPY = map[0].length;
        for (int[] ints : map) {
            for (int j = 0; j < MAPY; j++) {
                if (ints[j] != -1) return false;
            }
        }
        return true;
    }

    // 输出路径点
    public void PrintPOINT(int[] p) {
        System.out.printf("(%d, %d) \n", p[0], p[1]);
    }

    // 测试自动生成地图
    public void _testMapSummon() throws InterruptedException {
        int Count = 0;
        int MAPX = 10, MAPY = 10;
        int nType = 50;
        LinkyMap level = new LinkyMap(12, 12, 1, true);
        PrintMap(level.getMap());
    }

    // 测试华容道逻辑
    public void testHR() throws InterruptedException {
        int MAPX = 12, MAPY = 12;
        int[][] mp = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                mp[i][j] = -1;
            }
        }
        for (int i = 1; i < 10; i++) {
            for (int j =1; j < 10; j++) {
                mp[i][j] = -2;
            }
        }
        int[][] smallMap = {
                { 7,  3,  6,  2,  5,  4,  6},
                {-2, -1, -1, -1, -1, -1, -2},
                {-2, -1, -1, -1, -1, -1, -2},
                {-2, -1, -1, -1, -1, -1, -2},
                { 1, -1, -1, -1, -1, -1,  1},
                { 4, -1, -1, -1, -1, -1, -2},
                { 5, -2, -2,  2, -2,  3,  7}
        };
        for (int i = 0; i < 7; i++) {
            System.arraycopy(smallMap[i], 0, mp[i + 2], 2, 7);
        }
        HRLinkyMap hrLevel = getHrLinkyMap(MAPX, MAPY, mp);
        LinkyMap level = hrLevel.getLevel();

        printMap(level.getMap());
        Sleep(3000);

        /// testdata1[][i] = {x,y,dir}
        int[][] testdata1 = {
                {3,3,1},
                {5,6,0},
                {4,5,2},
                {5,4,3}
        };
        int[][] testdata2 = {
                {5,3,2},
                {6,3,3}
        };
        int l = testdata1.length;
        for (int[] testdatum : testdata1) {
            Clear();
            Crd c = new Crd(testdatum[0], testdatum[1]);
            hrLevel.Move(c, testdatum[2], true);
            printMap(level.getMap());
            //Sleep(3000);
        }

        ArrayList<Crd>path = hrLevel.findPath(new Crd(6,2), new Crd(6,8));
        printMap(showPath(path, level.getMap(), MAPX, MAPY));
        printPath(path);
        Sleep(1000);
        for (int[] testdatum : testdata2) {
            Clear();
            Crd c = new Crd(testdatum[0], testdatum[1]);
            hrLevel.Move(c, testdatum[2], true);
            printMap(level.getMap());
            Sleep(3000);
        }

    }

    private static HRLinkyMap getHrLinkyMap(int MAPX, int MAPY, int[][] mp) {
        ArrayList<HashSet<Crd>> blks = new ArrayList<>();
        HashSet<Crd> blk1 = new HashSet<>();
        blk1.add(new Crd(3,3));
        blk1.add(new Crd(3,4));
        HashSet<Crd> blk2 = new HashSet<>();
        blk2.add(new Crd(4,3));
        blk2.add(new Crd(4,4));
        blk2.add(new Crd(4,5));
        HashSet<Crd> blk3 = new HashSet<>();
        blk3.add(new Crd(5,4));
        HashSet<Crd> blk4 = new HashSet<>();
        blk4.add(new Crd(6,5));
        blk4.add(new Crd(6,6));
        blk4.add(new Crd(5,6));
//        HashSet<Crd> blk5 = new HashSet<>();
//        HashSet<Crd> blk6 = new HashSet<>();
//        HashSet<Crd> blk7 = new HashSet<>();
//        HashSet<Crd> blk8 = new HashSet<>();
//        HashSet<Crd> blk9 = new HashSet<>();
        blks.add(blk1);
        blks.add(blk2);
        blks.add(blk3);
        blks.add(blk4);
        return new HRLinkyMap(MAPX, MAPY, mp,blks);
    }

    public void printMap(int[][]map)
    {
        int MAPX = map.length;
        int MAPY = map[0].length;
        for (int[] ints : map) {
            for (int j = 0; j < MAPY; j++) {
                if (ints[j] >= 0)
                    System.out.printf(" %2d ", ints[j]);
                else if (ints[j] == -2)
                    System.out.print(" %% ");
                else if (ints[j] == -3)
                    System.out.print(" @@ ");
                else System.out.print("    ");
            }
            System.out.println();
        }
    }
}
