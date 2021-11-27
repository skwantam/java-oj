package tsj.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.ToString;
import lombok.Data;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@ToString
@Data
public class Person {
    
    protected String name = "YourBatman";
    Integer age = 18;
}
