package tsj.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import tsj.bean.Person;

import java.io.IOException;
import java.util.Arrays;

public class POJOWriteTest {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Person person = new Person();
        person.setName("YourBatman");
        person.setAge(18);
        person.setDog(null);
        person.setHobbies(Arrays.asList("足球","篮球"));

        String jsonStr = objectMapper.writeValueAsString(person);
        System.out.println(jsonStr);

    }
}
