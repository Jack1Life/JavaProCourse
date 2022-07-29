import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FilterField(type=FilterType.NUMERIC)
    private Long id;
    @FilterField(type=FilterType.STRING)
    private String name;
    @FilterField(type=FilterType.STRING)
    private String surname;
    @FilterField(type=FilterType.NUMERIC)
    private Integer age;
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
    @FilterField(type=FilterType.OBJ_ID)
    private Set<Order> orders;

    public Client() {
        this.orders = new HashSet<>();
    }

    public Client(String name, String surname, Integer age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.orders = new HashSet<>();
    }

    public Client(String name, String surname, Integer age, Set<Order> orders) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public String toString() {
        String res =  "Клієнт " + this.id + System.lineSeparator()
                + "Ім'я: "      + this.name + System.lineSeparator()
                + "Прізвище: "  + this.surname + System.lineSeparator()
                + "Вік: "       + this.age + System.lineSeparator()
                + "Замовлення:" + System.lineSeparator();
        for(Order ord : this.orders) {
            res += "\t" + ord.getId() + ": " + ord.getDateString() + " " + ord.getGoods().size() + " товари(ів)";
        }
        return res;
    }

    private static String getLastOrderDate(Client cl) {
        Order lastOrder = null;
        for(Order or : cl.getOrders()) {
            if(lastOrder == null || or.getDate() > lastOrder.getDate()) {
                lastOrder = or;
            }
        }
        return lastOrder == null ? "" : lastOrder.getDateString();
    }

    @ConsoleListOut
    public static String[][] getClientsTable(ArrayList<?> list) {
        ArrayList<Client> cliList = (ArrayList<Client>) list;
        String[] header = {"Id", "Прізвище", "Ім'я", "Вік", "Кількість замовлень", "Дата останнього замовлення"};
        String[][] table = new String[cliList.size() + 1][header.length];
        table[0] = header;

        int idx = 1;
        for(Client cl : cliList) {
            table[idx][0] = "" + cl.getId();
            table[idx][1] = cl.getSurname();
            table[idx][2] = cl.getName();
            table[idx][3] = "" + cl.getAge();
            table[idx][4] = "" + cl.getOrders().size();
            table[idx][5] = "" + Client.getLastOrderDate(cl);
            idx++;
        }
        return table;
    }

}
