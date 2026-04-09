package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Scanner;
import java.util.ArrayDeque;

import org.example.Functions.Logics;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    // Test the Logics.Linky
    // Test Functions have to be named after "test", like "testABC"
    public void testLinkyLogic() throws InterruptedException{
        int MAPX = 10, MAPY = 10;
        String map = "";
        map += " 2 642 8  ";
        map += "   9   6 2";
        map += " 2     2  ";
        map += "  8 7  0  ";
        map += " 5     57 ";
        map += " 20    1  ";
        map += "0514  9   ";
        map += "  5       ";
        map += "   2    0 ";
        map += "        2 ";

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
                {{6, 1}, {7, 2}}
        };
        Logics logics = new Logics();
        Scanner sc = new Scanner(System.in);
        int count = 0;
        int TestNumber = TestInput.length;
        while(count < TestNumber) // Run for TestNumber Times
        {
            Clear();
            PrintMap(map, MAPX, MAPY);
            Sleep(1000);

            ArrayDeque<int[]> path = logics.Linky(
                    MAPX, MAPY, map,
                    TestInput[count][0],TestInput[count][1]
            );

            String mapWithPath = ShowPath(path, map, MAPX, MAPY);
            Clear();
            PrintMap(mapWithPath, MAPX, MAPY);
            PrintPath(path);
            Sleep(3000);
            count ++ ;
        }

    }
    // All Pass !!!

    public String ShowPath(ArrayDeque<int[]> path, String map_, int MAPX, int MAPY)
    {
        StringBuilder temp = new StringBuilder(map_);
        for(int[] p : path)
        {
            temp.setCharAt(p[0] * MAPY + p[1], '#');
        }
        return temp.toString();
    }

    // Use this to Sleep for X miliseconds (just for simpler codes)
    public void Sleep(int MiliS) throws InterruptedException{
        Thread.sleep(MiliS);
    }

    // Use this to (pretend to) clear the output
    public void Clear()
    {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    // Use this to Print the rectangle map
    public void PrintMap(String map_, int MAPX, int MAPY)
    {
        for (int i = 0; i < MAPX; i++) {
            for (int j = 0; j < MAPY; j++) {
                System.out.printf(" %c ", map_.charAt(i*MAPY + j));
            }
            System.out.println();
        }
    }

    // Use this to Print the path
    public void PrintPath(ArrayDeque<int[]> path)
    {
        for(int[] p : path)
        {
            System.out.printf("(%d, %d) ", p[0], p[1]);
        }
        System.out.println();
    }
}
