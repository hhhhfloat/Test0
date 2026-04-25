package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayDeque;

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

    // Test the Logics.Linky
    // Test Functions have to be named after "test", like "testABC"
    public void _testLinkyLogic() throws InterruptedException {
        int MAPX = 10, MAPY = 10;
        String map = "";
        map += " 2 642 8  ";
        map += "   9   6 2";
        map += " 2     2  ";
        map += "  8 7  0  ";
        map += " 5     57 ";
        map += " 20    1  ";
        map += "0514  91  ";
        map += "  5       ";
        map += "   2    0 ";
        map += "        2 ";
        int[][] Map = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                char c = map.charAt(i * MAPY + j);
                Map[i][j] = (c == ' ') ? -1 : (c - '0');
            }
        }

        int[][][] TestInput = {
                // Straight Test
                {{2, 7}, {2, 1}},
                {{0, 1}, {2, 1}},
                // Row Detect Test
                {{3, 7}, {5, 2}},// Two twists
                {{8, 3}, {9, 8}},// One twist
                // Column Detect Test
                {{4, 8}, {3, 4}}, // Two twists (Only this case)
                // Special
                {{6, 1}, {7, 2}},
                {{5, 7}, {6, 7}}
        };
        Logics logics = new Logics();
        int count = 0;
        int TestNumber = TestInput.length;
        while (count < TestNumber) // Run for TestNumber Times
        {
            Clear();
            PrintMap(Map, MAPX, MAPY);
            Sleep(1000);

            ArrayDeque<int[]> path = logics.Linky(
                    MAPX, MAPY, Map,
                    TestInput[count][0], TestInput[count][1]
            );
            if (!path.isEmpty() && path.peek()[0] == -1) {
                System.out.println("   INVALID INPUT   ");
            } else if (path.isEmpty()) {
                System.out.println("   IMPOSSIBLE CONNECTION   ");
            } else {
                int[][] mapWithPath = ShowPath(path, Map, MAPX, MAPY);
                Clear();
                Map[TestInput[count][0][0]][TestInput[count][0][1]] = -1;
                Map[TestInput[count][1][0]][TestInput[count][1][1]] = -1;
                PrintMap(mapWithPath, MAPX, MAPY);
                PrintPath(path);

            }
            Sleep(3000);
            count++;
        }

    }

    public int[][] ShowPath(ArrayDeque<int[]> path, int[][] map_, int MAPX, int MAPY) {
        int[][] temp = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            System.arraycopy(map_[i], 0, temp[i], 0, MAPY);
        }
        for (int[] p : path) {
            temp[p[0]][p[1]] = '#';
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
    public void PrintMap(int[][] map_, int MAPX, int MAPY) {
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                int t = map_[i][j];
                if (t == -1) System.out.print("    ");
                else if (t == '#') System.out.print(" ## ");
                else System.out.printf(" %-3d", t);
            }
            System.out.println();
        }
    }

    // Use this to Print the path
    public void PrintPath(ArrayDeque<int[]> path) {
        for (int[] p : path) {
            System.out.printf("(%d, %d) ", p[0], p[1]);
        }
        System.out.println();
    }


    /*
     * 自动解题
     * */
    public void _testAutoSolve() throws InterruptedException {
        int MAPX = 6, MAPY = 6;
        String map = "";
        map += "54321 ";
        map += "98765 ";
        map += "54321 ";
        map += "056787";
        map += "129270";
        map += "5   51";
//
        int[][] Map = new int[MAPX][MAPY];
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                char c = map.charAt(i * MAPY + j);
                Map[i][j] = (c == ' ') ? -1 : (c - '0');
            }
        }

        Logics logics = new Logics();

        while (!isComplete(Map)) {
            Clear();
            //PrintMap(Map, MAPX, MAPY);
            //Sleep(1000);

            // 自动找到一个路径
            ArrayDeque<int[]> path = logics.HintSolution(MAPX, MAPY, Map);

            //Sleep(2000);
            if (path.isEmpty()) {
                System.out.println("没有路径！");
                break;
            } else {
                int[] p1 = path.peek();
                int[] p2 = path.getLast();
                int[][] mapWithPath = ShowPath(path, Map, MAPX, MAPY);
                //Clear();

                Map[p1[0]][p1[1]] = -1;
                Map[p2[0]][p2[1]] = -1;
                PrintMap(mapWithPath, MAPX, MAPY);
                PrintPath(path);

            }

            Sleep(1000);
        }

    }

    // 检测完成的方法
    public boolean isComplete(int[][] map_) {
        int MAPX = map_.length;
        int MAPY = map_[0].length;
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                if (map_[i][j] != -1) return false;
            }
        }
        return true;
    }

    // 输出路径点
    public void PrintPOINT(int[] p) {
        System.out.printf("(%d, %d) \n", p[0], p[1]);
    }


}
