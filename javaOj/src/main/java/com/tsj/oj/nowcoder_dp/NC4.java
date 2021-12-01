package com.tsj.oj.nowcoder_dp;
import java.util.*;

public class NC4{
    //小心整型和溢出
    public static int prefixSum(int[] a, int left, int right){
        int n = a.length;
        int[] prefix = new int[n+1];
        if(left < 1 || right > n ) return -1;
        prefix[1] = a[0];
        for(int i=1; i<n; i++){
            prefix[i+1] = prefix[i]+a[i];
        }
        return prefix[right] - prefix[left-1];
    }
}   