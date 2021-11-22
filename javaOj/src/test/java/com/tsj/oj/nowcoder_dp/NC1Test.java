// package com.tsj.oj.nowcoder_dp;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// public class NC1Test extends TestCase{
//     public NC1Test(){}
//     public NC1Test(String name){
//         super(name);
//     }

//     @Override
// 	public void setUp(){
// 	}

//     @Override
// 	public void tearDown(){}

//     public void testMaxSubArr(){
//         assertEquals(5, NC1.maxSubArr(3, new int[]{3, -4, 5}));
//     }

// 	public static Test suite(){
//         TestSuite testSuite=new TestSuite("All Test From TestCaseExample");
//         // 第一种方法
//         // testSuite.addTestSuite(NC1Test.class);
//         // 第二种方法
//         Test test = TestSuite.createTest(NC1Test.class, "testMaxSubArr");
//         testSuite.addTest(test);
//         return testSuite;
//     }
// }