package com.tsj.oj.guava_use.observer;
import com.google.common.eventbus.Subscribe;


public class GuavaEvent{
    @Subscribe
    public void subcribe(String str){
        System.out.println("执行subscribe方法，传入的参数是: " + str);
    }
}