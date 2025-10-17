package org.ssm.aiagent.util;

import lombok.Data;

import java.util.Objects;

// 测试用的 POJO 类定义
@Data
class Person {
    private String  name;
    private Integer age;
    private String  email;
    private Address address;

    // 必须有无参构造函数
    public Person() {}

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Person person = (Person) o;
        return Objects.equals(age, person.age) &&
                       java.util.Objects.equals(name, person.name) &&
                       java.util.Objects.equals(email, person.email) &&
                       java.util.Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age, email, address);
    }

    @Override
    public String toString() {
        return "Person{" +
                       "name='" + name + '\'' +
                       ", age=" + age +
                       ", email='" + email + '\'' +
                       ", address=" + address +
                       '}';
    }

}



