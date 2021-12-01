package tsj.bean;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Color {
    @JsonEnumDefaultValue
    RED,
    WHITE,
    BLACK;
}
