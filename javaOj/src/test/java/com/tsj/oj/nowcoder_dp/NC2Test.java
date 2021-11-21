// package com.tsj.oj.nowcoder_dp;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// public class NC2Test extends TestCase{
//     public NC2Test(){}
//     public NC2Test(String name){
//         super(name);
//     }

//     @Override
// 	public void setUp(){
// 	}

//     @Override
// 	public void tearDown(){}

//     public void testMaxNonSubArr(){
//         assertEquals(7, NC2.maxNonSubArr(4, new int[]{2, 6, 4, 1}));
//     }

// 	public static Test suite(){
//         TestSuite testSuite=new TestSuite("All Test From TestCaseExample");
//         // 第一种方法
//         // testSuite.addTestSuite(NC1Test.class);
//         // 第二种方法
//         Test test = TestSuite.createTest(NC2Test.class, "testMaxNonSubArr");
//         testSuite.addTest(test);
//         return testSuite;
//     }
// }