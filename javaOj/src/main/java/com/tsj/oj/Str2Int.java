package com.tsj.oj;
import java.util.*;
public class Str2Int{

    public static boolean validNum(String str){
        List<Map> automata= new ArrayList<>();
        automata.add(new HashMap<Character, Integer>(){{put(' ', 0); put('s', 1); put('d', 2); put('.', 3);}});       // 0-没有数字的空格
        automata.add(new HashMap<Character, Integer>(){{put('d', 2); put('.', 3); }});                                // 1-底数部分符号
        automata.add(new HashMap<Character, Integer>(){{put('d', 2); put('.', 3); put('e', 5); put(' ', 8); }});      // 2-底数部分的整数
        automata.add(new HashMap<Character, Integer>(){{put('d', 4); put('e', 5); put(' ', 8); }});                   // 3-底数部分的小数点
        automata.add(new HashMap<Character, Integer>(){{put('d', 4); put('e', 5); put(' ', 8); }});                   // 4-底数部分的小数
        automata.add(new HashMap<Character, Integer>(){{put('s', 6); put('d', 7); }});                                // 5-指数符号e/E
        automata.add(new HashMap<Character, Integer>(){{put('d', 7); }});                                             // 6-指数部分的符号
        automata.add(new HashMap<Character, Integer>(){{put('d', 7); put(' ', 8); }});                                // 7-指数部分的整数
        automata.add(new HashMap<Character, Integer>(){{put(' ', 8); }});                                             // 8-有效数字后的空格
        
        int start = 0; // 初始状态
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(c == ' ' || c == '.'){
                c = c;
            }else if( c >= '0' && c <= '9'){
                c = 'd';
            }else if( c == 'E' || c == 'e'){
                c = 'e';
            }else if( c == '+' || c == '-'){
                c = 's';
            }else{
                c = '?';
            }

            if(automata.get(start).containsKey(c)){
                start = (int)automata.get(start).get(c);
            }else{
                return false;
            }
            System.out.println(c + " start=" + start);
        }
        
        //可接受状态：2，3，4，7，8
        return start == 2 || start == 3 || start == 4 || start == 7 || start == 8;
    }
}