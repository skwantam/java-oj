package com.tsj.oj.nowcoder_dp;
import java.util.*;

public class NC3 {

    public static int maxValue(int len, int a, int b, int c, String ncs){
        if(len < 4) return 0;
        int[] dp = new int[len+1];
        for(int i= 4; i<=len; i++){
            dp[i] = dp[i-1];
            if(i >= 4 && ncs.substring(i-4, i).equals("nico")){
                dp[i] = Math.max(dp[i], dp[i-4]+a);
            }
            if(i >= 6 && ncs.substring(i-6, i).equals("niconi")){
                dp[i] = Math.max(dp[i], dp[i-6]+b);
            }
            if(i >= 10 && ncs.substring(i-10, i).equals("niconiconi")){
                dp[i] = Math.max(dp[i], dp[i-10]+c);
            }
        }
        return dp[len];
    }
}