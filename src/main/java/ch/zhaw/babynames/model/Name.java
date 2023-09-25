package ch.zhaw.babynames.model;

import lombok.Getter;

public class Name {

    public Name(String name, String gender, Integer amount) {
        this.name = name;
        this.gender = gender;
        this.amount = amount;
    }

    @Getter
    private String name;

    @Getter
    private String gender;

    @Getter
    private Integer amount;

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", amount=" + amount +
                '}';
    }
}
