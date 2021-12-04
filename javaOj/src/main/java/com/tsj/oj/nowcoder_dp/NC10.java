import java.util.*;
import java.lang.Math;

public class NC10{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), k = sc.nextInt();
        long[] nums = new long[n];
        for(int i=0; i<n; i++) nums[i] = sc.nextLong();
        System.out.println(handle(nums, n, k));
    }
    
    private static long handle(long[] nums, int n, int k){
        long[][] dp = new long[n+1][k]; //dp[i][j]表示前i个数中除以k的余数为j的最大和
        for(int i=0; i<=n; i++) Arrays.fill(dp[i], Long.MIN_VALUE);
        dp[0][0] = 0;
        for(int i=1; i<=n; i++){
            for(int j=0; j<k; j++){
                dp[i][(int)((j+nums[i-1])%k)] = Math.max(
                    dp[i-1][j] + nums[i-1], 
                    dp[i-1][(int)((j+nums[i-1])%k)]
                );
            }
        }
        
        if(dp[n][0] <=0 ) return -1;
        return dp[n][0];
    }
}