package com.tsj.oj.nowcoder_dp;
import java.util.*;

/**
    差分
 */
public class NC8{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt(), q = sc.nextInt();
        int[][] mat = new int[n+1][m+1];
        for(int i=1; i<=n; i++){
            for(int j=1; j<=m; j++){
                mat[i][j] = sc.nextInt();
            }
        }
        
        long[][] incr = new long[n+2][m+2];
        while(q-- > 0){
            int x1 = sc.nextInt(), y1 = sc.nextInt(), x2 = sc.nextInt(), y2 = sc.nextInt();
            int k = sc.nextInt();
            incr[x1][y1] +=k;
            incr[x1][y2+1] -=k;
            incr[x2+1][y1] -= k;
            incr[x2+1][y2+1] += k;
        }
        
        for(int i=1; i<=n; i++){
            for(int j=1; j<=m; j++){
                incr[i][j] += incr[i-1][j] + incr[i][j-1] - incr[i-1][j-1];
            }
        }
        
        for(int i=1; i<=n; i++){
            for(int j=1; j<=m; j++){
                System.out.print(mat[i][j] + incr[i][j] + " ");
            }
            System.out.println();
        }
    }
}