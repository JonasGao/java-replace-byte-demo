package com.example.demo;

import com.example.demo.constant.Lang;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;

    private Integer age;

    private Lang lang;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", lang=" + lang +
                '}';
    }
}
