// package com.tsj.oj.easy;
import java.util.*;
import java.util.stream.*;

public class Intersect{

    public static List<Integer> intersect(List<Integer> lst1, List<Integer> lst2){
        return lst1.stream().filter(item -> !lst2.contains(item)).collect(Collectors.toList());
    }

    public static class User{
        int id;
        public User(int id){
            this.id = id;
        }
    }

    public static List<Integer> getIds(){
        List<User> users = new ArrayList<>();
        users.add(new User(1));
        users.add(new User(2));
        users.add(new User(3));
        return users.stream().map(user -> user.id).collect(Collectors.toList());
    }

    public static Map<Integer, Integer> getMaps(){
        List<User> users = new ArrayList<>();
        users.add(new User(1));
        users.add(new User(2));
        users.add(new User(3));
        return users.stream().map(user -> user.id).collect(Collectors.toMap(x->x, x->x+1));
    }

    public static boolean numStartWith(Integer num, String a){
        return num.toString().startsWith(a);
    }

    public static void main(String[] args){
        // List<Integer> lst1 = new ArrayList<Integer>(){{add(1); add(2); add(3);}};
        // List<Integer> lst2 = new ArrayList<Integer>(){{add(3); add(4); add(5);}};
        // List<Integer> res = intersect(lst1, lst2);
        // res.parallelStream().forEach(System.out :: print);
        // System.out.println(getMaps());
        // System.out.println(numStartWith(111112, "1"));
        // IdTree root = new IdTree(1);
        // IdTree sub1 = new IdTree(101);
        // IdTree sub21 = new IdTree(10101);
        // IdTree sub22 = new IdTree(10102);
        // root.subIds = new ArrayList<IdTree>(){{add(sub1);}};
        // sub1.subIds = new ArrayList<IdTree>(){{add(sub21); add(sub22);}};
        //System.out.println(root.id);
        //System.out.println(root.subIds.get(0).id);
        // System.out.println(root.find(101).id);

        // System.out.println(false == new Boolean(false));
        // List<User> res = new ArrayList<User>(){{add(new User(1)); add(new User(2));}};
        // Map<Integer, User> map = res.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        // System.out.println(res);
        // String str = "----";
        // System.out.println(Integer.parseInt(str));
        // String str = "198";
        // List<Long> taskIds = Arrays.stream(str.split(";")).map(Long::parseLong).collect(Collectors.toList());
        // System.out.println(taskIds);
        // Map<Integer, Integer> map = new HashMap<Integer, Integer>(){{put(1,1); put(2,1); put(3,2);}};
        // List<Integer> list = new ArrayList<>(new HashSet<>(map.values()));
        // System.out.println(list);
    }
}

class User{
    Integer id;
    public User(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }
}


class IdTree{

    public Integer id;
    public List<IdTree> subIds;

    public IdTree(Integer id){ this.id = id; subIds = null;}
    public IdTree(Integer id, List<IdTree> subIds){ this.id = id; this.subIds = subIds;}

    public IdTree find(Integer id){
        if(id < 100){
            return getFromSub(id);
        }else{
            return find(id/100).getFromSub(id);
        }
    }

    public IdTree getFromSub(Integer id){
        if(this.subIds != null){
            for( IdTree it : this.subIds){
                if(it.id.equals(id)){
                    return it;
                }
            }
        }
        return null;
    }
}