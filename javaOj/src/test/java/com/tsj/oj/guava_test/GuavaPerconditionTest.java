package com.tsj.oj.guava_test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.*;
import com.google.common.base.Preconditions;


public class GuavaPerconditionTest extends TestCase{

    public double sqrt(double input) throws IllegalArgumentException{
        Preconditions.checkArgument(input > 0.0, "Illegal Argument passed in: Negative value is %s.", input);
        return Math.sqrt(input);
    }

    public Integer sum(Integer a, Integer b){
        Preconditions.checkNotNull(a, "Illegal Argument passed: First parameter is Null.");
        Preconditions.checkNotNull(b, "Illegal Argument passed: Second parameter is Null.");
        return a+b;
    }

    public int getValue(int input) {
      int[] data = {1,2,3,4,5};
      Preconditions.checkElementIndex(input, data.length, "Illegal Argument passed: Invalid index.");
      return data[input];
    }

    public void testSqrtNeg(){
        try{
            Double result = sqrt(-3.0);
            fail("sqrt by negative");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testSumNull(){
        try{
            int res = sum(null, 3);
            fail("sum of null");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testGetValueOutOfBound(){
        try{
            int res = getValue(6);
            fail("index out of bound");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static Test suite(){
        TestSuite testSuite=new TestSuite();
        Test test1 = TestSuite.createTest(GuavaPerconditionTest.class, "testSqrtNeg");
        Test test2 = TestSuite.createTest(GuavaPerconditionTest.class, "testSumNull");
        Test test3 = TestSuite.createTest(GuavaPerconditionTest.class, "testGetValueOutOfBound");
        testSuite.addTest(test1);
        testSuite.addTest(test2);
        testSuite.addTest(test3);
        return testSuite;
    }
}