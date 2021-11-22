// package com.tsj.oj;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// public class Str2IntTest extends TestCase{
//     public Str2IntTest(){}
//     public Str2IntTest(String name){
//         super(name);
//     }

//     @Override
// 	public void setUp(){
// 	}

//     @Override
// 	public void tearDown(){}

//     public void testValidNum(){
//         String[] trueTests = new String[]{"2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"};
//         for(int i=0; i<trueTests.length; i++){
//             System.out.println(trueTests[i]);
//             assertEquals(true, Str2Int.validNum(trueTests[i]));
//         }
//         String[] falseTests = new String[]{"abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"};
//         for(int i=0; i<falseTests.length; i++){
//             assertEquals(false, Str2Int.validNum(falseTests[i]));
//         }
//     }

// 	public static Test suite(){
//         TestSuite testSuite=new TestSuite("All Test From TestCaseExample");
//         // 第一种方法
//         // testSuite.addTestSuite(Str2IntTest.class);
//         // 第二种方法
//         // Test test = TestSuite.createTest(Str2IntTest.class, "testValidNum");
//         // testSuite.addTest(test);
//         return testSuite;
//     }

// }
