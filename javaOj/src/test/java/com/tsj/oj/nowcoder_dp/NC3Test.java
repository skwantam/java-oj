// package com.tsj.oj.nowcoder_dp;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// public class NC3Test extends TestCase{
//     public NC3Test(){}
//     public NC3Test(String name){
//         super(name);
//     }

//     @Override
// 	public void setUp(){
// 	}

//     @Override
// 	public void tearDown(){}

//     public void testMaxValue(){
//         assertEquals(7, NC3.maxValue(19, 1, 2, 5, "niconiconiconiconi~"));
//     }

// 	public static Test suite(){
//         TestSuite testSuite=new TestSuite("All Test From TestCaseExample");
//         // 第一种方法
//         // testSuite.addTestSuite(NC1Test.class);
//         // 第二种方法
//         Test test = TestSuite.createTest(NC3Test.class, "testMaxValue");
//         testSuite.addTest(test);
//         return testSuite;
//     }
// }