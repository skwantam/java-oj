package tsj;

import java.util.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tsj.beans.Person;
import org.junit.Test;

public class TestCase2 {

    @Test
    public void fun2() {
        Person person = new Person();
        // System.out.println(JSON.toJSONString(person)); //需getter/setter方法
        fastjsonSer(person);
        jacksonSer(new ObjectMapper(), person);
    }

    private static void jacksonSer(ObjectMapper mapper, Object obj) {
        try {
            System.out.println("jackson：" + mapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private static void fastjsonSer(Object obj) {
        System.out.println("fastjson：" + JSON.toJSONString(obj));
    }
}
