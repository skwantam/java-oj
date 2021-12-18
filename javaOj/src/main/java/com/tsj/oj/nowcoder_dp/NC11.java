import java.util.*;
import java.lang.Math;

public class NC11{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), v = sc.nextInt();
        int[] vs = new int[n];
        int[] ps = new int[n];
        for(int i=0; i<n; i++){
            vs[i] = sc.nextInt();
            ps[i] = sc.nextInt();
        }
        System.out.println(backpack1(n, v, vs, ps));
        System.out.println(backpack2(n, v, vs, ps));
    }
    
    private static int backpack1(int n, int v, int[] vs, int[] ps){
        int[][] dp = new int[n+1][v+1]; // dp[i][j]:在前i个物品里选，且体积不超过j时的最大价值
        for(int i=1; i<=n; i++){
            for(int j=1; j<=v; j++){
                if(j < vs[i-1]) {
                    dp[i][j] = dp[i-1][j];
                }else{
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-vs[i-1]] + ps[i-1]);
                }
            }
        }
        return dp[n][v];
    }
    
    private static int backpack2(int n, int v, int[] vs, int[] ps){
        int[][] dp = new int[n+1][v+1];
        for(int i=0; i<=n; i++) Arrays.fill(dp[i], Integer.MIN_VALUE);
        for(int i=0; i<=n; i++) dp[i][0] = 0;
        for(int i=1; i<=n; i++){
            for(int j=1; j<=v; j++){
                if(j >= vs[i-1])
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-vs[i-1]] + ps[i-1]);
                else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        if(dp[n][v] <= 0) return 0;
        return dp[n][v];
    }

    /**
        一维优化
     */
    // private static int backpack2(int n, int v, int[] vs, int[] ps){
    //     int[] dp = new int[v+1]; //dp[i]:在容量为i时背包恰好装满时，最大装的价值
    //     Arrays.fill(dp, Integer.MIN_VALUE);
    //     dp[0] = 0;
    //     for(int i=0; i<n; i++){
    //         for(int j=v; j>=vs[i]; j--){
    //             dp[j] = Math.max(dp[j], dp[j-vs[i]]+ps[i]);
    //         }
    //     }
    //     if(dp[v] < 0) return 0;
    //     return dp[v];
    // }
}