package com.nau.streamApiTask;

class Employee {

    private String fullName;
    private String department;
    private Integer age;
    private Double salary;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Employee(String fullName, String department, Integer age, Double salary) {
        this.fullName = fullName;
        this.department = department;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee: {\n" +
                "fullName: " + fullName + ", \n" +
                "age: " + age + ", \n" +
                "salary: " + salary + "\n" +
                "}";
    }
}
