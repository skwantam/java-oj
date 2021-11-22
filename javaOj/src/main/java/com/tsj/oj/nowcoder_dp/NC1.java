package com.tsj.oj.nowcoder_dp;

import java.util.*;

public class NC1{

    public static int maxSubArr(int n, int[] nums){
        if(nums == null || nums.length == 0) return 0;
        if( nums.length == 1) return nums[0];
        int tmpSum=nums[0], max=nums[0];
        for(int i=1; i<nums.length; i++){
            tmpSum = Math.max(0, tmpSum) + nums[i];
            max = Math.max(max, tmpSum);
        }
        return max;
    }
}