package com.example.SeventhHomemork;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class UpdateForm {
    private @Length(max = 20, min = 1, message = "文字数異常です") String name;
    private @Range(max = 150, min = 0, message = "ギネス記録になってしまいます") Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
