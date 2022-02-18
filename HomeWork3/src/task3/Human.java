package task3;

public class Human {
    @Save
    private String name;
    @Save
    private String surname;
    @Save
    private int age;
    @Save
    private int weight;

    public Human() {
        super();
    }

    public Human(String name, String surname, int age, int weight) {
        super();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Name: " + name + "; Surname: " + surname + ": Age: " + age + "; Weight: " + weight;
    }

}
