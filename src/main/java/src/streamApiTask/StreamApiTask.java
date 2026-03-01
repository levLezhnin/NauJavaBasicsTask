package src.streamApiTask;

import src.NauTask;

import java.util.Arrays;
import java.util.List;

public class StreamApiTask implements NauTask {

    private List<Employee> generateData() {
        return Arrays.asList(
                new Employee("Башмачкин Акакий Акакиевич", "Сенат", 50, 10387d),
                new Employee("Голядкин Яков Петрович", "Сенат", 56, 12345d),
                new Employee("Поприщин Аксентий Иванович", "Сенат", 42, 11111d),
                new Employee("Чичиков Павел Иванович", "Казначейство", 36, 25000d),
                new Employee("Плюшкин Степан Трифонович", "Казначейство", 62, 67000d),
                new Employee("Манилов Фома Фомич", "Земский суд", 34, 15200d),
                new Employee("Коробочка Настасья Петровна", "Земский суд", 52, 14300d),
                new Employee("Собакевич Михаил Семенович", "Губернское правление", 48, 18750d),
                new Employee("Ноздрев Порфирий Петрович", "Губернское правление", 45, 8900d)
        );
    }

    @Override
    public void solve() {

        String departmentQuery = "Сенат";

        List<Employee> employees = generateData();

        double averageSalary = employees.stream()
                                        .filter(employee -> departmentQuery.equals(employee.getDepartment()))
                                        .mapToDouble(Employee::getSalary)
                                        .average()
                                        .getAsDouble();

        System.out.printf("Средняя зарплата в департаменте '%s' составляет %f рублей\n", departmentQuery, averageSalary);
    }

}

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
