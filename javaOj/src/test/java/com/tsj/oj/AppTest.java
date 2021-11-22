// package com.tsj.oj;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// public class AppTest extends TestCase
// {
//     private App app = null;
    
//     public AppTest(){}
//     public AppTest(String name){
//         super(name);
//     }

//     @Override
// 	public void setUp()
// 	{
// 		app = new App();
// 	}

//     @Override
// 	public void tearDown(){}

//     public void testAdd(){
//         app=new App();
//         assertEquals(14, app.add(5, 9));
//     }

// 	public static Test suite(){
//         TestSuite testSuite=new TestSuite("All Test From TestCaseExample");
//         // 第一种方法
//         // testSuite.addTestSuite(AppTest.class);
//         第二种方法
//         Test test = TestSuite.createTest(AppTest.class, "testAdd");
//         testSuite.addTest(test);
//         return testSuite;
//     }

// }
