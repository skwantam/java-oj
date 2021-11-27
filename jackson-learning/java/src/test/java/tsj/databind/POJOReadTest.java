package tsj.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import tsj.bean.Person;

import java.io.File;
import java.io.IOException;

public class POJOReadTest {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Person person = objectMapper.readValue(new File("java-jackson/src/main/resources/person.json"), Person.class);
        System.out.println(person);
    }
}
