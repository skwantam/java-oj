package com.tsj.oj.nowcoder_dp;
import java.util.*;

/**
    差分
 */
public class NC7{

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        int[] a = new int[n+1];
        for(int i=1; i<=n; i++) a[i] = sc.nextInt();
        //数组a每一个位置的增量
        long [] incrs = new long[n+2];
        while(m-- > 0){
            //l, r均从1开始， [l,r]区间每个元素都增加k
            int l = sc.nextInt(), r = sc.nextInt(), k = sc.nextInt();
            incrs[l] += k;
            incrs[r+1] -= k;
        }
        for(int i=1; i<=n; i++) incrs[i] += incrs[i-1];
        
        for(int i=1; i<=n; i++){
            System.out.print(a[i] + incrs[i] + " ");
        }
    }
}