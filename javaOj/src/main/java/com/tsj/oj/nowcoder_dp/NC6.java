package com.tsj.oj.nowcoder_dp;
import java.util.*;

public class NC6{
    private static long[][] pre = null;
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt(), q= sc.nextInt();
        int[][] mat = new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                mat[i][j] = sc.nextInt();
            }
        }
        init(mat, n, m);
        while(q-- > 0){
            int x1 = sc.nextInt(), y1 = sc.nextInt(), x2 = sc.nextInt(), y2 = sc.nextInt();
            handle(x1, y1, x2, y2, n, m);
        }
    }
    
    private static void init(int[][] mat, int n, int m){
        pre = new long[n+1][m+1];
        for(int i=1; i<=n; i++) {
            pre[i][1] = pre[i-1][1] + mat[i-1][0];
        }
        for(int i=1; i<=m; i++){
            pre[1][i] = pre[1][i-1] + mat[0][i-1];
        }
        for(int i=2; i<=n; i++){
            for(int j=2; j<=m; j++){
                pre[i][j] = pre[i-1][j] + pre[i][j-1] - pre[i-1][j-1] + mat[i-1][j-1];
            }
        }
    }
    
    private static void handle(int x1, int y1, int x2, int y2, int n, int m){
        if(x1 < 1 ||  x2 < 1 || x1 > n || x2 > n || y1 < 1 || y2 < 2 || y1 > m || y2 > m){
            return;
        }
        long res = pre[x2][y2] - pre[x1-1][y2] - pre[x2][y1-1] + pre[x1-1][y1-1];
        System.out.println(res);
    }
}