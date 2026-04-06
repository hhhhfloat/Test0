package org.example;

import java.util.Scanner;
import org.example.Objects.SomeClass;
import org.example.Functions.SomeFunctions;

public class App 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextInt());
        SomeClass so = new SomeClass();
        System.out.println(so.GetName());
        SomeFunctions sf = new SomeFunctions();
        for (int i = 0; i < 100; i++) {
            System.out.println(sf.aaa());
        }
    }
}
