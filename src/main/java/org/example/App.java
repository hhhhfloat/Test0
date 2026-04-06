package org.example;

import java.util.Scanner;
import org.example.Objects.SomeClass;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextInt());
        SomeClass so = new SomeClass();
        System.out.println(so.GetName());
    }
}
