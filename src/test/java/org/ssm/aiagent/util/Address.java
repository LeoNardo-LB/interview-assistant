package org.ssm.aiagent.util;

import lombok.Data;

@Data
class Address {
    private String street;
    private String city;

    public Address() {}

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return java.util.Objects.equals(street, address.street) &&
                       java.util.Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(street, city);
    }

    @Override
    public String toString() {
        return "Address{" +
                       "street='" + street + '\'' +
                       ", city='" + city + '\'' +
                       '}';
    }

}