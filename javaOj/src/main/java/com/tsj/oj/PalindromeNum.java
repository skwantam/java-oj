package com.tsj.oj;
import java.util.*;

public class PalindromeNum{
    
    public static boolean isPalindrome(int x){
        if(x < 0) return false;
        int digit = 0, y = x;
        while( y > 0) {++digit; y/=10;}
        for(int i=0; i<digit/2; i++){
            int a = (int) Math.pow(10, i);
            int b = (int) Math.pow(10, digit-i-1);
            if(x/a%10 != x/b%10) return false;
        }
        return true;
    }
}