// package com.tsj.oj.easy;
import java.util.*;
import java.util.stream.*;

public class Intersect{

    public static List<Integer> intersect(List<Integer> lst1, List<Integer> lst2){
        return lst1.stream().filter(item -> !lst2.contains(item)).collect(Collectors.toList());
    }

    public static void main(String[] args){
        List<Integer> lst1 = new ArrayList<Integer>(){{add(1); add(2); add(3);}};
        List<Integer> lst2 = new ArrayList<Integer>(){{add(3); add(4); add(5);}};
        List<Integer> res = intersect(lst1, lst2);
        res.parallelStream().forEach(System.out :: print);
    }
}