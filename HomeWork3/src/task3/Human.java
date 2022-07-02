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
    @Save
    private Education education;
    @Save
    private Work job;

    public Human() {
        super();
    }

    public Human(String name, String surname, int age, int weight, Education education, Work job) {
        super();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.weight = weight;
        this.education = education;
        this.job = job;
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
        String res =  "Name: " + name + System.lineSeparator()
                        + "Surname: " + surname + System.lineSeparator()
                        + "Age: " + age + System.lineSeparator()
                        + "Weight: " + weight + System.lineSeparator()
                        + "Education: " + (education == null ? "null" :
                                    ("{" + System.lineSeparator() + education.toString() + System.lineSeparator() + "}")) + System.lineSeparator()
                        + "Job: " + (job == null ? "null" :
                                    ("{" + System.lineSeparator() + job.toString() + "}")) + System.lineSeparator();
        return  res;
    }

}
