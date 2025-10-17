package org.ssm.aiagent.util;

import lombok.Data;

import java.util.Objects;

@Data
class PersonDTO {
    private String  name;
    private Integer age;
    private String  email;

    public PersonDTO() {}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(age, personDTO.age) &&
                       java.util.Objects.equals(name, personDTO.name) &&
                       java.util.Objects.equals(email, personDTO.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age, email);
    }

}