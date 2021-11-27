package com.tsj.oj.nowcoder_dp;
import java.util.*;
/**
    后缀和
 */
public class NC5{

    public static long abbSubSeq(int n, String str){
        char[] cs = str.toCharArray();
        //sum[i][j] 在str.substring(i, n)中('a'+i)中出现的次数
        long[][] sum = new long[n+1][26];
        for(int i=n-1; i>=0; i--){
            for(int j=0; j<26; j++){
                sum[i][j] = sum[i+1][j];
            }
            sum[i][(cs[i]-'a')]++;
        }
        
        long res = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<26; j++){
                if( j != (int)(cs[i] -'a')){
                    res += (sum[i+1][j] * (sum[i+1][j] - 1) / 2);
                }
            }
        }
        return res;
    }
    
}
