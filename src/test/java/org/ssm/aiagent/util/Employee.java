package org.ssm.aiagent.util;

import lombok.Data;

import java.util.Objects;

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
