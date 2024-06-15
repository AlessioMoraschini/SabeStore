package com.am.design.development.various;

import java.util.ArrayList;
import java.util.List;

public class GenericsTest {

    public static int x;

    {
        x ++;
        System.out.println("Code block in class: " + x);
    }


    static {
        x ++;
        System.out.println("Code static block in class: " + x);
    }

    public static void main(String[] args){
        GenericsTest g = new GenericsTest();
    }
    public GenericsTest(){
        x ++;
        System.out.println("main: " + x);
    }

    public void testListSuper(){
        List<? super Number> a = new ArrayList<>();
        a.add(1);
        //a.add(new Object()); not allowed, since Object is superclass not subclass of Number!


        List<? extends Number> b = new ArrayList<>();
        //b.add(1); compilation error can't grant that list contains superclasses of integers!

    }

    public void testRunOrder(){

    }
}
