package org.ssm.aiagent.service;

import lombok.Data;

import java.util.List;

@Data
class User {

    private String name;

    private String email;

    private Integer age;

    private Address address;

    private List<String> tags;

    private List<User> friends;

    @Data
    public static class Address {

        private String country;

        private String city;

        private String street;

    }

}
