package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.HashSet;

import org.example.Functions.*;


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

    public int[][] ShowPath(ArrayList<Coordi> path, int[][] map_, int MAPX, int MAPY) {
        int[][] temp = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            System.arraycopy(map_[i], 0, temp[i], 0, MAPY);
        }
        for (Coordi p : path) {
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

    // Use this to Print the rectangle map
    public void PrintMap(int[][] map_) {
        int MAPX = map_.length;
        int MAPY = map_[0].length;
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                int t = map_[i][j];
                if (t == -1) System.out.print("    ");
                else if (t == -2) System.out.print(" ## ");
                else System.out.printf(" %-3d", t);
            }
            System.out.println();
        }
    }

    // Use this to Print the path
    public void PrintPath(HashSet<Coordi> path) {
        for (Coordi p : path) {
            System.out.printf("(%d, %d) ", p.x(), p.y());
        }
        System.out.println();
    }

    // 检测完成的方法
    public boolean isComplete(int[][] map) {
        int MAPX = map.length;
        int MAPY = map[0].length;
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                if (map[i][j] != -1) return false;
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
        LinkyMap level = new LinkyMap(12, 12, 1);
        PrintMap(level.getMap());
    }

    public void testFunc() throws InterruptedException {
        int MAPX = 12, MAPY = 12;
        int nType = 1;
        LinkyMap level = new LinkyMap(MAPX, MAPY, nType);

        boolean msg = true;
        while(!isComplete(level.getMap()))
        {
            ArrayList<Coordi> path = level.autoFindPath();
            if(path.isEmpty())
            {
                System.out.println("没有路径了");
                msg = false;
                break;
            }
            // 选用打印路径与地图
            int[][] map_p = ShowPath(path, level.getMap(), MAPX, MAPY);
            PrintMap(map_p);

            // 必须更新的
            level.delNumMap(level.HashPath(path));
            Sleep(3000);
        }
        if(msg)
        {
            System.out.println("消除完毕!");
        }
    }
}
