package ch.zhaw.babynames.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor()
@ToString()
@Getter()
public class Name {
    private String name;
    private String gender;
    private Integer amount;
}
