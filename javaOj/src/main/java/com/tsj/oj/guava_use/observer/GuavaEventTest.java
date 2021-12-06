package com.tsj.oj.guava_use.observer;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;


public class GuavaEventTest{
    public static void main(String[] args){
        EventBus eventbus = new EventBus();
        GuavaEvent event = new GuavaEvent();
        eventbus.register(event);
        eventbus.post("TSJ");
    }
}