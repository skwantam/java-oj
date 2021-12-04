import java.util.*;
import java.lang.Math;

public class NC9{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        String[] strmat = new String[n];
        char[][] mat = new char[n][m];
        for(int i=0; i<n; i++) {
            strmat[i] = sc.next();
            for(int j=0; j<m; j++){
                mat[i][j] = strmat[i].charAt(j);
            }
        }
        System.out.println(handle(mat, n, m));
    }
    
    private static int score(char c){
        if(c == 'l') return 4;
        else if(c == 'o') return 3;
        else if(c == 'v') return 2;
        else if(c == 'e') return 1;
        else return 0;
    }
    private static int handle(char[][] mat, int n, int m){
        int[][] dp = new int[n+1][m+1];
        for(int i=1; i<=n; i++) dp[i][1] = dp[i-1][1] + score(mat[i-1][0]);
        for(int i=1; i<=m; i++) dp[1][i] = dp[1][i-1] + score(mat[0][i-1]);
        for(int i=2; i<=n; i++){
            for(int j=2; j<=m; j++){
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]) + score(mat[i-1][j-1]);
            }
        }
        return dp[n][m];
    }
}