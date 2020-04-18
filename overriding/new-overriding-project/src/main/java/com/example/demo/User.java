package com.example.demo;

import com.example.demo.constants.Lang;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1;

    private String name;

    private Integer age;

    private transient Lang lang;

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

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        String langName = (String) stream.readObject();
        this.lang = Lang.valueOf(langName);
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.lang.name());
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
