package org.ssm.aiagent.util;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * MyBeanUtils 测试类
 */
public class MyBeanUtilsTest {

    @Test
    public void testCombineFieldsWithSimpleObjects() {
        // 准备测试数据
        Person person = new Person("张三", 25, "zhangsan@example.com");
        Employee employee = new Employee("李四", 30, "IT", 10000.0);

        // 执行合并操作
        Person result = MyBeanUtils.combineFields(Person.class, person, employee);

        // 验证结果
        // name和age来自employee（后一个对象优先）
        assertEquals("李四", result.getName());
        assertEquals(30, result.getAge());
        // email来自person（employee中没有email字段）
        assertEquals("zhangsan@example.com", result.getEmail());
    }

    @Test
    public void testCombineFieldsWithNullObjects() {
        // 准备测试数据
        Person person = new Person("张三", 25, "zhangsan@example.com");

        // 执行合并操作，其中一个对象为null
        Person result = MyBeanUtils.combineFields(Person.class, person, null);

        // 验证结果
        assertEquals("张三", result.getName());
        assertEquals(25, result.getAge());
        assertEquals("zhangsan@example.com", result.getEmail());
    }

    @Test
    public void testCombineFieldsWithComplexObjects() {
        // 准备测试数据
        Address address = new Address("中山路123号", "上海市");
        Person person = new Person();
        person.setName("王五");
        person.setAge(28);

        Employee employee = new Employee("赵六", 35, "HR", 12000.0);

        // 执行合并操作
        Person result = MyBeanUtils.combineFields(Person.class, person, address, employee);

        // 验证结果
        // name和age来自employee（后一个对象优先）
        assertEquals("赵六", result.getName());
        assertEquals(35, result.getAge());
        // email为null（所有源对象都没有设置）
        assertNull(result.getEmail());
        // address来自address对象
        assertNull(result.getAddress());
    }

    @Test
    public void testCombineFieldsWithDTO() {
        // 准备测试数据
        Person person = new Person("钱七", 32, "qianqi@example.com");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("孙八");
        personDTO.setAge(29);

        // 执行合并操作
        PersonDTO result = MyBeanUtils.combineFields(PersonDTO.class, person, personDTO);

        // 验证结果
        // name和age来自personDTO（后一个对象优先）
        assertEquals("孙八", result.getName());
        assertEquals(29, result.getAge());
        // email来自person，但PersonDTO没有email字段，所以不会被设置
        assertEquals(person.getEmail(), result.getEmail());
    }

    @Test
    public void testCombineFieldsEmptySources() {
        // 执行合并操作，不传入源对象
        Person result = MyBeanUtils.combineFields(Person.class);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getAge());
        assertNull(result.getEmail());
        assertNull(result.getAddress());
    }

    @Test
    public void testCombineFieldsAllNullSources() {
        // 执行合并操作，所有源对象都为null
        Person result = MyBeanUtils.combineFields(Person.class, null, null, null);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getAge());
        assertNull(result.getEmail());
        assertNull(result.getAddress());
    }

    @Test
    public void test_notCoverNullField() {
        Person person = new Person("张三", 25, "zhangsan@example.com");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("王五");
        // 执行合并操作，所有源对象都为null
        Person result = MyBeanUtils.combineFields(Person.class, person, personDTO, null);
        assertEquals("王五", result.getName());
        assertEquals(25, result.getAge());
        assertEquals("zhangsan@example.com", result.getEmail());
    }

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

    @Data
    class Employee {
        private String  name;
        private Integer age;
        private String  department;
        private Double  salary;

        public Employee() {}

        public Employee(String name, int age, String department, double salary) {
            this.name = name;
            this.age = age;
            this.department = department;
            this.salary = salary;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Employee employee = (Employee) o;
            return Objects.equals(age, employee.age) &&
                           Double.compare(employee.salary, salary) == 0 &&
                           java.util.Objects.equals(name, employee.name) &&
                           java.util.Objects.equals(department, employee.department);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, age, department, salary);
        }

        @Override
        public String toString() {
            return "Employee{" +
                           "name='" + name + '\'' +
                           ", age=" + age +
                           ", department='" + department + '\'' +
                           ", salary=" + salary +
                           '}';
        }

    }

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

}