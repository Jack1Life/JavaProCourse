package task3;

public class Work {
    @Save
    private String organization;
    @Save
    private String position;
    @Save
    private int salary;

    public Work() {
        super();
    }

    public Work(String organization, String position, int salary) {
        this.organization = organization;
        this.position = position;
        this.salary = salary;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        String res =  "Organization: " + organization + System.lineSeparator()
                + "Position: " + position + System.lineSeparator()
                + "Salary: " + salary + System.lineSeparator();
        return  res;
    }
}
